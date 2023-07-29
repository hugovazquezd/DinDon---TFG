package com.example.dindon.helpers.DiscoverHelpers;

import androidx.recyclerview.widget.DiffUtil;

import com.example.dindon.dtofront.ItemModel;

import java.util.List;
import java.util.Objects;

public class CardStackCallback extends DiffUtil.Callback {

    private final List<ItemModel> old, baru;

    public CardStackCallback(List<ItemModel> old, List<ItemModel> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (!old.get(oldItemPosition).getPiso().getImagenes().isEmpty()) {
            return Objects.equals(old.get(oldItemPosition).getPiso().getImagenes().get(0).getId(), baru.get(newItemPosition).getPiso().getId());
        } else {
            return false;
        }
    }


    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
