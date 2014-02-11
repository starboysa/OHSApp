package us.rjuhsd.ohs.OHSApp.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GradesDetailTask extends AsyncTask<SchoolClass,Void,Void> {

	private final static String VIEW_STATE_POST = "/wEPDwULLTEzNjEyNjA4NzgPZBYCZg9kFgICAw9kFgJmD2QWDAIDDw8WAh4EVGV4dAUJMjAxMy0yMDE0ZGQCBQ8PFgQfAAUTT2FrbW9udCBIaWdoIFNjaG9vbB4HVG9vbFRpcAUJU2Nob29sIDMzZGQCDw9kFgJmDxYCHglpbm5lcmh0bWwF0Sg8dGFibGUgd2lkdGg9IjEwMCUiIGJvcmRlcj0iMCIgY2VsbHBhZGRpbmc9IjAiIGNlbGxzcGFjaW5nPSIwIj48dHI+PHRkIGFsaWduPSJsZWZ0IiBjbGFzcz0iYWwiPjx0YWJsZSBjbGFzcz0ibWVudXRhYmxlIiBib3JkZXI9IjAiIGNlbGxwYWRkaW5nPSIwIiBjZWxsc3BhY2luZz0iMCI+PHRyIHZhbGlnbj0idG9wIj48dGQgYWxpZ249ImxlZnQiIGNsYXNzPSJhbCIgc3R5bGU9IndoaXRlLXNwYWNlOm5vd3JhcDsgZGlzcGxheTppbmxpbmU7Ij48dGFibGUgYm9yZGVyPSIwIiBjZWxscGFkZGluZz0iMCIgY2VsbHNwYWNpbmc9IjAiPjx0ciB2YWxpZ249InRvcCI+PHRkPjxkaXYgaWQ9IlRvcF8xIj48YSBjbGFzcz0idG9wZHluYW1pY21lbnVUYWJsZXQiIGhyZWY9IkRlZmF1bHQuYXNweCIgbmFtZT0iSG9tZSIgaWQ9IkhvbWUiPkhvbWU8L2E+PC9kaXY+PC90ZD48dGQ+PGRpdiBpZD0iVG9wXzIiPjxhIGNsYXNzPSJ0b3BkeW5hbWljbWVudVRhYmxldCIgaHJlZj0iU3R1ZGVudFByb2ZpbGUuYXNweCIgbmFtZT0iU3R1ZGVudCBJbmZvIiBpZD0iU3R1ZGVudCBJbmZvIj5TdHVkZW50IEluZm88L2E+PC9kaXY+PGRpdiBpZD0iU3ViXzIiIHN0eWxlPSJib3JkZXItYm90dG9tOiBvdXRzZXQgMXB4ICNGRkZGRkY7IGRpc3BsYXk6aW5saW5lOyBsZWZ0OjBweDsiPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iU3R1ZGVudFByb2ZpbGUuYXNweCIgbmFtZT0iUHJvZmlsZSIgaWQ9IlByb2ZpbGUiPlByb2ZpbGU8L2E+PGEgY2xhc3M9InN1YmR5bmFtaWNtZW51IiBocmVmPSJTdHVkZW50cy5hc3B4IiBuYW1lPSJEZW1vZ3JhcGhpY3MiIGlkPSJEZW1vZ3JhcGhpY3MiPkRlbW9ncmFwaGljczwvYT48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9IkNsYXNzZXMuYXNweCIgbmFtZT0iQ2xhc3NlcyIgaWQ9IkNsYXNzZXMiPkNsYXNzZXM8L2E+PGEgY2xhc3M9InN1YmR5bmFtaWNtZW51IiBocmVmPSJGZWVzLmFzcHgiIG5hbWU9IkZlZXMgYW5kIEZpbmVzIiBpZD0iRmVlcyBhbmQgRmluZXMiPkZlZXMgYW5kIEZpbmVzPC9hPjwvZGl2PjwvdGQ+PHRkPjxkaXYgaWQ9IlRvcF8zIj48YSBjbGFzcz0idG9wZHluYW1pY21lbnVUYWJsZXQiIGhyZWY9IkF0dGVuZGFuY2UuYXNweCIgbmFtZT0iQXR0ZW5kYW5jZSIgaWQ9IkF0dGVuZGFuY2UiPkF0dGVuZGFuY2U8L2E+PC9kaXY+PGRpdiBpZD0iU3ViXzMiIHN0eWxlPSJib3JkZXItYm90dG9tOiBvdXRzZXQgMXB4ICNGRkZGRkY7IGRpc3BsYXk6aW5saW5lOyBsZWZ0OjBweDsiPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iQXR0ZW5kYW5jZS5hc3B4IiBuYW1lPSJBdHRlbmRhbmNlIiBpZD0iQXR0ZW5kYW5jZSI+QXR0ZW5kYW5jZTwvYT48L2Rpdj48L3RkPjx0ZD48ZGl2IGlkPSJUb3BfNCI+PGEgY2xhc3M9InZpc2l0ZWR0b3BtZW51IiBocmVmPSJHcmFkZWJvb2tTdW1tYXJ5LmFzcHgiIG5hbWU9IkdyYWRlcyIgaWQ9IkdyYWRlcyI+R3JhZGVzPC9hPjwvZGl2PjxkaXYgaWQ9IlN1Yl80IiBzdHlsZT0iYm9yZGVyLWJvdHRvbTogb3V0c2V0IDFweCAjRkZGRkZGOyBkaXNwbGF5OmlubGluZTsgbGVmdDowcHg7Ij48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9IkdyYWRlYm9va1N1bW1hcnkuYXNweCIgbmFtZT0iR3JhZGVib29rIiBpZD0iR3JhZGVib29rIj5HcmFkZWJvb2s8L2E+PGEgY2xhc3M9InZpc2l0ZWRzdWJtZW51IiBocmVmPSJHcmFkZWJvb2tEZXRhaWxzLmFzcHgiIG5hbWU9IkdyYWRlYm9vayBEZXRhaWxzIiBpZD0iR3JhZGVib29rIERldGFpbHMiPkdyYWRlYm9vayBEZXRhaWxzPC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iR3JhZGVzLmFzcHgiIG5hbWU9IkdyYWRlcyIgaWQ9IkdyYWRlcyI+R3JhZGVzPC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iR3JhZHVhdGlvblJlcXVpcmVtZW50cy5hc3B4IiBuYW1lPSJHcmFkdWF0aW9uIFN0YXR1cyIgaWQ9IkdyYWR1YXRpb24gU3RhdHVzIj5HcmFkdWF0aW9uIFN0YXR1czwvYT48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9IlRyYW5zY3JpcHRzLmFzcHgiIG5hbWU9IlRyYW5zY3JpcHRzIiBpZD0iVHJhbnNjcmlwdHMiPlRyYW5zY3JpcHRzPC9hPjwvZGl2PjwvdGQ+PHRkPjxkaXYgaWQ9IlRvcF81Ij48YSBjbGFzcz0idG9wZHluYW1pY21lbnVUYWJsZXQiIGhyZWY9Ik1lZGljYWxIaXN0b3J5LmFzcHgiIG5hbWU9Ik1lZGljYWwiIGlkPSJNZWRpY2FsIj5NZWRpY2FsPC9hPjwvZGl2PjxkaXYgaWQ9IlN1Yl81IiBzdHlsZT0iYm9yZGVyLWJvdHRvbTogb3V0c2V0IDFweCAjRkZGRkZGOyBkaXNwbGF5OmlubGluZTsgbGVmdDowcHg7Ij48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9Ik1lZGljYWxIaXN0b3J5LmFzcHgiIG5hbWU9Ik1lZGljYWwgSGlzdG9yeSIgaWQ9Ik1lZGljYWwgSGlzdG9yeSI+TWVkaWNhbCBIaXN0b3J5PC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iSW1tdW5pemF0aW9ucy5hc3B4IiBuYW1lPSJJbW11bml6YXRpb25zIiBpZD0iSW1tdW5pemF0aW9ucyI+SW1tdW5pemF0aW9uczwvYT48L2Rpdj48L3RkPjx0ZD48ZGl2IGlkPSJUb3BfNiI+PGEgY2xhc3M9InRvcGR5bmFtaWNtZW51VGFibGV0IiBocmVmPSJDQUhTRUVTY29yZXMuYXNweCIgbmFtZT0iVGVzdCBTY29yZXMiIGlkPSJUZXN0IFNjb3JlcyI+VGVzdCBTY29yZXM8L2E+PC9kaXY+PGRpdiBpZD0iU3ViXzYiIHN0eWxlPSJib3JkZXItYm90dG9tOiBvdXRzZXQgMXB4ICNGRkZGRkY7IGRpc3BsYXk6aW5saW5lOyBsZWZ0OjBweDsiPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iQ0FIU0VFU2NvcmVzLmFzcHgiIG5hbWU9IkNBSFNFRSBTY29yZXMiIGlkPSJDQUhTRUUgU2NvcmVzIj5DQUhTRUUgU2NvcmVzPC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iVGVzdFNjb3Jlcy5hc3B4IiBuYW1lPSJUZXN0IFNjb3JlcyIgaWQ9IlRlc3QgU2NvcmVzIj5UZXN0IFNjb3JlczwvYT48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9IkNvbnRlbnRTdGFuZGFyZHNTY29yZXMuYXNweCIgbmFtZT0iQ29udGVudCBTdGFuZGFyZHMgU2NvcmVzIiBpZD0iQ29udGVudCBTdGFuZGFyZHMgU2NvcmVzIj5Db250ZW50IFN0YW5kYXJkcyBTY29yZXM8L2E+PGEgY2xhc3M9InN1YmR5bmFtaWNtZW51IiBocmVmPSJDb2xsZWdlVGVzdFNjb3Jlcy5hc3B4IiBuYW1lPSJDb2xsZWdlIEVudHJhbmNlIFRlc3RzIiBpZD0iQ29sbGVnZSBFbnRyYW5jZSBUZXN0cyI+Q29sbGVnZSBFbnRyYW5jZSBUZXN0czwvYT48L2Rpdj48L3RkPjwvdHI+PC90YWJsZT48L3RkPjx0ZCBhbGlnbj0icmlnaHQiPjx0YWJsZSBib3JkZXI9IjAiIGNlbGxwYWRkaW5nPSIwIiBjZWxsc3BhY2luZz0iMCI+PHRyIHZhbGlnbj0idG9wIj48dGQ+PGRpdiBpZD0iVG9wXzciPjxhIGNsYXNzPSJ0b3BkeW5hbWljbWVudVRhYmxldCIgaHJlZj0iQWRkTmV3U3R1ZGVudFRvUGFyZW50LmFzcHgiIG5hbWU9IkNoYW5nZSBTdHVkZW50IiBpZD0iQ2hhbmdlIFN0dWRlbnQiPkNoYW5nZSBTdHVkZW50PC9hPjwvZGl2PjxkaXYgaWQ9IlN1Yl83IiBzdHlsZT0iYm9yZGVyLWJvdHRvbTogb3V0c2V0IDFweCAjRkZGRkZGOyBkaXNwbGF5OmlubGluZTsgbGVmdDowcHg7Ij48YSBjbGFzcz0idmlzaXRlZHN1Ym1lbnUiIGhyZWY9IkNoYW5nZVN0dWRlbnQuYXNweD9jYWNoZT0yJTJmNiUyZjIwMTQrMTElM2ExOSUzYTAyK0FNJlNDPTMzJlNOPTM2OTA5JlJldHVyblBhZ2U9R3JhZGVib29rRGV0YWlscy5hc3B4IiBuYW1lPSIzM18zNjkwOSIgaWQ9IjMzXzM2OTA5Ij5KYW56ZW4sIEpvbmF0aGFuIEUgLSBHcmQgMTEgLSBPYWttb250IEhTPC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iQWRkTmV3U3R1ZGVudFRvUGFyZW50LmFzcHgiIG5hbWU9IkFkZCBOZXcgU3R1ZGVudCBUbyBZb3VyIEFjY291bnQiIGlkPSJBZGQgTmV3IFN0dWRlbnQgVG8gWW91ciBBY2NvdW50Ij5BZGQgTmV3IFN0dWRlbnQgVG8gWW91ciBBY2NvdW50PC9hPjwvZGl2PjwvdGQ+PHRkPjxkaXYgaWQ9IlRvcF84Ij48YSBjbGFzcz0idG9wZHluYW1pY21lbnVUYWJsZXQiIGhyZWY9IkNoYW5nZVBhc3N3b3JkLmFzcHgiIG5hbWU9Ik9wdGlvbnMiIGlkPSJPcHRpb25zIj5PcHRpb25zPC9hPjwvZGl2PjxkaXYgaWQ9IlN1Yl84IiBzdHlsZT0iYm9yZGVyLWJvdHRvbTogb3V0c2V0IDFweCAjRkZGRkZGOyBkaXNwbGF5OmlubGluZTsgbGVmdDowcHg7Ij48YSBjbGFzcz0ic3ViZHluYW1pY21lbnUiIGhyZWY9IkNoYW5nZVBhc3N3b3JkLmFzcHgiIG5hbWU9IkNoYW5nZSBQYXNzd29yZCIgaWQ9IkNoYW5nZSBQYXNzd29yZCI+Q2hhbmdlIFBhc3N3b3JkPC9hPjxhIGNsYXNzPSJzdWJkeW5hbWljbWVudSIgaHJlZj0iQ2hhbmdlRW1haWwuYXNweCIgbmFtZT0iQ2hhbmdlIEVtYWlsIiBpZD0iQ2hhbmdlIEVtYWlsIj5DaGFuZ2UgRW1haWw8L2E+PGEgY2xhc3M9InN1YmR5bmFtaWNtZW51IiBocmVmPSJQYXJlbnROb3RpZmljYXRpb25QcmVmZXJlbmNlcy5hc3B4IiBuYW1lPSJQYXJlbnQgTm90aWZpY2F0aW9uIFByZWZlcmVuY2VzIiBpZD0iUGFyZW50IE5vdGlmaWNhdGlvbiBQcmVmZXJlbmNlcyI+UGFyZW50IE5vdGlmaWNhdGlvbiBQcmVmZXJlbmNlczwvYT48L2Rpdj48L3RkPjx0ZD48ZGl2IGlkPSJUb3BfOSI+PGEgY2xhc3M9InRvcGR5bmFtaWNtZW51VGFibGV0IiBocmVmPSJMb2dvdXQuYXNweCIgbmFtZT0iTG9nb3V0IiBpZD0iTG9nb3V0Ij5Mb2dvdXQ8L2E+PC9kaXY+PC90ZD48L3RyPjwvdGFibGU+PHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPmF0X2F0dGFjaCgiVG9wXzEiLCAiU3ViXzEiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzIiLCAiU3ViXzIiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzMiLCAiU3ViXzMiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzQiLCAiU3ViXzQiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzUiLCAiU3ViXzUiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzYiLCAiU3ViXzYiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzciLCAiU3ViXzciLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzgiLCAiU3ViXzgiLCAicG9pbnRlciIpO2F0X2F0dGFjaCgiVG9wXzkiLCAiU3ViXzkiLCAicG9pbnRlciIpOzwvc2NyaXB0PjwvdGQ+PC90cj48L3RhYmxlPjwvdGQ+PC90cj48L3RhYmxlPmQCEw9kFgQCAQ9kFgJmDw9kDxAWAmYCARYCFgIeDlBhcmFtZXRlclZhbHVlBQIzMxYCHwMFBTM2OTA5FgICBQIFZGQCAw8PFhQeC0RvaW5nUnVicmljaB4NU2hvd0ZpbmFsTWFya2geF0RvaW5nU3VtbWF0aXZlRm9ybWF0aXZlaB4LRG9pbmdXZWlnaHRnHg9FeHBhbmRBbGxTY3JpcHRlHhNGb3JtYXRpdmVQZXJjZW50YWdlBQEwHg5Pbmx5U2hvd0NoZWNrc2geEUNvbnRyYWN0QWxsU2NyaXB0ZR4TU3VtbWF0aXZlUGVyY2VudGFnZQUBMB4SQ3VycmVudEFzc2lnbm1lbnRzMqUCAAEAAAD/////AQAAAAAAAAAEAQAAAH9TeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTIuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24GAAAICAkCAAAADAAAAAwAAAARAgAAABAAAAAGAwAAAAExBgQAAAABMgYFAAAAATMGBgAAAAE0BgcAAAABNQYIAAAAATYGCQAAAAE3BgoAAAABOAYLAAAAATkGDAAAAAIxMAYNAAAAAjExBg4AAAACMTINBAtkFgICAQ9kFgJmD2QWAmYPZBYCAgEPZBYCZg9kFgwCAw8QZA8WC2YCAQICAgMCBAIFAgYCBwIIAgkCChYLEAUpMS0gQVAgU3RhdGlzdGljcy0gUyAgMS83LzIwMTQgLSA1LzI5LzIwMTQFBzU0ODY1MDZnEAUqMy0gSCBQcmUtQ2FsY3VsdXMtIFMgIDEvNy8yMDE0IC0gNS8yOS8yMDE0BQcxNzc5NTQxZxAFLjQtIEluZm8gVGVjaCBFc3Nlbi0gUy1xMSAgMS83LzIwMTQgLSAzLzE0LzIwMTQFBzI2NTMxOTFnEAU3PDwgMS0gMSBBcnQgMSBmYWxsIHRlcm0gMS0gRiAgOC8xMi8yMDEzIC0gMTAvMTEvMjAxMyA+PgUHMjY3NjM2MWcQBTI8PCAxLSAxIEFydCAxdGVybSAyLSBGICAxMC8xNC8yMDEzIC0gMTIvMjAvMjAxMyA+PgUHMjc1MTgxN2cQBTA8PCAyLSBIIFBoeXNpY3MgRjEtIEYgIDgvMTIvMjAxMyAtIDEwLzExLzIwMTMgPj4FBzQxMzk1NDRnEAUxPDwgMi0gSCBQaHlzaWNzIEYyLSBGICAxMC8xNC8yMDEzIC0gMTIvMjAvMjAxMyA+PgUHNzMwMDMzNGcQBTc8PCAzLSBIIFNwYW5pc2ggNC0gRmFsbCAyMDEzICA4LzEyLzIwMTMgLSAxMC8xMS8yMDEzID4+BQc5NTEyNzgwZxAFNTw8IDMtIEggU3BhbmlzaCA0LSBXaW50ZXIgIDEwLzE1LzIwMTMgLSAxMi8yMC8yMDEzID4+BQcyOTgwMDEzZxAFODw8IDQtIEFQIExhbmcvQ29tcC0gRjEzU2VtMVA0ICA4LzEyLzIwMTMgLSAxMC8xNS8yMDEzID4+BQc0NTc2NjIwZxAFOTw8IDQtIEFQIExhbmcvQ29tcC0gRjEzU2VtMlA0ICAxMC8xNC8yMDEzIC0gMTIvMjAvMjAxMyA+PgUGODQ1OTE4Z2RkAgcPDxYCHwAFB011bGxlckFkZAIJDxYEHwIFEWFtdWxsZXJAcmp1aHNkLnVzHgRocmVmBRhtYWlsdG86YW11bGxlckByanVoc2QudXNkAg0PFgIeC18hSXRlbUNvdW50AgwWGmYPZBYEAgEPFgIeB29uY2xpY2sFG0V4cGFuZEFsbEdyZEJrMTc3OTU0MSh0aGlzKWQCAw8WAh4HVmlzaWJsZWhkAgEPZBYWZg8VAQExZAIBDxYCHxAFUFRvZ2dsZUdyYWRlYm9va0luZm8odGhpcywgJ2N0bDAwX01haW5Db250ZW50X3N1YkdCU19EYXRhRGV0YWlsc19jdGwwMV90YmxNb3JlJyk7ZAICDxUBCUNXIFVuaXQgMWQCAw9kFgZmD2QWAgIBD2QWAmYPFQEKMDEvMDcvMjAxNGQCAQ9kFgICAQ9kFgJmDxUBAGQCAg9kFgICAQ9kFgJmDxUBAGQCBA8VAglTdW1tYXRpdmUJQ2xhc3N3b3JrZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAATcAAAE4ZAIJD2QWAmYPFQUNZGlzcGxheTpub25lOwEwAAABMGQCCw9kFgJmDxUBBjg3LjUwJWQCDA8VBAAKMDEvMTAvMjAxNAowMS8xMC8yMDE0A1llc2QCDQ88KwAJAQAPFgQeCERhdGFLZXlzFgAfD2ZkZAICD2QWFmYPFQEBMmQCAQ8WAh8QBVBUb2dnbGVHcmFkZWJvb2tJbmZvKHRoaXMsICdjdGwwMF9NYWluQ29udGVudF9zdWJHQlNfRGF0YURldGFpbHNfY3RsMDJfdGJsTW9yZScpO2QCAg8VAQlIVyBVbml0IDFkAgMPZBYGZg9kFgICAQ9kFgJmDxUBCjAxLzA3LzIwMTRkAgEPZBYCAgEPZBYCZg8VAQBkAgIPZBYCAgEPZBYCZg8VAQBkAgQPFQIJU3VtbWF0aXZlCEhvbWV3b3JrZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAAjE2AAACMTZkAgkPZBYCZg8VBQ1kaXNwbGF5Om5vbmU7ATAAAAEwZAILD2QWAmYPFQEHMTAwLjAwJWQCDA8VBAAKMDEvMTAvMjAxNAowMS8xMC8yMDE0A1llc2QCDQ88KwAJAQAPFgQfEhYAHw9mZGQCAw9kFhZmDxUBATNkAgEPFgIfEAVQVG9nZ2xlR3JhZGVib29rSW5mbyh0aGlzLCAnY3RsMDBfTWFpbkNvbnRlbnRfc3ViR0JTX0RhdGFEZXRhaWxzX2N0bDAzX3RibE1vcmUnKTtkAgIPFQEJQ1cgVW5pdCAxZAIDD2QWBmYPZBYCAgEPZBYCZg8VAQowMS8xMy8yMDE0ZAIBD2QWAgIBD2QWAmYPFQEAZAICD2QWAgIBD2QWAmYPFQEAZAIEDxUCCVN1bW1hdGl2ZQlDbGFzc3dvcmtkAgUPFgIfEWgWAmYPFQEOZGlzcGxheTpibG9jaztkAgcPZBYCZg8VBQACMTIAAAIxMmQCCQ9kFgJmDxUFDWRpc3BsYXk6bm9uZTsBMAAAATBkAgsPZBYCZg8VAQcxMDAuMDAlZAIMDxUEAAowMS8xNy8yMDE0CjAxLzE3LzIwMTQDWWVzZAINDzwrAAkBAA8WBB8SFgAfD2ZkZAIED2QWFmYPFQEBNGQCAQ8WAh8QBVBUb2dnbGVHcmFkZWJvb2tJbmZvKHRoaXMsICdjdGwwMF9NYWluQ29udGVudF9zdWJHQlNfRGF0YURldGFpbHNfY3RsMDRfdGJsTW9yZScpO2QCAg8VAQlIVyBVbml0IDFkAgMPZBYGZg9kFgICAQ9kFgJmDxUBCjAxLzEzLzIwMTRkAgEPZBYCAgEPZBYCZg8VAQBkAgIPZBYCAgEPZBYCZg8VAQBkAgQPFQIJU3VtbWF0aXZlCEhvbWV3b3JrZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAAjE4AAACMjBkAgkPZBYCZg8VBQ1kaXNwbGF5Om5vbmU7ATAAAAEwZAILD2QWAmYPFQEGOTAuMDAlZAIMDxUEAAowMS8xNy8yMDE0CjAxLzE3LzIwMTQDWWVzZAINDzwrAAkBAA8WBB8SFgAfD2ZkZAIFD2QWFmYPFQEBNWQCAQ8WAh8QBVBUb2dnbGVHcmFkZWJvb2tJbmZvKHRoaXMsICdjdGwwMF9NYWluQ29udGVudF9zdWJHQlNfRGF0YURldGFpbHNfY3RsMDVfdGJsTW9yZScpO2QCAg8VAQtRdWl6IFVuaXQgMWQCAw9kFgZmD2QWAgIBD2QWAmYPFQEKMDEvMTMvMjAxNGQCAQ9kFgICAQ9kFgJmDxUBAGQCAg9kFgICAQ9kFgJmDxUBAGQCBA8VAglTdW1tYXRpdmUEUXVpemQCBQ8WAh8RaBYCZg8VAQ5kaXNwbGF5OmJsb2NrO2QCBw9kFgJmDxUFAAIxMwAAAjEyZAIJD2QWAmYPFQUNZGlzcGxheTpub25lOwEwAAABMGQCCw9kFgJmDxUBBzEwOC4zMyVkAgwPFQQACjAxLzEzLzIwMTQKMDEvMTMvMjAxNANZZXNkAg0PPCsACQEADxYEHxIWAB8PZmRkAgYPZBYWZg8VAQE2ZAIBDxYCHxAFUFRvZ2dsZUdyYWRlYm9va0luZm8odGhpcywgJ2N0bDAwX01haW5Db250ZW50X3N1YkdCU19EYXRhRGV0YWlsc19jdGwwNl90YmxNb3JlJyk7ZAICDxUBC1Rlc3QgVW5pdCAxZAIDD2QWBmYPZBYCAgEPZBYCZg8VAQowMS8xNy8yMDE0ZAIBD2QWAgIBD2QWAmYPFQEAZAICD2QWAgIBD2QWAmYPFQEAZAIEDxUCCVN1bW1hdGl2ZQRUZXN0ZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAAjg3AAADMTAwZAIJD2QWAmYPFQUNZGlzcGxheTpub25lOwEwAAABMGQCCw9kFgJmDxUBBjg3LjAwJWQCDA8VBAAKMDEvMTcvMjAxNAowMS8xNy8yMDE0A1llc2QCDQ88KwAJAQAPFgQfEhYAHw9mZGQCBw9kFhZmDxUBATdkAgEPFgIfEAVQVG9nZ2xlR3JhZGVib29rSW5mbyh0aGlzLCAnY3RsMDBfTWFpbkNvbnRlbnRfc3ViR0JTX0RhdGFEZXRhaWxzX2N0bDA3X3RibE1vcmUnKTtkAgIPFQEJQ1cgVW5pdCAyZAIDD2QWBmYPZBYCAgEPZBYCZg8VAQowMS8yMC8yMDE0ZAIBD2QWAgIBD2QWAmYPFQEAZAICD2QWAgIBD2QWAmYPFQEAZAIEDxUCCVN1bW1hdGl2ZQlDbGFzc3dvcmtkAgUPFgIfEWgWAmYPFQEOZGlzcGxheTpibG9jaztkAgcPZBYCZg8VBQABNAAAAThkAgkPZBYCZg8VBQ1kaXNwbGF5Om5vbmU7ATAAAAEwZAILD2QWAmYPFQEGNTAuMDAlZAIMDxUEAAowMS8yNC8yMDE0CjAxLzI0LzIwMTQDWWVzZAINDzwrAAkBAA8WBB8SFgAfD2ZkZAIID2QWFmYPFQEBOGQCAQ8WAh8QBVBUb2dnbGVHcmFkZWJvb2tJbmZvKHRoaXMsICdjdGwwMF9NYWluQ29udGVudF9zdWJHQlNfRGF0YURldGFpbHNfY3RsMDhfdGJsTW9yZScpO2QCAg8VAQlIVyBVbml0IDJkAgMPZBYGZg9kFgICAQ9kFgJmDxUBCjAxLzIwLzIwMTRkAgEPZBYCAgEPZBYCZg8VAQBkAgIPZBYCAgEPZBYCZg8VAQBkAgQPFQIJU3VtbWF0aXZlCEhvbWV3b3JrZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAAjE2AAACMTZkAgkPZBYCZg8VBQ1kaXNwbGF5Om5vbmU7ATAAAAEwZAILD2QWAmYPFQEHMTAwLjAwJWQCDA8VBAAKMDEvMjQvMjAxNAowMS8yNC8yMDE0A1llc2QCDQ88KwAJAQAPFgQfEhYAHw9mZGQCCQ9kFhZmDxUBATlkAgEPFgIfEAVQVG9nZ2xlR3JhZGVib29rSW5mbyh0aGlzLCAnY3RsMDBfTWFpbkNvbnRlbnRfc3ViR0JTX0RhdGFEZXRhaWxzX2N0bDA5X3RibE1vcmUnKTtkAgIPFQEJQ1cgVW5pdCAyZAIDD2QWBmYPZBYCAgEPZBYCZg8VAQowMS8yNy8yMDE0ZAIBD2QWAgIBD2QWAmYPFQEAZAICD2QWAgIBD2QWAmYPFQEAZAIEDxUCCVN1bW1hdGl2ZQlDbGFzc3dvcmtkAgUPFgIfEWgWAmYPFQENZGlzcGxheTpub25lO2QCBw9kFgJmDxUFABImbmJzcDsmbmJzcDsmbmJzcDsAAAE4ZAIJD2QWAmYPFQUNZGlzcGxheTpub25lOxImbmJzcDsmbmJzcDsmbmJzcDsAAAEwZAILD2QWAmYPFQEAZAIMDxUEAAAKMDEvMzEvMjAxNAJOb2QCDQ88KwAJAQAPFgQfEhYAHw9mZGQCCg9kFhZmDxUBAjEwZAIBDxYCHxAFUFRvZ2dsZUdyYWRlYm9va0luZm8odGhpcywgJ2N0bDAwX01haW5Db250ZW50X3N1YkdCU19EYXRhRGV0YWlsc19jdGwxMF90YmxNb3JlJyk7ZAICDxUBCUhXIFVuaXQgMmQCAw9kFgZmD2QWAgIBD2QWAmYPFQEKMDEvMjcvMjAxNGQCAQ9kFgICAQ9kFgJmDxUBAGQCAg9kFgICAQ9kFgJmDxUBAGQCBA8VAglTdW1tYXRpdmUISG9tZXdvcmtkAgUPFgIfEWgWAmYPFQENZGlzcGxheTpub25lO2QCBw9kFgJmDxUFABImbmJzcDsmbmJzcDsmbmJzcDsAAAIyMGQCCQ9kFgJmDxUFDWRpc3BsYXk6bm9uZTsSJm5ic3A7Jm5ic3A7Jm5ic3A7AAABMGQCCw9kFgJmDxUBAGQCDA8VBAAACjAxLzMxLzIwMTQCTm9kAg0PPCsACQEADxYEHxIWAB8PZmRkAgsPZBYWZg8VAQIxMWQCAQ8WAh8QBVBUb2dnbGVHcmFkZWJvb2tJbmZvKHRoaXMsICdjdGwwMF9NYWluQ29udGVudF9zdWJHQlNfRGF0YURldGFpbHNfY3RsMTFfdGJsTW9yZScpO2QCAg8VAQtRdWl6IFVuaXQgMmQCAw9kFgZmD2QWAgIBD2QWAmYPFQEKMDEvMjAvMjAxNGQCAQ9kFgICAQ9kFgJmDxUBAGQCAg9kFgICAQ9kFgJmDxUBAGQCBA8VAglTdW1tYXRpdmUEUXVpemQCBQ8WAh8RaBYCZg8VAQ5kaXNwbGF5OmJsb2NrO2QCBw9kFgJmDxUFAAE4AAACMTRkAgkPZBYCZg8VBQ1kaXNwbGF5Om5vbmU7ATAAAAEwZAILD2QWAmYPFQEGNTcuMTQlZAIMDxUEAAowMS8yOC8yMDE0CjAxLzI4LzIwMTQDWWVzZAINDzwrAAkBAA8WBB8SFgAfD2ZkZAIMD2QWFmYPFQECMTJkAgEPFgIfEAVQVG9nZ2xlR3JhZGVib29rSW5mbyh0aGlzLCAnY3RsMDBfTWFpbkNvbnRlbnRfc3ViR0JTX0RhdGFEZXRhaWxzX2N0bDEyX3RibE1vcmUnKTtkAgIPFQEGVW5pdCAyZAIDD2QWBmYPZBYCAgEPZBYCZg8VAQowMS8zMS8yMDE0ZAIBD2QWAgIBD2QWAmYPFQEAZAICD2QWAgIBD2QWAmYPFQEAZAIEDxUCCVN1bW1hdGl2ZQRUZXN0ZAIFDxYCHxFoFgJmDxUBDmRpc3BsYXk6YmxvY2s7ZAIHD2QWAmYPFQUAAjkzAAADMTAwZAIJD2QWAmYPFQUNZGlzcGxheTpub25lOwEwAAABMGQCCw9kFgJmDxUBBjkzLjAwJWQCDA8VBAAKMDEvMzEvMjAxNAowMS8zMS8yMDE0A1llc2QCDQ88KwAJAQAPFgQfEhYAHw9mZGQCDw8WAh8PAgYWDmYPZBYMAgcPFgIeBWNsYXNzBQZIQ2VsbFJkAgkPFgIfEWhkAgsPFgIfEWhkAg0PFgIfEWhkAg8PFgIfEWhkAhEPFgIfEWhkAgEPZBYUAgMPZBYCZg8VAQlDbGFzc3dvcmtkAgUPZBYCZg8VAQQ0LjAwZAIHD2QWAmYPFQEFMjMuMDBkAgkPZBYCZg8VAQIyOGQCCw9kFgJmDxUCBTgyLjE0ASVkAg0PFgIfEWgWAmYPFQEEMC4wMGQCDw8WAh8RaBYCZg8VAQEwZAIRDxYCHxFoFgJmDxUCBDAuMDABJWQCEw8WAh8RaBYCZg8VAgQwLjAwASVkAhUPFgIfEWgWAmYPFQEAZAICD2QWFAIDD2QWAmYPFQEFRmluYWxkAgUPZBYCZg8VAQUyMC4wMGQCBw9kFgJmDxUBBDAuMDBkAgkPZBYCZg8VAQEwZAILD2QWAmYPFQIEMC4wMAElZAINDxYCHxFoFgJmDxUBBDAuMDBkAg8PFgIfEWgWAmYPFQEBMGQCEQ8WAh8RaBYCZg8VAgQwLjAwASVkAhMPFgIfEWgWAmYPFQIEMC4wMAElZAIVDxYCHxFoFgJmDxUBAGQCAw9kFhQCAw9kFgJmDxUBCEhvbWV3b3JrZAIFD2QWAmYPFQEENi4wMGQCBw9kFgJmDxUBBTUwLjAwZAIJD2QWAmYPFQECNTJkAgsPZBYCZg8VAgU5Ni4xNQElZAINDxYCHxFoFgJmDxUBBDAuMDBkAg8PFgIfEWgWAmYPFQEBMGQCEQ8WAh8RaBYCZg8VAgQwLjAwASVkAhMPFgIfEWgWAmYPFQIEMC4wMAElZAIVDxYCHxFoFgJmDxUBAGQCBA9kFhQCAw9kFgJmDxUBBFF1aXpkAgUPZBYCZg8VAQQyLjAwZAIHD2QWAmYPFQEFMjEuMDBkAgkPZBYCZg8VAQIyNmQCCw9kFgJmDxUCBTgwLjc2ASVkAg0PFgIfEWgWAmYPFQEEMC4wMGQCDw8WAh8RaBYCZg8VAQEwZAIRDxYCHxFoFgJmDxUCBDAuMDABJWQCEw8WAh8RaBYCZg8VAgQwLjAwASVkAhUPFgIfEWgWAmYPFQEAZAIFD2QWFAIDD2QWAmYPFQEEVGVzdGQCBQ9kFgJmDxUBBTY4LjAwZAIHD2QWAmYPFQEGMTgwLjAwZAIJD2QWAmYPFQEDMjAwZAILD2QWAmYPFQIFOTAuMDABJWQCDQ8WAh8RaBYCZg8VAQQwLjAwZAIPDxYCHxFoFgJmDxUBATBkAhEPFgIfEWgWAmYPFQIEMC4wMAElZAITDxYCHxFoFgJmDxUCBDAuMDABJWQCFQ8WAh8RaBYCZg8VAQBkAgYPZBYYAgEPFgIfEwUFTmV3VExkAgMPFgIfEwUGSENlbGxMFgJmDxUBBVRvdGFsZAIFDxYEHxMFBkhDZWxsTB8CZWQCBw8WBB8TBQZIQ2VsbEwfAmVkAgkPFgQfEwUGSENlbGxMHwJlZAILDxYCHxMFBkhDZWxsUhYCZg8VAgU4OS44MwElZAINDxYCHxFoFgJmDxUBBDAuMDBkAg8PFgIfEWgWAmYPFQEBMGQCEQ8WAh8RaBYCZg8VAgQwLjAwASVkAhMPFgIfEWgWAmYPFQIEMC4wMAElZAIVDxYCHxFoFgJmDxUBAGQCFw8WAh8TBQVOZXdUUmQCEQ8PFgIfAGVkZAIVDw8WAh8ABQk1LjE0LjEuMjNkZAIXDw8WAh8ABTpDb3B5cmlnaHQgJmNvcHk7IDIwMTQgRWFnbGUgU29mdHdhcmUuIEFsbCBSaWdodHMgUmVzZXJ2ZWQuZGQYAgUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFMWN0bDAwJE1haW5Db250ZW50JHN1YkdCUyRjaGtNaXNzaW5nQXNzaWdubWVudE9ubHkFJ2N0bDAwJE1haW5Db250ZW50JHN1YlN0dVRvcCREYXRhRGV0YWlscw8UKwAHZGRkZBUCAlNDAlNOFgIPBQJTQwEhAA8FAlNOAq2gAgIBZLb6HZTZYO9GKuv/+2bK9MZtMQGX";

	ProgressDialog progressDialog;
	Activity activity;
	String error = "An unknown error occurred while loading your classes"; //This text should never appear, its the default
	ArrayList<SchoolClass> grades;
	AeriesManager aeriesManager;

	public GradesDetailTask(Activity activity, AeriesManager aeriesManager) {
		this.activity = activity;
		this.aeriesManager = aeriesManager;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Loading class details. Please Wait");
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	protected Void doInBackground(SchoolClass... schoolClasses) {
		try {
			//Navigate to the grades_detail page, used to rip the class IDs for further navigation
			HttpResponse response_main = aeriesManager.client.execute(new HttpGet(AeriesManager.GRADES_DETAIL));

			Document doc_main = Jsoup.parse(response_main.getEntity().getContent(), null, AeriesManager.GRADES_DETAIL);
			Elements options = doc_main.select("#ctl00_MainContent_subGBS_dlGN").first().children();

			for(Element option: options) {
				String stupidTitle = option.text();
				String stupidId = option.attr("value");
				if(!stupidTitle.startsWith("<<")) {
					for(SchoolClass schoolClass: schoolClasses) {
						if(stupidTitle.matches("(.*)" + schoolClass.className + "(.*)")) {
							schoolClass.aeriesID = stupidId;
						}
					}
				}
			}
			for(int i=0;i<schoolClasses.length;i++) {
				if(schoolClasses[i].aeriesID == null) {
					Log.d("ASSIGNAMEEGTSSTUPID",schoolClasses[i].className);
					continue; //Skip classes where we don't know the id, this shouldn't happen in theory
				}
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("ctl00$MainContent$subGBS$dlGN", schoolClasses[i].aeriesID));
				nvps.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$subGBS$dlGN"));
				//nvps.add(new BasicNameValuePair("__ASYNCPOST", "true")); //This is only needed to get the <select> element with ID listings
				nvps.add(new BasicNameValuePair("__VIEWSTATE", VIEW_STATE_POST)); //This is some stupid constant

				HttpPost request = new HttpPost(AeriesManager.GRADES_DETAIL);
				request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = aeriesManager.client.execute(request);
				Document doc = Jsoup.parse(response.getEntity().getContent(), null, request.getURI().toString());
				String table_locator = "div#ctl00_MainContent_subGBS_upEverything table table tr";
				Elements rows = doc.select(table_locator);
				Log.d("TheOneTableToRuleThemAll", ":" + rows.size());
				if(rows == null) {
					//No data was loaded, calling it quits here and posting a dialog explaining why
					error = "An error occurred, Aeries might be down";
					cancel(true);
					return null;
				}
				for(int j=0;j<rows.size();j++) {
					Element row = rows.get(j);
					if(!row.className().equals("SubHeaderRow")) {
						continue; //Skip the headers
					}
					Assignment assign = new Assignment();
					assign.description = row.children().get(3).text();
					Log.d("ASSIGNAMEEGTSSTUPID",assign.description);
					schoolClasses[i].assignments.add(assign);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//((OHSApplication)activity.getApplication()).aeriesManager.setSchoolClasses(grades);
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		aeriesManager.errorLoadingGrades(error);
	}

	@Override
	protected void onPostExecute(Void v) {
		super.onPostExecute(v);
		if(isCancelled()) {
			onCancelled();
			return;
		}
		progressDialog.dismiss();
	}
	public void inflateList(final Activity act) {
		final ArrayAdapter adapter = new GradesArrayAdapter(activity, R.layout.grades_list_item, aeriesManager.grades);
		final ListView listview = (ListView) act.findViewById(R.id.grades_detail_assign_list);
		listview.setAdapter(adapter);
		/*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent gradeDetailIntent = new Intent(act, GradesDetailActivity.class);
				gradeDetailIntent.putExtra("schoolClassId",arg2);
				act.startActivity(gradeDetailIntent);
			}

		});*/
	}
}
