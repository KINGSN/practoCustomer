package com.darwinbark.fabcustomer.interfacess;

import java.util.HashMap;

/**
 * Created by VARUN on 01/01/19.
 */

public interface Consts {
    String APP_NAME = "appointu";

    String BASE_URL = "https://darwinbark.com/projects/appointu/admin/Webservice/";
    String BASE_URL2 = "https://darwinbark.com/projects/gopunt";

    String PAYMENT_FAIL_Paypal = "https://darwinbark.com/projects/appointu/admin/corePaypal/fail.php?status=failure";
    String PAYMENT_SUCCESS_paypal = "https://darwinbark.com/projects/appointu/admin/corePaypal/payment-status.php?id=";
    String MAKE_PAYMENT_paypal = "https://offerwalk.com/projects/fabdoctor/corePaypal/wallet_checkout.php?";

    String INVOICE__PAYMENT_paypal = "https://darwinbark.com/projects/appointu/admin/fabdoctor/corePaypal/checkout.php?";

    String PAYMENT_FAIL = "https://darwinbark.com/projects/appointu/admin/Stripe/Payment/fail";
    String PAYMENT_SUCCESS = "https://darwinbark.com/projects/appointu/admin/Stripe/Payment/success";
    String MAKE_PAYMENT = "https://darwinbark.com/projects/appointu/admin/Stripe/Payment/make_payment/";

    String INVOICE_PAYMENT_FAIL_Stripe = "https://darwinbark.com/projects/appointu/admin/Stripe/BookingPayement/fail";
    String INVOICE_PAYMENT_SUCCESS_Stripe = "https://darwinbark.com/projects/appointu/admin/fabdoctor/Stripe/BookingPayement/success";
    String INVOICE_PAYMENT_Stripe = "https://darwinbark.com/projects/appointu/admin/Stripe/BookingPayement/make_payment/";

    String PRIVACY_URL = "privacyPolicy";
    String FAQ_URL = "faq";
    String TERMS_URL = "termsCondition";


    /*Api Details*/
    String GET_ALL_ARTISTS_API = "getAllArtists";
    String GET_ARTIST_BY_ID_API = "getArtistByid";
    String GET_NOTIFICATION_API = "getNotifications";
    String GET_INVOICE_API = "getMyInvoice";
    String GET_REFERRAL_CODE_API = "getMyReferralCode";
    String GET_CHAT_HISTORY_API = "getChatHistoryForUser";
    String GET_CHAT_API = "getChat";
    String SEND_CHAT_API = "sendmsg";
    String LOGIN_API = "signIn";
    String REGISTER_API = "SignUp";
    String UPDATE_PROFILE_API = "editPersonalInfo";
    String CURRENT_BOOKING_API = "getMyCurrentBookingUser";
    String BOOK_ARTIST_API = "book_artist";
    String BOOK_APPOINTMENT_SLOT = "booking_slots";
    String BOOK_APPOINTMENT_API = "book_appointment";
    String DECLINE_BOOKING_API = "decline_booking";
    String UPDATE_LOCATION_API = "updateLocation";
    String MAKE_PAYMENT_API = "makePayment";
    String CHECK_COUPON_API = "checkCoupon";
    String GET_MY_TICKET_API = "getMyTicket";
    String GENERATE_TICKET_API = "generateTicket";
    String GET_TICKET_COMMENTS_API = "getTicketComments";
    String ADD_TICKET_COMMENTS_API = "addTicketComments";
    String FORGET_PASSWORD_API = "forgotPassword";
    String FORGET_PASSWORD_BY_MOBILE_API = "forgotPasswordbymobile";
    String UPDATE_PASSWORD_API = "updatePassword";
    String GET_APPOINTMENT_API = "getAppointment";
    String EDIT_APPOINTMENT_API = "edit_appointment";
    String APPOINTMENT_OPERATION_API = "appointment_operation";
    String GET_ALL_CATEGORY_API = "getAllCaegory";
    String GET_ALL_JOB_USER_API = "get_all_job_user";
    String POST_JOB_API = "post_job_new";
    String GET_APPLIED_JOB_BY_ID_API = "get_applied_job_by_id";
    String JOB_STATUS_USER_API = "job_status_user";
    String EDIT_POST_JOB_API = "edit_post_job";
    String DELETE_JOB_API = "deletejob";
    String ADD_FAVORITES_API = "add_favorites";
    String REMOVE_FAVORITES_API = "remove_favorites";
    String GET_LOCATION_ARTIST_API = "getLocationArtist";
    String ADD_RATING_API = "addRating";
    String BOOKING_OPERATION_API = "booking_operation";
    String JOB_COMPLETE_API = "jobComplete";
    String DELETE_PROFILE_IMAGE_API = "deleteProfileImage";
    String ADD_MONEY_API = "addMoney";
    String GET_WALLET_HISTORY_API = "getWalletHistory";
    String GET_USER_WALLET_API = "getUserWallet";
    String GET_WALLET_API = "getWallet";
    String CUSTOMER_HOME_DATA = "customerHomeData";
    String AdminHomeData="/api_v1/api.php?adminSettings";
    String GET_CURRENCY_API = "getCurrency";
    String GET_ALL_ARTIST_FILTER = "getAllArtistsFilter";

