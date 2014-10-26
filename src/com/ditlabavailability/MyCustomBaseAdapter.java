package com.ditlabavailability;
 
import java.util.ArrayList;
 

import com.ditlabavailability.R;
 
import com.ditlabavailability.model.LabTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class MyCustomBaseAdapter extends BaseAdapter {
    private static ArrayList<LabTime> labArrayList;
 
    private LayoutInflater mInflater;
 
    public MyCustomBaseAdapter(Context context, ArrayList<LabTime> results) {
    	labArrayList = results;
        mInflater = LayoutInflater.from(context);
    }
 
    public int getCount() {
        return labArrayList.size();
    }
 
    public Object getItem(int position) {
        return labArrayList.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.txtName.setText(labArrayList.get(position).toString());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView txtName;
    }
}