package com.example.pratyeshsingh.accoliteassignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter<T> extends BaseAdapter {
    private LayoutInflater inflater = null;
    ArrayList<T> dataList;
    Activity activity;

    public MyAdapter(Activity activity, ArrayList dataList) {
        this.activity = activity;
        this.dataList = dataList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return dataList.size();
    }

    @Override
    public T getItem(int position) {

        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.row_item, null);
            holder = new ViewHolder(vi);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        T item = getItem(position);
        if (item instanceof Content) {
            holder.message.setText(( ((Content) item).getName()));
        }

        return vi;
    }

    class ViewHolder {
        TextView message;

        public ViewHolder(View vi) {
            message = (TextView) vi.findViewById(R.id.message);
        }
    }
}
