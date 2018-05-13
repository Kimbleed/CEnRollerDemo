package com.example.a49479.cenrollerdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 49479 on 2018/5/10.
 */

public class RollerAdapter extends RecyclerView.Adapter<RollerAdapter.RollerViewHolder> {

    private String[] mCells = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private LayoutInflater mInflater;

    private int mLayoutRes = -1;

    public RollerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public RollerAdapter(Context context,int layoutRes) {
        mInflater = LayoutInflater.from(context);
        mLayoutRes = layoutRes;
    }

    @Override
    public RollerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutRes==-1?R.layout.layout_cell:mLayoutRes, parent, false);
        RollerViewHolder viewHolder = new RollerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RollerViewHolder holder, int position) {
        holder.tv.setText(mCells[position % mCells.length]);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    class RollerViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public RollerViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

    }

}