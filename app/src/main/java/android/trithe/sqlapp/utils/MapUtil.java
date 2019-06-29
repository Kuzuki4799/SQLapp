package android.trithe.sqlapp.utils;

import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.trithe.sqlapp.R;

import com.google.android.gms.maps.model.LatLng;

public class MapUtil {
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }


    public static String formatDistance(Context context, Double meter) {
        if (meter < 1000) {
            String durationFormat = context.getResources().getString(R.string.duration_metter);
            return String.format(durationFormat, doubleToLong(meter * 1.0));
        } else {
            String durationFormat = context.getResources().getString(R.string.duration_kilommetter);
            double rs = meter / 1000;
            String distance = (rs - (int) rs > 0.05) ? String.format("%.1f", rs) : String.valueOf((int) rs);
            return String.format(durationFormat, distance).replace(",", ".");
        }
    }

    private static long doubleToLong(Double number) {
        Long tmp;
        tmp = (long) (number * 1.0);
        return tmp;
    }


    /**
     * Used for animating the user location icon
     *
     * @since 0.1.0
     */
    public static class PointEvaluator implements TypeEvaluator<LatLng> {
        // Method is used to interpolate the user icon animation.
        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            double lng = startValue.longitude + (
                    (endValue.longitude - startValue.longitude) * fraction);
            double lat = startValue.latitude + (
                    (endValue.latitude - startValue.latitude) * fraction);
            return new LatLng(lat, lng);
        }
    }
}
