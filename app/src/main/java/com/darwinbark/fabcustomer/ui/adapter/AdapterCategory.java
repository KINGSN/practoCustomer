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
import com.darwinbark.fabcustomer.dto.HomeCategoryDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.AdapterCategoryBinding;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.fragment.DiscoverNearBy;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {

    Context mContext;
    ArrayList<HomeCategoryDTO> categoryDTOArrayList;
    AdapterCategoryBinding binding;
    LayoutInflater layoutInflater;
    private SharedPrefrence sharedPrefrence;

    public AdapterCategory(Context mContext, ArrayList<HomeCategoryDTO> categoryDTOArrayList) {
        this.mContext = mContext;
        this.categoryDTOArrayList = categoryDTOArrayList;
        sharedPrefrence = SharedPrefrence.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_category, parent, false);
        View itemView = binding.getRoot();
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.binding.setHomeCategoryDTO(categoryDTOArrayList.get(position));

        Glide.with(mContext).
                load(categoryDTOArrayList.get(position).getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.cIvImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((BaseActivity) mContext).ivFilter.setVisibility(View.VISIBLE);
                    ((BaseActivity) mContext).header.setVisibility(View.VISIBLE);
                    BaseActivity.navItemIndex = 1;
                    BaseActivity.CURRENT_TAG = BaseActivity.TAG_MAIN;
                    ((BaseActivity) mContext).loadHomeFragment(new DiscoverNearBy(), BaseActivity.CURRENT_TAG);

                    sharedPrefrence.setValue(Consts.VALUE, categoryDTOArrayList.get(position).getId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDTOArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryBinding binding;

        public MyViewHolder(AdapterCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}