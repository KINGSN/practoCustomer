package com.darwinbark.fabcustomer.ui.adapter;
/**
 * Created by VARUN on 01/01/19.
 */

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darwinbark.fabcustomer.databinding.AdapterChatListBinding;
import com.darwinbark.fabcustomer.dto.ChatListDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.ui.activity.OneTwoOneChat;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import java.util.ArrayList;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ChatListDTO> chatList;
    AdapterChatListBinding binding;
    LayoutInflater layoutInflater;

    public ChatListAdapter(Context mContext, ArrayList<ChatListDTO> chatList) {
        this.mContext = mContext;
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_chat_list, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.binding.tvTitle.setText(chatList.get(position).getArtistName());
        holder.binding.tvMsg.setText(chatList.get(position).getMessage());

        try {
            holder.binding.tvDate.setText(ProjectUtils.convertTimestampDate(ProjectUtils.correctTimestamp(Long.parseLong(chatList.get(position).getSend_at()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(mContext).
                load(chatList.get(position).getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.IVprofile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, OneTwoOneChat.class);
                in.putExtra(Consts.ARTIST_ID, chatList.get(position).getArtist_id());
                in.putExtra(Consts.ARTIST_NAME, chatList.get(position).getArtistName());
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterChatListBinding binding;

        public MyViewHolder(AdapterChatListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}