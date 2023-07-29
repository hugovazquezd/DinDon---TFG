package com.example.dindon.helpers.DiscoverHelpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dindon.MainActivity1;
import com.example.dindon.R;
import com.example.dindon.bbddconection.BBDDManager;
import com.example.dindon.dtofront.User;
import com.example.dindon.helpers.CONSTANTS;
import com.example.dindon.helpers.LoginHelper.PreferencesManager;

import java.util.List;
import java.util.logging.Logger;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {

    private List<User> userList;
    private Context context;
    private static final String TAG = "DiscoverAdapter";

    public DiscoverAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peticion, parent, false);
        return new DiscoverViewHolder(view);
    }

    private void aceptarPeticion(User user, String userName) {
        BBDDManager bbddManager = new BBDDManager(context);
        user.getFriendsRequest().remove(userName);
        user.getFriends().add(userName);
        bbddManager.updateUserB(user.getId(), user,
                v -> {
                    Logger.getLogger(TAG).info("Petición de amistad aceptada");     //reinicio la actividad para que se actualice la lista de peticiones
                    Intent intent = new Intent(context, MainActivity1.class);
                    context.startActivity(intent);
                },
                error -> Logger.getLogger(TAG).

                        warning("Error al aceptar la petición de amistad")
        );
        //y ahora añado al usuario actual a la lista de amigos del usuario que ha aceptado la petición
        bbddManager.getUserById(
                user1 -> {
                    user1.getFriends().add(user.getId());
                    bbddManager.updateUserB(user1.getId(), user1,
                            v -> {
                                Logger.getLogger(TAG).info("Petición de amistad aceptada");     //reinicio la actividad para que se actualice la lista de peticiones
                                Intent intent = new Intent(context, MainActivity1.class);
                                context.startActivity(intent);

                            },
                            error -> Logger.getLogger(TAG).warning("Error al aceptar la petición de amistad")
                    );
                }, error -> Logger.getLogger(TAG).

                        warning("Error al cargar los datos del usuario"),

                userName
        );
    }

    private void rechazarPeticion(User user, String userName) {
        BBDDManager bbddManager = new BBDDManager(context);
        user.getFriendsRequest().remove(userName);
        bbddManager.updateUserB(user.getId(), user,
                v -> {
                    Logger.getLogger(TAG).info("Petición de amistad rechazada");
                    Intent intent = new Intent(context, MainActivity1.class);
                    context.startActivity(intent);
                },
                error -> Logger.getLogger(TAG).warning("Error al rechazar la petición de amistad")
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        User currentUser = userList.get(position);
        String userName = currentUser.getNombre();
        holder.tvUserName.setText(userName);
        // cargar la imagen en el ImageView
        ImageView imageView = holder.imageView;
        BBDDManager bbddManager = new BBDDManager(context);
        PreferencesManager preferencesManager = new PreferencesManager(context);
        bbddManager.getUserById(
                (User us) -> setFotoPerfil(us.getEmail(), imageView, context),
                error -> Logger.getLogger(TAG).warning("Error al cargar la imagen del usuario"),
                currentUser.getId()
        );

        bbddManager.getUserData(
                user -> {
                    holder.btnAccept.setOnClickListener(v -> aceptarPeticion(user, currentUser.getId()));
                    holder.btnReject.setOnClickListener(v -> rechazarPeticion(user, currentUser.getId()));
                },
                error -> Logger.getLogger(TAG).warning("Error al cargar los datos del usuario"),
                preferencesManager.getUserEmail()
        );


    }


    private void setFotoPerfil(String ruta, ImageView imageView, Context context) {
        if (ruta != null && !ruta.isEmpty()) {
            String urlNueva = CONSTANTS.URL_CONEXION + "/users/imagenes/" + ruta;
            Glide.with(context)
                    .load(urlNueva)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Glide", "La carga de la imagen falló", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .placeholder(0)
                    .error(0)
                    .dontAnimate()
                    .into(imageView);
        } else {
            // Si la ruta es nula o está vacía, puedes establecer una imagen por defecto
            imageView.setImageResource(R.drawable.go);
        }
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView btnAccept;
        ImageView btnReject;
        TextView tvUserName;

        public DiscoverViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}
