package com.darwinbark.fabcustomer.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.dto.LanguageDTO;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.AppIntro;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hemant on 08/01/2020.
 */

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.LanguageHolder> {
    private ArrayList<LanguageDTO> datas = new ArrayList<>();
    private Context mContext;
    private SharedPrefrence prefrence;
    private String half, second_half;
    String language = "";
    String type = "";

    public AdapterLanguage(ArrayList<LanguageDTO> datas, Context mContext, String type) {
        this.datas = datas;
        this.mContext = mContext;
        prefrence = SharedPrefrence.getInstance(mContext);
        this.type = type;
    }

    @Override
    public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_language, parent, false);
        LanguageHolder categoryHolder = new LanguageHolder(view);

        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(LanguageHolder holder, final int position) {

        try {
            second_half = datas.get(position).getLanguage_name().substring(datas.get(position).getLanguage_name().length() / 2);
            half = datas.get(position).getLanguage_name().substring(0, datas.get(position).getLanguage_name().length() / 2);
            String next = "<font color='#02688C'>" + half + "</font>";
            String last = "<font color='#02688C'>" + second_half + "</font>";
            String complete_word = next + last;

            holder.tvLanguage.setText(datas.get(position).getLanguage_name());
            language = datas.get(position).getLanguage_name();

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.lllanguagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(mContext, AppIntro.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ((Activity) mContext).startActivity(intent);
                    ((Activity) mContext).finish();
                } else if (type.equalsIgnoreCase("1")) {
                    ((Activity) mContext).finish();

                    Intent mIntent = new Intent(mContext, BaseActivity.class);
                    mIntent.putExtra("finish", true);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ((Activity) mContext).finish();
                    mContext.startActivity(mIntent);
                }


                prefrence.setValue(Consts.LANGUAGE_SELECTION, datas.get(position).getLanguage_code());
                prefrence.setValue(Consts.VOICE_PREFERENCE, datas.get(position).getLanguage_code());
                language(datas.get(position).getLanguage_code());
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class LanguageHolder extends RecyclerView.ViewHolder {
        public LinearLayout lllanguagelayout;
        public TextView tvLanguage;

        public LanguageHolder(View itemView) {
            super(itemView);
            tvLanguage = (TextView) itemView.findViewById(R.id.tvLanguage);
            lllanguagelayout = (LinearLayout) itemView.findViewById(R.id.lllanguagelayout);
        }
    }

    public void language(String language) {
        String languageToLoad = language; // your language


        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());

    }

}
