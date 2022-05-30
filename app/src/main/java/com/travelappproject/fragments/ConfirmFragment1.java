package com.travelappproject.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelappproject.R;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shuhart.stepview.StepView;
import com.travelappproject.activities.ConfirmActivity;
import com.travelappproject.adapter.PaymentAdapter;
import com.travelappproject.helperforzalopay.CreateOrder;
import com.travelappproject.model.hotel.Payment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ConfirmFragment1 extends Fragment {
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView rcvPaymentMethod;
    ConfirmActivity confirmActivity;
    TextView txtPrice;


    public ConfirmFragment1() {
        // Required empty public constructor
    }

    public static ConfirmFragment1 newInstance(String param1, String param2) {
        ConfirmFragment1 fragment = new ConfirmFragment1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        confirmActivity = (ConfirmActivity) getActivity();
        if (getArguments() != null) {
        }

        ZaloPaySDK.init(2554, Environment.SANDBOX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.app_bar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        toolbar = view.findViewById(R.id.toolbar);
        txtPrice = view.findViewById(R.id.txtPrice);


        initToolBar();

        StepView stepView = view.findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.done(true);
        stepView.go(1,true);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateOrder orderApi = new CreateOrder();
                try{
                    JSONObject data = orderApi.createOrder(txtPrice.getText().toString());
                    String token = data.getString("zp_trans_token");
                    ZaloPaySDK.getInstance().payOrder(getActivity(), token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {

                        }

                        @Override
                        public void onPaymentCanceled(String zpTransToken, String appTransID) {

                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                            //Toast.makeText(getActivity(), "Thanh toán không thành công.", Toast.LENGTH_SHORT).show();
                            if (zaloPayError == ZaloPayError.PAYMENT_APP_NOT_FOUND) {
                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("Lỗi thanh toán")
                                                .setMessage("Ứng dụng ZaloPay chưa được cài đặt trên thiết bị này.")
                                                .setPositiveButton("Mở cửa hàng ứng dụng", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ZaloPaySDK.getInstance().navigateToZaloPayOnStore(getContext());
                                                    }
                                                })
                                                .setNegativeButton("Trở về", null).show();

                                    }
                                }, 500);
                            } else {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Lỗi thanh toán")
                                        .setMessage(String.format("Mã lỗi: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Thoát", null).show();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        rcvPaymentMethod = view.findViewById(R.id.rcvPaymentMethod);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcvPaymentMethod.setLayoutManager(linearLayoutManager1);
        PaymentAdapter paymentAdapter = new PaymentAdapter(getContext());
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(new Payment("Payment at hotel",R.drawable.review));
        paymentList.add(new Payment("Payment with Zalo Pay",R.drawable.zalopay));
        paymentAdapter.setData(paymentList);
        rcvPaymentMethod.setAdapter(paymentAdapter);

        TextView txtBookingType, txtHotelName, txtRoomType,txtPrice,txtCancelPolicy;

        txtBookingType = view.findViewById(R.id.txtBookingType);
        txtHotelName = view.findViewById(R.id.nameHotel);
        txtRoomType = view.findViewById(R.id.txtTypeRoom);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtCancelPolicy = view.findViewById(R.id.txtCancelPolicy);

        txtCancelPolicy.setText(confirmActivity.room.getCancelPolicies());
        txtPrice.setText(String.valueOf(confirmActivity.room.getPrice() * confirmActivity.daysDiff));
        txtHotelName.setText(confirmActivity.hotelName);
        txtRoomType.setText(confirmActivity.room.getName());
        txtBookingType.setText(String.valueOf(confirmActivity.daysDiff) + " night");
    }

    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

}