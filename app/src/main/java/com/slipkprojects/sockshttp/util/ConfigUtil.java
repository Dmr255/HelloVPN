package com.slipkprojects.sockshttp.util;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ConfigUtil {

    Context context;

    public ConfigUtil(Context context) {
        this.context = context;
    }

    public String getVersion() {
        try {
            String version = getJSONConfig().getString("Version");
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getServersArray() {
        try {
            if (getJSONConfig() != null) {
                JSONArray array = getJSONConfig().getJSONArray("Servers");
                return array;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getNetworksArray() {
        try {
            if (getJSONConfig() != null) {
                JSONArray array = getJSONConfig().getJSONArray("Networks");
                return array;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean versionCompare(String NewVersion, String OldVersion) {
        String[] vals1 = NewVersion.split("\\.");
        String[] vals2 = OldVersion.split("\\.");
        int i = 0;

        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
 
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff) > 0;
        }
		
        return Integer.signum(vals1.length - vals2.length) > 0;
    }

    private JSONObject getJSONConfig() {
        try {
            File file = new File(context.getFilesDir(), "Config.json");
            if (file.exists()) {
                String json_file = readStream(new FileInputStream(file));
                String json = AESCrypt.decrypt(str, json_file);
                return new JSONObject(json);
            } else {
                InputStream inputStream = context.getAssets().open("config/config.json");
                String json = AESCrypt.decrypt(str, readStream(inputStream));
                return new JSONObject(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public static final String str = (new Object() {
		int PakkatCityStudio;
		public String toString() {
			byte[] buf = new byte[12];
			PakkatCityStudio = 1406984763;
			buf[0] = (byte) (PakkatCityStudio >>> 14);
			PakkatCityStudio = -1519194375;
			buf[1] = (byte) (PakkatCityStudio >>> 16);
			PakkatCityStudio = -2004917461;
			buf[2] = (byte) (PakkatCityStudio >>> 8);
			PakkatCityStudio = 898916211;
			buf[3] = (byte) (PakkatCityStudio >>> 8);
			PakkatCityStudio = 881516687;
			buf[4] = (byte) (PakkatCityStudio >>> 9);
			PakkatCityStudio = -593869763;
			buf[5] = (byte) (PakkatCityStudio >>> 22);
			PakkatCityStudio = 461268384;
			buf[6] = (byte) (PakkatCityStudio >>> 19);
			PakkatCityStudio = -763513542;
			buf[7] = (byte) (PakkatCityStudio >>> 7);
			PakkatCityStudio = -655992461;
			buf[8] = (byte) (PakkatCityStudio >>> 6);
			PakkatCityStudio = 1739157902;
			buf[9] = (byte) (PakkatCityStudio >>> 2);
			PakkatCityStudio = -1192660822;
			buf[10] = (byte) (PakkatCityStudio >>> 17);
			PakkatCityStudio = 216411225;
			buf[11] = (byte) (PakkatCityStudio >>> 17);
			return new String(buf);
		}
	}.toString());
	
    private String readStream(InputStream in)
    {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in));
            char[] buff = new char[1024];
            while (true) {
                int read = reader.read(buff, 0, buff.length);
                if (read <= 0) {
                    break;
                }
                sb.append(buff, 0, read);
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }
}
