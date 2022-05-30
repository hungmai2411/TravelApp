package com.travelappproject.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.SearchResult;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.travelappproject.R;
import com.travelappproject.model.hotel.Hotel;
import com.travelappproject.model.hotel.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText edtSearch;
    Client client;
    ListView lvSearch;
    ArrayAdapter<String> adapterSearch;
    List<String> listSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        client = new Client("U7G2YLYBKL", "0a97530f04c788c4a0c55be9bc98d85d");

        btnBack = findViewById(R.id.btnBack);
        edtSearch = findViewById(R.id.edtSearch);
        lvSearch = findViewById(R.id.lvSearch);
        listSearch = new ArrayList<>();
        adapterSearch = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listSearch);
        lvSearch.setAdapter(adapterSearch);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listSearch.clear();
                adapterSearch.notifyDataSetChanged();

                Index index = client.getIndex("hotels");
                com.algolia.search.saas.Query query = new Query(editable.toString())
                        .setAttributesToRetrieve("*")
                        .setHitsPerPage(10)
                        .setPage(0);

                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                        try {
                            JSONArray hits = jsonObject.getJSONArray("hits");
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++){
                                JSONObject jsonObject1 = hits.getJSONObject(i);
                                String name = jsonObject1.getString("name");
                                listSearch.add(name);
                            }
                            adapterSearch.notifyDataSetChanged();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}