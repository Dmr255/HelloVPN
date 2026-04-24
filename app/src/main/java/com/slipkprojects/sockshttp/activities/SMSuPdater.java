package com.slipkprojects.sockshttp.activities;

import com.magicvpn.net.*;

import com.slipkprojects.sockshttp.*;
import android.preference.*;
import android.content.*;
import android.app.*;
import android.media.*;
import org.json.*;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.os.*;
import com.slipkprojects.sockshttp.util.*;

import org.json.JSONObject;
import android.graphics.*;

public class SMSuPdater
{
	private SharedPreferences pref;
	private SweetAlertDialog pDialog;



	public SMSuPdater(final SocksHttpMainActivity h){
		pref = PreferenceManager.getDefaultSharedPreferences(h);
		new SMS_Updater(h, new SMS_Updater.Listener() {
				@Override
				public void onCompleted(final String SMS)
				{

					try
					{
						JSONObject sms_js = new JSONObject(SMS);
						if (sms_js.getInt("SendMessage") == pref.getInt(Utils.CurrentSMSVersion, 0))
						{
						}
						else
						{
							pref.edit().putBoolean("firstStartSMS", true).commit();
							if (new Boolean(pref.getBoolean("firstStartSMS", true)).booleanValue())
							{
								StringBuffer sb = new StringBuffer();
								sb.append(sms_js.getString("MyMessage"));

								NotificationManager notificationManager = (NotificationManager) h.getSystemService(Context.NOTIFICATION_SERVICE); 
								Notification.Builder notification = new Notification.Builder(h);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
									notification.setChannelId(h.getPackageName() + ".smsupdater");
									createNotificationChannel(notificationManager, h.getPackageName() + ".smsupdater");
								}
								notification.setStyle(new Notification.BigTextStyle(notification)
													  .bigText(sb.toString())
													  .setBigContentTitle("ATENCIÓN")
													  .setSummaryText("INFORMA"))
									.setContentTitle("¡¡¡Nuevo mensaje!!!")
									.setContentText(sb.toString())
									.setDefaults(Notification.DEFAULT_ALL)
									.setPriority(Notification.PRIORITY_HIGH)
									.setSmallIcon(R.drawable.ic_launcher);
								notificationManager.notify(4129,notification.getNotification());


								pDialog = new SweetAlertDialog(h, SweetAlertDialog.WARNING_TYPE);
								pDialog.setTitleText("BIENVENIDO");
								pDialog.setContentText(sb.toString());
								pDialog.setConfirmText("OK");
								pDialog.setCancelable(true);
								pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(SweetAlertDialog sDialog) {

											try
											{
												JSONObject sms_obj = new JSONObject(SMS);
												pref.edit().putInt(Utils.CurrentSMSVersion, sms_obj.getInt("SendMessage")).commit();
												pref.edit().putBoolean("firstStartSMS", false).commit();
											}
											catch (Exception e){
											}
											sDialog.dismiss();
										}
									});
								pDialog.show();
							}
						}
					}
					catch (Exception e)
					{
					}
				}



				@Override
				public void onCancelled()
				{

				}

				@Override
				public void onException(String ex)
				{

				}
			}).execute();
	}

	private void createNotificationChannel(NotificationManager notificationManager, String id)
	{
		NotificationChannel mChannel = new NotificationChannel(id, "SMSuUpdater Notification", NotificationManager.IMPORTANCE_HIGH);
		mChannel.setShowBadge(true);
		notificationManager.createNotificationChannel(mChannel);
		// TODO: Implement this method
	}
}

