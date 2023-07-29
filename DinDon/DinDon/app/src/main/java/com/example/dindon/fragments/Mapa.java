package com.example.dindon.fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;


import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.dindon.helpers.MapHelper;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;
import com.squareup.picasso.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class Mapa extends Fragment implements SearchView.OnSuggestionListener {
    MapHelper mapHelper = new MapHelper();
    private CursorAdapter suggestionAdapter;

    public Mapa() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        MapView map = view.findViewById(R.id.map);
        mapHelper.configuracionMapa(map, getContext(), getActivity());

        BBDDManager bbddManager = new BBDDManager(getContext());
        bbddManager.getAllPisos(p -> mapHelper.addMarkers(p, map, this.getActivity()), Throwable::printStackTrace);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnClickListener(v -> searchView.setIconified(false));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mapHelper.realizarBusqueda(query, map, getContext());
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                mapHelper.buscarCiudad(newText, view.getContext(), addresses -> {
                    List<String> cityNames = new ArrayList<>();
                    for (Address address : addresses) {
                        String cityName = address.getLocality();
                        if (cityName != null && !cityName.isEmpty()) {
                            cityNames.add(cityName);
                        }
                    }

                    String[] from = {SearchManager.SUGGEST_COLUMN_TEXT_1};
                    int[] to = {android.R.id.text1};
                    suggestionAdapter = new SimpleCursorAdapter(view.getContext(),
                            android.R.layout.simple_list_item_1, null, from, to,
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


                    searchView.setSuggestionsAdapter(suggestionAdapter);

                    MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1});
                    for (int i = 0; i < cityNames.size(); i++) {
                        cursor.addRow(new Object[]{i, cityNames.get(i)});
                    }

                    suggestionAdapter.changeCursor(cursor);

                    searchView.setOnSuggestionListener(Mapa.this);
                });

                return false;
            }
        });
        return view;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor = suggestionAdapter.getCursor();
        SearchView searchView = this.requireView().findViewById(R.id.searchView);
        if (cursor != null && cursor.moveToPosition(position)) {
            @SuppressLint("Range") String suggestion = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
            searchView.setQuery(suggestion, true);
        }
        return true;
    }

}

