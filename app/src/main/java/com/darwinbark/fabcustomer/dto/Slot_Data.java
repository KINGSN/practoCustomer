package com.darwinbark.fabcustomer.dto;

import org.json.JSONArray;

import java.io.Serializable;

public class Slot_Data implements Serializable {
   String slot_time;
    String remaining_booking;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Slot_Data(String slot_time, String remaining_booking) {
        this.slot_time = slot_time;
        this.remaining_booking = remaining_booking;
    }

    public String getSlot_time() {
        return slot_time;
    }

    public void setSlot_time(String slot_time) {
        this.slot_time = slot_time;
    }

    public String getRemaining_booking() {
        return remaining_booking;
    }

    public void setRemaining_booking(String remaining_booking) {
        this.remaining_booking = remaining_booking;
    }
}
