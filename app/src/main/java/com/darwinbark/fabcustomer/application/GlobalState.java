package com.darwinbark.fabcustomer.application;

import androidx.multidex.MultiDexApplication;

import com.darwinbark.fabcustomer.AppEnvironment;
import com.darwinbark.fabcustomer.dto.HomeDataDTO;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;


public class GlobalState extends MultiDexApplication {

    private static GlobalState mInstance;
    HomeDataDTO homeData;
    SharedPrefrence sharedPrefrence;
    AppEnvironment appEnvironment;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPrefrence = SharedPrefrence.getInstance(this);
        appEnvironment = AppEnvironment.PRODUCTION;
    }


    public static synchronized GlobalState getInstance() {
        return mInstance;
    }

    public HomeDataDTO getHomeData() {
        return homeData;
    }

    public void setHomeData(HomeDataDTO homeData) {
        this.homeData = homeData;
    }


}
