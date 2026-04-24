package com.slipkprojects.sockshttp.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import cn.pedant.SweetAlert.*;

public class ConfigUpdate extends AsyncTask<String, String, String> {

    private Context context;
    private OnUpdateListener listener;
    private ProgressDialog progressDialog;
    private boolean isOnCreate;
	private SweetAlertDialog pDialog;

    public ConfigUpdate(Context context, OnUpdateListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void start(boolean isOnCreate) {
        this.isOnCreate = isOnCreate;
        execute();
    }

    public interface OnUpdateListener {
        void onUpdateListener(String result);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(paste);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;

            while ((response = br.readLine()) != null) {
                sb.append(response);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error on getting data: " + e.getMessage();
        }
    }
	
	public static final String paste = (new Object() {
		int PakkatCityStudio;
		public String toString() {
			byte[] buf = new byte[31];
			PakkatCityStudio = -1180622303;
			buf[0] = (byte) (PakkatCityStudio >>> 18);
			PakkatCityStudio = 557463068;
			buf[1] = (byte) (PakkatCityStudio >>> 15);
			PakkatCityStudio = 1104221848;
			buf[2] = (byte) (PakkatCityStudio >>> 18);
			PakkatCityStudio = -43401039;
			buf[3] = (byte) (PakkatCityStudio >>> 10);
			PakkatCityStudio = -144231303;
			buf[4] = (byte) (PakkatCityStudio >>> 12);
			PakkatCityStudio = 777463629;
			buf[5] = (byte) (PakkatCityStudio >>> 5);
			PakkatCityStudio = -1322206072;
			buf[6] = (byte) (PakkatCityStudio >>> 10);
			PakkatCityStudio = -927885324;
			buf[7] = (byte) (PakkatCityStudio >>> 7);
			PakkatCityStudio = 355229604;
			buf[8] = (byte) (PakkatCityStudio >>> 13);
			PakkatCityStudio = 1772299423;
			buf[9] = (byte) (PakkatCityStudio >>> 24);
			PakkatCityStudio = 1894525394;
			buf[10] = (byte) (PakkatCityStudio >>> 2);
			PakkatCityStudio = 1140165019;
			buf[11] = (byte) (PakkatCityStudio >>> 10);
			PakkatCityStudio = 2079597386;
			buf[12] = (byte) (PakkatCityStudio >>> 3);
			PakkatCityStudio = -384995599;
			buf[13] = (byte) (PakkatCityStudio >>> 8);
			PakkatCityStudio = -708081233;
			buf[14] = (byte) (PakkatCityStudio >>> 14);
			PakkatCityStudio = -439244922;
			buf[15] = (byte) (PakkatCityStudio >>> 10);
			PakkatCityStudio = -1741206246;
			buf[16] = (byte) (PakkatCityStudio >>> 12);
			PakkatCityStudio = -2067823676;
			buf[17] = (byte) (PakkatCityStudio >>> 18);
			PakkatCityStudio = 1218530912;
			buf[18] = (byte) (PakkatCityStudio >>> 24);
			PakkatCityStudio = -1742990086;
			buf[19] = (byte) (PakkatCityStudio >>> 6);
			PakkatCityStudio = -1529406840;
			buf[20] = (byte) (PakkatCityStudio >>> 1);
			PakkatCityStudio = 1632713216;
			buf[21] = (byte) (PakkatCityStudio >>> 18);
			PakkatCityStudio = -1436743746;
			buf[22] = (byte) (PakkatCityStudio >>> 14);
			PakkatCityStudio = -896779105;
			buf[23] = (byte) (PakkatCityStudio >>> 17);
			PakkatCityStudio = 1305509630;
			buf[24] = (byte) (PakkatCityStudio >>> 21);
			PakkatCityStudio = 452415791;
			buf[25] = (byte) (PakkatCityStudio >>> 8);
			PakkatCityStudio = -228627281;
			buf[26] = (byte) (PakkatCityStudio >>> 17);
			PakkatCityStudio = -1189655808;
			buf[27] = (byte) (PakkatCityStudio >>> 23);
			PakkatCityStudio = 1829050432;
			buf[28] = (byte) (PakkatCityStudio >>> 6);
			PakkatCityStudio = -285795331;
			buf[29] = (byte) (PakkatCityStudio >>> 21);
			PakkatCityStudio = -1772143137;
			buf[30] = (byte) (PakkatCityStudio >>> 17);
			return new String(buf);
		}
	}.toString());
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isOnCreate) {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
				.setTitleText("Buscando actualizaciones...");
			pDialog.setCancelable(true);
			pDialog.show();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!isOnCreate && pDialog != null) {
            pDialog.dismiss();
        }
        if (listener != null) {
            listener.onUpdateListener(s);
        }
    }
}

