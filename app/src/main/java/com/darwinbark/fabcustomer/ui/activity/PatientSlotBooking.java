package com.darwinbark.fabcustomer.ui.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.darwinbark.fabcustomer.R;

import com.darwinbark.fabcustomer.application.GlobalState;
import com.darwinbark.fabcustomer.databinding.PatientSlotBookingBinding;
import com.darwinbark.fabcustomer.dto.ArtistDetailsDTO;
import com.darwinbark.fabcustomer.dto.ProductDTO;
import com.darwinbark.fabcustomer.dto.Slot_Data;
import com.darwinbark.fabcustomer.dto.UserDTO;
import com.darwinbark.fabcustomer.https.HttpsRequest;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.network.NetworkManager;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.adapter.bookingSlotAdapter;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.darwinbark.fabcustomer.interfacess.Consts.BookingDate;
import static com.darwinbark.fabcustomer.interfacess.Consts.BookingSlot;

public class PatientSlotBooking extends AppCompatActivity implements bookingSlotAdapter.TimeSlotAdapterListener{
    private PatientSlotBookingBinding binding;
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public ArtistDetailsDTO artistDetailsDTO;
  //  private Date date;
    private SimpleDateFormat sdf1, timeZone;
    private HashMap<String, String> paramsBookingOp = new HashMap<>();
    private String TAG = PatientSlotBooking.class.getSimpleName();
    private JSONArray array;
    private String servicesId;
    private ArrayList<ProductDTO> serviceList;
    private String artist_id = "";
    private int screen_tag = 0;
    private String price,orderId,checksum,bookingDate,BookingAddress,booking_date;
    private Dialog loadingDialog;
    private Button paybtn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String date, time = "", shift;
    private RecyclerView slot_recyclerView;
   // private List<Slot_Data> slot_data;
    LinearLayoutManager linearLayoutManager1;
    bookingSlotAdapter slotAdapter;
    HashMap<String, String> params = new HashMap<>();
    private BaseActivity baseActivity;
    Slot_Data slotDataDTO;
    GlobalState globalState;
    private SharedPrefrence sharedPrefrence;
    private String slotid = "";
    LinearLayoutManager linearLayoutManager2;
//    Slot_Data slotDTOArrayList = new Slot_Data();
    ArrayList<Slot_Data> slotDTOArrayList;

    public static final String MID = "jSbwjs96829691726449";
    public static final String WEBSITE = "DEFAULT";
    public static final String CHANNEL_ID = "WAP";
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    private Slot_Data Slot_Data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.patient_slot_booking);
        mContext = PatientSlotBooking.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);

        slotDTOArrayList = new ArrayList<>();
        slot_recyclerView = findViewById(R.id.bookingslot_recycler);
        slot_recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        sharedPrefrence = SharedPrefrence.getInstance(this);
        //date = new Date();
       // paybtn=(Button) findViewById(R.id.paybtn);


       // paramsBookingOp.put(Consts.DATE_STRING, BookingDate.toString().toUpperCase());
        //paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));

        loadingDialog = new Dialog(PatientSlotBooking.this);
        loadingDialog.setContentView(R.layout.lotiee_loading);
        loadingDialog.setCancelable(false);
        if (loadingDialog.getWindow() != null)
            loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
       // loadingDialog.show();


       // Toast.makeText(PatientSlotBooking.this, "Error: " + getIntent().hasExtra(Consts.ARTIST_DTO),
               // Toast.LENGTH_LONG).show();
        linearLayoutManager2 = new LinearLayoutManager(PatientSlotBooking.this, LinearLayoutManager.HORIZONTAL, false);

        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            bookingDate=getIntent().getStringExtra(Consts.BookingDate);
            BookingAddress=getIntent().getStringExtra(Consts.ADDRESS);
            Log.d(TAG, "onCreate: "+getIntent().getStringExtra(Consts.ARTIST_ID));


            if (screen_tag == 2) {
                servicesId = getIntent().getStringExtra(Consts.SERVICE_ARRAY);
                serviceList = (ArrayList<ProductDTO>) getIntent().getSerializableExtra(Consts.SERVICE_NAME_ARRAY);
                price = getIntent().getStringExtra(Consts.PRICE);


                Log.e(TAG, "onCreate:servicesId "+servicesId);
                Log.e(TAG, "onCreate:servicesName "+serviceList);

                try {
                    array = new JSONArray(servicesId);
                    System.out.println(array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        bookArtist();
        // setUiAction();
       /* paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GenerateChecksum();
                switchtobookingProActivity();
            }
        });*/
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPrefrence.getValue(Consts.Booking_time).equals("")) {
                   /* if (screen_tag == 1) {
                        paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                        paramsBookingOp.put(Consts.TITLE, "Direct Booking");*/
                       // submit();
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        // bookArtist();
                        // GenerateChecksum();
                        //switchtobookingProActivity();
                        switchtobookingProActivity();
                    } else {
                        ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                    }

                    } else if ((screen_tag == 2)) {
                        bookForService();

                    }
                }

        });
    }




    public void bookForService() {
        if ((array.length() > 0)) {
            String strTitle = serviceList.toString().replace("[", "")
                    .replace("]", "").replace(", ", ",");;
            paramsBookingOp.put(Consts.SERVICE_ID, array.toString());
            paramsBookingOp.put(Consts.TITLE, strTitle);
            //submit();
        } else {
            ProjectUtils.showLong(mContext, mContext.getResources().getString(R.string.select_any_service));
        }
    }


    public void bookArtist() {
        paramsBookingOp.put("user_id", artist_id);
        paramsBookingOp.put("booking_date",String.valueOf(bookingDate));

        Log.e(TAG, "bookArtist: "+paramsBookingOp.toString() );

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_APPOINTMENT_SLOT, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) throws JSONException {
                Log.e(TAG, "bookArtist: "+response );
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                   // ProjectUtils.showToast(mContext, msg);
                   // finish();
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
                        boolean status = jsonObject.getBoolean("status");
                        String message = jsonObject.getString("messege");
                        System.out.println("response"+response);
                        if (status==true){
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonObject1.getJSONArray("available_slots");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String slot_time = object.getString("slot_time");
                                String remaining_slot =object.getString("remaining_booking");
                                slotDTOArrayList.add(new Slot_Data(slot_time,remaining_slot));
                                slotAdapter = new bookingSlotAdapter(slotDTOArrayList,PatientSlotBooking.this,PatientSlotBooking.this);
                                slot_recyclerView.setAdapter(slotAdapter);
                                Log.d("sbcsbc",slot_time+"");
                            } linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                            // slotAdapter = new bookingSlotAdapter(slot_data,this);

                            String data = jsonObject1.getString("opening_time");
                            Log.d("sbcsbc",data+"");
                        }

