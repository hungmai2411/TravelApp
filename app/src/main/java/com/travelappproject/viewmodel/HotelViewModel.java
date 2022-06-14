package com.travelappproject.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.model.hotel.Banner;
import com.travelappproject.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelViewModel extends ViewModel {
    private MutableLiveData<List<Hotel>> listHotelLiveData;
    private MutableLiveData<List<Hotel>> listHotHotelLiveData;
    private MutableLiveData<Banner> bannerMutableLiveData;
    private int limit = 15;

    List<Hotel> listHotel = new ArrayList<>();
    List<Hotel> listTmp;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HotelViewModel() {
        bannerMutableLiveData = new MutableLiveData<>();
        listHotelLiveData = new MutableLiveData<List<Hotel>>();
        listHotHotelLiveData = new MutableLiveData<List<Hotel>>();
    }

    public interface IListenerListHotel {
        void onCallBack(List<Hotel> list);
    }

    public void getBanner(){
        db.collection("banners").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() == 0)
                    return;

                for (DocumentSnapshot doc : task.getResult()) {
                    Banner banner = doc.toObject(Banner.class);
                    bannerMutableLiveData.postValue(banner);
                }

            }
        });
    }

    public void getListHotel(String state, IListenerListHotel iListenerListHotel) {
        listHotel = new ArrayList<>();

        db.collection("Hotels")
                .whereEqualTo("provinceName", state)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listHotel.add(document.toObject(Hotel.class));
                            }
                            iListenerListHotel.onCallBack(listHotel);
                        } else {
                            Log.d("HOMEVM", "Error getting documents: ", task.getException());
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

    public void getList(String state) {
        getListHotel(state, new IListenerListHotel() {
            @Override
            public void onCallBack(List<Hotel> list) {
                listHotelLiveData.postValue(list);
            }
        });
    }

    public LiveData<List<Hotel>> observedHotelLiveData() {
        return listHotelLiveData;
    }
    public LiveData<Banner> observedBannerLiveData() {
        return bannerMutableLiveData;
    }

}

