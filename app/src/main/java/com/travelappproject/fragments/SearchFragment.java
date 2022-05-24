package com.travelappproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.HttpRequester;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchConfig;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.HttpRequest;
import com.algolia.search.models.HttpResponse;
import com.algolia.search.models.RequestOptions;
import com.algolia.search.models.indexing.Query;
import com.algolia.search.models.indexing.SearchResult;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;

import com.algolia.search.saas.Index;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelappproject.R;
import com.travelappproject.adapter.HotelSearchAdapter;
import com.travelappproject.model.hotel.Hotel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SearchFragment extends Fragment {
    EditText edtSearch;
    RecyclerView rcvSearch;
    List<Hotel> hotelList;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtSearch = view.findViewById(R.id.edtSearch);
        rcvSearch = view.findViewById(R.id.rcvSearch);
        hotelList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearch.setLayoutManager(linearLayoutManager);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SearchClient client =
                        DefaultSearchClient.create("U7G2YLYBKL", "0a97530f04c788c4a0c55be9bc98d85d");
//                SearchConfig config = new SearchConfig.Builder("U7G2YLYBKL", "0a97530f04c788c4a0c55be9bc98d85d").build();
//                SearchClient client = new SearchClient(config, httpRequester);

                SearchIndex<Hotel> index = client.initIndex("Hotels", Hotel.class);
//                Query query = new Query(editable.toString())
//                        .setAttributesToRetrieve(Arrays.asList("fullAddress", "nameVi"))
//                        .setHitsPerPage(50);
//                SearchResult<Hotel> search1 = index.search(new Query(editable.toString()));

                SearchResult<Hotel> search2 = index.search(new Query(editable.toString())
                        .setAttributesToRetrieve(Arrays.asList("fullAddress", "nameVi"))
                        .setHitsPerPage(50));

                hotelList = search2.getHits();
                HotelSearchAdapter hotelSearchAdapter = new HotelSearchAdapter(hotelList, getContext());
                rcvSearch.setAdapter(hotelSearchAdapter);

//                ObjectMapper objectMapper = new ObjectMapper();

//                index.searchAsync(query,null, new CompletionHandler(){
//                    @Override
//                    public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
//                        try{
//                            JSONArray hits = jsonObject.getJSONArray("Hotels");
//                            List<Hotel> hotelList = new ArrayList<>();
//                            for(int i = 0; i < hits.length(); i++){
//                                JSONObject jsonObject1 = hits.getJSONObject(i);
//                                Hotel hotel = objectMapper.readValue(jsonObject1.getJSONObject("Hotels").toString(), Hotel.class);
//                            }
//                        } catch (JSONException jsonException) {
//                            jsonException.printStackTrace();
//                        } catch (JsonMappingException jsonMappingException) {
//                            jsonMappingException.printStackTrace();
//                        } catch (JsonProcessingException jsonProcessingException) {
//                            jsonProcessingException.printStackTrace();
//                        }
//                    }
//                });
            }
        });
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}