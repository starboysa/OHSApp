package us.rjuhsd.ohs.OHSApp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.io.IOException;
import java.util.ArrayList;

public class ClassesOverviewTask extends AsyncTask<Void, Void, Void> {
	private final ClassesOverviewTaskReceiver layer;
	Context context;
	final AeriesManager aeriesManager;
	ArrayList<SchoolClass> grades;
	private String error = "An unknown error occurred while loading your classes"; //This text should never appear, its the default

	public ClassesOverviewTask(Context context, AeriesManager aeriesManager, ClassesOverviewTaskReceiver layer) {
		this.layer = layer;
		this.context = context;
		this.aeriesManager = aeriesManager;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		layer.onGradesStart();
	}

	@Override
	protected Void doInBackground(Void... voids) {
		grades = new ArrayList<SchoolClass>();
		try {
			String[] loginData = aeriesManager.aeriesLoginData();
			HttpResponse response = aeriesManager.client.execute(aeriesManager.getLoginRequest());

			Document doc = Jsoup.parse(response.getEntity().getContent(), null, AeriesManager.LOGIN_URL);
			int rowCount = 1;
			while (true) {
				String trId = "tr#ctl00_MainContent_ctl25_DataDetails_ctl0" + rowCount + "_trGBKItem";
				Element tr = doc.select(trId).first();
				if (tr == null) {
					if (rowCount == 1) {
						//No data was loaded, calling it quits here and posting a dialog explaining why
						if(loginData[0].equals("") || loginData[1].equals("")) {
							error = "Please check that you have entered your Aeries information correctly";
						} else {
							error = "Either the grades system is unavailable or your login is incorrect";
						}
						cancel(true);
						return null;
					}
					break;
				} else {
					SchoolClass sClass = new SchoolClass(rowCount - 1);
					Elements tds = tr.select("td");
					Element className = tds.get(1);
					if (className != null) {
						sClass.className = className.text();
					}
					Element period = tds.get(2);
					if (period != null) {
						sClass.period = period.text();
					}
					Element teacherName = tds.get(3);
					if (teacherName != null) {
						sClass.teacherName = teacherName.text();
					}
					Element percentage = tds.get(4);
					if (percentage != null) {
						sClass.percentage = percentage.text();
					}
					Element mark = tds.get(6);
					if (mark != null) {
						sClass.mark = mark.text();
					}
					Element missingAssign = tds.get(8);
					if (missingAssign != null) {
						sClass.missingAssign = missingAssign.text();
					}
					Element lastUpdate = tds.get(10);
					if (lastUpdate != null) {
						sClass.lastUpdate = lastUpdate.text();
					}
					grades.add(sClass);
				}
				rowCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		layer.onGradesError(error);
	}

	@Override
	protected void onPostExecute(Void v) {
		super.onPostExecute(v);
		if(isCancelled()) {
			onCancelled();
			return;
		}
		aeriesManager.setSchoolClasses(grades);
		layer.onGradesDone();
		aeriesManager.writeAllData();
	}
}
