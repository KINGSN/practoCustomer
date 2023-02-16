package com.darwinbark.fabcustomer.dto;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User1DTO implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("admin_user_id")
    @Expose
    private String adminUserId;
    @SerializedName("admin_refer_code")
    @Expose
    private String adminReferCode;
    @SerializedName("admin_password")
    @Expose
    private String adminPassword;
    @SerializedName("admin_name")
    @Expose
    private String adminName;
    @SerializedName("admin_email")
    @Expose
    private String adminEmail;
    @SerializedName("admin_mobile")
    @Expose
    private String adminMobile;
    @SerializedName("profileImg")
    @Expose
    private String profileImg;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("admin_status")
    @Expose
    private String adminStatus;
    @SerializedName("manageUserList")
    @Expose
    private String manageUserList;
    @SerializedName("manageDepositList")
    @Expose
    private String manageDepositList;
    @SerializedName("manageCreateIDList")
    @Expose
    private String manageCreateIDList;
    @SerializedName("manageWithdrawal")
    @Expose
    private String manageWithdrawal;
    @SerializedName("managePassword")
    @Expose
    private String managePassword;
    @SerializedName("manageCloseID")
    @Expose
    private String manageCloseID;
    @SerializedName("manageNotification")
    @Expose
    private String manageNotification;
    @SerializedName("manageAppSettings")
    @Expose
    private String manageAppSettings;
    @SerializedName("manageAdminList")
    @Expose
    private String manageAdminList;
    @SerializedName("admin_login_date")
    @Expose
    private String adminLoginDate;
    @SerializedName("admin_joining_date")
    @Expose
    private String adminJoiningDate;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_logo")
    @Expose
    private String appLogo;
    @SerializedName("app_description")
    @Expose
    private String appDescription;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("app_author")
    @Expose
    private String appAuthor;
    @SerializedName("app_contact")
    @Expose
    private String appContact;
    @SerializedName("app_email")
    @Expose
    private String appEmail;
    @SerializedName("app_website")
    @Expose
    private String appWebsite;
    @SerializedName("app_developed_by")
    @Expose
    private String appDevelopedBy;
    @SerializedName("redeem_currency")
    @Expose
    private String redeemCurrency;
    @SerializedName("bannerimg1_enabled")
    @Expose
    private String bannerimg1Enabled;
    @SerializedName("home_bannerimg1")
    @Expose
    private String homeBannerimg1;
    @SerializedName("home_bannerimg2")
    @Expose
    private String homeBannerimg2;
    @SerializedName("home_bannerimg2_enabled")
    @Expose
    private String homeBannerimg2Enabled;
    @SerializedName("home_bannerimg3")
    @Expose
    private String homeBannerimg3;
    @SerializedName("firebase_keys")
    @Expose
    private String firebaseKeys;
    @SerializedName("onesignalapp_id")
    @Expose
    private String onesignalappId;
    @SerializedName("onesignalapp_key")
    @Expose
    private String onesignalappKey;
    @SerializedName("refer_txt")
    @Expose
    private String referTxt;
    @SerializedName("adminUpiName")
    @Expose
    private String adminUpiName;
    @SerializedName("upiMobileNo")
    @Expose
    private String upiMobileNo;
    @SerializedName("adminUpi")
    @Expose
    private String adminUpi;
    @SerializedName("adminPaytmName")
    @Expose
    private String adminPaytmName;
    @SerializedName("adminPaytmNo")
    @Expose
    private String adminPaytmNo;
    @SerializedName("adminGpayName")
    @Expose
    private String adminGpayName;
    @SerializedName("adminGpaymobileNo")
    @Expose
    private String adminGpaymobileNo;
    @SerializedName("adminGpay")
    @Expose
    private String adminGpay;
    @SerializedName("adminAccountName")
    @Expose
    private String adminAccountName;
    @SerializedName("adminAccountNo")
    @Expose
    private String adminAccountNo;
    @SerializedName("adminAccountIfsc")
    @Expose
    private String adminAccountIfsc;
    @SerializedName("adminAccountType")
    @Expose
    private String adminAccountType;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("joining_bonus")
    @Expose
    private String joiningBonus;
    @SerializedName("per_refer")
    @Expose
    private String perRefer;
    @SerializedName("minDepositcoin")
    @Expose
    private String minDepositcoin;
    @SerializedName("dailytask_coin")
    @Expose
    private String dailytaskCoin;
    @SerializedName("hourly_quiz_coin")
    @Expose
    private String hourlyQuizCoin;
    @SerializedName("maths_quiz_coin")
    @Expose
    private String mathsQuizCoin;
    @SerializedName("maxm_maths_questn")
    @Expose
    private String maxmMathsQuestn;
    @SerializedName("hourly_spin_limit")
    @Expose
    private String hourlySpinLimit;
    @SerializedName("hourly_mathsquiz_limit")
    @Expose
    private String hourlyMathsquizLimit;
    @SerializedName("mathsQuiz_unlockMin")
    @Expose
    private String mathsQuizUnlockMin;
    @SerializedName("per_news_coin")
    @Expose
    private String perNewsCoin;
    @SerializedName("minimum_widthrawal")
    @Expose
    private String minimumWidthrawal;
    @SerializedName("min_redeem_amount")
    @Expose
    private String minRedeemAmount;
    @SerializedName("telegramlink")
    @Expose
    private String telegramlink;
    @SerializedName("youtube_link")
    @Expose
    private String youtubeLink;
    @SerializedName("facebook_page")
    @Expose
    private String facebookPage;
    @SerializedName("new_version")
    @Expose
    private String newVersion;
    @SerializedName("update_link")
    @Expose
    private String updateLink;
    @SerializedName("admin_msg")
    @Expose
    private String adminMsg;
    @SerializedName("join_group")
    @Expose
    private String joinGroup;
    @SerializedName("app_promo1")
    @Expose
    private String appPromo1;
    @SerializedName("app_promo2")
    @Expose
    private String appPromo2;
    @SerializedName("payment_gateway")
    @Expose
    private String paymentGateway;
    @SerializedName("offline_paymentGateway")
    @Expose
    private String offlinePaymentGateway;
    @SerializedName("paytm_mid")
    @Expose
    private String paytmMid;
    @SerializedName("paytm_key")
    @Expose
    private String paytmKey;
    @SerializedName("razorpay_mid")
    @Expose
    private String razorpayMid;
    @SerializedName("razorpay_key")
    @Expose
    private String razorpayKey;
    @SerializedName("payumoney_mid")
    @Expose
    private String payumoneyMid;
    @SerializedName("payumoney_key")
    @Expose
    private String payumoneyKey;
    @SerializedName("payumoney_salt")
    @Expose
    private String payumoneySalt;
    @SerializedName("date")
    @Expose
    private String date;

    public User1DTO(String id, String adminUserId, String adminReferCode, String adminPassword, String adminName, String adminEmail, String adminMobile, String profileImg, String deviceToken, String adminStatus, String manageUserList, String manageDepositList, String manageCreateIDList, String manageWithdrawal, String managePassword, String manageCloseID, String manageNotification, String manageAppSettings, String manageAdminList, String adminLoginDate, String adminJoiningDate, String appName, String appLogo, String appDescription, String appVersion, String appAuthor, String appContact, String appEmail, String appWebsite, String appDevelopedBy, String redeemCurrency, String bannerimg1Enabled, String homeBannerimg1, String homeBannerimg2, String homeBannerimg2Enabled, String homeBannerimg3, String firebaseKeys, String onesignalappId, String onesignalappKey, String referTxt, String adminUpiName, String upiMobileNo, String adminUpi, String adminPaytmName, String adminPaytmNo, String adminGpayName, String adminGpaymobileNo, String adminGpay, String adminAccountName, String adminAccountNo, String adminAccountIfsc, String adminAccountType, String adminId, String password, String image, String joiningBonus, String perRefer, String minDepositcoin, String dailytaskCoin, String hourlyQuizCoin, String mathsQuizCoin, String maxmMathsQuestn, String hourlySpinLimit, String hourlyMathsquizLimit, String mathsQuizUnlockMin, String perNewsCoin, String minimumWidthrawal, String minRedeemAmount, String telegramlink, String youtubeLink, String facebookPage, String newVersion, String updateLink, String adminMsg, String joinGroup, String appPromo1, String appPromo2, String paymentGateway, String offlinePaymentGateway, String paytmMid, String paytmKey, String razorpayMid, String razorpayKey, String payumoneyMid, String payumoneyKey, String payumoneySalt, String date) {

        this.id = id;
        this.adminUserId = adminUserId;
        this.adminReferCode = adminReferCode;
        this.adminPassword = adminPassword;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminMobile = adminMobile;
        this.profileImg = profileImg;
        this.deviceToken = deviceToken;
        this.adminStatus = adminStatus;
        this.manageUserList = manageUserList;
        this.manageDepositList = manageDepositList;
        this.manageCreateIDList = manageCreateIDList;
        this.manageWithdrawal = manageWithdrawal;
        this.managePassword = managePassword;
        this.manageCloseID = manageCloseID;
        this.manageNotification = manageNotification;
        this.manageAppSettings = manageAppSettings;
        this.manageAdminList = manageAdminList;
        this.adminLoginDate = adminLoginDate;
        this.adminJoiningDate = adminJoiningDate;
        this.appName = appName;
        this.appLogo = appLogo;
        this.appDescription = appDescription;
        this.appVersion = appVersion;
        this.appAuthor = appAuthor;
        this.appContact = appContact;
        this.appEmail = appEmail;
        this.appWebsite = appWebsite;
        this.appDevelopedBy = appDevelopedBy;
        this.redeemCurrency = redeemCurrency;
        this.bannerimg1Enabled = bannerimg1Enabled;
        this.homeBannerimg1 = homeBannerimg1;
        this.homeBannerimg2 = homeBannerimg2;
        this.homeBannerimg2Enabled = homeBannerimg2Enabled;
        this.homeBannerimg3 = homeBannerimg3;
        this.firebaseKeys = firebaseKeys;
        this.onesignalappId = onesignalappId;
        this.onesignalappKey = onesignalappKey;
        this.referTxt = referTxt;
        this.adminUpiName = adminUpiName;
        this.upiMobileNo = upiMobileNo;
        this.adminUpi = adminUpi;
        this.adminPaytmName = adminPaytmName;
        this.adminPaytmNo = adminPaytmNo;
        this.adminGpayName = adminGpayName;
        this.adminGpaymobileNo = adminGpaymobileNo;
        this.adminGpay = adminGpay;
        this.adminAccountName = adminAccountName;
        this.adminAccountNo = adminAccountNo;
        this.adminAccountIfsc = adminAccountIfsc;
        this.adminAccountType = adminAccountType;
        this.adminId = adminId;
        this.password = password;
        this.image = image;
        this.joiningBonus = joiningBonus;
        this.perRefer = perRefer;
        this.minDepositcoin = minDepositcoin;
        this.dailytaskCoin = dailytaskCoin;
        this.hourlyQuizCoin = hourlyQuizCoin;
        this.mathsQuizCoin = mathsQuizCoin;
        this.maxmMathsQuestn = maxmMathsQuestn;
        this.hourlySpinLimit = hourlySpinLimit;
        this.hourlyMathsquizLimit = hourlyMathsquizLimit;
        this.mathsQuizUnlockMin = mathsQuizUnlockMin;
        this.perNewsCoin = perNewsCoin;
        this.minimumWidthrawal = minimumWidthrawal;
        this.minRedeemAmount = minRedeemAmount;
        this.telegramlink = telegramlink;
        this.youtubeLink = youtubeLink;
        this.facebookPage = facebookPage;
        this.newVersion = newVersion;
        this.updateLink = updateLink;
        this.adminMsg = adminMsg;
        this.joinGroup = joinGroup;
        this.appPromo1 = appPromo1;
        this.appPromo2 = appPromo2;
        this.paymentGateway = paymentGateway;
        this.offlinePaymentGateway = offlinePaymentGateway;
        this.paytmMid = paytmMid;
        this.paytmKey = paytmKey;
        this.razorpayMid = razorpayMid;
        this.razorpayKey = razorpayKey;
        this.payumoneyMid = payumoneyMid;
        this.payumoneyKey = payumoneyKey;
        this.payumoneySalt = payumoneySalt;
        this.date = date;
    }

    public User1DTO() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminReferCode() {
        return adminReferCode;
    }

    public void setAdminReferCode(String adminReferCode) {
        this.adminReferCode = adminReferCode;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getManageUserList() {
        return manageUserList;
    }

    public void setManageUserList(String manageUserList) {
        this.manageUserList = manageUserList;
    }

    public String getManageDepositList() {
        return manageDepositList;
    }

    public void setManageDepositList(String manageDepositList) {
        this.manageDepositList = manageDepositList;
    }

    public String getManageCreateIDList() {
        return manageCreateIDList;
    }

    public void setManageCreateIDList(String manageCreateIDList) {
        this.manageCreateIDList = manageCreateIDList;
    }

    public String getManageWithdrawal() {
        return manageWithdrawal;
    }

    public void setManageWithdrawal(String manageWithdrawal) {
        this.manageWithdrawal = manageWithdrawal;
    }

    public String getManagePassword() {
        return managePassword;
    }

    public void setManagePassword(String managePassword) {
        this.managePassword = managePassword;
    }

    public String getManageCloseID() {
        return manageCloseID;
    }

    public void setManageCloseID(String manageCloseID) {
        this.manageCloseID = manageCloseID;
    }

    public String getManageNotification() {
        return manageNotification;
    }

    public void setManageNotification(String manageNotification) {
        this.manageNotification = manageNotification;
    }

    public String getManageAppSettings() {
        return manageAppSettings;
    }

    public void setManageAppSettings(String manageAppSettings) {
        this.manageAppSettings = manageAppSettings;
    }

    public String getManageAdminList() {
        return manageAdminList;
    }

    public void setManageAdminList(String manageAdminList) {
        this.manageAdminList = manageAdminList;
    }

    public String getAdminLoginDate() {
        return adminLoginDate;
    }

    public void setAdminLoginDate(String adminLoginDate) {
        this.adminLoginDate = adminLoginDate;
    }

    public String getAdminJoiningDate() {
        return adminJoiningDate;
    }

    public void setAdminJoiningDate(String adminJoiningDate) {
        this.adminJoiningDate = adminJoiningDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppAuthor() {
        return appAuthor;
    }

    public void setAppAuthor(String appAuthor) {
        this.appAuthor = appAuthor;
    }

    public String getAppContact() {
        return appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact = appContact;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getAppWebsite() {
        return appWebsite;
    }

    public void setAppWebsite(String appWebsite) {
        this.appWebsite = appWebsite;
    }

    public String getAppDevelopedBy() {
        return appDevelopedBy;
    }

    public void setAppDevelopedBy(String appDevelopedBy) {
        this.appDevelopedBy = appDevelopedBy;
    }

    public String getRedeemCurrency() {
        return redeemCurrency;
    }

    public void setRedeemCurrency(String redeemCurrency) {
        this.redeemCurrency = redeemCurrency;
    }

    public String getBannerimg1Enabled() {
        return bannerimg1Enabled;
    }

    public void setBannerimg1Enabled(String bannerimg1Enabled) {
        this.bannerimg1Enabled = bannerimg1Enabled;
    }

    public String getHomeBannerimg1() {
        return homeBannerimg1;
    }

    public void setHomeBannerimg1(String homeBannerimg1) {
        this.homeBannerimg1 = homeBannerimg1;
    }

    public String getHomeBannerimg2() {
        return homeBannerimg2;
    }

    public void setHomeBannerimg2(String homeBannerimg2) {
        this.homeBannerimg2 = homeBannerimg2;
    }

    public String getHomeBannerimg2Enabled() {
        return homeBannerimg2Enabled;
    }

    public void setHomeBannerimg2Enabled(String homeBannerimg2Enabled) {
        this.homeBannerimg2Enabled = homeBannerimg2Enabled;
    }

    public String getHomeBannerimg3() {
        return homeBannerimg3;
    }

    public void setHomeBannerimg3(String homeBannerimg3) {
        this.homeBannerimg3 = homeBannerimg3;
    }

    public String getFirebaseKeys() {
        return firebaseKeys;
    }

    public void setFirebaseKeys(String firebaseKeys) {
        this.firebaseKeys = firebaseKeys;
    }

    public String getOnesignalappId() {
        return onesignalappId;
    }

    public void setOnesignalappId(String onesignalappId) {
        this.onesignalappId = onesignalappId;
    }

    public String getOnesignalappKey() {
        return onesignalappKey;
    }

    public void setOnesignalappKey(String onesignalappKey) {
        this.onesignalappKey = onesignalappKey;
    }

    public String getReferTxt() {
        return referTxt;
    }

    public void setReferTxt(String referTxt) {
        this.referTxt = referTxt;
    }

    public String getAdminUpiName() {
        return adminUpiName;
    }

    public void setAdminUpiName(String adminUpiName) {
        this.adminUpiName = adminUpiName;
    }

    public String getUpiMobileNo() {
        return upiMobileNo;
    }

    public void setUpiMobileNo(String upiMobileNo) {
        this.upiMobileNo = upiMobileNo;
    }

    public String getAdminUpi() {
        return adminUpi;
    }

    public void setAdminUpi(String adminUpi) {
        this.adminUpi = adminUpi;
    }

    public String getAdminPaytmName() {
        return adminPaytmName;
    }

    public void setAdminPaytmName(String adminPaytmName) {
        this.adminPaytmName = adminPaytmName;
    }

    public String getAdminPaytmNo() {
        return adminPaytmNo;
    }

    public void setAdminPaytmNo(String adminPaytmNo) {
        this.adminPaytmNo = adminPaytmNo;
    }

    public String getAdminGpayName() {
        return adminGpayName;
    }

    public void setAdminGpayName(String adminGpayName) {
        this.adminGpayName = adminGpayName;
    }

    public String getAdminGpaymobileNo() {
        return adminGpaymobileNo;
    }

    public void setAdminGpaymobileNo(String adminGpaymobileNo) {
        this.adminGpaymobileNo = adminGpaymobileNo;
    }

    public String getAdminGpay() {
        return adminGpay;
    }

    public void setAdminGpay(String adminGpay) {
        this.adminGpay = adminGpay;
    }

    public String getAdminAccountName() {
        return adminAccountName;
    }

    public void setAdminAccountName(String adminAccountName) {
        this.adminAccountName = adminAccountName;
    }

    public String getAdminAccountNo() {
        return adminAccountNo;
    }

    public void setAdminAccountNo(String adminAccountNo) {
        this.adminAccountNo = adminAccountNo;
    }

    public String getAdminAccountIfsc() {
        return adminAccountIfsc;
    }

    public void setAdminAccountIfsc(String adminAccountIfsc) {
        this.adminAccountIfsc = adminAccountIfsc;
    }

    public String getAdminAccountType() {
        return adminAccountType;
    }

    public void setAdminAccountType(String adminAccountType) {
        this.adminAccountType = adminAccountType;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJoiningBonus() {
        return joiningBonus;
    }

    public void setJoiningBonus(String joiningBonus) {
        this.joiningBonus = joiningBonus;
    }

    public String getPerRefer() {
        return perRefer;
    }

    public void setPerRefer(String perRefer) {
        this.perRefer = perRefer;
    }

    public String getMinDepositcoin() {
        return minDepositcoin;
    }

    public void setMinDepositcoin(String minDepositcoin) {
        this.minDepositcoin = minDepositcoin;
    }

    public String getDailytaskCoin() {
        return dailytaskCoin;
    }

    public void setDailytaskCoin(String dailytaskCoin) {
        this.dailytaskCoin = dailytaskCoin;
    }

    public String getHourlyQuizCoin() {
        return hourlyQuizCoin;
    }

    public void setHourlyQuizCoin(String hourlyQuizCoin) {
        this.hourlyQuizCoin = hourlyQuizCoin;
    }

    public String getMathsQuizCoin() {
        return mathsQuizCoin;
    }

    public void setMathsQuizCoin(String mathsQuizCoin) {
        this.mathsQuizCoin = mathsQuizCoin;
    }

    public String getMaxmMathsQuestn() {
        return maxmMathsQuestn;
    }

    public void setMaxmMathsQuestn(String maxmMathsQuestn) {
        this.maxmMathsQuestn = maxmMathsQuestn;
    }

    public String getHourlySpinLimit() {
        return hourlySpinLimit;
    }

    public void setHourlySpinLimit(String hourlySpinLimit) {
        this.hourlySpinLimit = hourlySpinLimit;
    }

    public String getHourlyMathsquizLimit() {
        return hourlyMathsquizLimit;
    }

    public void setHourlyMathsquizLimit(String hourlyMathsquizLimit) {
        this.hourlyMathsquizLimit = hourlyMathsquizLimit;
    }

    public String getMathsQuizUnlockMin() {
        return mathsQuizUnlockMin;
    }

    public void setMathsQuizUnlockMin(String mathsQuizUnlockMin) {
        this.mathsQuizUnlockMin = mathsQuizUnlockMin;
    }

    public String getPerNewsCoin() {
        return perNewsCoin;
    }

    public void setPerNewsCoin(String perNewsCoin) {
        this.perNewsCoin = perNewsCoin;
    }

    public String getMinimumWidthrawal() {
        return minimumWidthrawal;
    }

    public void setMinimumWidthrawal(String minimumWidthrawal) {
        this.minimumWidthrawal = minimumWidthrawal;
    }

    public String getMinRedeemAmount() {
        return minRedeemAmount;
    }

    public void setMinRedeemAmount(String minRedeemAmount) {
        this.minRedeemAmount = minRedeemAmount;
    }

    public String getTelegramlink() {
        return telegramlink;
    }

    public void setTelegramlink(String telegramlink) {
        this.telegramlink = telegramlink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getFacebookPage() {
        return facebookPage;
    }

    public void setFacebookPage(String facebookPage) {
        this.facebookPage = facebookPage;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }

    public String getAdminMsg() {
        return adminMsg;
    }

    public void setAdminMsg(String adminMsg) {
        this.adminMsg = adminMsg;
    }

    public String getJoinGroup() {
        return joinGroup;
    }

    public void setJoinGroup(String joinGroup) {
        this.joinGroup = joinGroup;
    }

    public String getAppPromo1() {
        return appPromo1;
    }

    public void setAppPromo1(String appPromo1) {
        this.appPromo1 = appPromo1;
    }

    public String getAppPromo2() {
        return appPromo2;
    }

    public void setAppPromo2(String appPromo2) {
        this.appPromo2 = appPromo2;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getOfflinePaymentGateway() {
        return offlinePaymentGateway;
    }

    public void setOfflinePaymentGateway(String offlinePaymentGateway) {
        this.offlinePaymentGateway = offlinePaymentGateway;
    }

    public String getPaytmMid() {
        return paytmMid;
    }

    public void setPaytmMid(String paytmMid) {
        this.paytmMid = paytmMid;
    }

    public String getPaytmKey() {
        return paytmKey;
    }

    public void setPaytmKey(String paytmKey) {
        this.paytmKey = paytmKey;
    }

    public String getRazorpayMid() {
        return razorpayMid;
    }

    public void setRazorpayMid(String razorpayMid) {
        this.razorpayMid = razorpayMid;
    }

    public String getRazorpayKey() {
        return razorpayKey;
    }

    public void setRazorpayKey(String razorpayKey) {
        this.razorpayKey = razorpayKey;
    }

    public String getPayumoneyMid() {
        return payumoneyMid;
    }

    public void setPayumoneyMid(String payumoneyMid) {
        this.payumoneyMid = payumoneyMid;
    }

    public String getPayumoneyKey() {
        return payumoneyKey;
    }

    public void setPayumoneyKey(String payumoneyKey) {
        this.payumoneyKey = payumoneyKey;
    }

    public String getPayumoneySalt() {
        return payumoneySalt;
    }

    public void setPayumoneySalt(String payumoneySalt) {
        this.payumoneySalt = payumoneySalt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}