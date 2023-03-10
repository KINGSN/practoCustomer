package com.darwinbark.fabcustomer.https;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.interfacess.Helper;
import com.darwinbark.fabcustomer.jsonparser.JSONParser;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Created by VARUN on 01/01/19.
 */
public class HttpsRequest {
    private String match;
    private Map<String, String> params;
    private Map<String, File> fileparams;
    private Context ctx;
    private JSONObject jObject;
    private SharedPrefrence sharedPreference;

    public HttpsRequest(String match, Map<String, String> params, Context ctx) {
        this.match = match;
        this.params = params;
        this.ctx = ctx;
        this.sharedPreference = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, Map<String, String> params, Map<String, File> fileparams, Context ctx) {
        this.match = match;
        this.params = params;
        this.fileparams = fileparams;
        this.ctx = ctx;
        this.sharedPreference = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, Context ctx) {
        this.match = match;
        this.ctx = ctx;
        this.sharedPreference = SharedPrefrence.getInstance(ctx);
    }

    public HttpsRequest(String match, JSONObject jObject, Context ctx) {
        this.match = match;
        this.jObject = jObject;
        this.ctx = ctx;
        this.sharedPreference = SharedPrefrence.getInstance(ctx);

    }

    public void stringPostJson(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + match)
                .addJSONObjectBody(jObject)
                .setTag("test")
                .addHeaders(Consts.LANGUAGE, sharedPreference.getValue(Consts.LANGUAGE_SELECTION))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " response body --->" + jObject.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPost(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL + match)
                .addBodyParameter(params)
                .setTag("test")
                .addHeaders(Consts.LANGUAGE, sharedPreference.getValue(Consts.LANGUAGE_SELECTION))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringPost2(final String TAG, final Helper h) {
        AndroidNetworking.post(Consts.BASE_URL2 + match)
                .addBodyParameter(params)
                .setTag("test")
                .addHeaders(Consts.LANGUAGE, sharedPreference.getValue(Consts.LANGUAGE_SELECTION))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                       // JSONParser jsonParser = new JSONParser(ctx, response);
                        JSONParser jsonParser = null;
                        try {
                            jsonParser = new JSONParser(ctx, response.getJSONObject("darwinbarkk"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonParser.RESULT) {
                            try {
                                h.backResponse(jsonParser.RESULT,jsonParser.darwinbarkk, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void stringGet(final String TAG, final Helper h) {
        AndroidNetworking.get(Consts.BASE_URL + match)
                .setTag("test")
                .addHeaders(Consts.LANGUAGE, sharedPreference.getValue(Consts.LANGUAGE_SELECTION))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);
                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

    public void imagePost(final String TAG, final Helper h) {
        AndroidNetworking.upload(Consts.BASE_URL + match)
                .addMultipartFile(fileparams)
                .addMultipartParameter(params)
                .setTag("uploadTest")
                .addHeaders(Consts.LANGUAGE, sharedPreference.getValue(Consts.LANGUAGE_SELECTION))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        Log.e("Byte", bytesUploaded + "  !!! " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e(TAG, " response body --->" + response.toString());
                        Log.e(TAG, " param --->" + params.toString());
                        JSONParser jsonParser = new JSONParser(ctx, response);

                        if (jsonParser.RESULT) {

                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                h.backResponse(jsonParser.RESULT, jsonParser.darwinbarkk, jsonParser.MESSAGE, null);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        ProjectUtils.pauseProgressDialog();
                        Log.e(TAG, " error body --->" + anError.getErrorBody() + " error msg --->" + anError.getMessage());
                    }
                });
    }

}