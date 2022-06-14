package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.travelappproject.R;
import com.travelappproject.adapter.PhotoAdapter;
import com.travelappproject.model.hotel.Review;
import com.travelappproject.model.hotel.room.Photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import vn.thanguit.toastperfect.ToastPerfect;

public class ReviewActivity extends AppCompatActivity {
    Button btnChooseImage;
    RecyclerView rcvImages;
    PhotoAdapter photoAdapter;
    Toolbar toolbar;
    RatingBar ratingBar;
    List<String> listTmp = new ArrayList<>();
    ProgressDialog progressDialog;
    private FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String uid;
    List<String> images = new ArrayList<>();
    Long idHotel;
    String imgUser;
    String nameUser,idReview;
    String bookingID;
    ImageView btnDelete;
    Review review;
    boolean hasReviewed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getUid();
        }

        Intent intent = getIntent();

        if (intent != null) {
            idHotel = intent.getLongExtra("idHotel", 0);
            bookingID = intent.getStringExtra("bookingID");

            Bundle bundle = intent.getExtras();

            if(bundle != null){
                hasReviewed = intent.getBooleanExtra("hasReviewed",false);
                review = (Review) bundle.getSerializable("review");
                idReview = intent.getStringExtra("idReview");
            }
        }

        EditText edtReview = findViewById(R.id.edtReview);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://travel-81548.appspot.com");

        ImageButton btnSend = findViewById(R.id.btnSend);
        btnDelete = findViewById(R.id.btnDelete);
        ratingBar = findViewById(R.id.ratingBar);
        btnChooseImage = findViewById(R.id.btnChooseImages);
        rcvImages = findViewById(R.id.rcvImages);
        toolbar = findViewById(R.id.toolbar);
        photoAdapter = new PhotoAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvImages.setLayoutManager(linearLayoutManager);
        rcvImages.setAdapter(photoAdapter);

        if(hasReviewed == true){
            ratingBar.setRating(review.getRate());
            edtReview.setText(review.getReview());
            listTmp = review.getImages();
            photoAdapter.addData(listTmp);
            photoAdapter.notifyDataSetChanged();
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(ReviewActivity.this);
                firestore.collection("Hotels/" + idHotel + "/reviews")
                        .document(idReview)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dismissDialog();
                                ToastPerfect.makeText(ReviewActivity.this,ToastPerfect.SUCCESS, getResources().getString(R.string.update_review), ToastPerfect.BOTTOM, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtReview.getText().equals("")) {
                    if(!hasReviewed) {
                        showDialog(ReviewActivity.this);

                        firestore.collection("users/")
                                .document(String.valueOf(uid))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        nameUser = (String) task.getResult().get("name");
                                        imgUser = (String) task.getResult().get("image");

                                        float rate = ratingBar.getRating();
                                        String review = edtReview.getText().toString();

                                        for (String s : listTmp) {
                                            if (s != null) {
                                                Uri uri = Uri.parse(s);

                                                StorageReference riversRef = storageReference.child("reviews/" + uri);

                                                riversRef.putFile(uri)
                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                    @Override
                                                                    public void onSuccess(Uri uri) {
                                                                        images.add(uri.toString());

                                                                        if (images.size() == listTmp.size()) {
                                                                            saveToFireStore(rate, review, images, imgUser, nameUser);
                                                                        }
                                                                    }
                                                                })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.d("err", e.getMessage());
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }else{
                        float rate = ratingBar.getRating();
                        String review = edtReview.getText().toString();

                        for (String s : listTmp) {
                            if (s != null) {
                                Uri uri = Uri.parse(s);

                                if(s.contains("https://firebasestorage.googleapis.")) {
                                    images.add(s);

                                    if(images.size() == 0){
                                        dismissDialog();
                                        ToastPerfect.makeText(ReviewActivity.this,ToastPerfect.SUCCESS, getResources().getString(R.string.update_review), ToastPerfect.BOTTOM,Toast.LENGTH_SHORT).show();
                                    }
                                    continue;
                                }

                                StorageReference riversRef = storageReference.child("reviews/" + uri);

                                riversRef.putFile(uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                riversRef.getDownloadUrl()
                                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                images.add(uri.toString());

                                                                if (images.size() == listTmp.size()) {
                                                                    updateToFireStore(rate, review, images);
                                                                }
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("err", e.getMessage());
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
    }

    private void saveToFireStore(float rate, String review, List<String> images, String imgUser, String nameUser) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rate", rate);
        map.put("review", review);
        map.put("images", images);
        map.put("imgUser", imgUser);
        map.put("nameUser", nameUser);
        map.put("idUser",uid);
        map.put("bookingID",bookingID);
        firestore.collection("Hotels/" + idHotel + "/reviews").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                dismissDialog();
                ToastPerfect.makeText(ReviewActivity.this,ToastPerfect.SUCCESS, getResources().getString(R.string.add_review),ToastPerfect.BOTTOM ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateToFireStore(float rate, String reviewContent, List<String> images) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("rate", rate);
        map.put("review", reviewContent);
        map.put("images", images);
        map.put("imgUser", review.getImgUser());
        map.put("nameUser", review.getNameUser());
        map.put("idUser",uid);
        map.put("bookingID",bookingID);
        firestore.collection("Hotels/" + idHotel + "/reviews")
                .document(idReview)
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dismissDialog();
                        ToastPerfect.makeText(ReviewActivity.this,ToastPerfect.SUCCESS, getResources().getString(R.string.update_review), ToastPerfect.BOTTOM,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImagesFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(ReviewActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImagesFromGallery() {
        TedBottomPicker.with(ReviewActivity.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Xong")
                .setEmptySelectionText("Chưa chọn hình ảnh")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if (uriList != null && !uriList.isEmpty()) {
                            for (Uri uri : uriList) {
                                listTmp.add(uri.toString());
                            }
                            photoAdapter.addData(listTmp);
                        }
                    }
                });
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
}