    /*app data*/
    static String INTROAPP = "INTROAPP";
    String CAMERA_ACCEPTED = "camera_accepted";
    String STORAGE_ACCEPTED = "storage_accepted";
    String MODIFY_AUDIO_ACCEPTED = "modify_audio_accepted";
    String CALL_PRIVILAGE = "call_privilage";
    String FINE_LOC = "fine_loc";
    String CORAS_LOC = "coras_loc";
    String CALL_PHONE = "call_phone";
    String PAYMENT_URL = "payment_url";
    String SURL = "surl";
    String FURL = "furl";
    String SCREEN_TAG = "screen_tag";
    String SERVICE_ARRAY = "service_array";
    String SERVICE_NAME_ARRAY = "service_name_array";
    String DTO = "dto";
    String POSTION = "postion";
    String SURL_BOOKING = "surl_booking";
    String FURL_BOOKING = "furl_booking";
    static final String mBroadcastShowAdd = "FabCustomer.showAdd";
    /*app data*/

    /*Project Parameter*/
    String ARTIST_ID = "artist_id";
    String CHAT_LIST_DTO = "chat_list_dto";
    String USER_DTO = "user_dto";
    String USER1DTO = "user_1dto";
    String POST_JOB_DTO = "post_job_dto";
    String IS_REGISTERED = "is_registered";
    String IMAGE_URI_CAMERA = "image_uri_camera";
    String DATE_FORMATE_SERVER = "EEE, MMM dd, yyyy hh:mm a"; //Wed, JUL 06, 2018 04:30 pm
    String DATE_FORMATE_TIMEZONE = "z";
    String HISTORY_DTO = "history_dto";
    String BROADCAST = "broadcast";
    String ARTIST_DTO = "artist_dto";
    String FLAG = "flag";

    /*Parameter Get Artist and Search*/
    String USER_ID = "user_id";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
    String CATEGORY_ID = "category_id";

    /*Get All History*/
    String ROLE = "role";

    String paramsBookingOp = "paramsBookingOp";
    String BookingDate = "bookingdate";
    String BookingSlot = "bookingslot";

    /*Send Message*/
    String MESSAGE = "message";
    String SEND_BY = "send_by";
    String SENDER_NAME = "sender_name";


    /*Login Parameter*/
    String NAME = "name";
    String EMAIL_ID = "email_id";
    String PASSWORD = "password";
    String DEVICE_TYPE = "device_type";
    String DEVICE_TOKEN = "device_token";
    String DEVICE_ID = "device_id";
    String REFERRAL_CODE = "referral_code";


    /*Update Profile*/
    String NEW_PASSWORD = "new_password";
    String GENDER = "gender";
    String MOBILE = "mobile";
    String OFFICE_ADDRESS = "office_address";
    String ADDRESS = "address";
    String IMAGE = "image";
    String CITY = "city";
    String COUNTRY = "country";

