package com.travelappproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.travelappproject.R;

import java.util.ArrayList;
import java.util.List;

public class ChooseLocationActivity extends AppCompatActivity {
    ListView lvProvince, lvDistrict;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lvProvince = findViewById(R.id.lvProvince);
        lvDistrict = findViewById(R.id.lvDistrict);

        List<String> listProvince = new ArrayList<>();
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listProvince);

        db.collection("regions")
                .orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listProvince.add(document.get("name").toString());
                                Log.d("HOMEVM", document.getId() + " => " + document.getData());
                            }
                            provinceAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("HOMEVM", "Error getting documents: ", task.getException());
                        }
                    }
                });

        lvProvince.setAdapter(provinceAdapter);

        List<String> listDistricts = new ArrayList<>();
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listDistricts);

        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int sn = i + 1;
                districtAdapter.clear();

                for (int index = 0; index < lvProvince.getCount(); index++) {
                    TextView text = (TextView) getViewByPosition(index,lvProvince);
                    text.setTextColor(Color.rgb(0, 0, 0));
                }

                TextView text = (TextView) getViewByPosition(i,lvProvince);
                text.setTextColor(Color.rgb(0,154,203));

                db.collection("regions/" + sn + "/districts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listDistricts.add(document.get("name").toString());
                                        Log.d("districts", document.getId() + " => " + document.getData());
                                    }
                                    districtAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("districts", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                lvDistrict.setAdapter(districtAdapter);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}