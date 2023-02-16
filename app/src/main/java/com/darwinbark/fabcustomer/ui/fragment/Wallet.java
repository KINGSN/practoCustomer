package com.darwinbark.fabcustomer.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.darwinbark.fabcustomer.databinding.FragmentWalletBinding;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.dto.WalletCurrencyDTO;
import com.darwinbark.fabcustomer.dto.WalletHistory;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.AddMoney;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.adapter.AdapterWalletHistory;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Wallet extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private AdapterWalletHistory adapterWalletHistory;
    private ArrayList<WalletHistory> walletHistoryList;
    private ArrayList<WalletCurrencyDTO> walletCurrencyList;
    private String TAG = Wallet.class.getSimpleName();
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String status = "";
    private HashMap<String, String> parms;
    private HashMap<String, String> parmsGetWallet = new HashMap<>();
    private String amt = "";
    private String currency = "";
    private BaseActivity baseActivity;
    FragmentWalletBinding binding;
    WalletCurrencyDTO walletCurrencyDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        view = binding.getRoot();
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.ic_wallet));
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());

        parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {

        binding.tvAll.setOnClickListener(this);
        binding.tvDebit.setOnClickListener(this);
        binding.tvCredit.setOnClickListener(this);

        binding.llAddMoney.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.RVhistorylist.setLayoutManager(mLayoutManager);

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        binding.etCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etCurrency.showDropDown();
            }
        });

        binding.etCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.etCurrency.showDropDown();
                walletCurrencyDTO = (WalletCurrencyDTO) parent.getItemAtPosition(position);
                Log.e(TAG, "onItemClick: " + walletCurrencyDTO.getCurrency_code());

                setWalletData(position);
                filter(walletCurrencyDTO);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddMoney:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    Intent in = new Intent(getActivity(), AddMoney.class);
                    in.putExtra(Consts.AMOUNT, amt);
                    in.putExtra(Consts.CURRENCY, currency);
                    startActivity(in);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

                break;
            case R.id.tvAll:
                setSelected(true, false, false);
                try {
                    showData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvCredit:
                setSelected(false, false, true);
                status = "1";
                try {
                    if(walletCurrencyList!=null){
                        walletCurrencyDTO = walletCurrencyList.get(0);
                        if (walletCurrencyDTO != null) {
                            updateAccordingStatus(walletCurrencyDTO, "0");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tvDebit:
                setSelected(false, true, false);
                status = "0";
                try {
                    if(walletCurrencyList!=null){
                        walletCurrencyDTO = walletCurrencyList.get(0);
                        if (walletCurrencyDTO != null) {
                            updateAccordingStatus(walletCurrencyDTO, "1");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_USER_WALLET_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                binding.swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    binding.tvNo.setVisibility(View.GONE);
                    binding.RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        walletCurrencyList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<WalletCurrencyDTO>>() {
                        }.getType();
                        walletCurrencyList = (ArrayList<WalletCurrencyDTO>) new Gson().fromJson(response.getJSONObject("data").getJSONArray("currency").toString(), getpetDTO);
                        if (walletCurrencyList.size() > 0) {

                            ArrayAdapter<WalletCurrencyDTO> currencyAdapter = new ArrayAdapter<WalletCurrencyDTO>(baseActivity, android.R.layout.simple_list_item_1, walletCurrencyList);
                            binding.etCurrency.setAdapter(currencyAdapter);
                            binding.etCurrency.setCursorVisible(false);
                            binding.etCurrency.setText(binding.etCurrency.getAdapter().getItem(0).toString(), false);

                            setWalletData(0);
                        }
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.RVhistorylist.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        getWallet();
        binding.swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            binding.swipeRefreshLayout.setRefreshing(true);
                                            getHistroy();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );

    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                        binding.tvWallet.setText(currency + " " + amt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void showData() {
        if (walletHistoryList.size() > 0) {
            binding.tvNo.setVisibility(View.GONE);
            binding.RVhistorylist.setVisibility(View.VISIBLE);

            adapterWalletHistory = new AdapterWalletHistory(Wallet.this, walletHistoryList);
            binding.RVhistorylist.setAdapter(adapterWalletHistory);
        } else {
            binding.tvNo.setVisibility(View.VISIBLE);
            binding.RVhistorylist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }

    public void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN) {
        if (firstBTN) {
            binding.tvAllSelect.setVisibility(View.VISIBLE);
            binding.tvDebitSelect.setVisibility(View.GONE);
            binding.tvCreditSelect.setVisibility(View.GONE);

        }
        if (secondBTN) {
            binding.tvDebitSelect.setVisibility(View.VISIBLE);
            binding.tvAllSelect.setVisibility(View.GONE);
            binding.tvCreditSelect.setVisibility(View.GONE);

        }
        if (thirdBTN) {
            binding.tvCreditSelect.setVisibility(View.VISIBLE);
            binding.tvAllSelect.setVisibility(View.GONE);
            binding.tvDebitSelect.setVisibility(View.GONE);

        }
        binding.tvAllSelect.setSelected(firstBTN);
        binding.tvDebitSelect.setSelected(secondBTN);
        binding.tvCreditSelect.setSelected(secondBTN);

    }
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void setWalletData(int index) {
        amt = walletCurrencyList.get(index).getAmount();
        currency = walletCurrencyList.get(index).getCurrency_type();
        binding.tvWallet.setText(currency + " " + amt);
        walletHistoryList = walletCurrencyList.get(index).getWallet_history();
    }

    private void filter(WalletCurrencyDTO dto) {
        ArrayList<WalletHistory> filterdNames = new ArrayList<>(dto.getWallet_history());
        adapterWalletHistory.updateList(filterdNames);
    }

    private void updateAccordingStatus(WalletCurrencyDTO dto, String status) {
        ArrayList<WalletHistory> walletHistoryArrayList = new ArrayList<>();
        for (WalletHistory dto1 : dto.getWallet_history()) {
            if (dto1.getStatus().equalsIgnoreCase(status)) {
                walletHistoryArrayList.add(dto1);
            }
        }
        adapterWalletHistory.updateList(walletHistoryArrayList);
    }
}
