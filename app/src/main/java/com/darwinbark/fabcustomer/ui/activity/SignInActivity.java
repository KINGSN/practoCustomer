package com.darwinbark.fabcustomer.ui.activity;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.AccountPicker;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivitySignInBinding;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SOME_REQUEST_CODE = 12;
    private Context mContext;
    private String TAG = SignInActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivitySignInBinding binding;
    private String countryCode,signinm;
    ImageView ivLogo;
    LinearLayout LLsign;
    Animation from_top, from_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignInActivity.this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        mContext = SignInActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        setUiAction();

        ivLogo = findViewById(R.id.ivLogo);
        LLsign = findViewById(R.id.LLsign);

        //load animation
        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        // passing animation
        ivLogo.startAnimation(from_top);
        LLsign.startAnimation(from_bottom);

    }

    public void setUiAction() {
        countryCode="+"+binding.countryCodePicker.getSelectedCountryCode();
        binding.CBsignIn.setOnClickListener(this);
        binding.CTVBforgot.setOnClickListener(this);
        binding.CTVsignup.setOnClickListener(this);
        binding.ivEnterShow.setOnClickListener(this);
        binding.contactUs.setOnClickListener(this);
        binding.contactUs2.setOnClickListener(this);
       // binding.CETemailadd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CTVBforgot:
                startActivity(new Intent(mContext, ForgotPass.class));
                break;
            case R.id.CBsignIn:
                clickForSubmit();
                break;
            case R.id.CTVsignup:
                startActivity(new Intent(SignInActivity.this, numberVerificationActivity.class));
                break;
            case R.id.ivEnterShow:
                if (isHide) {
                    binding.ivEnterShow.setImageResource(R.drawable.ic_pass_visible);
                    binding.CETenterpassword.setTransformationMethod(null);
                    binding.CETenterpassword.setSelection(binding.CETenterpassword.getText().length());
                    isHide = false;
                } else {
                    binding.ivEnterShow.setImageResource(R.drawable.ic_pass_invisible);
                    binding.CETenterpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.CETenterpassword.setSelection(binding.CETenterpassword.getText().length());
                    isHide = true;
                }
                break;
            case R.id.contactUs:
                ProjectUtils.openSupportMail(mContext);
                break;

            case R.id.contactUs2:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://darwinbark.com/"));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setPackage("com.android.chrome");
                this.startActivity(i);
                break;
            case R.id.CETemailadd:
                //acpicker();
                break;
            case R.id.country_code_picker:
                clickCountrycodepicker();
                break;
        }
    }


    public void login() {
        if (prefrence.getValue(Consts.LANGUAGE_SELECTION).equalsIgnoreCase("")) {
            prefrence.setValue(Consts.LANGUAGE_SELECTION, "en");
        }

        //ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.LOGIN_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.pauseProgressDialog();
                    try {
                        Toasty.success(SignInActivity.this,"Login Success", Toast.LENGTH_LONG, true).show();

                       // ProjectUtils.showToast(mContext, msg);
                       // Toasty.success(mContext,"Login Success \n"+msg, Toast.LENGTH_LONG, true).show();
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        Log.d(TAG, "backResponse: "+userDTO.getName());
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        ProjectUtils.showToast(mContext, msg);
                        Intent in = new Intent(mContext, BaseActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.pauseProgressDialog();
                   // ProjectUtils.showToast(mContext, msg);
                    Toasty.error(mContext,"Login Failed \n"+msg, Toast.LENGTH_LONG, true).show();
                }


            }
        });
    }

    public void acpicker(){
        Intent intent =
                AccountPicker.newChooseAccountIntent(
                        new AccountPicker.AccountChooserOptions.Builder()
                                .setAllowableAccountsTypes(Collections.singletonList("com.google"))
                                .build());

        startActivityForResult(intent, SOME_REQUEST_CODE);


    }

    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOME_REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            binding.CETemailadd.setText(accountName);
        }
    }

    @SuppressLint("SetTextI18n")
    public void clickForSubmit() {



      /*  if (!ProjectUtils.isEmailValid(binding.CETemailadd.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        } else if (!ProjectUtils.isPasswordValid(binding.CETenterpassword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
*/

        if ((binding.countryCodePicker.getSelectedCountryCode() == null)) {
            Toasty.error(this, "Select Your Country").show();
        } else if (binding.CETemailadd.getText().toString().isEmpty()) {
            Toasty.error(this, "Enter Valid Number Or An Email Id", Toast.LENGTH_SHORT, true).show();

        }
        else if (ProjectUtils.isPhoneNumberValid(binding.CETemailadd.getText().toString().trim())) {
            binding.CETemailadd.setText(countryCode + "" + binding.CETemailadd.getText());
            signinm=binding.CETemailadd.getText().toString();
            }
        else if (ProjectUtils.isEmailValid(binding.CETemailadd.getText().toString().trim())) {
            signinm=binding.CETemailadd.getText().toString();
        }
            if (NetworkManager.isConnectToInternet(mContext)) {
                Log.d(TAG, "clickForSubmit: " +  signinm);
                // Toast.makeText(RegisterActivity.this, ""+mobileNumber, Toast.LENGTH_SHORT).show();
                ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
                // sendVerificationCodeToUser(mobileNumber);
                //checkmobile(mobileNumber);
                login();
            } else {
                //   ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                Toasty.error(this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT, true).show();


            }

        }



    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL_ID, signinm);
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.CETenterpassword));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.ROLE, "2");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.closeMsg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void clickCountrycodepicker() {

        binding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = "+" + binding.countryCodePicker.getSelectedCountryCode();

            }
        });

    }

}
