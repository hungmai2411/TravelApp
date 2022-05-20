package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.travelappproject.R;

public class ForgotPassActivity extends AppCompatActivity {

    private Button forgotpass;
    private ImageButton btnback;
    private EditText txtforgot;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        forgotpass = findViewById(R.id.btnForgotPass);
        txtforgot=findViewById(R.id.txtEmailFG);
        btnback=findViewById(R.id.btnBackFG);
        mauth=FirebaseAuth.getInstance();
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtforgot.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập email!!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String mail=txtforgot.getText().toString();
                    mauth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Reset link sent to your email.",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error! Reset link is not sent to your email. "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        });
    }
}