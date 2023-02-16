package com.darwinbark.fabcustomer.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivityNumberVerificationBinding;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.CustomEditText;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static com.google.firebase.auth.PhoneAuthProvider.getCredential;

public class numberVerificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
//    private String TAG = numberVerificationActivity.class.getSimpleName();
     private String TAG ="KINGSN";
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivityNumberVerificationBinding binding;
    String baseURL = "";
    CustomEditText mobiletxt;
    private String countryCode;
    private FirebaseAuth auth,mAuth;
    String verificationcodebysystem;
    com.chaos.view.PinView pinFromUser;
    String codeBySystem,mobileNumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private HashMap<String, String> parms = new HashMap<>();

    ImageView ivLogo;
    LinearLayout LLsignup;
    Animation from_top, from_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        ProjectUtils.Fullscreen(numberVerificationActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_number_verification);
        mContext = numberVerificationActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        setUiAction();

        ivLogo = findViewById(R.id.ivLogo);
        LLsignup = findViewById(R.id.LLsignup);

        //load animation
        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        // passing animation
        ivLogo.startAnimation(from_top);
        LLsignup.startAnimation(from_bottom);
        mobiletxt=findViewById(R.id.mobiletxt);

    }

    public void setUiAction() {
        countryCode="+"+binding.countryCodePicker.getSelectedCountryCode();


        binding.verifyBtn.setOnClickListener(this);
        binding.CTVsignin.setOnClickListener(this);
        binding.vrfbackBtn.setOnClickListener(this);
      //  binding.vrfbackBtn.setClickable(false);

        binding.verifyotpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOtp= binding.pinView.getText().toString();


                if (userOtp.isEmpty() || userOtp.length() < 6) {
                    binding.pinView.setError("Wrong OTP...");
                    binding.pinView.requestFocus();
                    return;
                }else {
                    verifyCode(userOtp);

                }


                // Toast.makeText(RegisterActivity.this, userOtp+"success"+codeBySystem, Toast.LENGTH_SHORT).show();
                if (getCredential(codeBySystem,codeBySystem).equals(userOtp)){

                    binding.otpLayout.setVisibility(View.GONE);
                   // registerLayout.setVisibility(View.VISIBLE);
                    opensignup();
                }

                //signInWithPhoneAuthCredential(credential);

            }
        });

        binding.vrfbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otpLayout.setVisibility(View.GONE);
                binding.verifyMobile.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verifyBtn:
                clickForSubmit();
                break;
            case R.id.CTVsignin:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
            case R.id.country_code_picker:
                clickCountrycodepicker();
                break;


        }

    }


    @SuppressLint("StringFormatInvalid")
    public void clickForSubmit() {
        if ((binding.countryCodePicker.getSelectedCountryCode()==null)) {
            Toasty.error(this,"Select Your Country").show();
        } else if (!ProjectUtils.isPhoneNumberValid(mobiletxt.getText().toString().trim())) {
            showSickbar("Enter Valid Number");
            mobiletxt.setError("Enter Valid Number");
            Toasty.error(this,"Enter Valid Number", Toast.LENGTH_SHORT, true).show();

        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                mobileNumber=countryCode+""+mobiletxt.getText().toString();
                Log.d(TAG, "clickForSubmit: "+mobileNumber);
                // Toast.makeText(RegisterActivity.this, ""+mobileNumber, Toast.LENGTH_SHORT).show();
                ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
               // sendVerificationCodeToUser(mobileNumber);
                checkmobile(mobileNumber);
            } else {
             //   ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                Toasty.error(this,getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT, true).show();


            }
        }


    }


    public void checkmobile( String mobileNumber) {
        ProjectUtils.pauseProgressDialog();
        parms.put(Consts.MOBILE, mobileNumber);
        parms.put(Consts.ROLE, String.valueOf(2));
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.FORGET_PASSWORD_BY_MOBILE_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                Log.d(TAG, "backResponsed: " + response);
                if (flag) {
                    ProjectUtils.pauseProgressDialog();
                   // ProjectUtils.showToast(mContext, msg);
                   /* finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);*/
//                    ProjectUtils.pauseProgressDialog();
//                    sendVerificationCodeToUser(numberVerificationActivity.this.mobileNumber);
                    Toasty.error(numberVerificationActivity.this,msg, Toast.LENGTH_SHORT, true).show();
                  //  Toasty.error(numberVerificationActivity.this, "Mobile Number Is Allready Registered", Toast.LENGTH_SHORT, true).show();

                } else {
                    // ProjectUtils.showToast(mContext, msg);
                  //  Toasty.error(numberVerificationActivity.this, msg, Toast.LENGTH_SHORT, true).show();
                    sendVerificationCodeToUser(numberVerificationActivity.this.mobileNumber);
                    ProjectUtils.pauseProgressDialog();
                }
            }
        });
    }
    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    private void clickCountrycodepicker() {

        binding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = "+" + binding.countryCodePicker.getSelectedCountryCode();

            }
        });

    }

    public void checkifphoneexist() {
//        mAuth.getCurrentUser(mobileNumber); userRecord = FirebaseAuth.getInstance().getCurrentUser();
//// See the UserRecord reference doc for the contents of userRecord.
//        System.out.println("Successfully fetched user data: " + userRecord.getPhoneNumber());


    }
    private void sendVerificationCodeToUser(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;

                    verificationcodebysystem= s;
//                    loadingDialog.show();
//                    verifyMobile.setVisibility(View.GONE);
//                    otpLayout.setVisibility(View.VISIBLE);

                    binding.verifyMobile.setVisibility(View.GONE);
                    binding.otpLayout.setVisibility(View.VISIBLE);



                    Toasty.success(numberVerificationActivity.this,"Mobile Verification Code sent, please check", Toast.LENGTH_SHORT, true).show();
                   // Toast.makeText(RegisterActivity.this, "Mobile Verification Code sent, please check", Toast.LENGTH_SHORT).show();
                   // loadingDialog.dismiss();
                    ProjectUtils.pauseProgressDialog();

                }



                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));

                    if (code != null) {
                        binding.pinView.setText(code);
                        verifyCode(code);
                        ProjectUtils.pauseProgressDialog();


                    }
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    // Toast.makeText(RegisterActivity.this, "codetimeout "+s, Toast.LENGTH_SHORT).show();
                    binding.vrfbackBtn.setClickable(true);
                      binding.vrfbackBtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Intent intent = new Intent(numberVerificationActivity.this,SignInActivity.class);
                              startActivity(intent);
                          }
                      });
                    ProjectUtils.pauseProgressDialog();
                   /* Intent intent = new Intent(numberVerificationActivity.this,SignUpActivity.class);
                    startActivity(intent);*/
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    ProjectUtils.pauseProgressDialog();
                     Toast.makeText(numberVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   // Toasty.error(mContext, "Verification Failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onVerificationFailed: "+e.getMessage());

                }
            };



    private void verifyCode(String code) {
        PhoneAuthCredential credential = getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);

        ProjectUtils.pauseProgressDialog();


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Verification completed successfully here Either
                            // store the data or do whatever desire
                            /*editor.putString(mobileno, String.valueOf(verifyNumber.getText()));*/
                            binding.verifyMobile.setVisibility(View.GONE);
                            binding.otpLayout.setVisibility(View.GONE);
                            opensignup();
                            //loadingDialog.dismiss();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //loadingDialog.dismiss();
                                Toast.makeText(mContext, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



    public void opensignup(){
        Intent viewService = new Intent(mContext, SignUpActivity.class);
        viewService.putExtra(Consts.CALL_PHONE, mobileNumber);
        //Log.d(TAG, "switchtoslotbooking: "+viewService.getStringExtra(Consts.ADDRESS));
        // Toast.makeText(Booking.this,"bookArtist4me: "+viewService.getStringExtra(String.valueOf(BookingDate)),Toast.LENGTH_LONG).show();
        mContext.startActivity(viewService);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        startActivity(new Intent(mContext, SignInActivity.class));
        finish();
    }
}
