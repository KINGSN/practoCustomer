package com.darwinbark.fabcustomer.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.darwinbark.fabcustomer.databinding.FragmentSettingBinding;
import com.google.gson.Gson;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.activity.LanguageSelection;
import com.darwinbark.fabcustomer.ui.activity.WebViewCommon;
import com.darwinbark.fabcustomer.utils.CustomEditText;
import com.darwinbark.fabcustomer.utils.CustomTextViewBold;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class Setting extends Fragment implements View.OnClickListener {
    private Dialog dialog_pass;
    private CustomTextViewBold tvYesPass, tvNoPass;
    private CustomEditText etOldPassD, etNewPassD, etConfrimPassD;
    private ImageView ivClose;
    private HashMap<String, String> params;
    private HashMap<String, String> paramsLogout = new HashMap<>();
    private HashMap<String, File> paramsFile = new HashMap<>();
    private SharedPrefrence preference;
    private UserDTO userDTO;
    private String TAG = Setting.class.getSimpleName();
    private View view;
    private BaseActivity baseActivity;
    FragmentSettingBinding binding;
    String baseURL = "";
    String url="https://darwinbark.com/contact";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        view = binding.getRoot();
        preference = SharedPrefrence.getInstance(getActivity());
        userDTO = preference.getParentUser(Consts.USER_DTO);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.settings));
        setUiAction();
        return view;
    }

    public void setUiAction() {
        binding.llChangePass.setOnClickListener(this);
        binding.llLanguage.setOnClickListener(this);
        binding.llPrivacy.setOnClickListener(this);
        binding.llFaq.setOnClickListener(this);
        binding.contactdev.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llChangePass:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    dialogPassword();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.ll_language:
                Intent intent = new Intent(baseActivity, LanguageSelection.class);
                intent.putExtra(Consts.TYPE, "1");
                startActivity(intent);
                break;
            case R.id.ll_privacy:
                baseURL = Consts.PRIVACY_URL;
                getURLForWebView();
                break;
            case R.id.ll_faq:
                baseURL = Consts.FAQ_URL;
                getURLForWebView();
                break;
            case R.id.contactdev:
                Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                if (i.resolveActivity(requireActivity().getPackageManager()) == null) {
                    i.setData(Uri.parse(url));
                }
                requireActivity().startActivity(i);
                break;
        }
    }


    private void getURLForWebView() {
        new HttpsRequest(baseURL, baseActivity).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                if (flag) {
                    try {
                        if (baseURL.equalsIgnoreCase(Consts.PRIVACY_URL)) {
                            Intent intent1 = new Intent(baseActivity, WebViewCommon.class);
                            intent1.putExtra(Consts.URL, msg);
                            intent1.putExtra(Consts.HEADER, getResources().getString(R.string.privacy_policy));
                            startActivity(intent1);
                        } else if (baseURL.equalsIgnoreCase(Consts.FAQ_URL)) {
                            Intent intent3 = new Intent(getActivity(), WebViewCommon.class);
                            intent3.putExtra(Consts.URL, msg);
                            intent3.putExtra(Consts.HEADER, getResources().getString(R.string.faq));
                            startActivity(intent3);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(baseActivity, msg);
                }
            }
        });
    }


    public void dialogPassword() {
        dialog_pass = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialog_pass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_pass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pass.setContentView(R.layout.dailog_password);

        ivClose = (ImageView) dialog_pass.findViewById(R.id.iv_close);
        etOldPassD = (CustomEditText) dialog_pass.findViewById(R.id.etOldPassD);
        etNewPassD = (CustomEditText) dialog_pass.findViewById(R.id.etNewPassD);
        etConfrimPassD = (CustomEditText) dialog_pass.findViewById(R.id.etConfrimPassD);

        etOldPassD.setTransformationMethod(new PasswordTransformationMethod());
        etNewPassD.setTransformationMethod(new PasswordTransformationMethod());
        etConfrimPassD.setTransformationMethod(new PasswordTransformationMethod());

        tvYesPass = (CustomTextViewBold) dialog_pass.findViewById(R.id.tvYesPass);
        dialog_pass.show();
        dialog_pass.setCancelable(false);

        tvYesPass.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params = new HashMap<>();
                        params.put(Consts.USER_ID, userDTO.getUser_id());
                        params.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(etOldPassD));
                        params.put(Consts.NEW_PASSWORD, ProjectUtils.getEditTextValue(etNewPassD));

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            Submit();

                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_pass.dismiss();
            }
        });
    }


    public void updateProfile() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_API, params, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        ProjectUtils.showToast(getActivity(), msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        preference.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    private void Submit() {
        if (!passwordValidation()) {
            return;
        } else if (!checkpass()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(getActivity())) {
                updateProfile();
                dialog_pass.dismiss();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
            }

        }
    }

    public boolean passwordValidation() {
        if (!ProjectUtils.isPasswordValid(etOldPassD.getText().toString().trim())) {
            etOldPassD.setError(getResources().getString(R.string.val_pass_c));
            etOldPassD.requestFocus();
            return false;
        } else if (!ProjectUtils.isPasswordValid(etNewPassD.getText().toString().trim())) {
            etNewPassD.setError(getResources().getString(R.string.val_pass_c));
            etNewPassD.requestFocus();
            return false;
        } else
            return true;

    }

    private boolean checkpass() {
        if (etNewPassD.getText().toString().trim().equals("")) {
            etNewPassD.setError(getResources().getString(R.string.val_new_pas));
            return false;
        } else if (etConfrimPassD.getText().toString().trim().equals("")) {
            etConfrimPassD.setError(getResources().getString(R.string.val_c_pas));
            return false;
        } else if (!etNewPassD.getText().toString().trim().equals(etConfrimPassD.getText().toString().trim())) {
            etConfrimPassD.setError(getResources().getString(R.string.val_n_c_pas));
            return false;
        }
        return true;
    }
}
