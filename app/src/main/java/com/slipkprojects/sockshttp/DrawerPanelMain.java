package com.slipkprojects.sockshttp;

import com.magicvpn.net.*;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.content.pm.PackageInfo;
import com.slipkprojects.sockshttp.util.Utils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.Build;
import android.content.Intent;
import android.net.Uri;
import com.slipkprojects.sockshttp.util.GoogleFeedbackUtils;
import com.slipkprojects.ultrasshservice.logger.SkStatus;
import android.support.v4.view.GravityCompat;
import com.slipkprojects.sockshttp.activities.ConfigGeralActivity;
import com.slipkprojects.sockshttp.activities.AboutActivity;

public class DrawerPanelMain
	
{
	private AppCompatActivity mActivity;
	
	public DrawerPanelMain(AppCompatActivity activity) {
		mActivity = activity;
	}
	

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle toggle;

	public void setDrawer(Toolbar toolbar) {
		//NavigationView drawerNavigationView = (NavigationView) mActivity.findViewById(R.id.drawerNavigationView);
		drawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawerLayoutMain);

		// set drawer
		toggle = new ActionBarDrawerToggle(mActivity,
			drawerLayout, toolbar, R.string.open, R.string.cancel);

        drawerLayout.setDrawerListener(toggle);

		toggle.syncState();

		// set app info
		PackageInfo pinfo = Utils.getAppInfo(mActivity);
		if (pinfo != null) {
			String version_nome = pinfo.versionName;
			int version_code = pinfo.versionCode;
			String header_text = String.format("v. %s (%d)", version_nome, version_code);

			//View view = drawerNavigationView.getHeaderView(0);

			//TextView app_info_text = view.findViewById(R.id.nav_headerAppVersion);
			//app_info_text.setText(header_text);
		}

		// set navigation view
		
	}
	
	public ActionBarDrawerToggle getToogle() {
		return toggle;
	}
	
	public DrawerLayout getDrawerLayout() {
		return drawerLayout;
	}
	

	
}
