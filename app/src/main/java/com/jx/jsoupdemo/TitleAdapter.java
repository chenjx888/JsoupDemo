package com.jx.jsoupdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder> {

    private Context mContext;
    private List<TitleBean> mData;

    public TitleAdapter(Context context, List<TitleBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_title, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final TitleBean bean = mData.get(i);
        holder.tv_title.setText(bean.getName());
        holder.tv_author.setText(bean.getAuthor());
        holder.tv_time.setText("发布于" + bean.getTime());
        holder.tv_plate.setText(bean.getPlate());
        holder.tv_count_label.setText(bean.getReplayCount() + " / " + bean.getLookCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.start(mContext, bean.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_author;
        private TextView tv_time;
        private TextView tv_plate;
        private TextView tv_count_label;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_plate = itemView.findViewById(R.id.tv_plate);
            tv_count_label = itemView.findViewById(R.id.tv_count_label);
        }
    }

}
