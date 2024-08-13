package com.example.healthfit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WaterAdapter extends ArrayAdapter<WaterTrackingData> {

    private static class ViewHolder {
        TextView waterTextView;
        TextView timeTextView;
    }

    public WaterAdapter(Context context, List<WaterTrackingData> dataItems) {
        super(context, 0, dataItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WaterTrackingData dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_water_tracking_info, parent, false);
            viewHolder.waterTextView = convertView.findViewById(R.id.waterIntakeInfo);
            viewHolder.timeTextView = convertView.findViewById(R.id.waterIntakeInfo2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.waterTextView.setText(dataItem.getWaterValue()+" milliliter (ml)");
        viewHolder.timeTextView.setText(String.valueOf(dataItem.getTime()));
        // Return the completed view to render on screen
        return convertView;
    }
}
