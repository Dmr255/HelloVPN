package com.slipkprojects.sockshttp;

import android.content.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.net.*;
import cn.pedant.SweetAlert.*;

public class AutoAppUpdateChecker extends AsyncTask<String, String, String> {

    private Context context;
    private Listener listener;
	private String url;

	private boolean isOnCreate;
	private SweetAlertDialog pDialog;

	public interface OnUpdateListener {
        void onUpdateListener(String result);
    }

    public interface Listener {
        void onLoading();

        void onCompleted(String config) throws Exception;
        void onCancelled();
        void onException(String ex);
    }

    public AutoAppUpdateChecker(Context context, String url,Listener listener) {
        this.context = context;
        this.url = url;
        this.listener = listener;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        if (!isOnCreate) {
//            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
//				.setTitleText("Buscando actualizaciones...");
//			pDialog.setCancelable(true);
//			pDialog.show();
//        }
//    }



    @Override
    protected void onPostExecute(String result) {

        try {
			if (result.equals("error")) {
				listener.onException(result);

			}
			else {
				listener.onCompleted(result);
			}

			if (!isOnCreate && pDialog != null) {
				pDialog.dismiss();
			}
		}

		catch (Exception e){
			listener.onException(e.getMessage());
		}
    }

	@Override
    protected void onCancelled() {
        super.onCancelled();

        listener.onCancelled();
    }

	// Start Fixed by Awoo
    @Override
    protected String doInBackground(String... urlArg) {
		StringBuilder sb = new StringBuilder();
		try {
			String api = url;
            if(!api.startsWith("http")) {
                api = new StringBuilder().append("http://").append(url).toString();
            }

			URL url = new URL(api);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setReadTimeout(30000);
			httpURLConnection.setDoInput(true);
			httpURLConnection.connect();

			InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(isr);	
            while (true) {
				String readLine = bufferedReader.readLine();
				if (readLine == null) {
					break;
				}
				sb.append(readLine);
			}

			bufferedReader.close();
			httpURLConnection.disconnect();
			if (httpURLConnection != null) {
				try {
					httpURLConnection.disconnect();
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
            return "Error on getting data: " + e.getMessage();
		}
	}
	// end of fixed line

}