    /*Book Artist*/

    String DATE_STRING = "date_string";
    String TIMEZONE = "timezone";
    String PRICE = "price";
    String PLACE = "place";
    String ESTIMATE_TIME = "estimate_time";
    String SERVICE_ID = "service_id";
    /*Decline*/
    String BOOKING_ID = "booking_id";
    String DECLINE_BY = "decline_by";
    String DECLINE_REASON = "decline_reason";

    /*Make Payment*/
    String INVOICE_ID = "invoice_id";
    // String USER_ID = "user_id";
    String COUPON_CODE = "coupon_code";
    String FINAL_AMOUNT = "final_amount";
    String PAYMENT_STATUS = "payment_status";


    /*Chat intent*/
    String ARTIST_NAME = "artist_name";

    /*Add Ticket*/
    String REASON = "reason";


    /*Get Ticket*/
    String TICKET_ID = "ticket_id";
    String COMMENT = "comment";


    /*Edit Appointment*/
    String APPOINTMENT_ID = "appointment_id";

    /*Decline Appointment*/
    String REQUEST = "request";


    /*Post Job*/
    String AVTAR = "avtar";
    String TITLE = "title";
    String DESCRIPTION = "description";
    String LATI = "lati";
    String LONGI = "longi";
    String JOB_DATE = "job_date";

    /*Get Applied Job*/
    String JOB_ID = "job_id";
    String aj_id = "aj_id";

    /*Job Status*/
    String AJ_ID = "aj_id";
    String STATUS = "status";

    // Google Console APIs developer key
    // Replace this key with your's
    static final String DEVELOPER_KEY = "AIzaSyBlLIsCaCw8ylCTPR0XhaKp-vkeD4S-5_0";


    /*Payment*/
    String PAYMENT_TYPE = "payment_type";
    String DISCOUNT_AMOUNT = "discount_amount";
    /*Chat*/
    String CHAT_TYPE = "chat_type";

    /*Paypal Client Id*/
    /*Add Review*/
    String RATING = "rating";


    /*Add Money*/
    String TXN_ID = "txn_id";
    String ORDER_ID = "order_id";
    String AMOUNT = "amount";
    String CURRENCY = "currency";

    /*Home*/
    String DISTANCE ="distance";

    String CURRENCY_ID = "currency";

    /*Notifications Codes*/
    String BOOK_ARTIST_NOTIFICATION = "10001";//ar
    String DECLINE_BOOKING_ARTIST_NOTIFICATION = "10002";//both
    String START_BOOKING_ARTIST_NOTIFICATION = "10003";
    String END_BOOKING_ARTIST_NOTIFICATION = "10004";//user
    String CANCEL_BOOKING_ARTIST_NOTIFICATION = "10005";
    String ACCEPT_BOOKING_ARTIST_NOTIFICATION = "10006";//user
    String CHAT_NOTIFICATION = "10007";//both
    String USER_BLOCK_NOTIFICATION = "1008";
    String TICKET_COMMENT_NOTIFICATION = "10009";//both
    String WALLET_NOTIFICATION = "10010";//both
    String JOB_NOTIFICATION = "10011";//ar
    String JOB_APPLY_NOTIFICATION = "10012";//user
    String DELETE_JOB_NOTIFICATION = "10013";//ar
    String BRODCAST_NOTIFICATION = "10014";//both
    String TICKET_STATUS_NOTIFICATION = "10015";//both
    String ADMIN_NOTIFICATION = "10016";
    String TYPE = "type";
    String TOPIC_CUSTOMER = "Customer";

    String LANGUAGE_SELECTION = "language_selection";
    String VOICE_PREFERENCE = "voice_preference";
    String VOICE_PREFERENCE_ENGLISH = "en";
    String VOICE_PREFERENCE_ARABIC = "ar";
    String LANGUAGE = "language";

    String ENGLISH_TAG = "en";
    String ARABIC_TAG = "ar";

    //webView
    String URL = "url";
    String HEADER = "header";

    String VALUE ="value";
    String Booking_time ="Booking_time";
    String available_weekdays ="available_weekdays";
}

