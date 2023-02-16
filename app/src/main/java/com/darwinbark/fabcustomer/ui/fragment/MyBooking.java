package com.darwinbark.fabcustomer.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.darwinbark.fabcustomer.dto.UserBooking;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.adapter.AdapterCustomerBooking;
import com.darwinbark.fabcustomer.utils.CustomTextViewBold;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyBooking extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = NotificationActivity.class.getSimpleName();
    private RecyclerView rvBooking;
    private AdapterCustomerBooking adapterCustomerBooking;
    private ArrayList<UserBooking> userBookingList;
    private ArrayList<UserBooking> userBookingListSection;
    private ArrayList<UserBooking> userBookingListSection1;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private View view;
    private BaseActivity baseActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView svSearch;
    private RelativeLayout rlSearch;
    IntentFilter intentFilter = new IntentFilter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_my_booking, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_bookings));
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        setUiAction(view);
        return view;
    }


    public void setUiAction(View v) {
        rlSearch = v.findViewById(R.id.rlSearch);
        svSearch = v.findViewById(R.id.svSearch);
        tvNo = v.findViewById(R.id.tvNo);
        rvBooking = v.findViewById(R.id.rvBooking);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBooking.setLayoutManager(mLayoutManager);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    adapterCustomerBooking.filter(newText.toString());

                } else {


                }
                return false;
            }
        });

        intentFilter.addAction(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION);
        intentFilter.addAction(Consts.START_BOOKING_ARTIST_NOTIFICATION);
        intentFilter.addAction(Consts.END_BOOKING_ARTIST_NOTIFICATION);
        intentFilter.addAction(Consts.ACCEPT_BOOKING_ARTIST_NOTIFICATION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, intentFilter);
    }


    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);

                                            getBooking();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );
    }


    public void getBooking() {
        // ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CURRENT_BOOKING_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                //   ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    rlSearch.setVisibility(View.VISIBLE);

                    try {
                        userBookingList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<UserBooking>>() {
                        }.getType();
                        userBookingList = (ArrayList<UserBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        // setSection();
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    rlSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showData() {
        adapterCustomerBooking = new AdapterCustomerBooking(MyBooking.this, baseActivity, userBookingList, userDTO, "booking");
        rvBooking.setAdapter(adapterCustomerBooking);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getBooking();
    }

    public void setSection() {
        HashMap<String, ArrayList<UserBooking>> has = new HashMap<>();
        userBookingListSection = new ArrayList<>();
        for (int i = 0; i < userBookingList.size(); i++) {


            if (has.containsKey(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()))) {
                userBookingListSection1 = new ArrayList<>();
                userBookingListSection1 = has.get(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()));
                userBookingListSection1.add(userBookingList.get(i));
                has.put(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()), userBookingListSection1);


            } else {
                userBookingListSection1 = new ArrayList<>();
                userBookingListSection1.add(userBookingList.get(i));
                has.put(ProjectUtils.changeDateFormate1(userBookingList.get(i).getBooking_date()), userBookingListSection1);
            }
        }

        for (String key : has.keySet()) {
            UserBooking userBooking = new UserBooking();
            userBooking.setSection(true);
            userBooking.setSection_name(key);
            userBookingListSection.add(userBooking);
            userBookingListSection.addAll(has.get(key));

        }


        showData();

    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION)
                    || intent.getAction().equalsIgnoreCase(Consts.START_BOOKING_ARTIST_NOTIFICATION)
                    || intent.getAction().equalsIgnoreCase(Consts.END_BOOKING_ARTIST_NOTIFICATION)
                    || intent.getAction().equalsIgnoreCase(Consts.ACCEPT_BOOKING_ARTIST_NOTIFICATION)) {
                getBooking();
                Log.e("BROADCAST", "BROADCAST");
            }
        }
    };

}
