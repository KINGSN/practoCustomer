package com.darwinbark.fabcustomer.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.application.GlobalState;
import com.darwinbark.fabcustomer.databinding.FragmentHomeBinding;
import com.darwinbark.fabcustomer.dto.HistoryDTO;
import com.darwinbark.fabcustomer.dto.HomeBannerDTO;
import com.darwinbark.fabcustomer.dto.HomeCategoryDTO;
import com.darwinbark.fabcustomer.dto.HomeDataDTO;
import com.darwinbark.fabcustomer.dto.HomeNearByArtistsDTO;
import com.darwinbark.fabcustomer.dto.HomeRecomendedDTO;
import com.darwinbark.fabcustomer.dto.User1DTO;
import com.darwinbark.fabcustomer.dto.UserBooking;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.adapter.AdapterCategory;
import com.darwinbark.fabcustomer.ui.adapter.AdapterCustomerBooking;
import com.darwinbark.fabcustomer.ui.adapter.AdapterInvoice;
import com.darwinbark.fabcustomer.ui.adapter.AdapterNearByArtist;
import com.darwinbark.fabcustomer.ui.adapter.AdapterRecommended;
import com.darwinbark.fabcustomer.ui.adapter.HomeBannerPagerAdapter;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.darwinbark.fabcustomer.utils.Transformer;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static androidx.databinding.DataBindingUtil.inflate;

