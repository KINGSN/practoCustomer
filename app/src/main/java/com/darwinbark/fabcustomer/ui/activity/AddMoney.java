package com.darwinbark.fabcustomer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.darwinbark.fabcustomer.AppEnvironment;
import com.darwinbark.fabcustomer.AppPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.darwinbark.fabcustomer.dto.CurrencyDTO;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivityAddMoneyBinding;
import com.darwinbark.fabcustomer.databinding.DailogPaymentOptionBinding;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.DecimalDigitsInputFilter;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddMoney extends AppCompatActivity implements View.OnClickListener {
    private String TAG = AddMoney.class.getSimpleName();
    private Context mContext;
    float rs = 0;
    float rs1 = 0;
    float final_rs = 0;
    private HashMap<String, String> parmas = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String amt = "";
    private String currency = "";
    private Dialog dialog;
    private ActivityAddMoneyBinding binding;
    private ArrayList<CurrencyDTO> currencyDTOArrayList = new ArrayList<>();
    String currencyCode ="";
    private AppPreference mAppPreference;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_money);
        mContext = AddMoney.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmas.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction();
    }

    public void setUiAction() {

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().hasExtra(Consts.AMOUNT)) {
            amt = getIntent().getStringExtra(Consts.AMOUNT);
            currency = getIntent().getStringExtra(Consts.CURRENCY);

            binding.tvWallet.setText(currency + " " + amt);
        }

        binding.cbAdd.setOnClickListener(this);

        binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());

        binding.tv1000.setOnClickListener(this);

        binding.tv1500.setOnClickListener(this);

        binding.tv2000.setOnClickListener(this);
        binding.tv2500.setOnClickListener(this);

        binding.tv1000.setText("+ " + "100");
        binding.tv1500.setText("+ " + "150");
        binding.tv2000.setText("+ " + "200");
        binding.tv2500.setText("+ " + "500");


        binding.etAddMoney.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(12, 2)});
        binding.etAddMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });


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
                CurrencyDTO currencyDTO = (CurrencyDTO) parent.getItemAtPosition(position);
                Log.e(TAG, "onItemClick: " + currencyDTO.getCurrency_symbol());

                currencyCode = currencyDTO.getCode();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (binding.etAddMoney.getText().toString().trim().equalsIgnoreCase("")) {
            rs1 = 0;

        } else {
            rs1 = Float.parseFloat(binding.etAddMoney.getText().toString().trim());

        }

        switch (v.getId()) {
            case R.id.tv1000:
                rs = 100;
                final_rs = rs1 + rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.tv1500:
                rs = 150;
                final_rs = rs1 + rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.tv2000:
                rs = 200;
                final_rs = rs1 + rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.tv2500:
                rs = 500;
                final_rs = rs1 + rs;
                binding.etAddMoney.setText(final_rs + "");
                binding.etAddMoney.setSelection(binding.etAddMoney.getText().length());
                break;
            case R.id.cbAdd:
                if (binding.etAddMoney.getText().toString().length() > 0 && Float.parseFloat(binding.etAddMoney.getText().toString().trim()) > 0) {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        parmas.put(Consts.AMOUNT, ProjectUtils.getEditTextValue(binding.etAddMoney));
                      // dialogPayment();
                        launchPayUMoneyFlow();



                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_money));
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS)) {
            prefrence.clearPreferences(Consts.SURL);
            finish();
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.PAYMENT_FAIL)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        } else if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS_paypal)) {
            prefrence.clearPreferences(Consts.SURL);
            addMoney();
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.PAYMENT_FAIL_Paypal)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        }

        try {
            getCurrencyValue();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void addMoney() {
        new HttpsRequest(Consts.ADD_MONEY_API, parmas, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }


    public void dialogPayment() {
        dialog = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final DailogPaymentOptionBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_payment_option, null, false);
        dialog.setContentView(binding1.getRoot());
        ///dialog.getWindow().setBackgroundDrawableResource(R.color.black);

        dialog.show();
        dialog.setCancelable(false);
        binding1.llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding1.llPaypall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.MAKE_PAYMENT_paypal + "user_id=" + userDTO.getUser_id() + "&amount=" + ProjectUtils.getEditTextValue(binding.etAddMoney) + "&currency_code="+currencyCode;
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();

            }
        });
        binding1.llStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.MAKE_PAYMENT + userDTO.getUser_id() + "/" + ProjectUtils.getEditTextValue(binding.etAddMoney);
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();

            }
        });
        binding1.llPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Consts.MAKE_PAYMENT + userDTO.getUser_id() + "/" + ProjectUtils.getEditTextValue(binding.etAddMoney);
                Intent in2 = new Intent(mContext, PaymetWeb.class);
                in2.putExtra(Consts.PAYMENT_URL, url);
                startActivity(in2);
                dialog.dismiss();

            }
        });

    }

    public void getCurrencyValue() {

        new HttpsRequest(Consts.GET_CURRENCY_API, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                if (flag) {
                    try {
                        currencyDTOArrayList = new ArrayList<>();
                        Type getCurrencyDTO = new TypeToken<List<CurrencyDTO>>() {
                        }.getType();
                        currencyDTOArrayList = (ArrayList<CurrencyDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getCurrencyDTO);

                        try {
                            ArrayAdapter<CurrencyDTO> currencyAdapter = new ArrayAdapter<CurrencyDTO>(mContext, android.R.layout.simple_list_item_1, currencyDTOArrayList);
                            binding.etCurrency.setAdapter(currencyAdapter);
                            binding.etCurrency.setCursorVisible(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        setInitialData(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }


    public void setInitialData(int index) {
        String currency = currencyDTOArrayList.get(index).toString();
        binding.etCurrency.setText(currency);

        currencyCode = currencyDTOArrayList.get(index).getCode();
    }



    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText((("APPOINTU").toString()));

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle((("APPOINTU PAYMENT").toString()));

        // payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(String.valueOf(binding.etAddMoney.getText()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        //String txnId = "TXNID720431525261327973";
        String phone = userDTO.getMobile().trim();
        String productName ="Appointu Payment";
        String firstName = userDTO.getName();
        String email = userDTO.getEmail_id();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";
        // appEnvironment == AppEnvironment.PRODUCTION
        AppEnvironment appEnvironment =AppEnvironment.PRODUCTION;

        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
             * Hash should always be generated from your server side.
             * */
            //    generateHashFromServer(mPaymentParams);

            /*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,AddMoney.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,
                        AddMoney.this,
                        R.style.AppTheme_default,false);
            }

        } catch (Exception e) {
            // some exception occurred
          //  Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            // payNowButton.setEnabled(true);
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        // AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        AppEnvironment appEnvironment =AppEnvironment.PRODUCTION;
        //  stringBuilder.append("seVTUgzrgE");
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    /**
     * This method generates hash from server.
     *
     * @param str payments params used for hash generation
     */


    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    final String payment_type="2";
                    //bookArtist(payment_type);
                    addMoney();
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

         /*       new android.app.AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
*/
                Log.d(TAG, "onActivityResult: "+("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse));
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }


}
