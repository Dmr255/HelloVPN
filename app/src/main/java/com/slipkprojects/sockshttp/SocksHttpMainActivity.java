package com.slipkprojects.sockshttp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sdsmdg.tastytoast.TastyToast;
import com.slipkprojects.sockshttp.DrawerLog;
import com.slipkprojects.sockshttp.SocksHttpApp;
import com.slipkprojects.sockshttp.activities.BaseActivity;
import com.slipkprojects.sockshttp.activities.ConfigGeralActivity;
import com.slipkprojects.sockshttp.activities.SMSuPdater;
import com.slipkprojects.sockshttp.adapter.SpinnerAdapter;
import com.slipkprojects.sockshttp.fragments.ProxyRemoteDialogFragment;
import com.slipkprojects.sockshttp.util.AESCrypt;
import com.slipkprojects.sockshttp.util.ConfigUpdate;
import com.slipkprojects.sockshttp.util.ConfigUtil;
import com.slipkprojects.sockshttp.util.KillThis;
import com.slipkprojects.sockshttp.util.Utils;
import com.slipkprojects.ultrasshservice.LaunchVpn;
import com.slipkprojects.ultrasshservice.config.ConfigParser;
import com.slipkprojects.ultrasshservice.config.PasswordCache;
import com.slipkprojects.ultrasshservice.config.Settings;
import com.slipkprojects.ultrasshservice.logger.ConnectionStatus;
import com.slipkprojects.ultrasshservice.logger.SkStatus;
import com.slipkprojects.ultrasshservice.tunnel.TunnelManagerHelper;
import com.slipkprojects.ultrasshservice.tunnel.TunnelUtils;
import com.slipkprojects.ultrasshservice.util.CustomNativeLoader;
import com.slipkprojects.ultrasshservice.util.SkProtect;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import jomar.dev.BottomNavigationViewNew;
import com.magicvpn.net.BuildConfig;
import com.magicvpn.net.R;
import org.json.JSONException;
import org.json.JSONObject;
import me.dawson.proxyserver.ui.ProxySettings;

import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;
import android.content.DialogInterface;

import android.support.design.internal.NavigationMenu;
import io.github.yavski.fabspeeddial.*;

/**
 * Activity Principal
 * @author SlipkHunter
 */

public class SocksHttpMainActivity extends BaseActivity
implements DrawerLayout.DrawerListener,
View.OnClickListener, RadioGroup.OnCheckedChangeListener,
CompoundButton.OnCheckedChangeListener, SkStatus.StateListener
{
	private static final String UPDATE_VIEWS = "MainUpdate";
	public static final String OPEN_LOGS = "com.slipkprojects.sockshttp:openLogs";

	public static String base_update = new String(new byte[]{104,116,116,112,115,58,47,47,98,105,116,98,105,110,46,105,116,47,81,70,116,69,78,76,88,72,47,114,97,119,47});
	
	//private static final String DNS_BIN = "libstartdns"; //slow
	//private Process dnsProcess; //slow
	//private File filedns; //slow

	private Chronometer chronometer; //timer
	private long pauseOffset; //timer
    private boolean running; //timer

	private TextView status; //status
	
	public static int PICK_FILE = 1; // Update offline
	
	public String versionName; // Update Online App
	
	private Button loginz;
    private Button cancelz;
	private Button closez;
	
	private SweetAlertDialog nops;
	
	private DrawerLog mDrawer;
	private Settings mConfig;
	private Toolbar toolbar_main;
	private Handler mHandler;
	private LinearLayout mainLayout;
	private LinearLayout loginLayout;
	private LinearLayout proxyInputLayout;
	private TextView proxyText;
	private RadioGroup metodoConexaoRadio;
	private LinearLayout payloadLayout;
	private TextInputEditText payloadEdit;
	private SwitchCompat customPayloadSwitch;
	private Button starterButton;

	private ImageButton inputPwShowPass;
	private TextInputEditText inputPwUser;
	private TextInputEditText inputPwPass;

	private LinearLayout configMsgLayout;
	private TextView configMsgText;

	private AdView adsBannerView;

	private ConfigUtil config;

	private Spinner serverSpinner;

	private SpinnerAdapter serverAdapter;

	private ArrayList<JSONObject> serverList;

	private BottomNavigationViewNew.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
	= new BottomNavigationViewNew.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {

				case R.id.updater:
					updater();
					return true;
			
				case R.id.login1:
					askForPW(R.string.password);
					return true;

				case R.id.settings:
					Intent intentSettings = new Intent(SocksHttpMainActivity.this, ConfigGeralActivity.class);
					startActivity(intentSettings);
					return true;	

                case R.id.about1:
                    aboutme();
					return true;	

				case R.id.logs:
					showLogWindow();
					return true;
					
				default:
					//Snackbar.make(tabs, "Coming Soon! 😂", 0).show();
					return true;

			}


		}

	};

	private void updater(){
		
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.updater, (ViewGroup) null);
        closez = (Button) inflate.findViewById(R.id.close);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        final AlertDialog alert = builer.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        closez.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
	}
	
	private void aboutme(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.aboutme, (ViewGroup) null);
        closez = (Button) inflate.findViewById(R.id.close);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(inflate);
        final AlertDialog alert = builer.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.show();
        closez.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.cancel();
                }
            });
	}
	
