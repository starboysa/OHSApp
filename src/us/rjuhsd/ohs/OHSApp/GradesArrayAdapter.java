package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GradesArrayAdapter extends ArrayAdapter<SchoolClass>{

	private final Context context;
	private final int layoutResourceId;
	private final ArrayList<SchoolClass> data;

	public GradesArrayAdapter(Context context, int layoutResourceId, ArrayList<SchoolClass> objects) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		GradesHolder holder;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new GradesHolder();
			holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
			holder.txtMain = (TextView)row.findViewById(R.id.txtMain);

			row.setTag(holder);
		}
		else
		{
			holder = (GradesHolder)row.getTag();
		}

		SchoolClass sClass = data.get(position);
		holder.txtTitle.setText(sClass.className);
		holder.txtMain.setText(sClass.percentage);

		return row;
	}
}

class GradesHolder {
	TextView txtTitle;
	TextView txtMain;
}
