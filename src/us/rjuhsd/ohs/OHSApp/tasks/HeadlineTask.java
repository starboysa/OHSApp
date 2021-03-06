package us.rjuhsd.ohs.OHSApp.tasks;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.managers.CentricityManager;
import us.rjuhsd.ohs.OHSApp.news.Article;
import us.rjuhsd.ohs.OHSApp.news.ArticleWrapper;

import java.io.IOException;

public class HeadlineTask extends AsyncTask<Void, ArticleWrapper, Void> {
	private static Document doc;
	private static CentricityManager cm;
	public static boolean forceUpdate;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cm = new CentricityManager();
		if(cm.listLength() == 0 || forceUpdate) {
			CentricityManager.addArticle("Loading articles...", "Please wait while articles are loaded from the OHS website", Article.LOADING_MESSAGE);
		}
	}

	@Override
	protected Void doInBackground(Void... unused) {
		if (cm.listLength() > 0 && !forceUpdate) {
			cm.reAddArticles();
		} else {
			try {
				String URL = "http://www.rjuhsd.us/oakmont";
				HttpGet request = new HttpGet(URL);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				doc = Jsoup.parse(response.getEntity().getContent(), null, URL);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Elements headlines = doc.select("div.headlines .ui-widget-detail ul li");
			CentricityManager.clearNotifications();
			for (Element headline : headlines) {
				publishProgress(new ArticleWrapper(headline));
			}
		}
		return null;
	}

	@Override
	public void onProgressUpdate(ArticleWrapper... item) {
		cm.addArticle(item[0], true);
	}
}

