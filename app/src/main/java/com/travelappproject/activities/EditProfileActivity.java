package com.travelappproject.activities;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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

import com.algolia.search.models.mcm.UserId;
import com.algolia.search.models.rules.Edit;
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
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.travelappproject.R;
import com.travelappproject.fragments.ProfileFragment;
import com.travelappproject.model.hotel.User;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    CircularImageView imgAvatar, imgAdd;
    EditText edtName, edtAddress, edtAbout, edtPhoneNumber;
    Button btnUpdate;
    ImageButton btnBack;
    String UserID;
    FirebaseFirestore firestore;
    User mUser;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private boolean isPhotoSelected = false;
    private Uri mImageUri = null;
    String url;

    //UploadTask uploadTask;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            UserID = user.getUid();
        }
        //UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();
        storageReference = storage.getReferenceFromUrl("gs://travel-81548.appspot.com");



        imgAvatar = (CircularImageView) findViewById(R.id.img_profile);
        imgAdd = (CircularImageView) findViewById(R.id.img_add);
        edtName = (EditText) findViewById(R.id.edtName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtAbout = (EditText) findViewById(R.id.edtAbout);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhonenumber);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, ProfileFragment.class));
            }
        });

        setUserInformation();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String address = edtAddress.getText().toString();
                String about = edtAbout.getText().toString();
                String phonenumber = edtPhoneNumber.getText().toString();
                uploadPicture();
                saveToFireStore(name, about, address, phonenumber);
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
            imgAvatar.setImageURI(mImageUri);
        }
    }

    private void uploadPicture() {
//        final String[] url = new String[1];
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();

        StorageReference riversRef = storageReference.child("image/" + UserID);
        riversRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> download = taskSnapshot.getStorage().getDownloadUrl();
                url = download.getResult().toString();
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded.", Snackbar.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(EditProfileActivity.this, "Failed to upload image.", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int)progressPercent + "%");
            }
        });


    }

    private void saveToFireStore(String name, String about, String address, String phonenumber) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("about", about);
        map.put("address", address);
        map.put("phonenumber", phonenumber);
        //map.put("image", URL);
        firestore.collection("users").document(UserID).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditProfileActivity.this, "Profile Settings Saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                    finish();
                } else {
//                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUserInformation() {
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

                        edtName.setText(UserName);
                        edtAddress.setText(Address);
                        edtAbout.setText(About);
                        edtPhoneNumber.setText(PhoneNumber);
                        //Glide.with(EditProfileActivity.this).load(image).error(R.drawable.profile).into(imgAvatar);
                    }
                }
            }
        });

    }

}