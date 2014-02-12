package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;

public class GradesDetailDetailActivity extends Activity { //"Detail" down the rabbit hole

	Assignment assign;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int sClassId = getIntent().getIntExtra("schoolClassId", -1);
		int assignId = getIntent().getIntExtra("assignmentId", -1);
		setContentView(R.layout.grades_detail);
		if(sClassId != -1 && assignId != -1) {
			SchoolClass sClass = ((OHSApplication)getApplication()).aeriesManager.getById(sClassId);
			assign = sClass.assignments.get(assignId);
		}
		fillViews();
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.grades_detail_className);
		classNameView.setText("Description: "+assign.description);
		final TextView percentageView = (TextView) findViewById(R.id.grades_detail_percentage);
		percentageView.setText("Percentage: "+assign.percent+"%");
	}
}
