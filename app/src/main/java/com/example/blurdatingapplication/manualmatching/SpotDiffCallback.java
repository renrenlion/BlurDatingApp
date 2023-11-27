package com.example.blurdatingapplication.manualmatching;

import androidx.recyclerview.widget.DiffUtil;
import java.util.List;

public class SpotDiffCallback extends DiffUtil.Callback {

    private List<Spot> oldList; // Data for the old list
    private List<Spot> newList; // Data for the new list

    public SpotDiffCallback(List<Spot> oldList, List<Spot> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size(); // Returns the number of items in the old list
    }

    @Override
    public int getNewListSize() {
        return newList.size(); // Returns the number of items in the new list
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Checks whether items are the same by comparing their IDs
        return oldList.get(oldItemPosition).getNumber() == newList.get(newItemPosition).getNumber();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Checks whether the contents of items are the same using the equals method
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
