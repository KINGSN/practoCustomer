package com.darwinbark.fabcustomer.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;

import com.bumptech.glide.Glide;
import com.darwinbark.fabcustomer.databinding.FragmentImageGalleryBinding;
import com.darwinbark.fabcustomer.dto.ArtistDetailsDTO;
import com.darwinbark.fabcustomer.dto.GalleryDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.ui.adapter.AdapterGallery;

import java.util.ArrayList;

public class ImageGallery extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ImageGallery.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<GalleryDTO> galleryList;
    private AdapterGallery adapterGallery;
    private Bundle bundle;
    private GridLayoutManager gridLayoutManager;
    FragmentImageGalleryBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_image_gallery);
        context = ImageGallery.this;
        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        }
        showUiAction();
    }

    public void showUiAction() {
        binding.ivClose.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
        showData();

    }


    public void showData() {
        gridLayoutManager = new GridLayoutManager(context, 2);
        galleryList = new ArrayList<>();
        galleryList = artistDetailsDTO.getGallery();
        if (galleryList.size() > 0) {
            binding.tvNotFound.setVisibility(View.GONE);
            binding.rlView.setVisibility(View.VISIBLE);
            adapterGallery = new AdapterGallery(ImageGallery.this, galleryList);
            binding.rvGallery.setLayoutManager(gridLayoutManager);
            binding.rvGallery.setAdapter(adapterGallery);
        } else {
            binding.tvNotFound.setVisibility(View.VISIBLE);
            binding.rlView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                binding.rlZoomImg.setVisibility(View.GONE);
                break;
            case R.id.llBack:
                finish();
                break;
        }
    }

    public void showImg(String imgURL) {
        binding.rlZoomImg.setVisibility(View.VISIBLE);
        Glide
                .with(context)
                .load(imgURL)
                .placeholder(R.drawable.dummyuser_image)
                .into(binding.ivZoom);
    }

}
