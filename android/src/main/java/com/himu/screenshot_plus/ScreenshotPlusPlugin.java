package jpg.ivan.screenshot_plus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.embedding.engine.renderer.FlutterRenderer;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterView;

/**
 * ScreenshotPlusPlugin
 */
public class ScreenshotPlusPlugin implements MethodCallHandler, FlutterPlugin, ActivityAware {
	private static final String TAG = "ScreenshotPlusPlugin";

	private Context context;
	private MethodChannel channel;
	private Activity activity;
	private Object renderer;

	private boolean ssError = false;
	private String ssPath;

	// Default constructor for old registrar
	public ScreenshotPlusPlugin() {
	} // ScreenshotPlusPlugin()

	// Condensed logic to initialize the plugin
	private void initPlugin(Context context, BinaryMessenger messenger, Activity activity, Object renderer) {
		this.context = context;
		this.activity = activity;
		this.renderer = renderer;

		this.channel = new MethodChannel(messenger, "screenshot_plus");
		this.channel.setMethodCallHandler(this);
	} // initPlugin()

	// New v2 listener methods
	@Override
	public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
		this.channel.setMethodCallHandler(null);
		this.channel = null;
		this.context = null;
	} // onDetachedFromEngine()

	@Override
	public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
		Log.println(Log.INFO, TAG, "Using *NEW* registrar method!");

		initPlugin(
				flutterPluginBinding.getApplicationContext(),
				flutterPluginBinding.getBinaryMessenger(),
				null,
				flutterPluginBinding.getFlutterEngine().getRenderer()); // initPlugin()
	} // onAttachedToEngine()

	// Old v1 register method
	// FIX: Make instance variables set with the old method
	public static void registerWith(Registrar registrar) {
		Log.println(Log.INFO, TAG, "Using *OLD* registrar method!");

		ScreenshotPlusPlugin instance = new ScreenshotPlusPlugin();

		instance.initPlugin(
				registrar.context(),
				registrar.messenger(),
				registrar.activity(),
				registrar.view()); // initPlugin()
	} // registerWith()

	// Activity condensed methods
	private void attachActivity(ActivityPluginBinding binding) {
		this.activity = binding.getActivity();
	} // attachActivity()

	private void detachActivity() {
		this.activity = null;
	} // attachActivity()

	// Activity listener methods
	@Override
	public void onAttachedToActivity(ActivityPluginBinding binding) {
		attachActivity(binding);
	} // onAttachedToActivity()

	@Override
	public void onDetachedFromActivityForConfigChanges() {
		detachActivity();
	} // onDetachedFromActivityForConfigChanges()

	@Override
	public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
		attachActivity(binding);
	} // onReattachedToActivityForConfigChanges()

	@Override
	public void onDetachedFromActivity() {
		detachActivity();
	} // onDetachedFromActivity()

	// MethodCall, manage stuff coming from Dart
	@Override
	public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
		if (!permissionToWrite()) {
			Log.println(Log.INFO, TAG, "Permission to write files missing!");

			result.success(null);

			return;
		} // if cannot write

		if (!call.method.equals("takeShot")) {
			Log.println(Log.INFO, TAG, "Method not implemented!");

			result.notImplemented();

			return;
		} // if not implemented

		// Need to fix takeShot()
		// it produces just a black image
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			// takeShot();
			takeShot();
		} else {
			takeShotOld();
		} // if

		if (this.ssError || this.ssPath == null || this.ssPath.isEmpty()) {
			result.success(null);

			return;
		} // if error

		result.success(this.ssPath);
	} // onMethodCall()

	// Own functions, plugin specific functionality
	private String getScreenshotName() {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		String sDate = sf.format(new Date());

		return "Shot-" + sDate + ".png";
	} // getScreenshotName()

	private String getApplicationName() {
		ApplicationInfo appInfo = null;
		try {
			appInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0);
		} catch (Exception ex) {
			Log.e(TAG, "Error getting package info", ex);
		}
	
		String appName = null;
		if (appInfo != null) {
			CharSequence cs = this.context.getPackageManager().getApplicationLabel(appInfo);
			appName = cs.toString().trim();
		}
	
		if (appName == null || appName.isEmpty()) {
			appName = this.context.getPackageName();
		}
	
		return appName;
	}
	
	private String getScreenshotPath() {
		String externalDir = getOrCreateDocDirectory();
		String sDir = externalDir
				+ File.separator
				+ getApplicationName();

		File dir = new File(sDir);

		String dirPath;

		if (dir.exists() || dir.mkdir()) {
			dirPath = sDir + File.separator + getScreenshotName();
		} else {
			dirPath = externalDir + File.separator + getScreenshotName();
		}

		Log.println(Log.INFO, TAG, "Built ScreeshotPath: " + dirPath);

		return dirPath;
	} // getScreenshotPath()

	// Create if not Dcumnets Directory
	private String getOrCreateDocDirectory() {
		// Get the path of the Docs directory
		String docDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
				.getAbsolutePath();

		// Check if the directory exists
		File docDirectory = new File(docDirectoryPath);
		if (!docDirectory.exists()) {
			// Create the directory if it doesn't exist
			boolean success = docDirectory.mkdirs();
			if (!success) {
				// Failed to create the directory, handle the error
				return null;
			}
		}

		// Return the path of the Docs directory
		return docDirectoryPath;
	}

	private String writeBitmap(Bitmap bitmap) {
		try {
			String path = getScreenshotPath();
			File imageFile = new File(path);
			FileOutputStream oStream = new FileOutputStream(imageFile);

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
			oStream.flush();
			oStream.close();

			return path;
		} catch (Exception ex) {
			Log.println(Log.INFO, TAG, "Error writing bitmap: " + ex.getMessage());
		}

		return null;
	} // writeBitmap()

	private void reloadMedia() {
		try {
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File file = new File(this.ssPath);
			Uri uri = Uri.fromFile(file);

			intent.setData(uri);
			this.activity.sendBroadcast(intent);
		} catch (Exception ex) {
			Log.println(Log.INFO, TAG, "Error reloading media lib: " + ex.getMessage());
		}
	} // reloadMedia()

	private void takeShot() {
		Log.println(Log.INFO, TAG, "Trying to take screenshot [new way]");

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
			this.ssPath = null;
			this.ssError = true;

			return;
		}

		try {
			Window window = this.activity.getWindow();
			View view = this.activity.getWindow().getDecorView().getRootView();

			Bitmap bitmap = Bitmap.createBitmap(
					view.getWidth(),
					view.getHeight(),
					Bitmap.Config.ARGB_8888); // Bitmap()

			Canvas canvas = new Canvas(bitmap);
			view.draw(canvas);

			String path = writeBitmap(bitmap);
			if (path == null || path.isEmpty()) {
				this.ssPath = null;
				this.ssError = true;
			} // if no path

			this.ssError = false;
			this.ssPath = path;

			reloadMedia();
		} catch (Exception ex) {
			Log.println(Log.INFO, TAG, "Error taking screenshot: " + ex.getMessage());
		}
	} // takeShot()

	private void takeShotOld() {
		Log.println(Log.INFO, TAG, "Trying to take screenshot [old way]");

		try {
			View view = this.activity.getWindow().getDecorView().getRootView();

			view.setDrawingCacheEnabled(true);

			Bitmap bitmap = null;
			if (this.renderer.getClass() == FlutterView.class) {
				bitmap = ((FlutterView) this.renderer).getBitmap();
			} else if (this.renderer.getClass() == FlutterRenderer.class) {
				bitmap = ((FlutterRenderer) this.renderer).getBitmap();
			}

			if (bitmap == null) {
				this.ssError = true;
				this.ssPath = null;

				Log.println(Log.INFO, TAG, "The bitmap cannot be created :(");

				return;
			} // if

			view.setDrawingCacheEnabled(false);

			String path = writeBitmap(bitmap);
			if (path == null || path.isEmpty()) {
				this.ssError = true;
				this.ssPath = null;

				Log.println(Log.INFO, TAG, "The bitmap cannot be written, invalid path.");

				return;
			} // if

			this.ssError = false;
			this.ssPath = path;

			reloadMedia();
		} catch (Exception ex) {
			Log.println(Log.INFO, TAG, "Error taking screenshot: " + ex.getMessage());
		}
	} // takeShot()

	private boolean permissionToWrite() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			Log.println(Log.INFO, TAG, "Permission to write false due to version codes.");
			return false;
		}
	
		if (this.activity == null) {
			Log.println(Log.INFO, TAG, "Activity is null. Cannot check permission.");
			return false;
		}
	
		int perm = this.activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
	
		if (perm == PackageManager.PERMISSION_GRANTED) {
			Log.println(Log.INFO, TAG, "Permission to write granted!");
			return true;
		}
	
		Log.println(Log.INFO, TAG, "Requesting permissions...");
		this.activity.requestPermissions(
				new String[]{
						Manifest.permission.WRITE_EXTERNAL_STORAGE
				},
				11
		);
	
		Log.println(Log.INFO, TAG, "No permissions :(");
		return false;
	}
	// permissionToWrite()
} // ScreenshotPlusPlugin
