package com.ditlabavailability.adapters;

import java.util.ArrayList;

import com.ditlabavailability.R;
import com.ditlabavailability.model.LabTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LabCardBaseAdapter extends BaseAdapter {
	public static ArrayList<LabTime> labArrayList;

	public LayoutInflater mInflater;

	public LabCardBaseAdapter(Context context, ArrayList<LabTime> results) {
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
		ViewHolder holder = new ViewHolder();
		boolean doesMatchPrevious;
		int unavailableColor = mInflater.getContext().getResources()
				.getColor(R.color.unavailable_lab_room_name);

		LabTime lab = labArrayList.get(position);

		if (position > 0) {
			doesMatchPrevious = labArrayList.get(position - 1).getRoom()
					.equals(lab.getRoom());
		} else {
			doesMatchPrevious = false;
		}

		if (doesMatchPrevious) {
			convertView = mInflater.inflate(R.layout.sub_lab_card, parent,
					false);

			holder.availability = (TextView) convertView
					.findViewById(R.id.availability);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		}

		else {
			convertView = mInflater.inflate(R.layout.lab_card_header, parent,
					false);

			holder.labName = (TextView) convertView.findViewById(R.id.labName);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.availability = (TextView) convertView
					.findViewById(R.id.availability);
			holder.location = (TextView) convertView
					.findViewById(R.id.location);
			convertView.setTag(holder);

			holder.labName.setText(lab.getRoom());
			holder.location.setText(lab.getLocation());

			if (lab.getAvailability() == false) {
				holder.labName.setTextColor(unavailableColor);
				holder.time.setTextColor(unavailableColor);
				holder.availability.setTextColor(unavailableColor);
				holder.location.setTextColor(unavailableColor);
			}

		}

		// common setters among lab cards
		holder.time.setText(lab.getHourStr() + " - " + lab.getUntilHourStr());
		holder.availability.setText(lab.getAvailabilityStr());

		return convertView;
	}

	static class ViewHolder {
		TextView labName;
		TextView time;
		TextView availability;
		TextView location;
	}
}