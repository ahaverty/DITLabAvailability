package com.ditlabavailability.adapters;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ditlabavailability.R;
import com.ditlabavailability.model.LabTime;

public class LabCardSubOnlyBaseAdapter extends LabCardBaseAdapter {
	
	public LabCardSubOnlyBaseAdapter(Context context, ArrayList<LabTime> results) {
		super(context, results);
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// TODO Implement recycled views for improved scrolling
		// TODO Remove lint warning suppress tag
		
		ViewHolder holder = new ViewHolder();

		convertView = Inflater.inflate(R.layout.sub_lab_card, parent, false);

		holder.availability = (TextView) convertView
				.findViewById(R.id.availability);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		convertView.setTag(holder);
		
		holder.time.setText(labArrayList.get(position).getHourStr() + " - "
				+ labArrayList.get(position).getUntilHourStr());
		holder.availability.setText(labArrayList.get(position)
				.getAvailabilityStr());

		return convertView;
	}
	
}
