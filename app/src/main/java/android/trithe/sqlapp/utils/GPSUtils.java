package android.trithe.sqlapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.trithe.sqlapp.R;

/**
 * Created by admin on 24/1/2018.
 */

public class GPSUtils {
    public static final int DEVICE_NOT_SUPPORT_GPS = -1;
    public static final int DEVICE_ENABLE_GPS = 1;
    public static final int DEVICE_DISABLE_GPS = 2;
    public static Dialog dialog;


    public static int statusGPS(Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            return DEVICE_NOT_SUPPORT_GPS;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                return DEVICE_ENABLE_GPS;
            } else {
                return DEVICE_DISABLE_GPS;
            }
        } else {
            return DEVICE_NOT_SUPPORT_GPS;
        }
    }

    public static void showPopupNotSupport(Context context) {
        if (dialog != null) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_warning));
        builder.setMessage(context.getString(R.string.message_not_support_gps));
        builder.setPositiveButton(context.getString(R.string.strOk), (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }

    public static void goToGPSSetting(Activity context, int requestCode) {
        if (dialog != null) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_neet_gps));
        builder.setMessage(context.getString(R.string.message_need_gps));
        builder.setPositiveButton(context.getString(R.string.strOk), (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, requestCode);
            dialog.dismiss();
        });
        builder.setNegativeButton(context.getString(R.string.late), (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }

    public static void goToGPSSetting(Activity context, int requestCode, GPSDeniCallback callback) {
        if (dialog != null) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.title_neet_gps));
        builder.setMessage(context.getString(R.string.message_need_gps));
        builder.setPositiveButton(context.getString(R.string.strOk), (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivityForResult(intent, requestCode);
            dialog.dismiss();
        });
        builder.setNegativeButton(context.getString(R.string.late), (dialog, which) -> {
            if (callback != null) {
                callback.onDeni();
            }
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }

    public interface GPSDeniCallback {
        void onDeni();
    }
}
