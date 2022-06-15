package com.travelappproject.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.travelappproject.LanguageManager;
import com.travelappproject.R;
import com.travelappproject.SharedPreferences.LocalDataManager;
import com.travelappproject.activities.ContactUsActivity;
import com.travelappproject.activities.EditProfileActivity;
import com.travelappproject.activities.LogoutActivity;
import com.travelappproject.activities.MainActivity;
import com.travelappproject.activities.SignInActivity;
import com.travelappproject.activities.VoucherActivity;
import com.travelappproject.model.hotel.User;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.thanguit.toastperfect.ToastPerfect;

public class ProfileFragment extends Fragment {
    Button btnEditProfile, btnSignOut, btnContact, btnSignin, btnVoucher;
    LinearLayout btnLanguage;
    FirebaseFirestore firestore;
    FirebaseUser user;
    MaterialDivider divider;
    FirebaseAuth auth;
    String UserID;
    TextView txtName, txtEmail, txtDes, txtLanguage;
    CircleImageView imgAvatar;
    private GoogleSignInClient gsc;
    private GoogleSignInOptions gso;
    GoogleSignInAccount account;
    Toolbar toolbar;
    LanguageManager languageManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firestore = FirebaseFirestore.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(),gso);
        account=GoogleSignIn.getLastSignedInAccount(getActivity());

        if (user != null) {
            UserID = auth.getCurrentUser().getUid();
            setUserInformation();
        }
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
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnLanguage = view.findViewById(R.id.btnLanguage);
        btnContact = view.findViewById(R.id.btnContact);
        btnSignin = view.findViewById(R.id.btnSignIn);
        btnVoucher = view.findViewById(R.id.btnVoucher);
        divider = view.findViewById(R.id.divider);
        txtDes = view.findViewById(R.id.txtDescription);
        txtLanguage = view.findViewById(R.id.txtCurrentlanguage);

        if(LocalDataManager.getLanguage().equals("vi"))
            txtLanguage.setText("Tiếng Việt");
        else
            txtLanguage.setText("English");

        if(user == null){
            imgAvatar.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            btnEditProfile.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);
            btnVoucher.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }else{
            btnSignin.setVisibility(View.GONE);
            txtDes.setVisibility(View.GONE);

        }

        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), VoucherActivity.class));
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ContactUsActivity.class));
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignInActivity.class));
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacNhanThoat();
            }
        });

        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageManager = new LanguageManager(getContext());
                if(LocalDataManager.getLanguage().equals("vi")){
                    languageManager.updateResource("en");
                    LocalDataManager.setLanguage("en");
                    refresh();
                    ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS, getString(R.string.ChangeLanguage), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                }else{
                    languageManager.updateResource("vi");
                    LocalDataManager.setLanguage("vi");
                    refresh();
                    ToastPerfect.makeText(getContext(), ToastPerfect.SUCCESS, getString(R.string.ChangeLanguage), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refresh(){
        Fragment profile = new ProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_container, profile).commit();
    }

    private void XacNhanThoat() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getActivity());
        alertdialog.setTitle(R.string.notification);
        alertdialog.setMessage(R.string.WantToExit);
        alertdialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(account!=null){
                    gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    });
                }else{
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        });
        alertdialog.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alertdialog.show();
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("users").document(UserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                txtEmail.setText(user.getEmail());
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String Name = task.getResult().getString("name");
                        String Address = task.getResult().getString("address");
                        String About = task.getResult().getString("about");
                        String PhoneNumber = task.getResult().getString("phonenumber");
                        String ImageURL = task.getResult().getString("image");
                        String type = task.getResult().getString("type");

                        if(type.equals("Email and password") || type.equals("Google")){
                            if(Name == null || Name.equals("")){
                                Name = user.getEmail().replaceAll("@.*","").replaceAll("[^a-zA-Z]+", " ").trim();
                            }
                        }else if(type.equals("Facebook")){
                            if(Name == null || Name.equals(""))
                                Name = user.getDisplayName();
                        }
                        txtName.setText(Name);
                        Glide.with(getContext()).load(ImageURL).into(imgAvatar);
                    }
                }
            }
        });
    }
}