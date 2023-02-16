package com.darwinbark.fabcustomer.ui.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.databinding.ActivityBookingBinding;
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
import com.darwinbark.fabcustomer.utils.CalenderUtil;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.darwinbark.fabcustomer.interfacess.Consts.BookingDate;
import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static java.lang.System.currentTimeMillis;

public class Booking extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public static final String MID = "jSbwjs96829691726449";
    public static final String WEBSITE = "DEFAULT";
    public static final String CHANNEL_ID = "WAP";
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
    final int MAX_SELECTABLE_DATE_IN_FUTURE = 10; // 365 days into the future max
    private final HashMap<String, String> paramsBookingOp = new HashMap<>();
    private final String TAG ="KINGSN";
    private final String time = "";
    private final String str = "hello";
    public ArtistDetailsDTO artistDetailsDTO;
    MaterialDatePicker materialDatePicker;
    LinearLayoutManager linearLayoutManager1;
    bookingSlotAdapter recyclerAdapter;
    int Year, Month, Day, Hour, Minute, avdays, d3, d4, d5, d6, d7;
    Calendar Selectable_Day;
    int d1;
    int d2;
    String demoDate = "22-5-2021";
    String demoDayName = "Friday";
    List<String> stringList = new ArrayList<>();
    private ActivityBookingBinding binding;
    private Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    //  private Date date;
    private SimpleDateFormat sdf1, timeZone;
    private JSONArray array;
    private String servicesId;
    private String available_days;
    private ArrayList<ProductDTO> serviceList;
    private String artist_id = "";
    private int screen_tag = 0;
    private String price, orderId, checksum, bookingDate, BookingAddress;
    private Dialog loadingDialog;
    private Button paybtn, pickdate;
    private Calendar calendar;
    private RelativeLayout mainlay;
    private FrameLayout frame;
    private CalendarView calendarView;
    private DatePickerDialog datePickerDialog;
    private String date;
    private String shift;
    private RecyclerView recyclerView;
    private List<Slot_Data> slot_data;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd;
    private CalendarView mCalendarView;
    private String Day1, Day2, Day3, Day4, Day5, Day6, Day7;


    public Booking() {
        // Required empty public constructor
    }

    public String[] responseStringToStringArray(String strResponse) {
        ArrayList<String> responseArraylist = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(strResponse);
            for (int i = 0; i < jsonArray.length(); i++)
                responseArraylist.add(jsonArray.getString(i));
            Log.d("shubhcode", "responseStringToStringArray: " + jsonArray.toString());

        } catch (JSONException e) {
            Log.d(TAG, "onClick: incorrect json array format");
            e.printStackTrace();
        }
        return responseArraylist.toArray(new String[0]); // String str[]= String[0]
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking);
        mContext = Booking.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);
        //date = new Date();
        paybtn = findViewById(R.id.paybtn);
        mainlay = findViewById(R.id.mainlay);
        frame = findViewById(R.id.frame);
        pickdate = findViewById(R.id.pickdate);


        binding.llDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String response = getIntent().getStringExtra(Consts.available_weekdays);
                Log.d("shubhcode", "onClick: old response is : " + response);

                String[] doctorAvailableCalenderArray = responseStringToStringArray(response);


                Calendar now = Calendar.getInstance();


                dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        Booking.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)

                );


                //suppose response from server
                // [monday, tuesday, saturday] = jsonArray
                // after converting to json string array = ["monday", "tuesday", "saturday"]
                // String[] availableDays = {"Monday", "tuesday", "wednesday"};


                // selected only available days
                dpd.setSelectableDays(CalenderUtil.getAvailableDatesOfCalenderArray(doctorAvailableCalenderArray));
                // highlighted available days
                dpd.setHighlightedDays(CalenderUtil.getAvailableDatesOfCalenderArray(doctorAvailableCalenderArray));
                dpd.setThemeDark(false);
                dpd.showNow(getSupportFragmentManager(), "Datepickerdialog");
                // dpd.getShowsDialog();


            }
        });


        // paramsBookingOp.put(Consts.DATE_STRING, BookingDate.toString().toUpperCase());
        //paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));

        loadingDialog = new Dialog(Booking.this);
        loadingDialog.setContentView(R.layout.lotiee_loading);
        loadingDialog.setCancelable(false);
        if (loadingDialog.getWindow() != null)
            loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        // loadingDialog.show();
