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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travelappproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

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

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập email!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập password!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.equals(repass)==false){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập lại đúng pass",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user =mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Verification Email has been sent",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Verification Email has not been sent",Toast.LENGTH_SHORT).show();
                                }
                            });
                            String hashpass= BCrypt.withDefaults().hashToString(12,pass.toCharArray());
                            userID=mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference =firestore.collection("users").document(userID);
                            Map<String,Object> user1 = new HashMap<>();
                            user1.put("type","Email and password");
                            user1.put("hashpass",hashpass);
                            documentReference.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            Toast.makeText(getApplicationContext(),"Hãy xác nhận email của bạnn !",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                                startActivity(intent);

                        }
                    }
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