package com.darwinbark.fabcustomer.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.darwinbark.fabcustomer.databinding.FragmentReviewsBinding;
import com.darwinbark.fabcustomer.dto.ArtistDetailsDTO;
import com.darwinbark.fabcustomer.dto.ReviewsDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.ui.adapter.ReviewAdapter;

import java.util.ArrayList;

public class Reviews extends AppCompatActivity {
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private ReviewAdapter reviewAdapter;
    private LinearLayoutManager mLayoutManagerReview;
    private ArrayList<ReviewsDTO> reviewsDTOList;
    private Bundle bundle;
    FragmentReviewsBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_reviews);
        context = Reviews.this;

        bundle = getIntent().getExtras();
        artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction();
    }

    public void showUiAction() {
        mLayoutManagerReview = new LinearLayoutManager(context);
        binding.rvReviews.setLayoutManager(mLayoutManagerReview);
        showData();
    }


    public void showData() {
        reviewsDTOList = new ArrayList<>();
        reviewsDTOList = artistDetailsDTO.getReviews();
        if (reviewsDTOList.size() > 0) {
            binding.llList.setVisibility(View.VISIBLE);
            binding.tvNotFound.setVisibility(View.GONE);
            reviewAdapter = new ReviewAdapter(context, reviewsDTOList);
            binding.rvReviews.setAdapter(reviewAdapter);
            binding.tvReviewsText.setText(getString(R.string.reviews) + reviewsDTOList.size() + ")");
        } else {
            binding.llList.setVisibility(View.GONE);
            binding.tvNotFound.setVisibility(View.VISIBLE);
        }

    }
}