//            StringUtils.substringBetween(available_days, "[", "]");
//        for (int i=0;i<=)


        Toast.makeText(Booking.this, "Doctor Available On " + getIntent().getStringExtra(Consts.available_weekdays),
                Toast.LENGTH_LONG).show();


        if (getIntent().hasExtra(Consts.ARTIST_DTO)) {
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
            artist_id = getIntent().getStringExtra(Consts.ARTIST_ID);
            screen_tag = getIntent().getIntExtra(Consts.SCREEN_TAG, 0);
            available_days = getIntent().getStringExtra(Consts.available_weekdays);


            Log.d(TAG, "IntentData: " + available_days);
            // .getSelectedItemPosition()==0)
            // System.out.println("hello"+available_days.indexOf("available_weekdays",1));


            if (screen_tag == 2) {
                servicesId = getIntent().getStringExtra(Consts.SERVICE_ARRAY);
                serviceList = (ArrayList<ProductDTO>) getIntent().getSerializableExtra(Consts.SERVICE_NAME_ARRAY);
                price = getIntent().getStringExtra(Consts.PRICE);


                Log.e(TAG, "onCreate:servicesId " + servicesId);
                Log.e(TAG, "onCreate:servicesName " + serviceList);

                try {
                    array = new JSONArray(servicesId);
                    System.out.println(array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        setUiAction();
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GenerateChecksum();
                switchtobookingProActivity();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setUiAction() {
        binding.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(mContext).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfile);
        binding.tvName.setText(artistDetailsDTO.getName());
        binding.tvWork.setText(artistDetailsDTO.getCategory_name());
        binding.tvdays.setText("Available on : " + artistDetailsDTO.getSelected_days().substring(1, available_days.length() - 1));
        String mk = (artistDetailsDTO.getSelected_days().substring(1, available_days.length() - 1));
        binding.tvdays.setText(mk);
        // binding.tvBookingDate.setText(sdf1.format(date).toString().toUpperCase());
        System.out.println(available_days);
        System.out.println("hello is it fine" + str.substring(1, str.length() - 1));


        String[] str = mk.split(",");
        for (int i = 0; i < str.length; i++) {
            Log.d(TAG, "value i" + i + str[i]);
            if (i == 0) {
                Day1 = str[i];
                Log.d(TAG, "valueofi" + Day1);
                if (Day1.equals("Monday")) {
                    d1 = Calendar.MONDAY;
                } else if (Day1.equals("Tuesday")) {
                    d1 = Calendar.TUESDAY;
                } else if (Day1.equals("Wednesday")) {
                    d1 = 4;
                } else if (Day1.equals("Thursday")) {
                    d1 = 5;
                } else if (Day1.equals("Friday")) {
                    d1 = 6;
                }
            } else if (i == 1) {
                Day2 = str[i];
                Log.d(TAG, "valueofi" + i + Day2);
                if (Day2.equals("Monday")) {
                    d2 = 2;
                } else if (Day2.equals("Tuesday")) {
                    d2 = 3;
                } else if (Day2.equals("Wednesday")) {
                    d2 = 4;
                } else if (Day2.equals("Thursday")) {
                    d2 = 5;
                } else if (Day2.equals("Friday")) {
                    d2 = 6;
                }


            }

            //str[i]=str[i].trim();

            //String val = String.valueOf(i);
            //System.out.println("daysdata4"+val);
            System.out.println("daysdata" + str[i]);
            // String day+""+str[i]=
            // String "day" + val =  str[i];
            String val = new StringBuilder().append("day").append(i).toString();
            // val=str[i];


            val = str[i];
            // day =str[i];
            //   available_days=val;

        }
        System.out.println("Selected " + Day1 + Day2 + Day3);
        System.out.println("val" + available_days);
        Log.d(TAG, "integer value" + d1 + d2);
//        for (int k = 0; k < 2; k++) {
//            System.out.println("daysdata"+str[k]);
//        }
        System.out.println("kingsn" + available_days);
        stringList.add(available_days);
        Log.d(TAG, "stringlist" + stringList);

        if (screen_tag == 1) {
            if (artistDetailsDTO.getArtist_commission_type().equalsIgnoreCase("0")) {
                binding.tvPrice.setText(artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice() + getResources().getString(R.string.hr_add_on));

            } else {
                binding.tvPrice.setText(artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice() + " " + getResources().getString(R.string.fixed_rate_add_on));

            }
        } else if (screen_tag == 2) {
            binding.tvPrice.setText(artistDetailsDTO.getCurrency_type() + price);
        }
        if (!userDTO.getOffice_address().equalsIgnoreCase("")) {
            binding.tvAddress.setText(userDTO.getOffice_address());
        }else{

        }

        if (!userDTO.getOffice_address().equalsIgnoreCase("")) {
           // paramsBookingOp.put(Consts.ADDRESS, userDTO.getOffice_address());
            paramsBookingOp.put(Consts.ADDRESS, userDTO.getOffice_address());
            paramsBookingOp.put(Consts.LATITUDE, userDTO.getLive_lat());
            paramsBookingOp.put(Consts.LONGITUDE, userDTO.getLive_long());
        }
        binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {
                    findPlace();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }
            }
        });


        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickScheduleDateTime();


                Calendar now = Calendar.getInstance();

                dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        Booking.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );


                Calendar sunday;
                Calendar tuesday;
                Calendar wednesday;
                List<Calendar> weekends = new ArrayList<>();
                int weeks = 4;

                for (int i = 0; i < (weeks * 7); i = i + 7) {
                    sunday = Calendar.getInstance();
                    tuesday = Calendar.getInstance();
                    wednesday = Calendar.getInstance();
                    sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    tuesday.add(Calendar.DAY_OF_YEAR, (Calendar.TUESDAY - tuesday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    wednesday.add(Calendar.DAY_OF_YEAR, (Calendar.WEDNESDAY - wednesday.get(Calendar.DAY_OF_WEEK) + 7 + i));
                    // saturday = Calendar.getInstance();
                    // saturday.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - saturday.get(Calendar.DAY_OF_WEEK) + i));
                    // weekends.add(saturday);
                    Log.d(TAG, "valueof1" + i);
                   /* {
                        if(i==0) {

                            if (Day1.equals("Tuesday")) {
                                weekends.add(tuesday);
                            }
                        } if (i==14) {
                            *//*Log.d(TAG, "onCl"+"kingsn");
                            if (Day2.equals("Wednesday")) {
                                Log.d(TAG, "onCl");*//*
                                weekends.add(wednesday);


                        }

                        Log.d(TAG, "Day2" + Day2);

                    }*/

                    weekends.add(tuesday);
                    weekends.add(wednesday);
                }
                Calendar[] disabledDays = weekends.toArray(new Calendar[0]);
                Log.d(TAG, "disabledDays" + Arrays.toString(disabledDays));
                // dpd.setDisabledDays(disabledDays);
                dpd.setSelectableDays(disabledDays);
                // dpd.setMinDate(calendar);
                dpd.showNow(getSupportFragmentManager(), "Datepickerdialog");
                // dpd.getShowsDialog();


            }


        });
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /////////////////////Paytm payment Begins /////////////////////////////


                /////////////////////after payment done /////////////////////////////
                if (binding.tvAddress.getText().toString().trim().length() > 0) {
                    if (screen_tag == 1) {
                        paramsBookingOp.put(Consts.PRICE, artistDetailsDTO.getPrice());
                        paramsBookingOp.put(Consts.TITLE, "Direct Booking");
                        submit();

                    } else if ((screen_tag == 2)) {
                        bookForService();

                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_address));
                }
            }
        });
    }

    public void bookForService() {
        if ((array.length() > 0)) {
            String strTitle = serviceList.toString().replace("[", "")
                    .replace("]", "").replace(", ", ",");
            paramsBookingOp.put(Consts.SERVICE_ID, array.toString());
            paramsBookingOp.put(Consts.TITLE, strTitle);
            submit();
        } else {
            ProjectUtils.showLong(mContext, mContext.getResources().getString(R.string.select_any_service));
        }
    }


    public void bookArtist() {
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());

        Log.e(TAG, "bookArtist: " + paramsBookingOp.toString());

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_ARTIST_API, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
                }


            }
        });

    }


    public void getAppointmentslot() {
        paramsBookingOp.put(Consts.USER_ID, userDTO.getUser_id());
        paramsBookingOp.put(Consts.ARTIST_ID, artistDetailsDTO.getUser_id());
        paramsBookingOp.put(bookingDate, String.valueOf(bookingDate));

        Log.e(TAG, "getAppointmentslot: " + paramsBookingOp.toString());

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOK_APPOINTMENT_SLOT, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, JSONObject darwinbarkk, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
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

            binding.tvAddress.setText(obj.getAddressLine(0));
            Log.d(TAG, "getAddress: "+binding.tvAddress.getText());

            paramsBookingOp.put(Consts.ADDRESS,  String.valueOf(binding.tvAddress.getText()));
            paramsBookingOp.put(Consts.LATITUDE, String.valueOf(lat));
            paramsBookingOp.put(Consts.LONGITUDE, String.valueOf(lng));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
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

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public void clickScheduleDateTime() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .curved()
                .mustBeOnFuture()
                .defaultDate(new Date())
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        paramsBookingOp.put(Consts.DATE_STRING, String.valueOf(sdf1.format(date).toUpperCase()));
                        bookingDate = sdf1.format(date).toUpperCase();
                        Toast.makeText(Booking.this, "You Selected Booking On: " + bookingDate, Toast.LENGTH_LONG).show();
                        paramsBookingOp.put(Consts.TIMEZONE, timeZone.format(date));
                        binding.tvBookingDate.setText(sdf1.format(date).toUpperCase());
                    }
                })
                .display();
    }


    public void submit() {
        if (!validation(binding.tvBookingDate, getResources().getString(R.string.val_date))) {
            return;
        } else if (!validation(binding.tvAddress, getResources().getString(R.string.val_address))) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                // bookArtist();
                // GenerateChecksum();
                //switchtobookingProActivity();
                switchtoslotbooking();
            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(TextView textView, String msg) {
        if (!ProjectUtils.isTextFilled(textView)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }

    private void GenerateChecksum() {
        Random r = new Random(currentTimeMillis());
        orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        String url = "https://darwinbark.com/projects/qrcoder/paytm1/generateChecksum.php";


        Map<String, String> params = new HashMap<>();
        params.put("MID", MID);
        params.put("ORDER_ID", orderId);
        params.put("CUST_ID", "7000621203");
        params.put("MOBILE_NO", "7000621203");
        params.put("EMAIL", "so@gmail.com");
        params.put("CHANNEL_ID", "WAP");
        params.put("TXN_AMOUNT", "1");
        params.put("WEBSITE", WEBSITE);
        params.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        params.put("CALLBACK_URL", CALLBACK_URL);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(Booking.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH")) {
                                checksum = jsonObject.getString("CHECKSUMHASH");
                                onStartTransaction();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Booking.this, "Error...!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("MID", MID);
                params.put("ORDER_ID", orderId);
                params.put("CUST_ID", "7000621203");
                params.put("MOBILE_NO", "7000621203");
                params.put("EMAIL", "so@gmail.com");
                params.put("CHANNEL_ID", "WAP");
                params.put("TXN_AMOUNT", "1");
                params.put("WEBSITE", WEBSITE);
                params.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
                params.put("CALLBACK_URL", CALLBACK_URL);
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
        paramMap.put("MID", MID);
        // Key in your staging and production MID available in your dashboard

        paramMap.put("ORDER_ID", orderId);
        paramMap.put("CUST_ID", "7000621203");
        paramMap.put("MOBILE_NO", "7000621203");
        paramMap.put("EMAIL", "so@gmail.com");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", "1");
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        paramMap.put("CALLBACK_URL", CALLBACK_URL);
        paramMap.put("CHECKSUMHASH", checksum);

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
                                Toast.makeText(Booking.this, " Transaction success", Toast.LENGTH_LONG).show();

                                //uploadData();
                                //login();
                                bookArtist();
                            } else if (!inResponse.getBoolean("STATUS")) {
                                //    Payment Failed
                                Toast.makeText(Booking.this, " Transaction Failed", Toast.LENGTH_LONG).show();
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

    private void switchtobookingProActivity() {
        /*Intent in = new Intent(mContext, PaymentProActivity.class);
        //in.putExtra(Consts.artistDetailsDTO, objects.get(position));
        mContext.startActivity(in);*/

        // Toast.makeText(Booking.this,"bookArtist: "+Consts.PRICE,Toast.LENGTH_LONG).show();
        //  Toast.makeText(Booking.this,"bookArtistB: "+paramsBookingOp,Toast.LENGTH_LONG).show();


        Intent viewService = new Intent(mContext, bookingProActivity.class);
        viewService.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        viewService.putExtra(Consts.ARTIST_ID, artist_id);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.paramsBookingOp, paramsBookingOp);
        viewService.putExtra(BookingDate, String.valueOf(bookingDate));
        //viewService.putExtra(String.valueOf(paramsBookingOp), paramsBookingOp);
        // Toast.makeText(Booking.this,"bookArtist4me: "+viewService.getStringExtra(String.valueOf(BookingDate)),Toast.LENGTH_LONG).show();
        mContext.startActivity(viewService);
    }

    private void switchtoslotbooking() {
        /*Intent in = new Intent(mContext, PaymentProActivity.class);
        //in.putExtra(Consts.artistDetailsDTO, objects.get(position));
        mContext.startActivity(in);*/

        // Toast.makeText(Booking.this,"bookArtist: "+Consts.PRICE,Toast.LENGTH_LONG).show();
        //  Toast.makeText(Booking.this,"bookArtistB: "+paramsBookingOp,Toast.LENGTH_LONG).show();


        Intent viewService = new Intent(mContext, PatientSlotBooking.class);
        viewService.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
        viewService.putExtra(Consts.ARTIST_ID, artist_id);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.SCREEN_TAG, 1);
        viewService.putExtra(Consts.paramsBookingOp, paramsBookingOp);
        viewService.putExtra(BookingDate, String.valueOf(bookingDate));
        viewService.putExtra(Consts.ADDRESS, String.valueOf(binding.tvAddress.getText()));
        Log.d(TAG, "switchtoslotbooking: "+viewService.getStringExtra(Consts.ADDRESS));
        // Toast.makeText(Booking.this,"bookArtist4me: "+viewService.getStringExtra(String.valueOf(BookingDate)),Toast.LENGTH_LONG).show();
        mContext.startActivity(viewService);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date1 = "You picked the following date: " + dayOfMonth + "/" + (++monthOfYear) + "/" + year;
        String date = dayOfMonth + "-" + (monthOfYear) + "-" + year;
        bookingDate = date.toUpperCase();
        binding.tvBookingDate.setText(date1);
        bookingDate = date.toUpperCase();
    }


    public Date stringToDate(String dateString) {
        Date date = null;
        try {
            date = sdf1.parse(dateString);
            Log.d("dateToCalendar", "stringToDate: succesfully converd to date" + date.toString());
        } catch (ParseException e) {
            Log.e("dateToCalendar", "stringToDate: inside catch block,,, exception");
            e.printStackTrace();
        }
        return date;
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.e("dateToCalendar", "dateToCalendar: " + calendar.getTime());
        return calendar;


    }
    //[] json array ["djsk", "hdhhdhd"] {}
    // [demo1, demo1]


}
