package com.darwinbark.fabcustomer.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darwinbark.fabcustomer.dto.HistoryDTO;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.CustomEditText;
import com.darwinbark.fabcustomer.utils.CustomTextView;
import com.darwinbark.fabcustomer.utils.CustomTextViewBold;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaymentProActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = PaymentProActivity.class.getSimpleName();
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HistoryDTO historyDTO;

    private CircleImageView ivArtist;
    private CustomTextView tvCategory, tvLocation;
    private CustomTextViewBold tvName, tvApplyCode, tvAmount, tvCancelCode;
    private LinearLayout llPayment, llCash, llWallet;
    private CustomEditText etCode;
    private String merchantKey, salt, userCredentials, invoice_id, user_id, coupon_code = "", final_amount, email;
    private HashMap<String, String> params = new HashMap<>();
    private ImageView IVback;
    private Dialog dialog;
    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;
    private LinearLayout llPaypall, llStripe, llCancel;
    private String amt1 = "";
    private String discount_amount = "0";
    private String currency = "";
    private HashMap<String, String> parmsGetWallet = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mContext = PaymentProActivity.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());

        if (getIntent().hasExtra(Consts.HISTORY_DTO)) {
            historyDTO = (HistoryDTO) getIntent().getSerializableExtra(Consts.HISTORY_DTO);
        }
        setUiAction();
    }

    @SuppressLint("SetTextI18n")
    public void setUiAction() {
        IVback = (ImageView) findViewById(R.id.IVback);
        IVback.setOnClickListener(this);
        ivArtist = findViewById(R.id.ivArtist);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        tvName = findViewById(R.id.tvName);
        tvApplyCode = findViewById(R.id.tvApplyCode);
        tvCancelCode = findViewById(R.id.tvCancelCode);
        tvAmount = findViewById(R.id.tvAmount);
        llPayment = findViewById(R.id.llPayment);
        llCash = findViewById(R.id.llCash);
        llWallet = findViewById(R.id.llWallet);
        etCode = findViewById(R.id.etCode);

        llPayment.setOnClickListener(this);
        llCash.setOnClickListener(this);
        tvApplyCode.setOnClickListener(this);
        tvCancelCode.setOnClickListener(this);
        llWallet.setOnClickListener(this);

        Glide.with(mContext).
                load(historyDTO.getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivArtist);

        tvCategory.setText(historyDTO.getCategoryName());
        tvLocation.setText(historyDTO.getAddress());
        tvName.setText(ProjectUtils.getFirstLetterCapital(historyDTO.getArtistName()));
        tvAmount.setText(historyDTO.getCurrency_type() + historyDTO.getTotal_amount());

        final_amount = historyDTO.getTotal_amount();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPayment:
                dialogPayment();
                break;
            case R.id.llWallet:
               if (Float.parseFloat(amt1) >= Float.parseFloat(final_amount)) {
                    cashDialog(getResources().getString(R.string.wallet_payment), getResources().getString(R.string.wallet_msg), "2");
               } else {
                    ProjectUtils.showToast(mContext, "Insufficient balance, please add money to wallet first.");
                   Toast.makeText(PaymentProActivity.this,"paypro: "+amt1,Toast.LENGTH_LONG).show();
               }

                break;
            case R.id.llCash:
                cashDialog(getResources().getString(R.string.cash_payment), getResources().getString(R.string.cash_msg), "1");
                break;
            case R.id.tvApplyCode:
                params.put(Consts.INVOICE_ID, historyDTO.getInvoice_id());
                params.put(Consts.COUPON_CODE, ProjectUtils.getEditTextValue(etCode));

                checkCoupon();
                break;
            case R.id.tvCancelCode:
                etCode.setText("");
                tvAmount.setText(historyDTO.getCurrency_type() + historyDTO.getTotal_amount());
                final_amount = historyDTO.getTotal_amount();
                tvApplyCode.setVisibility(View.VISIBLE);
                tvCancelCode.setVisibility(View.GONE);
                coupon_code = "";
                etCode.setEnabled(true);
                break;
            case R.id.IVback:
                finish();
                break;
        }
    }


    public void checkCoupon() {
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.CHECK_COUPON_API, params, mContext).stringPost(TAG, new Helper() {
            @SuppressLint("SetTextI18n")
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        ProjectUtils.showToast(mContext, msg);
                        String amt = response.getString("final_amount").toString();
                        discount_amount = response.getString("discount_amount").toString();
                        final_amount = amt;
                        tvAmount.setText(historyDTO.getCurrency_type() + amt);
                        tvApplyCode.setVisibility(View.GONE);
                        tvCancelCode.setVisibility(View.VISIBLE);
                        coupon_code = etCode.getText().toString().trim();
                        etCode.setEnabled(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                    etCode.setEnabled(true);
                    coupon_code = "";

                }


            }
        });
    }

    public void sendPayment(String type) {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MAKE_PAYMENT_API, getParms(type), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    Intent in = new Intent(mContext, WriteReview.class);
                    in.putExtra(Consts.HISTORY_DTO, historyDTO);
                    startActivity(in);
                    finish();
                } else {
                    ProjectUtils.showToast(mContext, msg);

                }
            }
        });
    }


    public Map<String, String> getParms(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Consts.INVOICE_ID, historyDTO.getInvoice_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.COUPON_CODE, coupon_code);
        params.put(Consts.FINAL_AMOUNT, final_amount);
        params.put(Consts.PAYMENT_STATUS, "1");
        params.put(Consts.PAYMENT_TYPE, type);
        params.put(Consts.DISCOUNT_AMOUNT, discount_amount);

        Log.e("sendPaymentConfirm", params.toString());
        return params;
    }


    public void cashDialog(String title, String msg, final String type) {
        try {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(title)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendPayment(type);
                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.INVOICE_PAYMENT_SUCCESS_Stripe)) {
            prefrence.clearPreferences(Consts.SURL);
            sendPayment("0");
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.INVOICE_PAYMENT_FAIL_Stripe)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        } else if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS_paypal)) {
            prefrence.clearPreferences(Consts.SURL);
            sendPayment("0");
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.PAYMENT_FAIL_Paypal)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        }
    }

    public void dialogPayment() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_payment_option);


        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);
        llPaypall = (LinearLayout) dialog.findViewById(R.id.llPaypall);
        llStripe = (LinearLayout) dialog.findViewById(R.id.llStripe);
        llCancel = (LinearLayout) dialog.findViewById(R.id.llCancel);

        dialog.show();
        dialog.setCancelable(false);
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llPaypall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_code = ProjectUtils.getEditTextValue(etCode);
                String url = Consts.INVOICE__PAYMENT_paypal + "&id=" + historyDTO.getInvoice_id() + "&coupon_code=" + coupon_code;
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();
            }
        });
        llStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.INVOICE_PAYMENT_Stripe + userDTO.getUser_id() + "/" + final_amount;
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();
            }
        });

    }

    public void getWallet() {
        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        amt1 = response.getJSONObject("data").getString("amount");
                        Toast.makeText(PaymentProActivity.this,"paypro2: "+amt1,Toast.LENGTH_LONG).show();
                        currency = response.getJSONObject("data").getString("currency_type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

}
