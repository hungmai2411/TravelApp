package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travelappproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import vn.thanguit.toastperfect.ToastPerfect;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailedit,passedit,repassedit;
    private Button btnsignup;
    private ImageButton btnBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userID;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();

        emailedit=findViewById(R.id.txtEmailSU);
        passedit=findViewById(R.id.txtPassSU);
        repassedit=findViewById(R.id.txtRePass);
        btnsignup=findViewById(R.id.btnSignUpSU);
        btnBack=findViewById(R.id.btnBack);
        firestore=FirebaseFirestore.getInstance();

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass,repass;
                email=emailedit.getText().toString();
                pass=passedit.getText().toString();
                repass=repassedit.getText().toString();
                FirebaseUser fuser = mAuth.getCurrentUser();

                if(TextUtils.isEmpty(email)){
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.fillinemail), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.fillinpassword), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                    return;
                }
                if(pass.length()<6){
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.passover6char), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                    return;
                }
                if(pass.equals(repass)==false){
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.rightrewritepass), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                    return;
                }
                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = !task.getResult().getSignInMethods().isEmpty();
                        if(!check){
                            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        //FirebaseUser user =mAuth.getCurrentUser();
//                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                ToastPerfect.makeText(getApplicationContext(), ToastPerfect.SUCCESS, getString(R.string.emailsent), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                ToastPerfect.makeText(getApplicationContext(), ToastPerfect.ERROR, getString(R.string.failsentemail) + e.getMessage(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                                            }
//                                        });
                                        String hashpass= BCrypt.withDefaults().hashToString(12,pass.toCharArray());
                                        userID=mAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference =firestore.collection("users").document(userID);
                                        Map<String,Object> user1 = new HashMap<>();
                                        user1.put("type","Email and password");
                                        user1.put("hashpass",hashpass);
                                        user1.put("image", "https://firebasestorage.googleapis.com/v0/b/travel-81548.appspot.com/o/image%2Fprofile.png?alt=media&token=cc15b7cb-6ea8-4522-bdb7-b21ed5f37070");

                                        documentReference.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                                        //ToastPerfect.makeText(getApplicationContext(), ToastPerfect.INFORMATION, getString(R.string.verifyemail), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                        startActivity(intent);

                                    }
                                }
                            });

                        }
                        else
                            ToastPerfect.makeText(getApplicationContext(), ToastPerfect.SUCCESS, getString(R.string.emailverified), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();                    }
                });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}