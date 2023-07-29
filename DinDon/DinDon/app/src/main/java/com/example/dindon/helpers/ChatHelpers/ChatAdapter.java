package com.example.dindon.helpers.ChatHelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dindon.Chat;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final Context context;
    private final List<String> userList;
    private String mail;
    String friendName, id, friendId;

    public ChatAdapter(Context context, List<String> userList, String mail, String id, String friendId) {
        this.context = context;
        this.userList = userList;
        this.mail = mail;
        this.id = id;
        this.friendId = friendId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        ChatViewHolder viewHolder = new ChatViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                // Aquí puedes obtener el amigo asociado con este ViewHolder
                String friend = friendName;

                Intent intent = new Intent(context, Chat.class);
                // Aquí puedes poner extras en el intent para pasar datos a la nueva actividad
                intent.putExtra("friend", friend);
                intent.putExtra("user", id);
                intent.putExtra("friendId", friendId);
                context.startActivity(intent);
            }
        });
        return new ChatViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        friendName = userList.get(position);

        holder.userNameTextView.setText(friendName);

        String friendEmail = mail;

        setFotoPerfil(friendEmail, holder.userImageView, context);
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        ImageView userImageView;
        TextView userNameTextView;
        TextView lastMessageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            userImageView = itemView.findViewById(R.id.chatImage);
            userNameTextView = itemView.findViewById(R.id.chatName);;
        }
    }

    private void setFotoPerfil(String ruta, ImageView imageView, Context context) {
        String urlNueva = CONSTANTS.URL_CONEXION + "/users/imagenes/" + ruta;
        System.out.println(urlNueva);
        Glide.with(context)
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);
    }
}
