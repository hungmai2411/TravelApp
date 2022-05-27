package com.travelappproject.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travelappproject.R;
import com.travelappproject.activities.EditProfileActivity;
import com.travelappproject.model.hotel.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    Button btnEditProfile;
    FirebaseFirestore firestore;
    FirebaseUser user;
    User mUser;
    FirebaseAuth auth;
    String UserID;
    TextView txtName, txtEmail, txtAbout, txtPhoneNumber, txtAddress;
    ImageView imgUser;
    CircleImageView imgAvatar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            UserID = auth.getCurrentUser().getUid();
        }
        firestore = FirebaseFirestore.getInstance();

        setUserInformation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAbout = view.findViewById(R.id.txtAbout);
        txtPhoneNumber = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        imgUser = view.findViewById(R.id.imgUser);
        imgAvatar = view.findViewById(R.id.imgAvatar);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
    }

    private void setUserInformation() {
        firestore.collection("users").document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                txtEmail.setText(user.getEmail());
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String Name = task.getResult().getString("name");
                        String Address = task.getResult().getString("address");
                        String About = task.getResult().getString("about");
                        String PhoneNumber = task.getResult().getString("phonenumber");
                        String ImageURL = task.getResult().getString("image");

                        txtName.setText(Name);
                        txtAddress.setText(Address);
                        txtPhoneNumber.setText(PhoneNumber);
                        txtAbout.setText(About);
//                            Glide.with(getContext()).load(user.getPhotoUrl()).error(R.drawable.profile).into(imgAvatar);
//                            Glide.with(getContext()).load(user.getPhotoUrl()).error(R.drawable.profile).into(imgUser);
                    }
                }
            }
        });

    }
}