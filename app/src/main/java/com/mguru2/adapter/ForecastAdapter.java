package com.mguru2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mguru2.cheflingtest.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sk Maniruddin on 23-10-2016.
 */
public class ForecastAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> data = null;
    private Context mContext;
    private Typeface typeface;

    public ForecastAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        this.mContext = context;
        this.data = data;
        this.typeface = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue_lt_com_55_roman.ttf");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.dayWithDateTextView = (TextView) view.findViewById(R.id.day_with_date_text_view);
            viewHolder.highTextView = (TextView) view.findViewById(R.id.high_text_view);
            viewHolder.lowTextView = (TextView) view.findViewById(R.id.low_text_view);
            viewHolder.conditionTextView = (TextView) view.findViewById(R.id.condition_text_view);
            viewHolder.whetherImageView = (ImageView) view.findViewById(R.id.whether_image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.dayWithDateTextView.setTypeface(typeface);
        viewHolder.highTextView.setTypeface(typeface);
        viewHolder.lowTextView.setTypeface(typeface);
        viewHolder.conditionTextView.setTypeface(typeface);

        viewHolder.dayWithDateTextView.setText(data.get(i).get("day").toString() + "\n" + data.get(i).get("date").toString());
        viewHolder.highTextView.setText(data.get(i).get("high").toString());
        viewHolder.lowTextView.setText(data.get(i).get("low").toString());
        viewHolder.conditionTextView.setText(data.get(i).get("text").toString());

        if (data.get(i).get("text").toString().trim().equalsIgnoreCase("Mostly Sunny") || data.get(i).get("text").toString().trim().equalsIgnoreCase("Sunny")) {
            viewHolder.whetherImageView.setImageResource(R.drawable.sunney);
        } else if (data.get(i).get("text").toString().trim().equalsIgnoreCase("Partly Cloudy") || data.get(i).get("text").toString().trim().equalsIgnoreCase("Cloudy")) {
            viewHolder.whetherImageView.setImageResource(R.drawable.cloud);
        } else if (data.get(i).get("text").toString().trim().equalsIgnoreCase("Rain")) {
            viewHolder.whetherImageView.setImageResource(R.drawable.rain);
        } else {
            viewHolder.whetherImageView.setImageResource(R.drawable.sunney);
        }

        return view;
    }

    static class ViewHolder {
        TextView dayWithDateTextView, highTextView, lowTextView, conditionTextView;
        ImageView whetherImageView;
    }
}

