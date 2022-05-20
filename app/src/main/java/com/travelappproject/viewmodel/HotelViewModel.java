package com.travelappproject.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.model.hotel.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelViewModel extends ViewModel {
    private MutableLiveData<List<Hotel>> listHotelLiveData;
    private MutableLiveData<List<Hotel>> listHotHotelLiveData;
    private MutableLiveData<List<Hotel>> listNewHotelLiveData;

    List<Hotel> listHotel = new ArrayList<>();
    List<Hotel> listHot;
    List<Hotel> listNew;
    List<Hotel> listTmp;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HotelViewModel() {
        listHotelLiveData = new MutableLiveData<List<Hotel>>();
        listHotHotelLiveData = new MutableLiveData<List<Hotel>>();
        listNewHotelLiveData = new MutableLiveData<List<Hotel>>();
    }

    boolean isExisted(long snHotel) {
        for (int i = 0; i < listTmp.size(); i++) {
            if (listTmp.get(i).getId() == snHotel) {
                return true;
            }
        }

        return false;
    }

    public void getListHotHotel(String state, IListenerListHotel iListenerListHotel) {
        listHot = new ArrayList<>();

        db.collection("hotels")
                .whereGreaterThan("starRating", 4)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listHot.add(document.toObject(Hotel.class));
                            }
                            iListenerListHotel.onCallBack(listHot);
                        } else {
                            Log.d("HOMEVM", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getListNewHotel(String state, IListenerListHotel iListenerListHotel) {
        listNew = new ArrayList<>();

        db.collection("hotels")
                .whereEqualTo("newHotel", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listNew.add(document.toObject(Hotel.class));
                                Log.d("HOMEVM", document.getId() + " => " + document.getData());
                            }
                            iListenerListHotel.onCallBack(listNew);
                        } else {
                            Log.d("HOMEVM", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public interface IListenerListHotel {
        void onCallBack(List<Hotel> list);
    }

    public void getListHotel(String state, IListenerListHotel iListenerListHotel) {
        listHotel = new ArrayList<>();

        db.collection("hotels")
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
                });
    }

    public void getListHot(String state) {
        getListHotHotel(state, new IListenerListHotel() {
            @Override
            public void onCallBack(List<Hotel> list) {
                listHotHotelLiveData.postValue(list);
            }
        });
    }

    public void getListNew(String state) {
        getListNewHotel(state, new IListenerListHotel() {
            @Override
            public void onCallBack(List<Hotel> list) {
                listNewHotelLiveData.postValue(list);
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

    public LiveData<List<Hotel>> observedHotHotelLiveData() {
        return listHotHotelLiveData;
    }

    public LiveData<List<Hotel>> observedNewHotelLiveData() {
        return listNewHotelLiveData;
    }
}

