package com.darwinbark.fabcustomer.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;

import com.google.gson.JsonArray;
import com.darwinbark.fabcustomer.databinding.ActivityViewServicesBinding;
import com.darwinbark.fabcustomer.dto.ArtistDetailsDTO;
import com.darwinbark.fabcustomer.dto.ProductDTO;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.adapter.AdapterServices;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import java.util.ArrayList;

public class ViewServices extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private ArtistDetailsDTO artistDetailsDTO;
    private AdapterServices adapterServices;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<ProductDTO> serviceList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private JsonArray array;
    private DialogInterface dialog_book;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String artist_id = "";
    public ActivityViewServicesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_services);
        mContext = ViewServices.this;
        prefrence = SharedPrefrence.getInstance(mContext);

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
        }
        showUiAction();
    }


    public void showUiAction() {
        binding.llBack.setOnClickListener(this);
        binding.cardBook.setOnClickListener(this);
        showData();

    }

    public void showData() {
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        if (productDTOList.size() > 0) {
            binding.tvNotFound.setVisibility(View.GONE);
            binding.rvServices.setVisibility(View.VISIBLE);
            binding.cardBook.setVisibility(View.VISIBLE);
            adapterServices = new AdapterServices(ViewServices.this, binding, productDTOList, artistDetailsDTO);
            binding.rvServices.setLayoutManager(gridLayoutManager);
            binding.rvServices.setAdapter(adapterServices);
        } else {
            binding.tvNotFound.setVisibility(View.VISIBLE);
            binding.rvServices.setVisibility(View.GONE);
            binding.cardBook.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                onBackPressed();
                break;
            case R.id.cardBook:
                updateList();
                if ((array.size() > 0) ) {
                    Intent in = new Intent(mContext, Booking.class);
                    in.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                    in.putExtra(Consts.ARTIST_ID, artist_id);
                    in.putExtra(Consts.SERVICE_ARRAY, array.toString());
                    in.putExtra(Consts.SERVICE_NAME_ARRAY, serviceList);
                    in.putExtra(Consts.SCREEN_TAG, 2);
                    in.putExtra(Consts.PRICE, binding.tvPrice.getText().toString().trim());
                    startActivity(in);
                    //finish();
                } else {
                    ProjectUtils.showLong(mContext, mContext.getResources().getString(R.string.select_any_service));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public void updateList() {
        array = new JsonArray();
        for (int i = 0; i < productDTOList.size(); i++) {
            if (productDTOList.get(i).isSelected()) {
                array.add(productDTOList.get(i).getId());
                serviceList.add(productDTOList.get(i));
            }
        }
    }
}
