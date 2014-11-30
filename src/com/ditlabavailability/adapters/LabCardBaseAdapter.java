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

/**
 * The base adapter for the primary Lab Card rows.
 * 
 * @author Alan Haverty
 *
 */
public class LabCardBaseAdapter extends BaseAdapter {

	public ArrayList<LabTime> labArrayList;
	public LayoutInflater Inflater;

	private ViewHolder mHolder;
	private View mConvertView;

	public LabCardBaseAdapter(Context context, ArrayList<LabTime> results) {
		labArrayList = results;
		Inflater = LayoutInflater.from(context);
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

	/**
	 * Sets up the list for the main activity, with logic for applying different
	 * styles and views according to the labs position and availability status.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {

		this.mConvertView = convertView;
		LabTime lab = labArrayList.get(position);

		boolean MatchesPrevious;
		int unavailableColor = Inflater.getContext().getResources()
				.getColor(R.color.unavailable_lab_room_name);

		/*
		 * grouping labs by same room name and applying appropriate layout
		 * according to order, i.e the first hour of every room is given the
		 * header view layout
		 */
		if (position > 0) {
			MatchesPrevious = labArrayList.get(position - 1).getRoom()
					.equals(lab.getRoom());
		} else {
			MatchesPrevious = false;
		}

		if (MatchesPrevious) {
			mConvertView = Inflater.inflate(R.layout.sub_lab_card, parent,
					false);

			mHolder = createHolder();

			mConvertView.setTag(mHolder);

		} else {

			mConvertView = Inflater.inflate(R.layout.lab_card_header, parent,
					false);
			mHolder = createHolder();
			mConvertView.setTag(mHolder);

			mHolder.labName.setText(lab.getRoom());
			mHolder.location.setText(lab.getLocation());

			if (lab.getAvailability() == false) {
				mHolder.labName.setTextColor(unavailableColor);
				mHolder.time.setTextColor(unavailableColor);
				mHolder.availability.setTextColor(unavailableColor);
				mHolder.location.setTextColor(unavailableColor);
			}
		}

		// common setters among lab cards
		mHolder.time.setText(lab.getHourStr() + " - " + lab.getUntilHourStr());
		mHolder.availability.setText(lab.getAvailabilityStr());

		return mConvertView;
	}

	/**
	 * @return A {@link ViewHolder} with all the possible TextViews for this
	 *         BaseAdapter
	 */
	private ViewHolder createHolder() {
		ViewHolder holder = new ViewHolder();

		holder.availability = (TextView) mConvertView
				.findViewById(R.id.availability);
		holder.time = (TextView) mConvertView.findViewById(R.id.time);
		holder.labName = (TextView) mConvertView.findViewById(R.id.labName);
		holder.time = (TextView) mConvertView.findViewById(R.id.time);
		holder.availability = (TextView) mConvertView
				.findViewById(R.id.availability);
		holder.location = (TextView) mConvertView.findViewById(R.id.location);

		return holder;
	}

	/**
	 * @see <a
	 *      href="http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder">
	 *      http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder</a>
	 * @see
	 * @author Alan Haverty
	 *
	 */
	static class ViewHolder {
		TextView labName;
		TextView time;
		TextView availability;
		TextView location;
	}
}