// Contador dados >>>
	
	private long mStartRX = 0;
    private long mStartTX = 0;
	private Handler mHandler2 = new Handler();
	private final Runnable mRunnable = new Runnable() {
		
        public void run() {
            TextView textView = (TextView) SocksHttpMainActivity.this.findViewById(R.id.RX);
            TextView textView2 = (TextView) SocksHttpMainActivity.this.findViewById(R.id.TX);
            long totalRxBytes = TrafficStats.getTotalRxBytes();
            long totalRxBytes2 = TrafficStats.getTotalRxBytes() - SocksHttpMainActivity.this.mStartRX;
            String str = " bytes";
            textView.setText(Long.toString(totalRxBytes2) + str);
            String gb = " GB";
            String mb = " MB";
            String kb = " KB";
            if (totalRxBytes2 >= 1024) {
                totalRxBytes2 /= 1024;
                textView.setText(Long.toString(totalRxBytes2) + kb);
                if (totalRxBytes2 >= 1024) {
                    totalRxBytes2 /= 1024;
                    textView.setText(Long.toString(totalRxBytes2) + mb);
                    if (totalRxBytes2 >= 1024) {
                        textView.setText(Long.toString(totalRxBytes2 / 1024) + gb);
                    }
                }
            }
            SocksHttpMainActivity.this.mStartRX = totalRxBytes;
            totalRxBytes = TrafficStats.getTotalTxBytes();
            totalRxBytes2 = TrafficStats.getTotalTxBytes() - SocksHttpMainActivity.this.mStartTX;
            textView2.setText(Long.toString(totalRxBytes2) + str);
            if (totalRxBytes2 >= 1024) {
                totalRxBytes2 /= 1024;
                textView2.setText(Long.toString(totalRxBytes2) + kb);
                if (totalRxBytes2 >= 1024) {
                    totalRxBytes2 /= 1024;
                    textView2.setText(Long.toString(totalRxBytes2) + mb);
                    if (totalRxBytes2 >= 1024) {
                        textView2.setText(Long.toString(totalRxBytes2 / 1024) + gb);
                    }
                }
            }
            SocksHttpMainActivity.this.mStartTX = totalRxBytes;
            SocksHttpMainActivity.this.mHandler2.postDelayed(SocksHttpMainActivity.this.mRunnable, 1000);
        }
    };

// Contador dados <<<
	
	@Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
		new SMSuPdater(this); // SMS UPDATE
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getCanonicalName());
        wakelock.acquire();
		startNetworkBroadcastReceiver(this);

        super.onCreate(savedInstanceState);
		
		
// Contador dados >>>
		
		this.mStartRX = TrafficStats.getTotalRxBytes();
        long totalTxBytes = TrafficStats.getTotalTxBytes();
        this.mStartTX = totalTxBytes;
        if (this.mStartRX == -1 || totalTxBytes == -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Uh Oh!");
            builder.setMessage((CharSequence) "Seu dispositivo não suporta monitoramento de estatísticas de tráfego.");
            builder.show();
        } else {
            this.mHandler2.postDelayed(this.mRunnable, 1000);
        }
		
