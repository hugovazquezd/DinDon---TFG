package com.example.dindon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.User;
import com.example.dindon.helpers.ChatHelpers.ChatAdapter;
import com.example.dindon.helpers.DiscoverHelpers.DiscoverAdapter;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Discover1 extends Fragment {

    private List<User> friendsRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Sin peticiones:
        TextView emptyTextView = new TextView(this.getContext());
        configureTextView(emptyTextView, view);

        // Aquí asumimos que tienes una lista de usuarios llamada "userList"
        PreferencesManager preferencesManager = new PreferencesManager(this.requireContext());
        String email = preferencesManager.getUserEmail();
        BBDDManager bbddManager = new BBDDManager(this.getContext());
        setRequests(chatRecyclerView, emptyTextView, email, bbddManager);
        friendsRequest = new ArrayList<>();

        return view;
    }

    private void setRequests(RecyclerView chatRecyclerView, TextView emptyTextView, String email, BBDDManager bbddManager) {
        bbddManager.getUserData(
                (User user) -> {
                    if (user.getFriendsRequest() != null) {
                        if (!user.getFriendsRequest().isEmpty()) {
                            // contador para rastrear cuándo se han completado todas las operaciones asincrónicas
                            final int[] counter = {user.getFriendsRequest().size()};
                            //recorro la lista de peticiones
                            for (String request : user.getFriendsRequest()) {
                                bbddManager.getUserById(
                                        (User requestUser) -> {
                                            friendsRequest.add(requestUser);
                                            counter[0]--;
                                            // si todas las operaciones asincrónicas han terminado, configura el adaptador y la visibilidad del TextView
                                            if (counter[0] == 0) {
                                                DiscoverAdapter chatAdapter = new DiscoverAdapter(friendsRequest, this.getContext());
                                                chatRecyclerView.setAdapter(chatAdapter);
                                                //Si no hay amigos:
                                                if (friendsRequest.isEmpty()) {
                                                    emptyTextView.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        },
                                        error -> Logger.getLogger("Chat_fragment").warning("El error es: " + error.getMessage()), request);
                            }
                        } else {
                            // Si la lista de peticiones está vacía, mostramos el TextView
                            emptyTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Si la lista de peticiones es null, mostramos el TextView
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                },
                error -> Logger.getLogger("Chat_fragment").warning("El error es: " + error.getMessage()), email);

    }

    private void configureTextView(TextView emptyTextView, View view) {
        emptyTextView.setText(R.string.noPeticiones);
        emptyTextView.setTextSize(24);
        emptyTextView.setTextColor(this.getResources().getColor(R.color.black));

        // Create RelativeLayout.LayoutParams to center the TextView
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        emptyTextView.setLayoutParams(params);

        // Add the TextView to the container
        ((ViewGroup) view).addView(emptyTextView);

        // Hide the TextView by default
        emptyTextView.setVisibility(View.GONE);
    }
}