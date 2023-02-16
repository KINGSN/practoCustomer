package com.darwinbark.fabcustomer;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.AppIntro;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.utils.ProjectUtils;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import es.dmoral.toasty.Toasty;

@RequiresApi(api = Build.VERSION_CODES.R)
public class SplashActivity extends AppCompatActivity {
    private static final String MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage";
    private static final int REQUEST_PERMISSIONS = 1234;
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private static final int SPLASH_TIME_OUT = 4000; //3000
    Context mContext;
    ImageView logo;
    TextView appname, tagline;
    Animation from_top, from_bottom;
    private SharedPrefrence prefference;

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
                Intent in = new Intent(mContext, BaseActivity.class);
                startActivity(in);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            } else {
                startActivity(new Intent(SplashActivity.this, AppIntro.class));
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);
            }


        }

    };
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public boolean accessNetState;
    private final Handler handler = new Handler();

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

   @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fabric.with(this, new Crashlytics());

        ProjectUtils.Fullscreen(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        prefference = SharedPrefrence.getInstance(SplashActivity.this);
        FirebaseMessaging.getInstance().subscribeToTopic(Consts.TOPIC_CUSTOMER)
                .addOnCompleteListener(task -> {

                });

        logo = findViewById(R.id.logo);
        appname = findViewById(R.id.appname);
        tagline = findViewById(R.id.tagline);

        //load animation
        from_top = AnimationUtils.loadAnimation(this, R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);

        // passing animation
        logo.startAnimation(from_top);
        appname.startAnimation(from_bottom);
        tagline.startAnimation(from_bottom);

       /* if (Environment.isExternalStorageManager()) {
            //todo when permission is granted
            // Toast.makeText(this, "all files permission granted", Toast.LENGTH_SHORT).show();
        } else {


           // askper();
            //request for the permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }*/

      /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arePermissionDenied()) {

           // If Android 11 Request for Manage File Access Permission
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
               Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);

               startActivityForResult(intent, REQUEST_PERMISSIONS);
               return;
           }

           requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
       }*/



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions(SplashActivity.this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            callinappupdate();
            //handler.postDelayed(mTask, SPLASH_TIME_OUT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            try {

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                boolean fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                boolean corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                handler.postDelayed(mTask, 3000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final int UPDATE_REQUEST_CODE=1612;
    public void callinappupdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener( appUpdateInfo ->{
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            )
                try {
                    // Request the update.
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE
                            ,SplashActivity.this,UPDATE_REQUEST_CODE);
                }catch (IntentSender.SendIntentException exception){
                    Log.e("KINGSN", "callinappupdate: "+exception.getMessage() );
                }

            else{
                Log.d("KINGSN","UPDATE NOT AVAILABLE");
              //  goToMainPage();
                handler.postDelayed(mTask, SPLASH_TIME_OUT);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
           // handler.postDelayed(mTask, SPLASH_TIME_OUT);
            Log.d("KINGSN","DATA_NULL");
            goToMainPage();
        }
        if (requestCode==UPDATE_REQUEST_CODE){
            Toasty.success(SplashActivity.this, "App Update Started", Toast.LENGTH_LONG, true).show();
           // goToMainPage();
            //handler.postDelayed(mTask, SPLASH_TIME_OUT);
            if (resultCode != RESULT_OK){
                Log.e("KINGSN", "onActivityResult: UPDATE FLOW FAILED"+resultCode);
               //goToMainPage();
                handler.postDelayed(mTask, SPLASH_TIME_OUT);
            }
        }
    }

    public void goToMainPage(){
        if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {
            Intent in = new Intent(mContext, BaseActivity.class);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else {
            startActivity(new Intent(SplashActivity.this, AppIntro.class));
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        }
    }


    public void askper() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(Html.fromHtml(getResources().getString(R.string.grant_all_files_permission)))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.R)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                       // finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        askper();
                    }
                })
                .show();
    }
    private boolean arePermissionDenied() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return checkStorageApi30();
        }

        for (String permissions : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permissions) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    boolean checkStorageApi30() {
        AppOpsManager appOps = getApplicationContext().getSystemService(AppOpsManager.class);
        int mode = appOps.unsafeCheckOpNoThrow(
                MANAGE_EXTERNAL_STORAGE_PERMISSION,
                getApplicationContext().getApplicationInfo().uid,
                getApplicationContext().getPackageName()
        );
        return mode != AppOpsManager.MODE_ALLOWED;

    }
}


