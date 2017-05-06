package com.randomanimals.www.randomanimals.adapters;

import android.graphics.Typeface;

import com.randomanimals.www.randomanimals.models.Animal;
import com.randomanimals.www.randomanimals.services.NumberUtil;

import java.util.List;

public class UserPlaysAdapter extends PlaysAdapter {

    public UserPlaysAdapter(List<Animal> animals) {
        super(animals);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // convert animal to String play count.
        holder.playView.setText(mDataset.get(position).username + ": ");
        holder.playView.setTypeface(null, Typeface.BOLD);
        final int count = mDataset.get(position).count;
        final String countString = NumberUtil.getCountStringFromCount(count);
        holder.countView.setText(countString);
    }
}
