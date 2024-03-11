package com.example.cp.qr_car_parking_app;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Slots_adapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> slot_Id;
    private ArrayList<String> slot_No;

    public Slots_adapter(Activity context,
                                  ArrayList<String> slot_id, ArrayList<String> slot_no) {
        super(context, R.layout.activity_slot_adapter,slot_id);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.slot_Id=slot_id;
        this.slot_No=slot_no;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_slot_adapter, null, true);

        TextView txtAreaN = (TextView) rowView.findViewById(R.id.txtAreaN);
        TextView txtSlotNo = (TextView) rowView.findViewById(R.id.txtSlotNo);

        txtSlotNo.setText(slot_No.get(position));
        //txtPlaceAddr.setText();
        return rowView;
    }

}
