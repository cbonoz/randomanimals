package com.randomanimals.www.randomanimals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomanimals.www.randomanimals.R;
import com.randomanimals.www.randomanimals.models.Animal;

import java.util.List;

/**
 * Created by cbuonocore on 3/18/17.
 */
public abstract class PlaysAdapter extends RecyclerView.Adapter<PlaysAdapter.ViewHolder> {
    protected List<Animal> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView playView;
        public TextView countView;
        public ViewHolder(View v) {
            super(v);
            playView = (TextView) v.findViewById(R.id.playView);
            countView = (TextView) v.findViewById(R.id.countView);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    // was String[]
    public PlaysAdapter(List<Animal> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.play_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

