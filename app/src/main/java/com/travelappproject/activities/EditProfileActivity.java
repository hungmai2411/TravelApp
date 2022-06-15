package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.travelappproject.R;
import com.travelappproject.fragments.ProfileFragment;
import com.travelappproject.model.hotel.User;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import vn.thanguit.toastperfect.ToastPerfect;

public class EditProfileActivity extends AppCompatActivity {
    CircularImageView imgAvatar, imgAdd;
    EditText edtName, edtAddress, edtAbout, edtPhoneNumber, edtEmail;
    Button btnUpdate;
    ImageButton btnBack;
    String UserID;
    FirebaseFirestore firestore;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private boolean isPhotoSelected = false;
    private Uri mImageUri = null;
    String url;
    ProgressDialog progressDialog;
    String name,address,about,phonenumber, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            UserID = user.getUid();
        }
        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://travel-81548.appspot.com");

        imgAvatar = (CircularImageView) findViewById(R.id.img_profile);
        imgAdd = (CircularImageView) findViewById(R.id.img_add);
        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtAbout = (EditText) findViewById(R.id.edtAbout);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhonenumber);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUserInformation();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                address = edtAddress.getText().toString();
                about = edtAbout.getText().toString();
                phonenumber = edtPhoneNumber.getText().toString();
                email = edtEmail.getText().toString();
                uploadPicture();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData() != null){
            mImageUri = data.getData();
            Glide.with(EditProfileActivity.this).load(mImageUri).error(R.drawable.profile).into(imgAvatar);
        }
    }

    public void showDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.upload) + "...");
        pd.show();

        StorageReference riversRef = storageReference.child("image/" + UserID);
        if(mImageUri != null){
            riversRef.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url = uri.toString();
                                    saveToFireStore(name, about, address, phonenumber,url, email);
                                }
                            });
                        }
                    });
        }else if(mImageUri == null){
            firestore.collection("users").document(UserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    url = documentSnapshot.getString("image");
                    saveToFireStore(name, about, address, phonenumber,url, email);
                }
            });
        }
    }

    private void saveToFireStore(String name, String about, String address, String phonenumber,String url, String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(!email.equals(""))
            user.updateEmail(email);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.upload) + "...");
        pd.show();

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("about", about);
        map.put("address", address);
        map.put("phonenumber", phonenumber);
        map.put("image", url);
        firestore.collection("users").document(UserID).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ToastPerfect.makeText(EditProfileActivity.this, ToastPerfect.SUCCESS, getString(R.string.profilesaved), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                    finish();
                    pd.dismiss();
                } else{
                    ToastPerfect.makeText(EditProfileActivity.this, ToastPerfect.ERROR, task.getException().toString(), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                ToastPerfect.makeText(EditProfileActivity.this, ToastPerfect.ERROR, getString(R.string.faileduploadimage), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
            }
        });
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        firestore.collection("users").document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String UserName = task.getResult().getString("name");
                        String Address = task.getResult().getString("address");
                        String About = task.getResult().getString("about");
                        String PhoneNumber = task.getResult().getString("phonenumber");
                        String image = task.getResult().getString("image");

                        String type = task.getResult().getString("type");

                        if(type.equals("Email and password") || type.equals("Google")){
                            if(UserName == null || UserName.equals("")){
                                UserName = user.getEmail().replaceAll("@.*","").replaceAll("[^a-zA-Z]+", " ").trim();
                            }
                        }else if(UserName == null || type.equals("Facebook")){
                            if(UserName.equals(""))
                                UserName = user.getDisplayName();
                        }

                        edtName.setText(UserName);
                        edtAddress.setText(Address);
                        edtAbout.setText(About);
                        edtPhoneNumber.setText(PhoneNumber);
                        String email = user.getEmail();
                        if(email != null){
                            edtEmail.setText(email);
                        }
                        Glide.with(EditProfileActivity.this).load(image).into(imgAvatar);

                    }
                }
            }
        });
    }
}