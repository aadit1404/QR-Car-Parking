package com.example.cp.qr_car_parking_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class transaction_adapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> bid;
    private ArrayList<String> cost;
    private ArrayList<String> date;
    private ArrayList<String> name;

    public transaction_adapter(Activity context,
                         ArrayList<String> slot_id, ArrayList<String> slot_no,ArrayList<String> date_,ArrayList<String> name_) {
        super(context, R.layout.adapter_transaction,slot_id);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.bid=slot_id;
        this.cost=slot_no;
        this.date=date_;
        this.name=name_;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.adapter_transaction, null, true);

        TextView txtAreaN = (TextView) rowView.findViewById(R.id.txtTransCost);
        TextView txtSlotNo = (TextView) rowView.findViewById(R.id.txtTransDate);
        TextView txtName = (TextView) rowView.findViewById(R.id.txtMallName);

        txtAreaN.setText(cost.get(position));
        txtSlotNo.setText(date.get(position));
        txtName.setText(name.get(position));

        return rowView;
    }

}
