package com.jx.jsoupdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private Context mContext;
    private List<DetailBean> mData;

    public DetailAdapter(Context context, List<DetailBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        DetailBean bean = mData.get(i);
        Glide.with(mContext).load(bean.getAvatar()).into(holder.iv_avatar);
        holder.tv_name.setText(bean.getName());
        holder.tv_time.setText(bean.getTime());

        holder.tv_content.setText(Html.fromHtml(bean.getContent(), new URLImageParser(holder.tv_content, mContext), null));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;
        private ImageView iv_avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_content = itemView.findViewById(R.id.tv_content);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
        }
    }

}
