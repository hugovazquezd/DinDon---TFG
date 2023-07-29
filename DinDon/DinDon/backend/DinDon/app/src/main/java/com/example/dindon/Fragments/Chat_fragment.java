package com.example.dindon.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dindon.DTOFront.User;
import com.example.dindon.Helpers.ChatHelpers.ChatAdapter;
import com.example.dindon.Helpers.LoginHelper.PreferencesManager;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;

import java.util.ArrayList;
import java.util.List;

public class Chat_fragment extends Fragment {

    private List<String> friends;

    public Chat_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create the "No friends" TextView
        TextView emptyTextView = new TextView(this.getContext());
        configureTextView(emptyTextView, view);
        // Aquí asumimos que tienes una lista de usuarios llamada "userList"
        PreferencesManager preferencesManager = new PreferencesManager(this.requireContext());
        String email = preferencesManager.getUserEmail();
        BBDDManager bbddManager = new BBDDManager(this.getContext());
        setFriends(chatRecyclerView, emptyTextView, email, bbddManager);
        friends = new ArrayList<>();
        return view;
    }

    private void setFriends(RecyclerView chatRecyclerView, TextView emptyTextView, String email, BBDDManager bbddManager) {
        bbddManager.getUserData(
                (User user) -> {
                    // contador para rastrear cuándo se han completado todas las operaciones asincrónicas
                    final int[] counter = {user.getFriends().size()};
                    //recorro la lista de amigos
                    for (String friend : user.getFriends()) {
                        bbddManager.getUserById(
                                (User friendUser) -> {
                                    friends.add(friendUser.getNombre());
                                    counter[0]--;
                                    // si todas las operaciones asincrónicas han terminado, configura el adaptador y la visibilidad del TextView
                                    if (counter[0] == 0) {
                                        ChatAdapter chatAdapter = new ChatAdapter(getActivity(), friends, friendUser.getEmail(), user.getId(), friendUser.getId());
                                        chatRecyclerView.setAdapter(chatAdapter);
                                        //Si no hay amigos:
                                        if (friends.isEmpty()) {
                                            emptyTextView.setVisibility(View.VISIBLE);
                                        }
                                    }
                                },
                                error -> System.out.println("El error es: " + error.getMessage()), friend);
                    }
                },
                error -> System.out.println("El error es: " + error.getMessage()), email);

    }

    private void configureTextView(TextView emptyTextView, View view) {
        emptyTextView.setText(R.string.noFriends);
        emptyTextView.setTextSize(24);
        emptyTextView.setTextColor(this.getResources().getColor(R.color.black));

        // Create RelativeLayout.LayoutParams to center the TextView
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        emptyTextView.setLayoutParams(params);

        // Add the TextView to the container
        ((ViewGroup) view).addView(emptyTextView);

        // Hide the TextView by default
        emptyTextView.setVisibility(View.GONE);
    }
}