@RequiresApi(api = Build.VERSION_CODES.R)
public class Home extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
   // private String TAG = Home.class.getSimpleName();
    private String TAG = "KINGSN";
    private SharedPrefrence preference;
    private UserDTO userDTO;
    private User1DTO user1DTO,usermdto;
    private RecyclerView recyclerView;
    HashMap<String, String> params = new HashMap<>();
    private BaseActivity baseActivity;
    FragmentHomeBinding binding;
      HomeDataDTO homeDataDTO;
    GlobalState globalState;

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    ArrayList<HomeBannerDTO> bannerDTOArrayList = new ArrayList<>();
    HomeBannerPagerAdapter homeBannerPagerAdapter;

    AdapterNearByArtist nearByAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HomeNearByArtistsDTO> nearByArtistsDTOArrayList = new ArrayList<>();

    AdapterCustomerBooking activeBookingAdapter;
    LinearLayoutManager linearLayoutManager1;
    ArrayList<UserBooking> activeBookingDTOArrayList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager4;
    AdapterRecommended recommendedAdapter;
    ArrayList<HomeRecomendedDTO> recomendedDTOArrayList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager3;
    AdapterInvoice invoiceAdapter;
    ArrayList<HistoryDTO> invoiceDTOArrayList = new ArrayList<>();

    LinearLayoutManager linearLayoutManager2;
    AdapterCategory categoryAdapter;
    ArrayList<HomeCategoryDTO> categoryDTOArrayList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflate(inflater, R.layout.fragment_home, container, false);
        view = binding.getRoot();
        baseActivity.headerNameTV.setText(getResources().getString(R.string.app_name));
        baseActivity.headerNameTV.setText(getResources().getString(R.string.app_name));
        preference = SharedPrefrence.getInstance(getActivity());
        preference = SharedPrefrence.getInstance(getActivity());
        userDTO = preference.getParentUser(Consts.USER_DTO);
        user1DTO = preference.getParentUser2(Consts.USER1DTO);
        //recyclerView = (RecyclerView) view.findViewById(R.id.rv_categories);
       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        //recyclerView.setHasFixedSize(true);
       // checkpermi();
        Log.d(TAG, "onCreateView: "+user1DTO.getAdminMobile()+user1DTO.getAdminAccountIfsc());
        setUiAction();

        return view;

    }

    public void setUiAction() {

         globalState = GlobalState.getInstance();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Objects.requireNonNull(binding.rvNearBy).setLayoutManager(linearLayoutManager);
        linearLayoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Objects.requireNonNull(binding.rvRecommended).setLayoutManager(linearLayoutManager4);
        linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvMyBookings.setLayoutManager(linearLayoutManager1);
        linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
       // binding.rvCategories.setLayoutManager(linearLayoutManager2);
        linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvRecentInvoice.setLayoutManager(linearLayoutManager3);



        if (globalState.getHomeData() != null) {
            homeDataDTO = globalState.getHomeData();
            setData();
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.ivMenu.setOnClickListener(this);
        binding.tvSeeAll.setOnClickListener(this);
        binding.tvSeeAll1.setOnClickListener(this);
        binding.tvSeeAll2.setOnClickListener(this);
        binding.tvSeeAll3.setOnClickListener(this);
        binding.tvSeeAll6.setOnClickListener(this);

        params.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.LATITUDE, ""+preference.getValue(Consts.LATITUDE));
        params.put(Consts.LONGITUDE, ""+preference.getValue(Consts.LONGITUDE));
        params.put(Consts.DISTANCE, "50");

        params.put("adminUserId", "super_admin");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddMoney:
                break;
            case R.id.iv_menu:
                if (baseActivity.drawer.isDrawerVisible(GravityCompat.START)) {
                    baseActivity.drawer.closeDrawer(GravityCompat.START);
                } else {
                    baseActivity.drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.tv_see_all:
            case R.id.tv_see_all3:
            case R.id.tv_see_all6:
                try {
                    baseActivity.ivFilter.setVisibility(View.GONE);
                    baseActivity.header.setVisibility(View.VISIBLE);
                    BaseActivity.navItemIndex = 1;
                    BaseActivity.CURRENT_TAG = BaseActivity.TAG_MAIN;
                    baseActivity.loadHomeFragment(new DiscoverNearBy(), BaseActivity.CURRENT_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_see_all1:
                try {
                    baseActivity.ivFilter.setVisibility(View.GONE);
                    baseActivity.header.setVisibility(View.VISIBLE);
                    BaseActivity.navItemIndex = 3;
                    BaseActivity.CURRENT_TAG = BaseActivity.TAG_BOOKING;
                    baseActivity.loadHomeFragment(new MyBooking(), BaseActivity.CURRENT_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_see_all2:
                try {
                    baseActivity.ivFilter.setVisibility(View.GONE);
                    baseActivity.header.setVisibility(View.VISIBLE);
                    BaseActivity.navItemIndex = 9;
                    BaseActivity.CURRENT_TAG = BaseActivity.TAG_HISTORY;
                    baseActivity.loadHomeFragment(new HistoryFragment(), BaseActivity.CURRENT_TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public void getHomeData() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CUSTOMER_HOME_DATA, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    binding.tvNo.setVisibility(View.GONE);
                    try {
                        homeDataDTO = new Gson().fromJson(response.getJSONObject("data").toString(), HomeDataDTO.class);
                        globalState.setHomeData(homeDataDTO);
                        Log.d(TAG, "activebookingotp:"+response);
                       // Toast.makeText(getContext(), "You Selected Your Slot"+homeDataDTO , Toast.LENGTH_SHORT).show();
                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvNearBy.setVisibility(View.GONE);
                    binding.rvRecommended.setVisibility(View.GONE);
                    binding.rvMyBookings.setVisibility(View.GONE);
                    binding.rvCategories.setVisibility(View.GONE);
                    binding.rvRecentInvoice.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getHomeData2() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.AdminHomeData, params, getActivity()).stringPost2(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    binding.tvNo.setVisibility(View.GONE);
                    try {
                        user1DTO = new Gson().fromJson(response.getJSONObject("darwinbarkk").getJSONObject("Results").toString(), User1DTO.class);
                        preference.setParentUser2(user1DTO, Consts.USER1DTO);
                        //globalState.setHomeData(homeDataDTO);

                        usermdto=preference.getParentUser2(Consts.USER1DTO);
                        Log.d(TAG, "KINGSN123:"+usermdto.getAdminAccountIfsc()+usermdto.getAdminMobile());

                        Log.d(TAG, "activebookingotp:"+usermdto.getAdminMobile());
                        // Toast.makeText(getContext(), "You Selected Your Slot"+homeDataDTO , Toast.LENGTH_SHORT).show();
                       // setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvNearBy.setVisibility(View.GONE);
                    binding.rvRecommended.setVisibility(View.GONE);
                    binding.rvMyBookings.setVisibility(View.GONE);
                    binding.rvCategories.setVisibility(View.GONE);
                    binding.rvRecentInvoice.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //checkpermi();
        binding.swipeRefreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                Log.e("Runnable", "FIRST");
                                                if (NetworkManager.isConnectToInternet(getActivity())) {
                                                    binding.swipeRefreshLayout.setRefreshing(true);
                                                    getHomeData();
                                                   // getHomeData2();

                                                } else {
                                                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                                }
                                            }
                                        }
        );

    }


    public void setData() {
        bannerDTOArrayList = homeDataDTO.getBanner();
        nearByArtistsDTOArrayList = homeDataDTO.getNear_by_artist();
        recomendedDTOArrayList = homeDataDTO.getRecommended();
        invoiceDTOArrayList = homeDataDTO.getInvoice();
        categoryDTOArrayList = homeDataDTO.getCategory();
        activeBookingDTOArrayList = homeDataDTO.getActive_booking();

        if (bannerDTOArrayList.size() > 0) {
            homeBannerPagerAdapter = new HomeBannerPagerAdapter(Home.this, baseActivity, bannerDTOArrayList);
            binding.mViewPager.setAdapter(homeBannerPagerAdapter);
            binding.mViewPager.setCurrentItem(0);
            binding.mViewPager.scrollTo(1, bannerDTOArrayList.size());
            //page transformer
           binding.mViewPager.setPageTransformer(false, new Transformer());
           binding.mViewPager.setOffscreenPageLimit(3);
           binding.mViewPager.setCurrentItem(homeBannerPagerAdapter.getCount() /2);

            binding.tabDots.setViewPager(binding.mViewPager);
            binding.mViewPager.setNestedScrollingEnabled(false);
        }

        if (nearByArtistsDTOArrayList.size() > 0) {
            binding.rlNearBy.setVisibility(View.VISIBLE);
            nearByAdapter = new AdapterNearByArtist(baseActivity, nearByArtistsDTOArrayList);
            binding.rvNearBy.setAdapter(nearByAdapter);
            binding.rvNearBy.setNestedScrollingEnabled(false);
        } else {
            binding.rlNearBy.setVisibility(View.GONE);
        }

        if (recomendedDTOArrayList.size() > 0) {
            binding.rlRecommended.setVisibility(View.VISIBLE);
            recommendedAdapter = new AdapterRecommended(baseActivity, recomendedDTOArrayList);
            binding.rvRecommended.setAdapter(recommendedAdapter);
            binding.rvRecommended.setNestedScrollingEnabled(false);
        } else {
            binding.rlRecommended.setVisibility(View.GONE);
        }

        if (invoiceDTOArrayList.size() > 0) {
            binding.rlRecentInvoice.setVisibility(View.VISIBLE);
            invoiceAdapter = new AdapterInvoice(baseActivity, invoiceDTOArrayList, LayoutInflater.from(getActivity()));
            binding.rvRecentInvoice.setAdapter(invoiceAdapter);
            binding.rvRecentInvoice.setNestedScrollingEnabled(false);
        } else {
            binding.rvRecentInvoice.setVisibility(View.GONE);
        }

        if (categoryDTOArrayList.size() > 0) {
            binding.rlCategories.setVisibility(View.VISIBLE);
            binding.rvCategories.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
            binding.rvCategories.setHasFixedSize(true);
            categoryAdapter = new AdapterCategory(baseActivity, categoryDTOArrayList);
            binding.rvCategories.setAdapter(categoryAdapter);
            binding.rvCategories.setNestedScrollingEnabled(false);
        } else {
            binding.rvCategories.setVisibility(View.GONE);
        }

        if (activeBookingDTOArrayList.size() > 0) {
            binding.rlMyBookings.setVisibility(View.VISIBLE);
            activeBookingAdapter = new AdapterCustomerBooking(null, baseActivity, activeBookingDTOArrayList, userDTO, "home");
            binding.rvMyBookings.setAdapter(activeBookingAdapter);
            binding.rvMyBookings.setNestedScrollingEnabled(false);
        } else {
            binding.rvMyBookings.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHomeData();
       // checkpermi();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkpermi() {
       /* if (!hasPermissions(requireActivity(), permissions)) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }*/
        if (Environment.isExternalStorageManager()) {
            //todo when permission is granted
            // Toast.makeText(this, "all files permission granted", Toast.LENGTH_SHORT).show();
        } else {
            // callinappupdate();
            //handler.postDelayed(mTask, SPLASH_TIME_OUT);
            ProjectUtils.askper(requireActivity());
        }
    }




}
