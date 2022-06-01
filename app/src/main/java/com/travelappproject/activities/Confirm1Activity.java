package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.adapter.PaymentAdapter;
import com.travelappproject.helperforzalopay.AppInfo;
import com.travelappproject.helperforzalopay.CreateOrder;
import com.travelappproject.model.hotel.Booking;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Payment;
import com.travelappproject.model.hotel.room.Room;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class Confirm1Activity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView rcvPaymentMethod;
    TextView txtPrice;
    int check = 0;
    Room room;
    Long daysDiff,startDate,endDate;
    String uid;
    Hotel mHotel;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm1);

        if(mAuth.getCurrentUser() != null){
            uid = mAuth.getCurrentUser().getUid();
        }

        Intent intent = getIntent();

        if(intent != null){
            Bundle bundle = intent.getExtras();
            room = (Room) bundle.getSerializable("room");
            mHotel = (Hotel) intent.getSerializableExtra("hotel");
            daysDiff = intent.getLongExtra("daysdiff",1);
            startDate = intent.getLongExtra("startDate",0);
            endDate = intent.getLongExtra("endDate",0);
        }

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        txtPrice = findViewById(R.id.txtPrice);
        initToolBar();
        StepView stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.done(true);
        stepView.go(1, true);

        rcvPaymentMethod = findViewById(R.id.rcvPaymentMethod);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvPaymentMethod.setLayoutManager(linearLayoutManager1);
        PaymentAdapter paymentAdapter = new PaymentAdapter(this);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(new Payment("Payment at hotel", R.drawable.review));
        paymentList.add(new Payment("Payment with Zalo Pay", R.drawable.zalopay));
        paymentAdapter.setData(paymentList);
        rcvPaymentMethod.setAdapter(paymentAdapter);

        TextView txtBookingType, txtHotelName, txtRoomType, txtPrice, txtCancelPolicy;

        txtBookingType = findViewById(R.id.txtBookingType);
        txtHotelName = findViewById(R.id.nameHotel);
        txtRoomType = findViewById(R.id.txtTypeRoom);
        txtPrice = findViewById(R.id.txtPrice);
        txtCancelPolicy = findViewById(R.id.txtCancelPolicy);

        txtCancelPolicy.setText(room.getCancelPolicies());
        txtPrice.setText(new HandleCurrency().handle(room.getPrice() * daysDiff));
        txtHotelName.setText(mHotel.getName());
        txtRoomType.setText(room.getName());
        txtBookingType.setText(String.valueOf(daysDiff) + " night");

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment payment = paymentAdapter.getItemSelected();
                choice = payment.getPaymentMethod();

                if(choice.equals("Payment at hotel")){
                    addToBooked();
                }else {
                    if (check == 0) {
                        CreateOrder orderApi = new CreateOrder();

                        try {
                            JSONObject data = orderApi.createOrder(String.valueOf(1000));
                            String code = data.getString("return_code");

                            if (code.equals("1")) {

                                String token = data.getString("zp_trans_token");

                                ZaloPaySDK.getInstance().payOrder(Confirm1Activity.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                        check = 1;
                                        btnConfirm.setText("Tiếp tục");
                                    }

                                    @Override
                                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                        Toast.makeText(Confirm1Activity.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                        Toast.makeText(Confirm1Activity.this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            Log.d("err", e.getMessage());
                        }
                    } else {
                        Intent intent1 = new Intent(Confirm1Activity.this, Confirm2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("hotel",mHotel);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                    }
                }
            }
        });
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addToBooked(){
        Map<String, Object> booksMap = new HashMap<>();

        booksMap.put("addressHotel",mHotel.getFullAddress());
        booksMap.put("timestamp", FieldValue.serverTimestamp());
        booksMap.put("idHotel",mHotel.getId());
        booksMap.put("idRoom",room.getId());
        booksMap.put("nameRoom",room.getName());
        booksMap.put("choice",choice);
        booksMap.put("startDate",startDate);
        booksMap.put("endDate",endDate);
        booksMap.put("startTime",mHotel.getCheckInTime());
        booksMap.put("endTime",mHotel.getCheckOutTime());
        booksMap.put("daysdiff",daysDiff);
        booksMap.put("hotelName",mHotel.getName());
        long price = mHotel.getPrice() * daysDiff;
        booksMap.put("price",price);

        db.collection("users/" + uid + "/booked").add(booksMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Intent intent1 = new Intent(Confirm1Activity.this, Confirm2Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("idBooking",task.getResult().getId());
                    intent1.putExtras(bundle);
                    intent1.putExtra("hotel",mHotel);
                    startActivity(intent1);
                }else {

                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}