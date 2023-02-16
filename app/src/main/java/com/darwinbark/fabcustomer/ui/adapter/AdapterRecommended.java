package com.darwinbark.fabcustomer.ui.adapter;

/**
 * Created by VARUN on 01/01/19.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.AdapterRecommendedBinding;
import com.darwinbark.fabcustomer.dto.HomeRecomendedDTO;

import java.util.ArrayList;

public class AdapterRecommended extends RecyclerView.Adapter<AdapterRecommended.MyViewHolder> {

    Context mContext;
    ArrayList<HomeRecomendedDTO> recomendedDTOArrayList;
    AdapterRecommendedBinding binding;
    LayoutInflater layoutInflater;

    public AdapterRecommended(Context mContext, ArrayList<HomeRecomendedDTO> recomendedDTOArrayList) {
        this.mContext = mContext;
        this.recomendedDTOArrayList = recomendedDTOArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_recommended, parent, false);
        View itemView = binding.getRoot();
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.binding.setHomeRecommendedDTO(recomendedDTOArrayList.get(position));

        Glide.with(mContext).
                load(recomendedDTOArrayList.get(position).getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.cIvImage);
    }

    @Override
    public int getItemCount() {
        return recomendedDTOArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterRecommendedBinding binding;

        public MyViewHolder(AdapterRecommendedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}