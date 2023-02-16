package com.darwinbark.fabcustomer.ui.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.common.AccountPicker;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivitySignUpBinding;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
  //  private String TAG = SignUpActivity.class.getSimpleName();
    private String TAG ="KINGSN";
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivitySignUpBinding binding;
    String baseURL = "";
    String mobileNumber;
    private static final int SOME_REQUEST_CODE = 12;
    ImageView ivLogo;
    LinearLayout LLsignup;
    Animation from_top, from_bottom;
String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignUpActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));

        if (getIntent().hasExtra(Consts.CALL_PHONE)) {
            mobileNumber=getIntent().getStringExtra(Consts.CALL_PHONE);
            binding.CETmobile.setText(mobileNumber);
            //Toasty.error(this,""+mobileNumber, Toast.LENGTH_LONG, true).show();

        }
        setUiAction();

        ivLogo = findViewById(R.id.ivLogo);
        LLsignup = findViewById(R.id.LLsignup);

        //load animation
        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        // passing animation
        ivLogo.startAnimation(from_top);
        LLsignup.startAnimation(from_bottom);
        binding.CETmobile.setEnabled(false);

    }

    public void setUiAction() {
        binding.CBsignup.setOnClickListener(this);
        binding.CTVsignin.setOnClickListener(this);
        binding.tvTerms.setOnClickListener(this);
        binding.tvPrivacy.setOnClickListener(this);
        binding.ivReEnterShow.setOnClickListener(this);
        binding.ivEnterShow.setOnClickListener(this);
        binding.CETemailadd.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBsignup:
                clickForSubmit();
                break;
            case R.id.CTVsignin:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
            case R.id.tvTerms:
                baseURL = Consts.TERMS_URL;
                getURLForWebView();
                break;
            case R.id.tvPrivacy:
                baseURL = Consts.PRIVACY_URL;
                getURLForWebView();
                break;
            case R.id.CETemailadd:
                acpicker();
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
            case R.id.ivReEnterShow:
                if (isHide) {
                    binding.ivReEnterShow.setImageResource(R.drawable.ic_pass_visible);
                    binding.CETenterpassagain.setTransformationMethod(null);
                    binding.CETenterpassagain.setSelection(binding.CETenterpassagain.getText().length());
                    isHide = false;
                } else {
                    binding.ivReEnterShow.setImageResource(R.drawable.ic_pass_invisible);
                    binding.CETenterpassagain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.CETenterpassagain.setSelection(binding.CETenterpassagain.getText().length());
                    isHide = true;
                }
                break;

        }

    }


    public void register() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.REGISTER_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    Log.d(TAG, "backResponse: "+response);
                    try {
                      //  ProjectUtils.showToast(mContext, msg);
                        Toasty.success(mContext,"You Account Has been Created..! Please Login", Toast.LENGTH_SHORT, true).show();

                        startActivity(new Intent(mContext, SignInActivity.class));
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //ProjectUtils.showToast(mContext, msg);
                    Toasty.error(mContext,msg, Toast.LENGTH_SHORT, true).show();

                }


            }
        });
    }

    public void clickForSubmit() {
        if (!validation(binding.CETfirstname, getResources().getString(R.string.val_name))) {
            return;
        } else if (!ProjectUtils.isEmailValid(binding.CETemailadd.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_email));
        } else if (!ProjectUtils.isPasswordValid(binding.CETenterpassword.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.val_pass));
        } else if (!checkpass()) {
            return;
        } else if (!validateTerms()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
               // findPlace();
                register();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    private boolean checkpass() {
        if (binding.CETenterpassword.getText().toString().trim().equals("")) {
            showSickbar(getResources().getString(R.string.val_pass));
            return false;
        } else if (binding.CETenterpassagain.getText().toString().trim().equals("")) {
            showSickbar(getResources().getString(R.string.val_pass1));
            return false;
        } else if (!binding.CETenterpassword.getText().toString().trim().equals(binding.CETenterpassagain.getText().toString().trim())) {
            showSickbar(getResources().getString(R.string.pass_not_match));
            return false;
        }
        return true;
    }

    private boolean validateTerms() {
        if (binding.termsCB.isChecked()) {
            return true;
        } else {
            showSickbar(getResources().getString(R.string.trms_acc));
            return false;
        }
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.CETfirstname));
        parms.put(Consts.MOBILE,mobileNumber);
        parms.put(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(binding.CETemailadd));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(binding.CETenterpassword));
        parms.put(Consts.ROLE, "2");
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.REFERRAL_CODE, ProjectUtils.getEditTextValue(binding.etReferal));
        Log.e(TAG, parms.toString());
        return parms;
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    private void getURLForWebView() {
        if (prefrence.getValue(Consts.LANGUAGE_SELECTION).equalsIgnoreCase("")) {
            prefrence.setValue(Consts.LANGUAGE_SELECTION, "en");
        }
        new HttpsRequest(baseURL, mContext).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                if (flag) {
                    try {
                        if (baseURL.equalsIgnoreCase(Consts.PRIVACY_URL)) {
                            Intent intent1 = new Intent(mContext, WebViewCommon.class);
                            intent1.putExtra(Consts.URL, msg);
                            intent1.putExtra(Consts.HEADER, getResources().getString(R.string.privacy_policy));
                            startActivity(intent1);
                        } else if (baseURL.equalsIgnoreCase(Consts.TERMS_URL)) {
                            Intent intent3 = new Intent(mContext, WebViewCommon.class);
                            intent3.putExtra(Consts.URL, msg);
                            intent3.putExtra(Consts.HEADER, getResources().getString(R.string.terms_of_use));
                            startActivity(intent3);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
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


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(mContext, SignInActivity.class));
        finish();
    }


    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);

            address=obj.getAddressLine(0) ;
          /*  binding.tvAddress.setText(obj.getAddressLine(0));
            Log.d(TAG, "getAddress: "+binding.tvAddress.getText());

            paramsBookingOp.put(Consts.ADDRESS,  String.valueOf(binding.tvAddress.getText()));
            paramsBookingOp.put(Consts.LATITUDE, String.valueOf(lat));
            paramsBookingOp.put(Consts.LONGITUDE, String.valueOf(lng));
            parms.put(Consts.ADDRESS,(obj.getAddressLine(0) ));
*/
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

