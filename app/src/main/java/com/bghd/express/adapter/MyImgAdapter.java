package com.bghd.express.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bghd.express.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by lixu on 2018/1/29.
 */

public class MyImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int IMAGE_MAX_NUM = 2;
    private Context context;
    private ArrayList<String> itemsEntities;

    public MyImgAdapter(Context context, ArrayList<String> itemsEntities) {
        this.context = context;
        this.itemsEntities = itemsEntities;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onAddClick(View view, int position);
        void onDeleteClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemsEntities.size() < IMAGE_MAX_NUM ? itemsEntities.size() + 1 : itemsEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent,
                false);
        return new ItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.d("qqqq","```"+position);
        if (position < itemsEntities.size()) {
            ((ItemViewHolder) holder).ivDelete.setVisibility(View.VISIBLE);
            String url = itemsEntities.get(position);

            Glide.with(context)
                    .load(url)
                    .into(((ItemViewHolder) holder).ivImg);
            if (onItemClickListener != null) {
                ((ItemViewHolder) holder).llImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();

                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                ((ItemViewHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onDeleteClick(holder.itemView, position);
                    }
                });
            }
        }else {
            ((ItemViewHolder) holder).ivDelete.setVisibility(View.GONE);
            ((ItemViewHolder) holder).ivImg.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.icon_add_pic));
            if (onItemClickListener != null) {
                ((ItemViewHolder) holder).llImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onAddClick(holder.itemView, position);
                    }
                });
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout llImg;
        ImageView ivImg;
        ImageView ivDelete;


        public ItemViewHolder(View view) {
            super(view);
            llImg = (RelativeLayout) findView(llImg, R.id.ll_img);
            ivImg = (ImageView) findView(ivImg, R.id.item_img);
            ivDelete = (ImageView) findView(ivDelete, R.id.item_img_delete);

        }

        protected final View findView(View view, int resid) {
            if (resid > 0 && itemView != null && view == null) {
                return itemView.findViewById(resid);
            }
            return view;
        }

    }

    public void updateData(ArrayList<String> datas) {
        this.itemsEntities.clear();
        this.itemsEntities.addAll(datas);
        notifyDataSetChanged();
    }
}