// Contador dados <<<
		
		mHandler = new Handler();
		mConfig = new Settings(this);
		mDrawer = new DrawerLog(this);
		//mDrawerPanel = new DrawerPanelMain(this);

		SharedPreferences prefs = getSharedPreferences(SocksHttpApp.PREFS_GERAL, Context.MODE_PRIVATE);

		boolean showFirstTime = prefs.getBoolean("connect_first_time", true);
		int lastVersion = prefs.getInt("last_version", 0);

		// se primeira vez
		if (showFirstTime)
        {
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("connect_first_time", false);
            pEdit.apply();

			Settings.setDefaultConfig(this);


        }

		try {
			int idAtual = ConfigParser.getBuildId(this);

			if (lastVersion < idAtual) {
				SharedPreferences.Editor pEdit = prefs.edit();
				pEdit.putInt("last_version", idAtual);
				pEdit.apply();

				// se estiver atualizando
				if (!showFirstTime) {
					if (lastVersion <= 12) {
						Settings.setDefaultConfig(this);
						Settings.clearSettings(this);

						Toast.makeText(this, "La configuración se ha limpiado para evitar errores.",
									   Toast.LENGTH_LONG).show();
					}
				}

			}
		} catch(IOException e) {}


		// set layout
		doLayout();
		
		autoupdateApp(); // Auto Update Online base App
		
		// verifica se existe algum problema
		SkProtect.CharlieProtect();

		// recebe local dados
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_VIEWS);
		filter.addAction(OPEN_LOGS);

		LocalBroadcastManager.getInstance(this)
			.registerReceiver(mActivityReceiver, filter);

		doUpdateLayout();

		TextView version = (TextView)findViewById ( R.id.config_version_info); // version update
		version.setText("Act Serv.: " + config.getVersion()); //version update

		//timer >>>
		chronometer = (Chronometer)findViewById(R.id.cmTimer);
		chronometer.setOnChronometerTickListener(new OnChronometerTickListener(){
				@Override
				public void onChronometerTick(Chronometer chronometer) {
					long time = SystemClock.elapsedRealtime() - chronometer.getBase();
					int h   = (int)(time /3600000);
					int m = (int)(time - h*3600000)/60000;
					int s= (int)(time - h*3600000- m*60000)/1000 ;
					String t = (h < 10 ? "0"+h: h)+"h:"+(m < 10 ? "0"+m: m)+"m:"+ (s < 10 ? "0"+s: s)+"s";
					chronometer.setText(t);
				}
			});
		chronometer.setBase(SystemClock.elapsedRealtime());
		chronometer.setText("00h:00m:00s");
		//timer <<<

	}
	
	private void startNetworkBroadcastReceiver(SocksHttpMainActivity p0)
	{
		// TODO: Implement this method
	}

	// Update Offline >>>
	public void offlineUpdate() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);
	}
	// Update Offline <<<
	
	/**
	 * Layout
	 */
			
	public void askForPW(final int type){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View userpwlayout = inflater.inflate(R.layout.userpass, (ViewGroup) null);
        loginz = (Button) userpwlayout.findViewById(R.id.loginz);
        cancelz = (Button) userpwlayout.findViewById(R.id.cancelz);
        ((EditText) userpwlayout.findViewById(R.id.username)).setText(mConfig.getPrivString(Settings.USUARIO_KEY));
        ((EditText) userpwlayout.findViewById(R.id.password)).setText(mConfig.getPrivString(Settings.SENHA_KEY));
        ((CheckBox) userpwlayout.findViewById(R.id.save_password)).setChecked(true);
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setView(userpwlayout);
        final AlertDialog alert = builer.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.setView(userpwlayout);
		
		((EditText) userpwlayout.findViewById(R.id.username)).setText(mConfig.getPrivString(Settings.USUARIO_KEY));
        ((EditText) userpwlayout.findViewById(R.id.password)).setText(mConfig.getPrivString(Settings.SENHA_KEY));
        ((CheckBox) userpwlayout.findViewById(R.id.save_password)).setChecked(true);
        ((ImageButton) userpwlayout.findViewById(R.id.show_password)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					isMostrarSenha = !isMostrarSenha;
					if (isMostrarSenha) {
						((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						((ImageButton) userpwlayout.findViewById(R.id.show_password)).setImageDrawable(ContextCompat.getDrawable(SocksHttpMainActivity.this, R.drawable.ic_visibility_black_24dp));
					}
					else {
						((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
						((ImageButton) userpwlayout.findViewById(R.id.show_password)).setImageDrawable(ContextCompat.getDrawable(SocksHttpMainActivity.this, R.drawable.ic_visibility_off_black_24dp));
					}
				}
			});
			
		dialog.setView(userpwlayout);
		
        loginz.setOnClickListener(new View.OnClickListener() {
                private static final int START_VPN_PROFILE = 0;

                private String mTransientAuthPW;
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    if (type == R.string.password) {
                        SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();

                        String mUsername = ((EditText) userpwlayout.findViewById(R.id.username)).getText().toString();

                        edit.putString(Settings.USUARIO_KEY, mUsername);

                        String pw = ((EditText) userpwlayout.findViewById(R.id.password)).getText().toString();
                        if (((CheckBox) userpwlayout.findViewById(R.id.save_password)).isChecked()) {
                            edit.putString(Settings.SENHA_KEY, pw);
                        } else {
                            edit.remove(Settings.SENHA_KEY);
                            mTransientAuthPW = pw;
                        }

                        edit.apply();
                    }

                    if (mTransientAuthPW != null)
                        PasswordCache.setCachedPassword(null, PasswordCache.AUTHPASSWORD, mTransientAuthPW);
                    onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);

                    Toast.makeText(SocksHttpMainActivity.this, "LOGIN SALVO!", 0).show();
                }
            });
        cancelz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert.cancel();
                    //Toast.makeText(SocksHttpMainActivity.this, "LOGIN NÃO ALTERADO!", 0).show();
                }
            });
        alert.create();
        alert.show();
    }
	
	
	protected String getMainComponentName() {
		return "wakelock";
	}
	
	private void doLayout() {
		setContentView(R.layout.activity_main_drawer);
		toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
		setSupportActionBar(toolbar_main);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		mDrawer.setDrawer(this);

		// set ADS
		adsBannerView = (AdView) findViewById(R.id.adBannerMainView);

		if (!BuildConfig.DEBUG) {
			adsBannerView.setAdUnitId(SocksHttpApp.ADS_UNITID_BANNER_MAIN);
		}

		if (TunnelUtils.isNetworkOnline(SocksHttpMainActivity.this)) {
			adsBannerView.setAdListener(new AdListener() {
					@Override
					public void onAdLoaded() {
						if (adsBannerView != null) {
							adsBannerView.setVisibility(View.VISIBLE);
						}
					}
				});
			adsBannerView.loadAd(new AdRequest.Builder()
								 .build());
		}

		status= (TextView) findViewById(R.id.status1); //status
		
		//button menu >>>
		FabSpeedDial fabSpeedDial = (FabSpeedDial)findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
		
				@Override
                public boolean onPrepareMenu(NavigationMenu navigationMenu) {

                    return true;

                }

                @Override
                public boolean onMenuItemSelected(MenuItem menuItem)
                {

                    switch (menuItem.getItemId()) {
						
						case R.id.apn:
							Intent n = new Intent();
							n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							n.setAction(android.provider.Settings.ACTION_APN_SETTINGS);
							startActivity(n);
							return true;

						case R.id.updater:
							updater();
							return true;
							
						case R.id.hostshare:
							Intent hostshare2 = new Intent(SocksHttpMainActivity.this, ProxySettings.class);
							startActivity(hostshare2);
							break;	

						case R.id.settings:
							Intent intentSettings = new Intent(SocksHttpMainActivity.this, ConfigGeralActivity.class);
							startActivity(intentSettings);
							return true;	
							
					}
                    return true;
                }

				private void setInterval(int p0)
				{
					// TODO: Implement this method
				}

				private void setTimeout(int p0)
				{
					// TODO: Implement this method
				}

                @Override
                public void onMenuClosed() {

                }
			});
			//button menu <<<
			
		//version app  >>>
		PackageInfo pinfo = Utils.getAppInfo(this);
        if (pinfo != null) {
            String version_nome = pinfo.versionName;
            int version_code = pinfo.versionCode;
            String header_text = String.format("App Versión: %s ", version_nome, version_code);

            TextView app_info_text = (TextView) findViewById(R.id.app_info);
			app_info_text.setText(header_text);
		} // version app <<<

		mainLayout = (LinearLayout) findViewById(R.id.activity_mainLinearLayout);
		loginLayout = (LinearLayout) findViewById(R.id.activity_mainInputPasswordLayout);
		starterButton = (Button) findViewById(R.id.activity_starterButtonMain);

		BottomNavigationViewNew navigation = (BottomNavigationViewNew) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


		inputPwUser = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordUserEdit);
		inputPwPass = (TextInputEditText) findViewById(R.id.activity_mainInputPasswordPassEdit);

		inputPwShowPass = (ImageButton) findViewById(R.id.activity_mainInputShowPassImageButton);


		proxyInputLayout = (LinearLayout) findViewById(R.id.activity_mainInputProxyLayout);
		proxyText = (TextView) findViewById(R.id.activity_mainProxyText);

		config = new ConfigUtil(this);

		serverSpinner = (Spinner) findViewById(R.id.serverSpinner);

		serverList = new ArrayList<>();

		serverAdapter = new SpinnerAdapter(this, R.id.serverSpinner, serverList);

		serverSpinner.setAdapter(serverAdapter);

		loadServer();
		updateConfig(true);
		SharedPreferences sPrefs = mConfig.getPrefsPrivate();
		sPrefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
		sPrefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();


		metodoConexaoRadio = (RadioGroup) findViewById(R.id.activity_mainMetodoConexaoRadio);
		customPayloadSwitch = (SwitchCompat) findViewById(R.id.activity_mainCustomPayloadSwitch);

		starterButton.setOnClickListener(this);
		proxyInputLayout.setOnClickListener(this);

		payloadLayout = (LinearLayout) findViewById(R.id.activity_mainInputPayloadLinearLayout);
		payloadEdit = (TextInputEditText) findViewById(R.id.activity_mainInputPayloadEditText);

		configMsgLayout = (LinearLayout) findViewById(R.id.activity_mainMensagemConfigLinearLayout);
		configMsgText = (TextView) findViewById(R.id.activity_mainMensagemConfigTextView);

		// fix bugs
		if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			if (mConfig.getPrefsPrivate().getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));
			}
		}
		else {
			payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
		}

		metodoConexaoRadio.setOnCheckedChangeListener(this);
		customPayloadSwitch.setOnCheckedChangeListener(this);
		inputPwShowPass.setOnClickListener(this);

		//systemcode by @imlonely_withoutyou devdevanFAMILY
		final SharedPreferences prefsTxt = mConfig.getPrefsPrivate();
		inputPwUser.setText(prefsTxt.getString(Settings.USUARIO_KEY, ""));
		inputPwPass.setText(prefsTxt.getString(Settings.SENHA_KEY, ""));
		inputPwUser.addTextChangedListener(new TextWatcher() {

				public void afterTextChanged(Editable s) {
					if(!s.toString().isEmpty()) {
						prefsTxt.edit().putString(Settings.USUARIO_KEY, s.toString()).apply();
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {}
			});
		inputPwPass.addTextChangedListener(new TextWatcher() {

				public void afterTextChanged(Editable s) {
					if(!s.toString().isEmpty()) {
						prefsTxt.edit().putString(Settings.SENHA_KEY, s.toString()).apply();
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

				public void onTextChanged(CharSequence s, int start, int before, int count) {}
			});
	}
	private void doUpdateLayout() {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = SkStatus.isTunnelActive();
		int tunnelType = prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);

		setStarterButton(starterButton, this);
		setPayloadSwitch(tunnelType, !prefs.getBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true));

		String proxyStr = getText(R.string.no_value).toString();

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			proxyStr = "*******";
			proxyInputLayout.setEnabled(false);
		}
		else {
			String proxy = mConfig.getPrivString(Settings.PROXY_IP_KEY);

			if (proxy != null && !proxy.isEmpty())
				proxyStr = String.format("%s:%s", proxy, mConfig.getPrivString(Settings.PROXY_PORTA_KEY));
			proxyInputLayout.setEnabled(!isRunning);
		} 

		proxyText.setText(proxyStr);


		switch (tunnelType) {
			case Settings.bTUNNEL_TYPE_SSH_DIRECT:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHDirectRadioButton))
					.setChecked(true);
				break;

			case Settings.bTUNNEL_TYPE_SSH_PROXY:
				((AppCompatRadioButton) findViewById(R.id.activity_mainSSHProxyRadioButton))
					.setChecked(true);
				break;
		}

		int msgVisibility = View.GONE;
		int loginVisibility = View.GONE;
		String msgText = "";
		boolean enabled_radio = !isRunning;

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {

			if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				loginVisibility = View.VISIBLE;

				inputPwUser.setText(mConfig.getPrivString(Settings.USUARIO_KEY));
				inputPwPass.setText(mConfig.getPrivString(Settings.SENHA_KEY));

				inputPwUser.setEnabled(!isRunning);
				inputPwPass.setEnabled(!isRunning);
				inputPwShowPass.setEnabled(!isRunning);

			}

			String msg = mConfig.getPrivString(Settings.CONFIG_MENSAGEM_KEY);
			if (!msg.isEmpty()) {
				msgText = msg.replace("\n", "<br/>");
				msgVisibility = View.VISIBLE;
			}

			if (mConfig.getPrivString(Settings.PROXY_IP_KEY).isEmpty() ||
				mConfig.getPrivString(Settings.PROXY_PORTA_KEY).isEmpty()) {
				enabled_radio = false;
			}
		}

		loginLayout.setVisibility(loginVisibility);
		configMsgText.setText(msgText.isEmpty() ? "" : Html.fromHtml(msgText));
		configMsgLayout.setVisibility(msgVisibility);

		// desativa/ativa radio group
		for (int i = 0; i < metodoConexaoRadio.getChildCount(); i++) {
			metodoConexaoRadio.getChildAt(i).setEnabled(enabled_radio);
		}
		
		// Salvar posicão >>>
		serverSpinner.setSelection(prefs.getInt("ServerPos", 0));
		// Salvar posicão <<<
	}


	
	
	private synchronized void doSaveData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();

			if (prefs.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				edit.putString(Settings.USUARIO_KEY, inputPwUser.getEditableText().toString());
				edit.putString(Settings.SENHA_KEY, inputPwPass.getEditableText().toString());
			}
			
			edit.apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadServerData() {
		try {
			SharedPreferences prefs = mConfig.getPrefsPrivate();
			SharedPreferences.Editor edit = prefs.edit();
			
			int pos1 = serverSpinner.getSelectedItemPosition();
			edit.putInt("ServerPos", pos1); // Salva posição Servidor
	
			String ssh_server = config.getServersArray().getJSONObject(pos1).getString("ServerIP");
			String ssh_port = config.getServersArray().getJSONObject(pos1).getString("ServerPort");
			String ssl_port = config.getServersArray().getJSONObject(pos1).getString("SSLPort");
			
			String remote_proxy = config.getServersArray().getJSONObject(pos1).getString("ProxyIP");
			String proxy_port = config.getServersArray().getJSONObject(pos1).getString("ProxyPort");
            
			String payload = config.getServersArray().getJSONObject(pos1).getString("Payload");
			String sni = config.getServersArray().getJSONObject(pos1).getString("SNI");
			
			String serverNameKey = config.getServersArray().getJSONObject(pos1).getString("SlowServer");
			String chaveKey = config.getServersArray().getJSONObject(pos1).getString("pKey");
			String dnsKey = config.getServersArray().getJSONObject(pos1).getString("hDNS");
			
			//String dateUrl = config.getServersArray().getJSONObject(pos1).getString("DateChecker");
			//String serverUrl = config.getServersArray().getJSONObject(pos1).getString("ServerChecker");
			
            boolean useDirect = config.getServersArray().getJSONObject(pos1).getBoolean("isDirect");
			boolean useInject = config.getServersArray().getJSONObject(pos1).getBoolean("isInject");
			boolean useSsl = config.getServersArray().getJSONObject(pos1).getBoolean("isSSL");
			boolean useSslpay = config.getServersArray().getJSONObject(pos1).getBoolean("isSslPay");
			boolean useSslProxy = config.getServersArray().getJSONObject(pos1).getBoolean("isSslProxy");
			boolean useSlow = config.getServersArray().getJSONObject(pos1).getBoolean("isSlow");


            //SSH DIRECT
			if (useDirect)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				
				Settings.filterappDesativado(this);
			}

			//SSH PROXY
			if (useInject)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();

				prefs.edit().putString(Settings.PROXY_IP_KEY, remote_proxy).apply();
				prefs.edit().putString(Settings.PROXY_PORTA_KEY, proxy_port).apply();
				
				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				
				Settings.filterappDesativado(this);
			}
		
            //SSL SNI
			if (useSsl)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_SSL).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true).apply();
				
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssl_port).apply();

				prefs.edit().putString(Settings.CUSTOM_SNI, sni).apply();
				
				Settings.filterappDesativado(this);
			}
			
			//SSL PAY+SNI
			if (useSslpay)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
			
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssl_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				prefs.edit().putString(Settings.CUSTOM_SNI, sni).apply();
				
				Settings.filterappDesativado(this);
			}
			
			//SSL PROXY+SNI+PAY
			if (useSslProxy)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_PAY_SSL).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, false).apply();
				
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssl_port).apply();

				prefs.edit().putString(Settings.PROXY_IP_KEY, remote_proxy).apply();
				prefs.edit().putString(Settings.PROXY_PORTA_KEY, proxy_port).apply();

				prefs.edit().putString(Settings.CUSTOM_PAYLOAD_KEY, payload).apply();
				prefs.edit().putString(Settings.CUSTOM_SNI, sni).apply();

				Settings.filterappDesativado(this);
			}

			//SLOW DIRECT
			if (useSlow)
			{
				prefs.edit().putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SLOWDNS).apply();
				prefs.edit().putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, true).apply();
				
				prefs.edit().putString(Settings.SERVIDOR_KEY, ssh_server).apply();
				prefs.edit().putString(Settings.SERVIDOR_PORTA_KEY, ssh_port).apply();
				
				prefs.edit().putString(Settings.CHAVE_KEY, chaveKey).apply();
				prefs.edit().putString(Settings.NAMESERVER_KEY, serverNameKey).apply();
				prefs.edit().putString(Settings.DNS_KEY, dnsKey).apply();
				
				Settings.filterappAtivado(this);
			}

			edit.apply();

		} catch (Exception e) {
			SkStatus.logInfo(e.getMessage());
		}
	}
	


	private void loadServer() {
		try {
			if (serverList.size() > 0) {
				serverList.clear();
				serverAdapter.notifyDataSetChanged();
			}
			for (int i = 0; i < config.getServersArray().length(); i++) {
				JSONObject obj = config.getServersArray().getJSONObject(i);
				serverList.add(obj);
				serverAdapter.notifyDataSetChanged();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateConfig(final boolean isOnCreate) {
		new ConfigUpdate(this, new ConfigUpdate.OnUpdateListener() {


				@Override
				public void onUpdateListener(String result) {
					try {
						if (!result.contains("Error on getting data")) {
							String json_data = AESCrypt.decrypt(config.str, result);
							if (isNewVersion(json_data)) {
								newUpdateDialog(result);
							} else {
								if (!isOnCreate) {
									noUpdateDialog();
								}
							}
						} else if(result.contains("Error on getting data") && !isOnCreate){
							errorUpdateDialog(result);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start(isOnCreate);
	}

	private boolean isNewVersion(String result) {
		try {
			String current = config.getVersion();
			String update = new JSONObject(result).getString("Version");
			return config.versionCompare(update, current);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}


	private void newUpdateDialog(final String result) throws JSONException, GeneralSecurityException{


		String json_data = AESCrypt.decrypt(config.str, result);
		String notes = new JSONObject(json_data).getString("ReleaseNotes");
		Ringnotif();
		nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
		nops.setTitleText("ACTUALIZACION DISPONIBLE");
		nops.setContentText(notes);
		nops.setConfirmText("ACTUALIZAR");
		nops.setCancelText("SALIR");
		nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    try
                    {
                        File file = new File(getFilesDir(), "Config.json");
                        OutputStream out = new FileOutputStream(file);
                        out.write(result.getBytes());
                        out.flush();
                        out.close();
						restart_app();
                        //mRestart();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }});

		nops.setCancelClickListener((new SweetAlertDialog.OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog)
			{
			nops.cancel();
			}
			}));
		nops.show();

    }
	
	private void noUpdateDialog() {
        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("ACTUALIZADO")
            .setContentText("Todas las configuraciones está actualizado.")
            .show();
	}

	private void errorUpdateDialog(String error) {
        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("ERROR")
            .setContentText("Comprueba tu conexión y vuelve a intentarlo.\n" +"Nota: La Aplicación solo se actualizará mediante datos moviles o Wifi.\n" +" Si aún tienes problemas, contacta al desarrollador.")
            .show();
	}
	
	// Update Offline >>>
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE)
        {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String intentData = importer(uri);
                    //String cipter = AESCrypt.decrypt(ConfigUtil.str, intentData);
                    File file = new File(getFilesDir(), "Config.json");
                    OutputStream out = new FileOutputStream(file);
                    out.write(intentData.getBytes());
                    out.flush();
                    out.close();
                    loadServer();
                    restart_app();
                    //mRestart();
					
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	private String importer(Uri uri)
    {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));

            String line = "";
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            reader.close();
        }
        catch (IOException e) {e.printStackTrace();}
        return builder.toString();
	} 
	// Update Offline <<<
	
	private void restart_app() {
		Intent intent = new Intent(this, SocksHttpMainActivity.class);
		int i = 123456;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + ((long) 1000), pendingIntent);
		finish();
	}
	
	/**
	 * Tunnel SSH
	 */

	public void startOrStopTunnel(Activity activity) {
		if (SkStatus.isTunnelActive()) {
			TunnelManagerHelper.stopSocksHttp(activity);
			serverSpinner.setEnabled(true);
			inputPwUser.setEnabled(true);
			inputPwShowPass.setEnabled(true);
			inputPwPass.setEnabled(true);

			
			chronometer.setText("⏱️"); //timer
			pauseChronometer(); //timer
			
		}
		else {
			
//			// slow >>>
//			try {
//				startdnsvoid();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			// slow <<<

			// oculta teclado se vísivel, tá com bug, tela verde
			//Utils.hideKeyboard(activity);

			Settings config = new Settings(activity);

			if (config.getPrefsPrivate()
				.getBoolean(Settings.CONFIG_INPUT_PASSWORD_KEY, false)) {
				if (inputPwUser.getText().toString().isEmpty() || 
					inputPwPass.getText().toString().isEmpty()) {
					Toast.makeText(this, R.string.error_userpass_empty, Toast.LENGTH_SHORT)
						.show();
					return;
				}
			}
			inputPwUser.setEnabled(false);
			inputPwShowPass.setEnabled(false);
			inputPwPass.setEnabled(false);
			serverSpinner.setEnabled(false);

			chronometer.setText("⏱️"); //timer
			startChronometer();  //timer
			resetChronometer (); //timer
			
			Intent intent = new Intent(activity, LaunchVpn.class);
			intent.setAction(Intent.ACTION_MAIN);

			if (config.getHideLog()) {
				intent.putExtra(LaunchVpn.EXTRA_HIDELOG, true);
			}

			activity.startActivity(intent);
		}
	}

	// timer >>>
	public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
	public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }
	// timer <<<

	private void setPayloadSwitch(int tunnelType, boolean isCustomPayload) {
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		boolean isRunning = SkStatus.isTunnelActive();

		customPayloadSwitch.setChecked(isCustomPayload);

		if (prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
			payloadEdit.setEnabled(false);

			if (mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY).isEmpty()) {
				customPayloadSwitch.setEnabled(false);
			}
			else {
				customPayloadSwitch.setEnabled(!isRunning);
			}

			if (!isCustomPayload && tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY)
				payloadEdit.setText(Settings.PAYLOAD_DEFAULT);
			else
				payloadEdit.setText("*******");
		}
		else {
			customPayloadSwitch.setEnabled(!isRunning);

			if (isCustomPayload) {
				payloadEdit.setText(mConfig.getPrivString(Settings.CUSTOM_PAYLOAD_KEY));
				payloadEdit.setEnabled(!isRunning);
			}
			else if (tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY) {
				payloadEdit.setText(Settings.PAYLOAD_DEFAULT);
				payloadEdit.setEnabled(false);
			}
		}

		if (isCustomPayload || tunnelType == Settings.bTUNNEL_TYPE_SSH_PROXY) {
			payloadLayout.setVisibility(View.VISIBLE);
		}
		else {
			payloadLayout.setVisibility(View.GONE);
		}
	}

	public void setStarterButton(Button starterButton, Activity activity) {
		String state = SkStatus.getLastState();
		boolean isRunning = SkStatus.isTunnelActive();

		if (starterButton != null) {
			int resId;

			SharedPreferences prefsPrivate = new Settings(activity).getPrefsPrivate();

			if (ConfigParser.isValidadeExpirou(prefsPrivate
											   .getLong(Settings.CONFIG_VALIDADE_KEY, 0))) {
				resId = R.string.expired;
				starterButton.setEnabled(false);

				if (isRunning) {
					startOrStopTunnel(activity);
				}
			}
			else if (prefsPrivate.getBoolean(Settings.BLOQUEAR_ROOT_KEY, false) &&
					 ConfigParser.isDeviceRooted(activity)) {
				resId = R.string.blocked;
				starterButton.setEnabled(false);

				Toast.makeText(activity, R.string.error_root_detected, Toast.LENGTH_SHORT)
					.show();

				if (isRunning) {
					startOrStopTunnel(activity);
				}
			}
			else if (SkStatus.SSH_INICIANDO.equals(state)) {
				resId = R.string.stop;
				starterButton.setEnabled(false);
				status.setText("CONECTANDO"); //status
				TastyToast.makeText(getApplicationContext(), "VERIFICANDO ACESSO", TastyToast.LENGTH_LONG, TastyToast.INFO);
			}


			else if (SkStatus.SSH_PARANDO.equals(state)) {
				resId = R.string.state_stopping;
				starterButton.setEnabled(false);
				status.setText("DESCONECTANDO"); //status
				//stopdns(); //slow
				TastyToast.makeText(getApplicationContext(), "FINALIZANDO CONEXÃO", TastyToast.LENGTH_LONG, TastyToast.WARNING);
			}
			else {
				resId = isRunning ? R.string.stop : R.string.start;
				starterButton.setEnabled(true);
			}

			starterButton.setText(resId);

			if (SkStatus.SSH_CONECTADO.equals(state)) {
				status.setText("CONECTADO"); //status
			}

			if (SkStatus.SSH_DESCONECTADO.equals(state)) {
				status.setText("DESCONECTADO"); //status
			}
		}
	}


	@Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

	private boolean isMostrarSenha = false;

	@Override
	public void onClick(View p1)
	{
		SharedPreferences prefs = mConfig.getPrefsPrivate();

		switch (p1.getId()) {
			case R.id.activity_starterButtonMain:
				doSaveData();
				loadServerData();
				startOrStopTunnel(this);
				break;

			case R.id.activity_mainInputProxyLayout:
				if (!prefs.getBoolean(Settings.CONFIG_PROTEGER_KEY, false)) {
					doSaveData();

					DialogFragment fragProxy = new ProxyRemoteDialogFragment();
					fragProxy.show(getSupportFragmentManager(), "proxyDialog");
				}
				break;



			case R.id.activity_mainInputShowPassImageButton:
				isMostrarSenha = !isMostrarSenha;
				if (isMostrarSenha) {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_black_24dp));
				}
				else {
					inputPwPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					inputPwShowPass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_visibility_off_black_24dp));
				}
				break;
				
			
		}
	}

