package android.trithe.sqlapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.trithe.sqlapp.BuildConfig;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.stepstone.apprating.AppRatingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static android.support.constraint.Constraints.TAG;

public class Utils {
    static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public class Authenticated {
        public String token_type;
        public String access_token;
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, delayMillis);
    }

    public static boolean blnProcessDismiss = false;

    private static AlertDialog alertDialog = null;

    public static void showAlertDialog1(Context context, int strMsg) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(null)
                .setMessage(strMsg)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertDialog1(Context context, int title, int strMsg) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(strMsg)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertDialog1(Context context, String strMsg) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(null)
                .setMessage(strMsg)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showAlertDialog1(Context context, String title, String ms, String strTextOne,
                                        String strTextTwo, OnClickListener okListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(ms);
        alertDialogBuilder.setPositiveButton(strTextOne, okListener);
        alertDialogBuilder.setNegativeButton(strTextTwo, null);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void showAlertDialog2(Context context, String ms, String strTextOne,
                                        String strTextTwo, OnClickListener oklistener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(ms);
        alertDialogBuilder.setPositiveButton(strTextTwo, oklistener);
        alertDialogBuilder.setNegativeButton(strTextOne, null);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void closeAlertDialog() {
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            // nothing
        }
    }

    public static boolean isImageFileCheck(Context mContext, String imageName) {
        String filePath = "/data/data/" + mContext.getPackageName() + "/files/" + imageName;
        File file = new File(filePath);

        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static int dip2px(Context context, float dipValue) {

        float m = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * m + 0.5f);

    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float resizeWidth, float resizeHeight) {
        float resizeScaleWidth;
        float resizeScaleHeight;

        Matrix matrix = new Matrix();
        resizeScaleWidth = resizeWidth / bitmap.getWidth();
        resizeScaleHeight = resizeHeight / bitmap.getHeight();
        matrix.postScale(resizeScaleWidth, resizeScaleHeight);
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return resizeBitmap;
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.COUNTER_CHANGED");
        intent.putExtra("count", count);
        intent.putExtra("package", context.getPackageName());
        intent.putExtra("class", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static String checkString(String strData) {
        if (strData == null) {
            strData = "";
        }
        if (strData.equals("") || strData.length() == 0 || strData.equals("null")) {
            strData = "";
        }

        return strData;
    }

    public static int stringToInt(String strData) {
        if (strData == null) {
            strData = "0";
        }
        if (strData.equals("") || strData.length() == 0 || strData.equals("null")) {
            strData = "0";
        }

        return Integer.valueOf(strData);
    }

    public static Boolean telCheck(String str) {
        Boolean bool = str.matches("^0\\d{1,4}-\\d{1,4}-\\d{4}$");
        return bool;
    }

    public static Boolean mailCheck(String str) {
        Boolean bool = str.matches("[\\w\\.\\-]+@(?:[\\w\\-]+\\.)+[\\w\\-]+");
        return bool;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @SuppressLint("DefaultLocale")
    public static String ipWifiAddress(Context context) {
        String ipString;
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiMgr != null;
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        ipString = String.format(
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
        return ipString;
    }

    public static boolean getPacketApp(Context context) {

        PackageManager packManager = context.getPackageManager();
        List<ApplicationInfo> packages = packManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            if (packageInfo.packageName.contains("com.twitter.android")) {
                return true;
            }
        }
        return false;
    }

    public static boolean getPacketAppFacebook(Context context) {

        PackageManager packManager = context.getPackageManager();
        List<ApplicationInfo> packages = packManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            if (packageInfo.packageName.contains("com.facebook.android")) {
                return true;
            }
        }
        return false;
    }

    public static void takeScreenshot(Activity activity, String ms) {

        try {
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            getImagesShare(activity, ms, bitmap);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Log.e("", e.getMessage());
        }
    }

    private static void getImagesShare(Activity activity, String ms, Bitmap bitmap) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path + "/Mypachi_screen_shoot.jpg");
        file.getParentFile().mkdirs();
        try {
            if (file.exists())
                file.delete();
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        shareTwitter(activity, ms, file);
    }

    private static void shareTwitter(Activity activity, String ms, File path) {
        Uri imageUri = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider", path);
        if (Utils.getPacketApp(activity)) {
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_TEXT, ms);
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setPackage("com.twitter.android");
            activity.startActivity(intent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, ms);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(ms)));
            activity.startActivity(i);
        }
    }

    public static void infoTwitterShow(Activity activity) {
        if (Utils.getPacketApp(activity)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "mypachi_jp")));
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mypachi_jp"));
            activity.startActivity(intent);
        }
    }

    public static void infoFacebook(Activity activity) {
        if (Utils.getPacketAppFacebook(activity)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.URL_FB_PROFILE + SharedPrefUtils.getString(Constant.ID_SOCIAL, "") + Config.URL_FB_PROFILE)));
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.URL_FB_PROFILE + SharedPrefUtils.getString(Constant.ID_SOCIAL, "") + Config.URL_FB_INFO));
            activity.startActivity(intent);
        }
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("urlEncode", "UTF-8 should always be supported", e);
            return "";
        }
    }

    public static void shareUrl(Context context, String url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share link");
        share.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(share, "Share"));
    }

    public static void showDialog(Context context) {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(3)
                .setTitle("Rate this movie")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(android.R.color.white)
                .setDescriptionTextColor(android.R.color.darker_gray)
                .setHint("Please write your comment here ...")
                .setHintTextColor(android.R.color.darker_gray)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create((FragmentActivity) context)
                .show();
    }

    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}
