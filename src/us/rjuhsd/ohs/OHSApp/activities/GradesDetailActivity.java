package us.rjuhsd.ohs.OHSApp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import us.rjuhsd.ohs.OHSApp.OHSApplication;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;

public class GradesDetailActivity extends Activity {
	private SchoolClass sClass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int id = getIntent().getIntExtra("schoolClassId", -1);
		setContentView(R.layout.grades_detail);
		if(id != -1) {
			sClass = ((OHSApplication)getApplication()).aeriesManager.getById(id);
		}
		fillViews();
	}

	private void fillViews() {
		final TextView classNameView = (TextView) findViewById(R.id.className);
		classNameView.setText(sClass.className);
	}
}