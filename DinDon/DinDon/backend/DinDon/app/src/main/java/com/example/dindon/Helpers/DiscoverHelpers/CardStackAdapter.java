package com.example.dindon.Helpers.DiscoverHelpers;

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
import com.example.dindon.DTOFront.ImageMetadata;
import com.example.dindon.DTOFront.Pisos;
import com.example.dindon.Fragments.main_discover;
import com.example.dindon.Helpers.CONSTANTS;
import com.example.dindon.DTOFront.ItemModel;
import com.example.dindon.R;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<ItemModel> items;
    private Context context;

    public CardStackAdapter(List<ItemModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setData(items.get(position), context);

        // Agrega un escuchador de clics al cuadro de texto
        holder.descripcion.setOnClickListener(v -> {
            Intent intent = new Intent(context, main_discover.class);
            intent.putExtra("piso", items.get(position).getPiso());
            // Inicia la nueva Activity
            context.startActivity(intent);
        });
        holder.precio.setOnClickListener(v -> {
            Intent intent = new Intent(context, main_discover.class);
            intent.putExtra("piso", items.get(position).getPiso());
            // Inicia la nueva Activity
            context.startActivity(intent);
        });
        holder.direccion.setOnClickListener(v -> {
            Intent intent = new Intent(context, main_discover.class);
            intent.putExtra("piso", items.get(position).getPiso());
            // Inicia la nueva Activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView descripcion, direccion, precio;
        int currentImageIndex;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            descripcion = itemView.findViewById(R.id.descripcion);
            direccion = itemView.findViewById(R.id.direccion);
            precio = itemView.findViewById(R.id.precio);

            image.setOnClickListener(v -> {
                Pisos piso = items.get(getAdapterPosition()).getPiso();
                currentImageIndex = (currentImageIndex + 1) % piso.getImagenes().size();
                ImageMetadata img = piso.getImagenes().get(currentImageIndex);
                obtenerImagenDesdeServidor(img.getRuta(), image, context);
            });
        }

        @SuppressLint("SetTextI18n")
        void setData(ItemModel data, Context context) {
            Pisos piso = data.getPiso();
            if (piso.getImagenes() != null && !piso.getImagenes().isEmpty()) {
                ImageMetadata img = piso.getImagenes().get(0);
                obtenerImagenDesdeServidor(img.getRuta(), image, context);
            }
            descripcion.setText(piso.getDescripcion());
            direccion.setText(piso.getDireccion());
            precio.setText(piso.getPrecio() + "€");
        }
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }

    public void obtenerImagenDesdeServidor(String ruta, ImageView imageView, Context context) {
        String urlNueva = CONSTANTS.URL_CONEXION + "/pisos/" + ruta;
        Glide.with(context)
                .load(urlNueva)
                .placeholder(0)
                .error(0)
                .dontAnimate()
                .into(imageView);
    }
}
