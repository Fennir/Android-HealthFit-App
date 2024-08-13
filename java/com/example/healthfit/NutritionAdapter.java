package com.example.healthfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NutritionAdapter extends ArrayAdapter<NutritionTrackingData> {

    private static class ViewHolder {
        TextView caloriesTextView;
        TextView timeTextView;
    }

    public NutritionAdapter(Context context, List<NutritionTrackingData> dataItems) {
        super(context, 0, dataItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NutritionTrackingData dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_nutrition_tracking_info, parent, false);
            viewHolder.caloriesTextView = convertView.findViewById(R.id.nutritionIntakeInfo);
            viewHolder.timeTextView = convertView.findViewById(R.id.nutritionIntakeInfo2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.caloriesTextView.setText(dataItem.getTotalCalories()+" Kilojoules (kcals)");
        viewHolder.timeTextView.setText(String.valueOf(dataItem.getTime()));
        // Return the completed view to render on screen
        return convertView;
    }
}
