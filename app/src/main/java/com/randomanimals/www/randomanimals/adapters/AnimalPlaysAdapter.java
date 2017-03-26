package com.randomanimals.www.randomanimals.adapters;

import android.graphics.Typeface;

import com.randomanimals.www.randomanimals.models.Animal;
import com.randomanimals.www.randomanimals.services.HelperUtil;

import java.util.List;

/**
 * Created by cbuonocore on 3/18/17.
 */

public class AnimalPlaysAdapter extends PlaysAdapter {

    public AnimalPlaysAdapter(List<Animal> animals) {
        super(animals);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // convert animal to String play count.
        holder.playView.setText(mDataset.get(position).animal + ": ");
        holder.playView.setTypeface(null, Typeface.BOLD);
        final int count = mDataset.get(position).count;
        final String countString = HelperUtil.getCountStringFromCount(count);
        holder.countView.setText(countString);

    }
}
