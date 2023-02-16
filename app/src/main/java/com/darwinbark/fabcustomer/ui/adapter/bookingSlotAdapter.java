package com.darwinbark.fabcustomer.ui.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.darwinbark.fabcustomer.R;
import com.darwinbark.fabcustomer.dto.Slot_Data;
import com.darwinbark.fabcustomer.interfacess.Consts;
import com.darwinbark.fabcustomer.preferences.SharedPrefrence;
import com.darwinbark.fabcustomer.ui.activity.BaseActivity;
import com.darwinbark.fabcustomer.ui.activity.PatientSlotBooking;
import com.darwinbark.fabcustomer.ui.fragment.DiscoverNearBy;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import static com.darwinbark.fabcustomer.R.color.colorAccent;
import static com.darwinbark.fabcustomer.R.color.green;


public class bookingSlotAdapter extends RecyclerView.Adapter<bookingSlotAdapter.MyViewHolder> {

    //private static final String TAG="RecyclerAdapter";
    ArrayList<Slot_Data> slot_data;
    private Context context;
    private SharedPrefrence sharedPrefrence;
    private int selectedItemPosition,lastclicked;
    private String clicked;
    private TimeSlotAdapterListener timeSlotAdapterListener;



    // int count=0;
    public bookingSlotAdapter(ArrayList<Slot_Data> slot_data, Context context,TimeSlotAdapterListener timeSlotAdapterListener) {
        this.slot_data = slot_data;
        this.context = context;
        this.timeSlotAdapterListener = timeSlotAdapterListener;
        sharedPrefrence = SharedPrefrence.getInstance(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Log.i(TAG, "onCreateViewHolder: " + count++);
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bookingslot_data, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Slot_Data slotData = slot_data.get(position);
        holder.slotTime.setText(slotData.getSlot_time());

        if(slotData.isSelected()) {
            if (lastclicked == position) {
                holder.slotcard.setBackgroundResource(green);
                slotData.setSelected(false);
                return;
            } else {
                lastclicked = position;
                sharedPrefrence.setValue(Consts.Booking_time,slot_data.get(position).getSlot_time());
                //  holder.slotcard.setCardBackgroundColor(Color.parseColor("#006400"));
                holder.slotcard.setBackgroundResource(colorAccent);
//            holder.slotcard.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
            }
        }else{
            //holder.slotcard.setCardBackgroundColor(Color.parseColor("#22cb4a"));
            holder.slotcard.setBackgroundResource(green);
            return;
//            holder.slotcard.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
        }
//        holder.slotcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    selectedItemPosition = position;
//                    holder.slotcard.setBackgroundResource(colorAccent);
//                   // sharedPrefrence.setValue(Consts.Booking_time,slot_data.get(position).getSlot_time());
//                   // Toast.makeText(v.getContext()," Item Slot Details"+position + ""+slot_data.get(position).getSlot_time()+"const"
//                           // +sharedPrefrence.getValue(Consts.Booking_time),Toast.LENGTH_LONG).show();
//                   // holder.slotcard.setBackgroundResource(colorAccent);
//                   // Toast.makeText(v.getContext(), "Error: " + selectedItemPosition,
//                           // Toast.LENGTH_LONG).show();
//                   if( lastclicked == selectedItemPosition+1) {
//                        holder.slotcard.setBackgroundResource(colorAccent);
//                       Log.d("sbcsbc",selectedItemPosition+"selectedItemPosition");
//                       Log.d("sbcsbcd",lastclicked+"lastclicked");
//                       sharedPrefrence.setBooleanValue(clicked,true);
//                       Log.d("sbcsbcd",sharedPrefrence.getBooleanValue(clicked)+"clicked to select");
//                       sharedPrefrence.setValue(Consts.Booking_time,slot_data.get(position).getSlot_time());
//                      if (Boolean.getBoolean(clicked)){
//                          holder.slotcard.setBackgroundResource(green);
//                      }
//                    } else
//                        {
//                            sharedPrefrence.setBooleanValue(clicked,false);
//                            holder.slotcard.setBackgroundResource(green);
//                            lastclicked=position;
//                   }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        });

       }

    @Override
    public int getItemCount() {
        return slot_data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView slotTime;
        CardView slotcard;
        Chip click;

       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           slotTime = itemView.findViewById(R.id.slot_time);
           slotcard = itemView.findViewById(R.id.time1);

          /* amount=itemView.findViewById(R.id.amount);
           date=itemView.findViewById(R.id.wallet_date);
           name=itemView.findViewById(R.id.name);*/
           slotcard.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   timeSlotAdapterListener.clickTimeSlot(slot_data.get(getAdapterPosition()), getAdapterPosition());
               }
           });
       }
   }
    public interface TimeSlotAdapterListener {
        void clickTimeSlot(Slot_Data timeSlotData, int position);
    }

    public void setSelectedTimeSlot(Slot_Data timeSlot) {
        for (int i = 0; i < slot_data.size(); i++) {
            if (slot_data.get(i).getSlot_time().equals(timeSlot.getSlot_time())) {
                slot_data.get(i).setSelected(true);
            } else {
                slot_data.get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();

        for (int i=0;i<slot_data.size();i++){
            Log.d("dffdfdf",slot_data.get(i).getSlot_time()+"    "+slot_data.get(i).isSelected());
        }
    }
}