//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
                }


            }
        });
    }





    private void findPlace() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withGooglePlacesEnabled()
                //.withLocation(41.4036299, 2.1743558)
                .build(mContext);

        startActivityForResult(locationPickerIntent, 101);
    }




  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                try {
                    getAddress(data.getDoubleExtra(LATITUDE, 0.0), data.getDoubleExtra(LONGITUDE, 0.0));


                } catch (Exception e) {

                }
            }
        }

    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


 /*   public void submit() {
        if (!validation(binding.tvBookingDate, getResources().getString(R.string.val_date))) {
            return;
        } else if (!validation(binding.tvAddress, getResources().getString(R.string.val_address))) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
               // bookArtist();
               // GenerateChecksum();
                //switchtobookingProActivity();
                switchtobookingProActivity();
              } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }*/

    public boolean validation(TextView textView, String msg) {
        if (!ProjectUtils.isTextFilled(textView)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }


    private void GenerateChecksum() {
        Random r = new Random(System.currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        String url="https://darwinbark.com/projects/qrcoder/paytm1/generateChecksum.php";


        Map<String, String> params = new HashMap<>();
        params.put( "MID" , MID);
        params.put( "ORDER_ID" , orderId);
        params.put( "CUST_ID" , "7000621203");
        params.put( "MOBILE_NO" , "7000621203");
        params.put( "EMAIL" , "so@gmail.com");
        params.put( "CHANNEL_ID" , "WAP");
        params.put( "TXN_AMOUNT" , "1");
        params.put( "WEBSITE" , WEBSITE);
        params.put( "INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
        params.put( "CALLBACK_URL", CALLBACK_URL);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(PatientSlotBooking.this,response,Toast.LENGTH_SHORT).show();
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

                Toast.makeText(PatientSlotBooking.this,"Error...!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put( "MID" , MID);
                params.put( "ORDER_ID" , orderId);
                params.put( "CUST_ID" , "7000621203");
                params.put( "MOBILE_NO" , "7000621203");
                params.put( "EMAIL" , "so@gmail.com");
                params.put( "CHANNEL_ID" , "WAP");
                params.put( "TXN_AMOUNT" , "1");
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
        PaytmPGService Service = PaytmPGService.getProductionService();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put( "MID" , MID);
        // Key in your staging and production MID available in your dashboard

        paramMap.put( "ORDER_ID" , orderId);
        paramMap.put( "CUST_ID" , "7000621203");
        paramMap.put( "MOBILE_NO" , "7000621203");
        paramMap.put( "EMAIL" , "so@gmail.com");
        paramMap.put( "CHANNEL_ID" , "WAP");
        paramMap.put( "TXN_AMOUNT" , "1");
        paramMap.put( "WEBSITE" , WEBSITE);
        paramMap.put( "INDUSTRY_TYPE_ID" , INDUSTRY_TYPE_ID);
        paramMap.put( "CALLBACK_URL", CALLBACK_URL);
        paramMap.put( "CHECKSUMHASH" , checksum);

/*
        paramMap.put("MID" , "WorldP64425807474247");
        paramMap.put("ORDER_ID" , "210lkldfka2a27");
        paramMap.put("CUST_ID" , "mkjNYC1227");
        paramMap.put("INDUSTRY_TYPE_ID" , "Retail");
        paramMap.put("CHANNEL_ID" , "WAP");
        paramMap.put("TXN_AMOUNT" , "1");
        paramMap.put("WEBSITE" , "worldpressplg");
        paramMap.put("CALLBACK_URL" , "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");*/


        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);

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
                                Toast.makeText(PatientSlotBooking.this," Transaction success",Toast.LENGTH_LONG).show();

                                //uploadData();
                                //login();
                                bookArtist();
                            } else if (!inResponse.getBoolean("STATUS")) {
                                //    Payment Failed
                                Toast.makeText(PatientSlotBooking.this," Transaction Failed",Toast.LENGTH_LONG).show();
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

    private void switchtobookingProActivity(){
        /*Intent in = new Intent(mContext, PaymentProActivity.class);
        //in.putExtra(Consts.artistDetailsDTO, objects.get(position));
        mContext.startActivity(in);*/

       // Toast.makeText(Booking.this,"bookArtist: "+Consts.PRICE,Toast.LENGTH_LONG).show();
        Toast.makeText(mContext,"bookArtistB: "+sharedPrefrence.getValue(Consts.Booking_time),Toast.LENGTH_LONG).show();


        Intent viewService = new Intent(mContext, bookingProActivity.class);
        viewService.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        viewService.putExtra(Consts.ARTIST_ID, artist_id);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.paramsBookingOp, paramsBookingOp);
        viewService.putExtra(BookingDate, String.valueOf(bookingDate));
        viewService.putExtra(BookingSlot,sharedPrefrence.getValue(Consts.Booking_time));
        viewService.putExtra(Consts.ADDRESS, (BookingAddress));
        //viewService.putExtra(String.valueOf(paramsBookingOp), paramsBookingOp);
       // Toast.makeText(Booking.this,"bookArtist4me: "+viewService.getStringExtra(String.valueOf(BookingDate)),Toast.LENGTH_LONG).show();
        mContext.startActivity(viewService);
    }

    @Override
    public void clickTimeSlot(com.darwinbark.fabcustomer.dto.Slot_Data timeSlotData, int position) {
        slotid = timeSlotData.getSlot_time();
        slotAdapter.setSelectedTimeSlot(timeSlotData);
        Log.d("fshgch",slotid+"");
    }

 /*public void getjsondata(){

     JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
             Request.Method.POST, RestAPI.API_Refer_History+ReferCode,null,
             new Response.Listener<JSONArray>() {
                 @Override
                 public void onResponse(JSONArray response) {
                     // Do something with response
                     //mTextView.setText(response.toString());
                     //Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                     // Process the JSON
                     try{
                         // Loop through the array elements
                         for(int i=0;i<response.length();i++){
                             // Get current json object
                             JSONObject ob = response.getJSONObject(i);

                             //JSONObject ob=array.getJSONObject(i);

                             Refer_Data ld1=new Refer_Data(ob.getString("reffered_paid"),ob.getString("joining_time"),ob.getString("name"));
                             refer_data.add(ld1);
                             //Toast.makeText(getActivity(), "hello"+ob.getString("amount"), Toast.LENGTH_LONG).show();


                         }
                         referAdapter= new ReferAdapter(refer_data,getContext());
                         //recyclerView.setLayoutManager(new LinearLayoutManager(view));
                         //GridLayoutManager lm = new GridLayoutManager(view, 1);
                         // recyclerView.setLayoutManager();
                         refer_recycler.setAdapter(referAdapter);

                     }catch (JSONException e){
                         e.printStackTrace();
                         //  Toast.makeText(getActivity(), "hello"+e, Toast.LENGTH_LONG).show();
                     }
                 }
             },
             new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     Toast.makeText(ReferActivity.this, "RESPONSE: " + error, Toast.LENGTH_SHORT).show();
                     Log.e("Error", "" + error.getMessage());
                     Toast.makeText(ReferActivity.this, "ErrorV: " + error.getMessage(),
                             Toast.LENGTH_SHORT).show();
                     loadingDialog.dismiss();
                 }
             }) {  @Override
     protected Map<String, String> getParams() {
         Map<String, String> params = new HashMap<>();
         params.put("user_Referal_history","");
         params.put("users_id",test);
         params.put("refer_code",ReferCode);

         return params;
     }
     };


     RequestQueue requestQueue= Volley.newRequestQueue(ReferActivity.this);
     requestQueue.add(jsonArrayRequest);

 }*/

}
