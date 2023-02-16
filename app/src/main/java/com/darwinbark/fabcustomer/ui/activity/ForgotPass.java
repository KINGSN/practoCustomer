package com.darwinbark.fabcustomer.ui.activity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivityForgotPassBinding;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

import static com.google.firebase.auth.PhoneAuthProvider.getCredential;

public class ForgotPass extends AppCompatActivity {
    private Context mContext;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = ForgotPass.class.getSimpleName();
    private ActivityForgotPassBinding binding;

    ImageView ivLogo;
    LinearLayout LLforgot;
    Animation from_top, from_bottom;
    private FirebaseAuth auth,mAuth;
    String verificationcodebysystem;
    com.chaos.view.PinView pinFromUser;
    String codeBySystem,mobileNumber;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_forgot_pass);
        mContext = ForgotPass.this;
        setUiAction();

        ivLogo = findViewById(R.id.ivLogo);
        LLforgot = findViewById(R.id.LLforgot);

        //load animation
        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        // passing animation
        ivLogo.startAnimation(from_top);
        LLforgot.startAnimation(from_bottom);

    }

    public void setUiAction() {
        countryCode="+"+binding.countryCodePicker.getSelectedCountryCode();

        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitForm();
              //  sendVerificationCodeToUser(mobileNumber);

                if (binding.mobiletxt.getText().toString().isEmpty()){
                    binding.mobiletxt.setError("Mobile No. can not be Empty");
                    // Toast.makeText(RegisterActivity.this, "Mobile No. can not be Empty", Toast.LENGTH_SHORT).show();
                    // return;
                }
                else if (binding.mobiletxt.getText().toString().length()!=10)
                {
                    binding.mobiletxt.setError("Insert 10 Digit Phone Number");
                    // Toast.makeText(RegisterActivity.this, "Input valid Mobile No.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                     mobileNumber=countryCode+""+binding.mobiletxt.getText().toString();
                   // loadingDialog.show();
                   // ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
                    sendVerificationCodeToUser(mobileNumber);


                }

                checkmobile(mobileNumber);
            }
        });
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = "+" + binding.countryCodePicker.getSelectedCountryCode();

            }
        });

        binding.verifyotpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userOtp= Objects.requireNonNull(binding.pinView.getText()).toString();


                if (userOtp.isEmpty() || userOtp.length() < 6) {
                    binding.pinView.setError("Wrong OTP...");
                    binding.pinView.requestFocus();
                    return;
                }else {
                    verifyCode(userOtp);
                    ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));

                }


                // Toast.makeText(RegisterActivity.this, userOtp+"success"+codeBySystem, Toast.LENGTH_SHORT).show();
                if (getCredential(codeBySystem,codeBySystem).equals(userOtp)){

                    binding.otpLayout.setVisibility(View.GONE);
                    binding.resetPaswdlay.setVisibility(View.VISIBLE);
                }

                //signInWithPhoneAuthCredential(credential);

            }
        });


        binding.resendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otpLayout.setVisibility(View.GONE);
                binding.LLforgot.setVisibility(View.VISIBLE);
              //  loginLayout.setVisibility(View.GONE);
                binding.resetPaswdlay.setVisibility(View.GONE);

            }
        });



        binding.resetloginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.LLforgot.setVisibility(View.VISIBLE);

            }
        });
        binding.vrfbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.otpLayout.setVisibility(View.GONE);
                binding.LLforgot.setVisibility(View.VISIBLE);
            }
        });

        binding.resetpswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.resetpaswdtxt1.getText().toString().equals("") || binding.resetpaswdtxt2.getText().toString().equals(""))
                {
                    if(binding.resetpaswdtxt1.getText().toString().equals(""))
                    {
                        binding.resetpaswdtxt1.setError("Insert A Password");
                    }  else if (binding.resetpaswdtxt2.getText().toString().equals(""))
                    {
                        binding.resetpaswdtxt2.setError("Insert The Same Password As Above");
                        //Toast.makeText(RegisterActivity.this,"Please Provide 6 Character Password",Toast.LENGTH_SHORT).show();
                    }

                    // Toast.makeText(RegisterActivity.this,"Fields can not be Empty",Toast.LENGTH_SHORT).show();
                }
                else if (binding.resetpaswdtxt1.getText().toString().length()<6)
                {
                    binding.resetpaswdtxt1.setError("New Pasword Lenght Should be 6 Digit");
                }

                else if (binding.resetpaswdtxt1.getText().toString().equals(binding.resetpaswdtxt2.getText().toString()))
                {
                    //loadingDialog.show();
                    ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
                    updatepass();

                }
                else
                {
                    binding.resetpaswdtxt2.setError("Password Didn't Matched");
                    //Toast.makeText(RegisterActivity.this,"Pass does not match",Toast.LENGTH_SHORT).show();
                }

                ////////////////////////////////////////////////////////////////////


            }
        });
    }

    public void submitForm() {
        if (!ValidateEmail()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                updatepass();

            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }


    public boolean ValidateEmail() {
        if (!ProjectUtils.isEmailValid( binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError(getResources().getString(R.string.val_email));
            binding.etEmail.requestFocus();
            return false;
        }
        return true;
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

                Log.d(TAG, "backResponse: "+response);
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                   /* finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);*/
                    ProjectUtils.pauseProgressDialog();
                    sendVerificationCodeToUser(ForgotPass.this.mobileNumber);
                } else {
                    // ProjectUtils.showToast(mContext, msg);
                    Toasty.error(ForgotPass.this,msg, Toast.LENGTH_SHORT, true).show();

                }
            }
        });
    }
    public void updatepass() {
        parms.put(Consts.MOBILE,mobileNumber);
        parms.put(Consts.ROLE, String.valueOf(2));
        parms.put(Consts.PASSWORD,binding.resetpaswdtxt1.getText().toString());
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                 //   ProjectUtils.showToast(mContext, msg);
                   /* finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
*/
                        Toasty.success(ForgotPass.this, msg, Toast.LENGTH_SHORT).show();


                        // finish();
                        // startActivity(getIntent());
                        // loadingDialog.dismiss();
                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ForgotPass.this);
                        alertDialogBuilder.setTitle("Password Has Been Updated");
                        alertDialogBuilder.setMessage((msg));
                        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                        alertDialogBuilder.setPositiveButton(ForgotPass.this.getResources().getString(R.string.ok_message),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // finish();
                                        Intent intent = new Intent(ForgotPass.this, SignInActivity.class);
                                        startActivity(intent);
                                       // loadingDialog.dismiss();
                                        //finishAffinity();
                                    }
                                });

                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                       // Toast.makeText(ForgotPass.this, msg, Toast.LENGTH_LONG).show();


                } else {
                   // ProjectUtils.showToast(mContext, msg);
                    ProjectUtils.pauseProgressDialog();
                }
            }
        });
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

                    binding.LLforgot.setVisibility(View.GONE);
                    binding.otpLayout.setVisibility(View.VISIBLE);

                    ProjectUtils.pauseProgressDialog();

                    Toasty.success(ForgotPass.this,"Mobile Verification Code sent, please check", Toast.LENGTH_SHORT, true).show();
                    // Toast.makeText(RegisterActivity.this, "Mobile Verification Code sent, please check", Toast.LENGTH_SHORT).show();
                    // loadingDialog.dismiss();
                    ProjectUtils.pauseProgressDialog();
                    ProjectUtils.pauseProgressDialog();

                }



                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));

                    if (code != null) {
                        pinFromUser.setText(code);
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
                            Intent intent = new Intent(ForgotPass.this,SignInActivity.class);
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
                    // Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toasty.error(mContext, "Verification Failed", Toast.LENGTH_SHORT).show();

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
                            Toasty.success(ForgotPass.this,"Mobile Number Verified, please Enter your desired password", Toast.LENGTH_SHORT, true).show();

                            binding.LLforgot.setVisibility(View.GONE);
                            binding.otpLayout.setVisibility(View.GONE);
                           // opensignup();
                            binding.otpLayout.setVisibility(View.GONE);
                            binding.resetPaswdlay.setVisibility(View.VISIBLE);
                            //loadingDialog.dismiss();
                            ProjectUtils.pauseProgressDialog();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //loadingDialog.dismiss();
                                Toasty.error(mContext, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }



}
