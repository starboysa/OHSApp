package us.rjuhsd.ohs.OHSApp.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.tasks.GradesOverviewTask;

import java.util.ArrayList;

public class AeriesManager {

	public static String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";
	//public static String DEFAULT_URL = "http://homelink.rjuhsd.us/Default.aspx";

	Activity activity;

	private boolean loadingGrades = true;
	private GradesOverviewTask gradesTask;

	private ArrayList<SchoolClass> grades;

	public void getGradesOverview(Activity activity) {
		if(grades != null) {
			gradesTask.inflateList(activity);
		} else {
			startLoadingGrades(activity);
		}
	}

	private void startLoadingGrades(final Activity activity) {
		this.activity = activity;
		loadingGrades = true;
		gradesTask = new GradesOverviewTask(activity, this);
		gradesTask.execute();
	}

	public static String[] aeriesLoginData(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String[] toReturn = new String[2];
		toReturn[0] = prefs.getString("aeries_username", "");
		toReturn[1] = prefs.getString("aeries_password", "");
		return toReturn;
	}

	public SchoolClass getById(int id) {
		return grades.get(id);
	}

	public void setSchoolClasses(ArrayList<SchoolClass> grades) {
		Log.d("SchoolClasses","Loaded school classes");
		this.grades = grades;
	}

	public void setGradesLoaded(boolean isDone) {
		this.loadingGrades = isDone;
	}

	public void errorLoadingGrades(String errorText) {
		AlertDialog.Builder adb = new AlertDialog.Builder(activity)
				.setTitle("Login Failure!")
				.setMessage(errorText)
				.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
					}
				});
		adb.show();
	}
}