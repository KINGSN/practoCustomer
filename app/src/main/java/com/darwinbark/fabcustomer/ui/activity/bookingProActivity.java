package com.darwinbark.fabcustomer.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darwinbark.fabcustomer.AppEnvironment;
import com.darwinbark.fabcustomer.AppPreference;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.dto.ArtistDetailsDTO;
import com.darwinbark.fabcustomer.dto.ArtistLBookingDTO;
import com.darwinbark.fabcustomer.dto.HistoryDTO;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.CustomEditText;
import com.darwinbark.fabcustomer.utils.CustomTextView;
import com.darwinbark.fabcustomer.utils.CustomTextViewBold;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class bookingProActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG ="KINGSN";
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HistoryDTO historyDTO;
    public ArtistDetailsDTO artistDetailsDTO;
    public ArtistLBookingDTO artistLBookingDTO;
    private String artist_id = "";
    private String servicesId,payment_type;
    private int screen_tag = 0;

    private String hello;

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
    private String price,orderId,checksum,timezone,parmdetails,parmdetailsDTO,BookingDate,
            bookingDate,BookingSlot,booking_amount,BookingAddress;
    private String paycusid,paycusmobile,paycusemail,payartistprice;
    private Dialog loadingDialog;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();

    public static final String MID = "jSbwjs96829691726449";
    public static final String WEBSITE = "DEFAULT";
    public static final String CHANNEL_ID = "WAP";
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    private SimpleDateFormat sdf1, timeZone;
    private Date date;
    private AppPreference mAppPreference;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_payment);
        mContext = bookingProActivity.this;
        getWallet();
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());
        params.put(Consts.USER_ID, userDTO.getUser_id());

       // Toast.makeText(bookingProActivity.this,"userid: "+userDTO.getUser_id(),Toast.LENGTH_LONG).show();

        loadingDialog = new Dialog(bookingProActivity.this);
        loadingDialog.setContentView(R.layout.lotiee_loading);
        loadingDialog.setCancelable(false);
        if (loadingDialog.getWindow() != null)
            loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        // loadingDialog.show();






        //paramsBookingOp.put(Consts.DATE_STRING, sdf1.format(date).toString().toUpperCase());
       // paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));

        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            //artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            bookingDate=getIntent().getStringExtra(Consts.BookingDate);
            BookingSlot= getIntent().getStringExtra(Consts.BookingSlot);
            BookingAddress=getIntent().getStringExtra(Consts.ADDRESS);
            Log.d(TAG, "onCreate: "+BookingAddress+"BookingDate"+BookingDate);
           // Toast.makeText(bookingProActivity.this,"booking Details: "+"\n"+"Booking Date"+bookingDate+"\n"+"Time Of Appointmnent"+BookingSlot,Toast.LENGTH_LONG).show();
            Toasty.warning(bookingProActivity.this, "booking Details: "+"\n"+"Booking Date"+bookingDate+"\n"+"Time Of Appointmnent"+BookingSlot,Toast.LENGTH_LONG, true).show();

        }

        if (getIntent().hasExtra(Consts.paramsBookingOp)) {
    /*artistLBookingDTO = (ArtistLBookingDTO) getIntent().getSerializableExtra(Consts.paramsBookingOp);
    parmdetails = getIntent().getStringExtra(Consts.paramsBookingOp);*/
           // artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
           // paramsBookingOp= Consts.paramsBookingOp;
          //  Toast.makeText(bookingProActivity.this,"bookArtistintent1: "+Consts.paramsBookingOp,Toast.LENGTH_LONG).show();

        }

       // paycusid= userDTO.getMobile();
        paycusid=userDTO.getUser_id();
        paycusemail=userDTO.getEmail_id();
        paycusmobile=userDTO.getMobile();
        payartistprice=artistDetailsDTO.getPrice();
       // Toast.makeText(bookingProActivity.this,""+( userDTO.getMobile())+paycusid+
               // userDTO.getEmail_id()+"price"+artistDetailsDTO.getPrice(),Toast.LENGTH_SHORT).show();
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
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivArtist);

        tvCategory.setText(artistDetailsDTO.getCategory_name());
        tvLocation.setText(artistDetailsDTO.getLocation());
        tvName.setText(ProjectUtils.getFirstLetterCapital(artistDetailsDTO.getName()));
       /* tvAmount.setText(artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice());*/

        final_amount =artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice();
        booking_amount = artistDetailsDTO.getPrice();
        tvAmount.setText(final_amount);
        getWallet();
       // Toast.makeText(bookingProActivity.this,"bookArtistintent1: "+final_amount,Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPayment:
                //dialogPayment();
               // loadingDialog.show();
               // GenerateChecksum();
               // bookArtist();
                launchPayUMoneyFlow();

               // Booking booking = new Booking();
               // booking.bookArtist2();

                break;
            case R.id.llWallet:
                if (Float.parseFloat(amt1) >= Float.parseFloat(payartistprice)) {
                    cashDialog(getResources().getString(R.string.wallet_payment), getResources().getString(R.string.wallet_msg), "2");
                } else {
                    //ProjectUtils.showToast(mContext, "Insufficient balance, please add money to wallet first.");
                    Toasty.error(mContext, "Insufficient balance, please add money to wallet first.", Toast.LENGTH_SHORT, true).show();

                }

                break;
            case R.id.llCash:
               // cashDialog(getResources().getString(R.string.cash_payment), getResources().getString(R.string.cash_msg), "1");
                break;
            case R.id.tvApplyCode:
               // params.put(Consts.INVOICE_ID, historyDTO.getInvoice_id());
                params.put(Consts.COUPON_CODE, ProjectUtils.getEditTextValue(etCode));
                params.put(Consts.AMOUNT, artistDetailsDTO.getPrice());

                checkCoupon();
                break;
            case R.id.tvCancelCode:
                etCode.setText("");
                tvAmount.setText(artistDetailsDTO.getCurrency_type() + historyDTO.getTotal_amount());
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
                        ProjectUtils.showToast(mContext, "Coupon Applied Successfully");
                        String amt = response.getString("final_amount");
                        discount_amount = response.getString("discount_amount").toString();
                        Log.d(TAG, "Discount"+discount_amount+"amt"+amt);
                        booking_amount = amt;
                       /* tvAmount.setText(historyDTO.getCurrency_type() + amt);*/
                        tvApplyCode.setVisibility(View.GONE);
                        tvCancelCode.setVisibility(View.VISIBLE);
                        coupon_code = etCode.getText().toString().trim();
                        etCode.setEnabled(false);
                        tvAmount.setText(artistDetailsDTO.getCurrency_type() +final_amount);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, "Coupon Not Valid");
                    etCode.setEnabled(true);
                    etCode.setText("");
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
                           // sendPayment(type);
                         final String payment_type="2";
                            bookArtist(payment_type);
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
          //  sendPayment("0");
            bookArtist(payment_type);
        } else if (prefrence.getValue(Consts.FURL).equalsIgnoreCase(Consts.INVOICE_PAYMENT_FAIL_Stripe)) {
            prefrence.clearPreferences(Consts.FURL);
            finish();
        } else if (prefrence.getValue(Consts.SURL).equalsIgnoreCase(Consts.PAYMENT_SUCCESS_paypal)) {
            prefrence.clearPreferences(Consts.SURL);
           // sendPayment("0");
            bookArtist(payment_type);
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
                        currency = response.getJSONObject("data").getString("currency_type");
                        //Toast.makeText(bookingProActivity.this,"custwallet: "+amt1,Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    private void GenerateChecksum() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        String url="http://appointu.online/admin/paytm1/generateChecksum.php";


        Map<String, String> params = new HashMap<>();
        params.put( "MID" , MID);
        params.put( "ORDER_ID" , orderId);
        params.put( "CUST_ID" , paycusid);
        params.put( "MOBILE_NO" ,userDTO.getMobile());
        params.put( "EMAIL" , paycusemail);
        params.put( "CHANNEL_ID" , "WAP");
        params.put( "TXN_AMOUNT" , artistDetailsDTO.getPrice());
        params.put( "WEBSITE" , WEBSITE);
        params.put( "INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
        params.put( "CALLBACK_URL", CALLBACK_URL);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,

                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Toast.makeText(bookingProActivity.this,response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH"))
                            {
                                checksum=jsonObject.getString("CHECKSUMHASH");
                                onStartTransaction();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(bookingProActivity.this,"Error...!",Toast.LENGTH_SHORT).show();
//                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params=new HashMap<String, String>();
                params.put( "MID" , MID);
                params.put( "ORDER_ID" , orderId);
                params.put( "CUST_ID" , paycusid);
                params.put( "MOBILE_NO" ,userDTO.getMobile());
                params.put( "EMAIL" , paycusemail);
                params.put( "CHANNEL_ID" , "WAP");
                params.put( "TXN_AMOUNT" , payartistprice);
                params.put( "WEBSITE" , WEBSITE);
                params.put( "INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
                params.put( "CALLBACK_URL", CALLBACK_URL);
                return params;
            }
        };


        queue.add(stringRequest);

// Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void onStartTransaction() {
        loadingDialog.show();
        //paycusid= userDTO.getMobile();
        paycusid=userDTO.getUser_id();
        paycusemail=userDTO.getEmail_id();
        paycusmobile=userDTO.getMobile();
        payartistprice=artistDetailsDTO.getPrice();

        PaytmPGService Service = PaytmPGService.getProductionService();
        HashMap<String, String> paramMap = new HashMap<>();
        System.out.println("===== parms " + params.toString());
        paramMap.put( "MID" , MID);
        // Key in your staging and production MID available in your dashboard

        paramMap.put( "ORDER_ID" , orderId);
        paramMap.put( "CUST_ID" , paycusid);
        paramMap.put( "MOBILE_NO" ,userDTO.getMobile());
        paramMap.put( "EMAIL" , paycusemail);
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , payartistprice);
        paramMap.put( "WEBSITE" , WEBSITE);
        paramMap.put( "INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
        paramMap.put( "CALLBACK_URL", CALLBACK_URL);
        paramMap.put( "CHECKSUMHASH" , checksum);



        PaytmOrder Order = new PaytmOrder(paramMap);

		/*PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true,
                true, new PaytmPaymentTransactionCallback() {

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        System.out.println("===== onTransactionResponse " + inResponse.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(inResponse.getString("STATUS"), "TXN_SUCCESS")) {
                                //    Payment Success
                               // Toast.makeText(bookingProActivity.this," Transaction success",Toast.LENGTH_LONG).show();
                                Toasty.success(bookingProActivity.this, " Transaction success", Toast.LENGTH_LONG, true).show();



                                //uploadData();
                                //login();
                                //Booking booking = new Booking();
                                //booking.bookArtist();
                                final String payment_type="2";
                                bookArtist(payment_type);

                            } else if (!inResponse.getBoolean("STATUS")) {
                                //    Payment Failed
                              //  Toast.makeText(bookingProActivity.this," Transaction Failed",Toast.LENGTH_LONG).show();
                                Toasty.error(bookingProActivity.this, " Transaction Failed", Toast.LENGTH_LONG, true).show();
                                //startActivity(new Intent(getContext(), HomeActivity.class));
                                finish();
                                startActivity(getIntent());
                                loadingDialog.dismiss();

                            }
                        }
                    }

                    @Override
                    public void networkNotAvailable() {
                        // network error
                        //clickOnGenerate();
                        finish();
                        startActivity(getIntent());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        // AuthenticationFailed
                        finish();
                        startActivity(getIntent());

                        // clickOnGenerate();
                        //startActivity(new Intent(getContext(), HomeActivity.class));
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        // UI Error
                        // clickOnGenerate();
                        finish();
                        startActivity(getIntent());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        //  Web page loading error
                        //clickOnGenerate();
                        //startActivity(new Intent(getContext(), HomeActivity.class));
                        finish();
                        startActivity(getIntent());
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onBackPressedCancelTransaction() {
                        // on cancelling transaction

                        //clickOnGenerate();
                        // startActivity(new Intent(getContext(), HomeActivity.class));
                        finish();
                        startActivity(getIntent());
                        loadingDialog.dismiss();

                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        // maybe same as onBackPressedCancelTransaction()
                        //clickOnGenerate();
                        //startActivity(new Intent(getContext(), HomeActivity.class));
                        finish();
                        startActivity(getIntent());
                        loadingDialog.dismiss();
                    }
                });
    }

    public void bookArtist(String payment_type) {

       // Toast.makeText(bookingProActivity.this," type_p"+payment_type,Toast.LENGTH_LONG).show();

        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
        paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
        paramsBookingOp.put(Consts.TITLE, "Direct Booking");
        //paramsBookingOp.put(Consts.ADDRESS, userDTO.getOffice_address());
        paramsBookingOp.put(Consts.ADDRESS, BookingAddress);
        paramsBookingOp.put(Consts.LATITUDE, userDTO.getLive_lat());
        paramsBookingOp.put(Consts.LONGITUDE, userDTO.getLive_long());
        paramsBookingOp.put(Consts.DATE_STRING, bookingDate);
        paramsBookingOp.put(Consts.BookingSlot, BookingSlot);
       // paramsBookingOp.put(Consts.BookingSlot, "10:00:00");
        paramsBookingOp.put(Consts.TIMEZONE,"GMT+05:30");
        paramsBookingOp.put("payment_type",payment_type);


        Log.e(TAG, "bookArtist: "+paramsBookingOp.toString() );

       // Toast.makeText(bookingProActivity.this,"bookArtist: "+Consts.PRICE,Toast.LENGTH_LONG).show();

        //Toast.makeText(bookingProActivity.this,"bookArtistint2: "+paramsBookingOp.toString(),Toast.LENGTH_LONG).show();
        //paramsBookingOp=Consts.paramsBookingOp;

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_ARTIST_API, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                   //finish();
                    Intent viewService = new Intent(mContext, BaseActivity.class);
                    mContext.startActivity(viewService);
                    loadingDialog.dismiss();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                    loadingDialog.dismiss();
                }


            }
        });


    }


    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText((("APPOINTU")));

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle((("APPOINTU PAYMENT")));

       // payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(booking_amount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        //String txnId = "TXNID720431525261327973";
        String phone = userDTO.getMobile().trim();
        String productName ="Appointu Payment";
        String firstName = userDTO.getName().trim();
        String email = paycusemail.trim();
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
            calculateServerSideHashAndInitiatePayment1(mPaymentParams);

            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,bookingProActivity.this, AppPreference.selectedTheme,mAppPreference.isOverrideResultScreen());
            } else {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,
                        bookingProActivity.this,
                       R.style.AppTheme_default,false);
            }

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage()+"kingsn", Toast.LENGTH_LONG).show();
           // payNowButton.setEnabled(true);
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     */
    private void calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4)).append("|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5)).append("||||||");

       // AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        AppEnvironment appEnvironment =AppEnvironment.PRODUCTION;
       // stringBuilder.append("EQIBm758lb");
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

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
            byte[] messageDigest = algorithm.digest();
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
                    final String payment_type="1";
                    bookArtist(payment_type);
                } else {
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();

          /*      new android.app.AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();*/
                Log.d(TAG, "onActivityResult: "+("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse));
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }


}