//	//slow >>>
//	private void startdnsvoid() throws IOException {
//
//		//Onde fica salvo as coisas no aparelho
//		final SharedPreferences slowprefs = mConfig.getPrefsPrivate();
//
//		StringBuilder cmd1 = new StringBuilder();
//		filedns = CustomNativeLoader.loadNativeBinary(this, DNS_BIN, new File(this.getFilesDir(),DNS_BIN));
//
//		if (filedns == null){
//			throw new IOException("Bin DNS não encontrado");
//		}
//
//		final String chave = slowprefs.getString(Settings.SLOW_CHAVE_KEY, "slowchave");
//		final String nameserver = slowprefs.getString(Settings.SLOW_NAMESERVER_KEY, "slowns");
//		final String dns = slowprefs.getString(Settings.SLOW_DNSKEY, "slowdns");
//		// executa comando
//		cmd1.append(filedns.getCanonicalPath());
//		cmd1.append(" -udp "+dns+":53   -pubkey "+chave+" "+nameserver+" 127.0.0.1:2222");
//
//		dnsProcess = Runtime.getRuntime().exec(cmd1.toString());
//
//		try {
//			//dnsProcess.waitFor();
//
//		} catch (Exception e) {
//			//	SkStatus.logDebug("BIN Error: " + e);
//		}
//	}
//
//	private void stopdns(){
//
//		if (dnsProcess != null)
//			dnsProcess.destroy();
//
//		try {
//			if (filedns != null)
//				KillThis.killProcess(filedns);
//		} catch (Exception e) {}
//
//		dnsProcess = null;
//		filedns = null;
//	} //slow <<<

	@Override
	public void onCheckedChanged(RadioGroup p1, int p2)
	{
		SharedPreferences.Editor edit = mConfig.getPrefsPrivate().edit();

		switch (p1.getCheckedRadioButtonId()) {
			case R.id.activity_mainSSHDirectRadioButton:
				edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT);
				proxyInputLayout.setVisibility(View.GONE);
				break;

			case R.id.activity_mainSSHProxyRadioButton:
				edit.putInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_PROXY);
				proxyInputLayout.setVisibility(View.VISIBLE);
				break;
		}

		edit.apply();

		doSaveData();
		doUpdateLayout();
	}

	@Override
	public void onCheckedChanged(CompoundButton p1, boolean p2)
	{
		SharedPreferences prefs = mConfig.getPrefsPrivate();
		SharedPreferences.Editor edit = prefs.edit();

		switch (p1.getId()) {
			case R.id.activity_mainCustomPayloadSwitch:
				edit.putBoolean(Settings.PROXY_USAR_DEFAULT_PAYLOAD, !p2);
				setPayloadSwitch(prefs.getInt(Settings.TUNNELTYPE_KEY, Settings.bTUNNEL_TYPE_SSH_DIRECT), p2);
				break;
		}

		edit.apply();

		doSaveData();
	}

	private void showLogWindow() {
		Intent updateView = new Intent("com.slipkprojects.sockshttp:openLogs");
		LocalBroadcastManager.getInstance(this)
			.sendBroadcast(updateView);
	}

	
	@Override
	public void updateState(final String state, String msg, int localizedResId, final ConnectionStatus level, Intent intent)
	{
		mHandler.post(new Runnable() {
				@Override
				public void run() {
					doUpdateLayout();
				}
			});

		switch (state) {
			case SkStatus.SSH_CONECTADO:
				// carrega ads banner
				if (adsBannerView != null && TunnelUtils.isNetworkOnline(SocksHttpMainActivity.this)) {
					adsBannerView.setAdListener(new AdListener() {
							@Override
							public void onAdLoaded() {
								if (adsBannerView != null && !isFinishing()) {
									adsBannerView.setVisibility(View.VISIBLE);
								}
							}
						});
					adsBannerView.postDelayed(new Runnable() {
							@Override
							public void run() {
								// carrega ads interestitial
								AdsManager.newInstance(getApplicationContext())
									.loadAdsInterstitial();
								// ads banner
								if (adsBannerView != null && !isFinishing()) {
									adsBannerView.loadAd(new AdRequest.Builder()
														 .build());
								}
							}
						}, 5000);
				}
				break;
				
			
		}
	}


	/**
	 * Recebe locais Broadcast
	 */

	private BroadcastReceiver mActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;

            if (action.equals(UPDATE_VIEWS) && !isFinishing()) {
				doUpdateLayout();
			}
			else if (action.equals(OPEN_LOGS)) {
				if (mDrawer != null && !isFinishing()) {
					DrawerLayout drawerLayout = mDrawer.getDrawerLayout();

					if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
						drawerLayout.openDrawer(GravityCompat.END);
					}
				}
			}
        }
    };


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{

		// Menu Itens
		switch (item.getItemId()) {



			case R.id.miLimparLogs:
				mDrawer.clearLogs();
				break;

		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onBackPressed() {
		// mostra opção para sair
		showExitDialog();
	}


	@Override
    public void onResume() {
        super.onResume();

		mDrawer.onResume();

		//doSaveData();
		//doUpdateLayout();

		SkStatus.addStateListener(this);

		if (adsBannerView != null) {
			adsBannerView.resume();
		}
    }

	@Override
	protected void onPause()
	{
		super.onPause();

		doSaveData();

		SkStatus.removeStateListener(this);

		if (adsBannerView != null) {
			adsBannerView.pause();
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		mDrawer.onDestroy();

		LocalBroadcastManager.getInstance(this)
			.unregisterReceiver(mActivityReceiver);

		if (adsBannerView != null) {
			adsBannerView.destroy();
		}
	}


	/**
	 * DrawerLayout Listener
	 */

	@Override
	public void onDrawerOpened(View view) {
		if (view.getId() == R.id.activity_mainLogsDrawerLinear) {
			toolbar_main.getMenu().clear();
			getMenuInflater().inflate(R.menu.logs_menu, toolbar_main.getMenu());
		}
	}

	@Override
	public void onDrawerClosed(View view) {
		if (view.getId() == R.id.activity_mainLogsDrawerLinear) {
			toolbar_main.getMenu().clear();
			getMenuInflater().inflate(R.menu.main_menu, toolbar_main.getMenu());
		}
	}

	@Override
	public void onDrawerStateChanged(int stateId) {}
	@Override
	public void onDrawerSlide(View view, float p2) {}


	/**
	 * Utils
	 */
 
	public void offlinez(View v)
	{
		offlineUpdate();
	}
	
    public void onlinez(View v)
    {
        updateConfig(false);	
    }

	public void appupdate(View v)
    {
        updateApp();
    }
	
	public void Contato_Adm(View v)
    {
        openInCustomTab("https://t.me/AlfSilic");
    }
	
    public void Grupo_telegram(View v)
    {
        openInCustomTab("https://t.me/+9Q_jw9uqvYA3NmMx");
    }

	public void Suporte_Whats(View v)
    {
        openInCustomTab("https://t.me/AlfSilic");
    }
	
	public void Contato_Acesso(View v)
    {
        openInCustomTab("https://t.me/AlfSilic");	
    }
	
    public void clearz(View v)
    {
        clears();
    }
	 
	public static void updateMainViews(Context context) {
		Intent updateView = new Intent(UPDATE_VIEWS);
		LocalBroadcastManager.getInstance(context)
			.sendBroadcast(updateView);
	}
	
	private void Ringnotif(){
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		r.play();
	}
	
	// Update Online base App >>>
	private void updateApp()
    {

		try {
			versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
		
		
        new AppUpdateChecker(this, base_update, new AppUpdateChecker.Listener() {

				@Override
				public void onLoading()
				{
					// TODO: Implement this method
				}

			
				private SweetAlertDialog nops;
              // @Override
             //  public void onLoading(){}
                @Override
               public void onCompleted(final String config)
                {
                    try
                    {
                        final JSONObject obj = new JSONObject(config);
						if(versionName.equals(obj.getString("versionCode")))
						{

							nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
							nops.setTitleText("ACTUALIZADO");
							nops.setContentText("Felicitaciones, su aplicación está en la última versión.");
							nops.show();

							//	Toast.makeText(SocksHttpMainActivity.this,("No Update Available") , 0).show();
                        }else{
							
							Ringnotif();
							
                            nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
							nops.setTitleText("APLICACIÓN DISPONIBLE");
							nops.setContentText(obj.getString("Mensaje"));
							nops.setConfirmText("ACTUALIZAR");
							nops.setCancelText("SALIR");
							nops.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

									@Override
									public void onClick(SweetAlertDialog sweetAlertDialog)
									{
										// TODO: Implement this method
										nops.cancel();
									}

								});


							nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

									@Override
									public void onClick(SweetAlertDialog sweetAlertDialog)
									{
                                        try
                                        {
											startActivity(new Intent(Intent.ACTION_VIEW,
																	 Uri.parse(obj.getString("url"))));
                                        }

                                        catch (JSONException e)
                                        {}
										sweetAlertDialog.dismissWithAnimation();
									}
                                })
								//  .setNegativeButton("CANCEL", null
                                .show();
                        }

                    }
                    catch (Exception e)
                    {
                        // Toast.makeText(MainActivity.this, e.getMessage() , 0).show();
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
    } // Update Online base App <<<
	
	// Auto Update Online base App >>>
	
	private void autoupdateApp()
    {

		try {
			versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

		
	
		new AutoAppUpdateChecker(this, base_update, new AutoAppUpdateChecker.Listener() {

				@Override
				public void onLoading()
				{
					// TODO: Implement this method
				}


	private SweetAlertDialog nops;
	// @Override
	//  public void onLoading(){}
	@Override
	public void onCompleted(final String config)
	{
		try
		{
			final JSONObject obj = new JSONObject(config);
			if(versionName.equals(obj.getString("versionCode")))
			{

				nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
				nops.setTitleText("ACTUALIZADO");
				nops.setContentText("Felicitaciones, su aplicación está en la última versión.");
				nops.show();

				//	Toast.makeText(SocksHttpMainActivity.this,("No Update Available") , 0).show();
			}else{

				Ringnotif();

				nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
				nops.setTitleText("APLICACIÓN DISPONIBLE");
				nops.setContentText(obj.getString("Mensaje"));
				nops.setConfirmText("ACTUALIZAR");
				nops.setCancelText("SALIR");
				nops.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog)
						{
							// TODO: Implement this method
							nops.cancel();
						}

					});


				nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

						@Override
						public void onClick(SweetAlertDialog sweetAlertDialog)
						{
							try
							{
								startActivity(new Intent(Intent.ACTION_VIEW,
														 Uri.parse(obj.getString("url"))));
							}

							catch (JSONException e)
							{}
							sweetAlertDialog.dismissWithAnimation();
						}
					})
					//  .setNegativeButton("CANCEL", null
					.show();
			}

		}
		catch (Exception e)
		{
			// Toast.makeText(MainActivity.this, e.getMessage() , 0).show();
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
	
	// Auto Update Online base App <<<
	
		private void openInCustomTab(String url){

        Uri websiteUri;
        if(!url.contains("https://") && !url.contains("http://")){
            websiteUri = Uri.parse("http://" + url);
        } else {
            websiteUri = Uri.parse(url);
        }
        CustomTabsIntent.Builder customtabintent = new CustomTabsIntent.Builder();
        customtabintent.setToolbarColor(this.getResources().getColor(R.color.colorPrimary));
        customtabintent.setShowTitle(true);
        {
            
        }
        customtabintent.build().launchUrl(SocksHttpMainActivity.this,websiteUri);
    }
    {
        try{
          
            
        } catch (Exception e){
          
        }
    }
		
	// Limpar Aplicativo >>>
	private void clears()
    {
        nops = new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE);
        nops.setTitleText("ATENCIÓN");
        nops.setContentText("Al hacer clic en BORRAR se borrarán todas las configuraciones de su aplicación.");
        nops.setCancelText("SALIR");
        nops.setConfirmText("BORRAR");
        nops.showCancelButton(true);
        nops.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    nops.cancel();
                }
            });
        nops.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    try {
                        // clearing app data
                        String packageName = getApplicationContext().getPackageName();
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("pm clear "+packageName);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        nops.show();
	}
	// Limpar Aplicativo <<<
	
	public void showExitDialog() {

        new SweetAlertDialog(SocksHttpMainActivity.this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(getString(R.string.attention))
            .setContentText(getString(R.string.alert_exit))
            .setConfirmText(getString(R.string.exit))
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

				
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    Utils.exitAll(SocksHttpMainActivity.this);
                }})
				
            .setCancelText(getString(R.string.minimize))
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){

                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    // TODO: Implement this method
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }


            })

            .show();

}
	}
