package com.example.dindon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dindon.dtofront.User;
import com.example.dindon.helpers.ChatHelpers.ChatAdapter;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ChatFragment extends Fragment {

    private List<String> friends;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView emptyTextView = new TextView(this.getContext());
        configureTextView(emptyTextView, view);
        PreferencesManager preferencesManager = new PreferencesManager(this.requireContext());
        String email = preferencesManager.getUserEmail();
        BBDDManager bbddManager = new BBDDManager(this.getContext());
        setFriends(chatRecyclerView, emptyTextView, email, bbddManager);
        friends = new ArrayList<>();
        return view;
    }

    public void setFriends(RecyclerView chatRecyclerView, TextView emptyTextView, String email, BBDDManager bbddManager) {
        bbddManager.getUserData(
                (User user) -> {
                    final int[] counter = {user.getFriends().size()};
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
                                error -> Logger.getLogger("Chat_fragment").warning("El error es: " + error.getMessage()), friend);
                    }
                },
                error -> Logger.getLogger("Chat_fragment").warning("El error es: " + error.getMessage()), email);

    }

    private void configureTextView(TextView emptyTextView, View view) {
        emptyTextView.setText(R.string.noFriends);
        emptyTextView.setTextSize(24);
        emptyTextView.setTextColor(this.getResources().getColor(R.color.black));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        emptyTextView.setLayoutParams(params);

        ((ViewGroup) view).addView(emptyTextView);

        emptyTextView.setVisibility(View.GONE);
    }
}
