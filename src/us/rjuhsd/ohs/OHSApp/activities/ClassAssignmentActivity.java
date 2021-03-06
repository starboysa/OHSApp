package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.drawer.DrawerList;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

public class ClassAssignmentActivity extends Activity {

	private Assignment assign;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int sClassId = getIntent().getIntExtra("schoolClassId", -1);
		int assignId = getIntent().getIntExtra("assignmentId", -1);
		setContentView(R.layout.assignment_details);
		if(sClassId != -1 && assignId != -1) {
			SchoolClass sClass = new AeriesManager(this).grades.get(sClassId);
			assign = sClass.assignments.get(assignId);
		}
		DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.assignment_details_drawer_layout);
		ListView drawerList = (ListView) this.findViewById(R.id.assignment_details_drawer_list);
		new DrawerList(this, drawerLayout, drawerList);
	}

	@Override
	public void onResume() {
		super.onResume();
		fillViews();
	}

	private void fillViews() {
		((TextView) findViewById(R.id.assignment_details_description)).setText("Description: " + assign.description);
		((TextView) findViewById(R.id.assignment_details_percentage)).setText("Percentage: "+assign.percent);
		((TextView) findViewById(R.id.assignment_details_type)).setText("Type: "+assign.type);
		((TextView) findViewById(R.id.assignment_details_category)).setText("Category: "+assign.category);
		((TextView) findViewById(R.id.assignment_details_score)).setText("Score: "+assign.score);
		((TextView) findViewById(R.id.assignment_details_dateCompleted)).setText("Date Completed: "+assign.dateCompleted);
		((TextView) findViewById(R.id.assignment_details_dateDue)).setText("Date Due: "+assign.dateDue);
		((TextView) findViewById(R.id.assignment_details_gradingCompleted)).setText("Grading Completed: "+assign.gradingComplete);
	}
}
