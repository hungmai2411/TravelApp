package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.travelappproject.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import vn.thanguit.toastperfect.ToastPerfect;


public class SignInActivity extends AppCompatActivity {

    private TextView forgotpass, SignUp;
    private EditText emailedit, passedit;
    private Button btnsignin;
    private ImageButton btnFB, btnGG;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient gsc;
    private FirebaseFirestore firestore;
    private String userID;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        emailedit = findViewById(R.id.txtEmail);
        passedit = findViewById(R.id.txtPass);
        btnsignin = findViewById(R.id.btnSignIn);
        SignUp = findViewById(R.id.txtsignUp);
        btnFB = findViewById(R.id.FBSignIn);
        btnGG = findViewById(R.id.GGSignIn);
        forgotpass = findViewById(R.id.forgotpass);
        firestore = FirebaseFirestore.getInstance();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        token = task.getResult();
                    }
                });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id1))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();

        //sign in bằng email và mật khẩu
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass;
                email = emailedit.getText().toString();
                pass = passedit.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.fillinemail), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    ToastPerfect.makeText(getApplicationContext(), ToastPerfect.WARNING, getString(R.string.fillinpassword), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,Object> map = new HashMap<>();

                if(!token.equals(""))
                    map.put("token",token);

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firestore.collection("users").document(task.getResult().getUser().getUid()).update(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            ToastPerfect.makeText(getApplicationContext(), ToastPerfect.SUCCESS, getString(R.string.signinsuccessfully), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                        } else {
                            ToastPerfect.makeText(getApplicationContext(), ToastPerfect.ERROR, getString(R.string.signinfailed), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
            }
        });

        //Sign up
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        //Sign in bằng facebook

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ToastPerfect.makeText(getApplicationContext(), ToastPerfect.SUCCESS, getString(R.string.signinsuccessfully), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //SignIn bằng Google
        btnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();

            }
        });
        //Forgot Password
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPassActivity.class));
            }
        });
    }

    private void SignIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Map<String, Object> user1 = new HashMap<>();
                            user1.put("type", "Facebook");
                            user1.put("image", "https://firebasestorage.googleapis.com/v0/b/travel-81548.appspot.com/o/image%2Fprofile.png?alt=media&token=cc15b7cb-6ea8-4522-bdb7-b21ed5f37070");

                            userID = mAuth.getCurrentUser().getUid();

                            firestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().exists()) {
                                        firestore.collection("users").document(userID).update(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                            }
                                        });
                                    } else {
                                        firestore.collection("users").document(userID).set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                            }
                                        });
                                    }
                                }
                            });
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            ToastPerfect.makeText(getApplicationContext(), ToastPerfect.ERROR, getString(R.string.signinfailed), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        token = task.getResult();
                        updateUI(currentUser);
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            userID = mAuth.getCurrentUser().getUid();

            firestore.collection("users").document(userID).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            });

            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                ToastPerfect.makeText(getApplicationContext(), ToastPerfect.SUCCESS, getString(R.string.signinsuccessfully), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                //SignIn successful now show authentication
                FirebaseGoogleAuth(account);

            } catch (ApiException e) {
                e.printStackTrace();
                ToastPerfect.makeText(getApplicationContext(), ToastPerfect.ERROR, getString(R.string.errorcode) + e.getStatusCode(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                FirebaseGoogleAuth(null);
                updateUI(null);
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        //here we are checking the Authentication Credential and checking the task is successful or not and display the message
        //based on that.
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    userID = mAuth.getCurrentUser().getUid();
                    Map<String, Object> user1 = new HashMap<>();
                    user1.put("type", "Google");
                    user1.put("image", "https://firebasestorage.googleapis.com/v0/b/travel-81548.appspot.com/o/image%2Fprofile.png?alt=media&token=cc15b7cb-6ea8-4522-bdb7-b21ed5f37070");

                    firestore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                firestore.collection("users").document(userID).update(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                });
                            } else {
                                firestore.collection("users").document(userID).set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                });
                            }
                        }
                    });
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    updateUI(null);
                }
            }
        });
    }
}