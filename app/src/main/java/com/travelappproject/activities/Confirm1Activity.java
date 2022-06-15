package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;
import com.shuhart.stepview.StepView;
import com.travelappproject.HandleCurrency;
import com.travelappproject.R;
import com.travelappproject.RetrofitInstance;
import com.travelappproject.SendMessageApi;
import com.travelappproject.adapter.PaymentAdapter;
import com.travelappproject.helperforzalopay.AppInfo;
import com.travelappproject.helperforzalopay.CreateOrder;
import com.travelappproject.model.hotel.Booking;
import com.travelappproject.model.hotel.Data;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Message;
import com.travelappproject.model.hotel.Payment;
import com.travelappproject.model.hotel.User;
import com.travelappproject.model.hotel.room.Room;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.thanguit.toastperfect.ToastPerfect;
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
    TextView txtPrice, txtTotal, txtDiscount, txtDiscountStatus;
    int check = 0;
    Room room;
    Long daysDiff, startDate, endDate;
    String uid;
    Hotel mHotel;
    String choice;
    User user;
    ExecutorService executorService;
    SendMessageApi sendMessageApi;
    EditText edtDiscount;
    Button btnApply;
    long discount;
    String idVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm1);

        sendMessageApi = RetrofitInstance.retrofit.create(SendMessageApi.class);
        executorService = Executors.newSingleThreadExecutor();

        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        }

        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            user = (User) bundle.getSerializable("user");
            room = (Room) bundle.getSerializable("room");
            mHotel = (Hotel) intent.getSerializableExtra("hotel");
            daysDiff = intent.getLongExtra("daysdiff", 1);
            startDate = intent.getLongExtra("startDate", 0);
            endDate = intent.getLongExtra("endDate", 0);
        }

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        edtDiscount = findViewById(R.id.edtDiscount);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setEnabled(false);
        txtDiscountStatus = findViewById(R.id.txtDiscountStatus);
        txtTotal = findViewById(R.id.txtTotal);
        txtDiscount = findViewById(R.id.txtSale);
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
        paymentList.add(new Payment(getString(R.string.pay_at_hotel), R.drawable.review));
        paymentList.add(new Payment(getString(R.string.pay_with_zalo_pay), R.drawable.zalopay));
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
        txtDiscount.setText(new HandleCurrency().handle(0));
        txtTotal.setText(new HandleCurrency().handle(room.getPrice() * daysDiff));

        txtHotelName.setText(mHotel.getName());
        txtRoomType.setText(room.getName());
        txtBookingType.setText(String.valueOf(daysDiff) + " " + getString(R.string.night));


        edtDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    btnApply.setEnabled(true);
                    btnApply.setBackgroundColor(getResources().getColor(R.color.primary));
                }
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users/" + uid + "/vouchers")
                        .whereEqualTo("code", edtDiscount.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().isEmpty()) {
                                    ToastPerfect.makeText(Confirm1Activity.this, "Wrong", Toast.LENGTH_SHORT).show();
                                    txtDiscountStatus.setText(getResources().getString(R.string.ap_dung_ma_that_bai));
                                    txtDiscountStatus.setVisibility(View.VISIBLE);
                                } else {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        try {
                                            Date dateEnd = new SimpleDateFormat("dd-MM-yyyy").parse(documentSnapshot.getString("endDate"));
                                            Date dateNow = new Date();

                                            if (dateEnd.compareTo(dateNow) == -1) {
                                                txtDiscountStatus.setText(getResources().getString(R.string.ap_dung_ma_that_bai));
                                                txtDiscountStatus.setVisibility(View.VISIBLE);
                                                txtDiscount.setText(new HandleCurrency().handle((0)));
                                                txtTotal.setText(new HandleCurrency().handle(room.getPrice() * daysDiff));
                                            } else {
                                                idVoucher = documentSnapshot.getId();
                                                long fee = room.getPrice() * daysDiff;
                                                discount = documentSnapshot.getLong("number");
                                                long total = (long) (fee - (fee * discount * 0.01));
                                                txtDiscount.setText(new HandleCurrency().handle((long) (fee * discount * 0.01)));
                                                txtTotal.setText(new HandleCurrency().handle(total));
                                                txtDiscountStatus.setText(getResources().getString(R.string.ap_dung_ma_thanh_cong));
                                                txtDiscountStatus.setVisibility(View.VISIBLE);
                                                btnApply.setEnabled(false);
                                                btnApply.setBackgroundColor(getResources().getColor(R.color.grey));
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment payment = paymentAdapter.getItemSelected();
                choice = payment.getPaymentMethod();

                if (choice.equals(getString(R.string.pay_at_hotel))) {
                    addToBooked();
                } else {
                    if (check == 0) {
                        CreateOrder orderApi = new CreateOrder();

                        try {
                            long price = (long) (room.getPrice() * daysDiff - (room.getPrice() * daysDiff * discount * 0.01));

                            JSONObject data = orderApi.createOrder(String.valueOf(price));
                            String code = data.getString("return_code");

                            if (code.equals("1")) {

                                String token = data.getString("zp_trans_token");

                                ZaloPaySDK.getInstance().payOrder(Confirm1Activity.this, token, "demozpdk://app", new PayOrderListener() {
                                    @Override
                                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                        check = 1;
                                        btnConfirm.setText("Tiếp tục");
                                        addToBooked();
                                    }
                                    @Override
                                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                        ToastPerfect.makeText(Confirm1Activity.this, ToastPerfect.ERROR, getString(R.string.paymentcancel), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                        Log.d("err",zaloPayError.toString());
                                        ToastPerfect.makeText(Confirm1Activity.this, ToastPerfect.ERROR, getString(R.string.paymenterror), ToastPerfect.BOTTOM, ToastPerfect.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.d("err", e.getMessage());
                        }
                    } else {
                        Intent intent1 = new Intent(Confirm1Activity.this, Confirm2Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("hotel", mHotel);
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

    private void addToBooked() {
        Map<String, Object> booksMap = new HashMap<>();

        FieldValue timestamp = FieldValue.serverTimestamp();

        booksMap.put("addressHotel", mHotel.getFullAddress());
        booksMap.put("timestamp", timestamp);
        booksMap.put("idHotel", mHotel.getId());
        booksMap.put("idRoom", room.getId());
        booksMap.put("nameRoom", room.getName());
        booksMap.put("startDate", startDate);
        booksMap.put("endDate", endDate);
        booksMap.put("startTime", mHotel.getCheckInTime());
        booksMap.put("endTime", mHotel.getCheckOutTime());
        booksMap.put("daysdiff", daysDiff);
        booksMap.put("hotelName", mHotel.getName());
        booksMap.put("username", user.getName());
        booksMap.put("phonenumber", user.getPhoneNumber());
        booksMap.put("status", "Booked");
        booksMap.put("idUser",uid);

        if(choice.equals(getString(R.string.pay_at_hotel))){
            booksMap.put("paymentStatus",getString(R.string.trang_thai));
            booksMap.put("choice", "Pay at hotel");
        }else{
            booksMap.put("paymentStatus",getString(R.string.trang_thai_2));
            booksMap.put("choice", "Pay with Zalo Pay");
        }

        long price = (long) (room.getPrice() * daysDiff - (room.getPrice() * daysDiff * discount * 0.01));
        booksMap.put("price", price);

        db.collection("users/" + uid + "/booked").add(booksMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String id = task.getResult().getId();
                    booksMap.put("idBooking", id);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            if(!edtDiscount.getText().toString().equals("") && txtDiscountStatus.getText().toString().equals(R.string.ap_dung_ma_thanh_cong)) {
                                db.collection("users/" + uid + "/vouchers")
                                        .document(idVoucher)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }

                            HashMap<String, Object> notiMap = new HashMap<>();
                            notiMap.put("timestamp", timestamp);
                            notiMap.put("type", "booking");
                            notiMap.put("hasSeen", false);

                            db.collection("users/" + uid + "/notifications")
                                    .add(notiMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                }
                            });
                        }
                    });

                    db.collection("Hotels/" + mHotel.getId() + "/booked")
                            .document(task.getResult().getId()).set(booksMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Message message = new Message();
                                    Data data = new Data();
                                    data.setUserName("Uit Trip Notification");
                                    data.setDescription("[Booked] Your booking " + id + " is confirmed");
                                    message.setPriority("high");
                                    message.setData(data);
                                    message.setTo(user.getToken());

                                    Call<Message> repos = sendMessageApi.sendMessage(message);
                                    repos.enqueue(new Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {
                                            if (response.body() != null) {

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Message> call, Throwable t) {
                                            Log.d("Confirm1Activity", t.getMessage().toString());
                                        }
                                    });

                                    Intent intent1 = new Intent(Confirm1Activity.this, Confirm2Activity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("idBooking", id);
                                    intent1.putExtras(bundle);
                                    intent1.putExtra("hotel", mHotel);
                                    startActivity(intent1);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("err", e.toString());
                        }
                    });

                    try {
                        db.collection("partners")
                                .whereEqualTo("idHotel", String.valueOf(mHotel.getId()))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot doc : task.getResult()) {
                                            Message message = new Message();
                                            Data data = new Data();
                                            data.setUserName("Uit Trip Notification");
                                            data.setDescription("Có 1 khách hàng đặt phòng");
                                            message.setPriority("high");
                                            message.setData(data);
                                            message.setTo(doc.getString("token"));

                                            Call<Message> repos = sendMessageApi.sendMessage(message);
                                            repos.enqueue(new Callback<Message>() {
                                                @Override
                                                public void onResponse(Call<Message> call, Response<Message> response) {
                                                    if (response.body() != null) {

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Message> call, Throwable t) {
                                                    Log.d("Confirm1Activity", t.getMessage().toString());
                                                }
                                            });

                                            HashMap<String, Object> notiMap = new HashMap<>();
                                            notiMap.put("timestamp", timestamp);
                                            notiMap.put("type", "booking");
                                            notiMap.put("hasSeen", false);
                                            notiMap.put("room",room.getName());

                                            db.collection("partners/" + doc.getId() + "/notifications")
                                                    .add(notiMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                                }
                                            });
                                        }
                                    }
                                });


                    }catch (Exception e){
                        Log.d("err",e.getMessage());
                    }
                } else {

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
