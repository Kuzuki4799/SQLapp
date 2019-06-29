package android.trithe.sqlapp.utils;

import android.text.TextUtils;

import com.google.android.gms.maps.model.Marker;

public class MarkerType {
    public static final String NOTHING = "NOTHING";
    public static final String EVENT = "EVENT";

    public static String getMarkerType(Marker marker) {
        try {
            if (TextUtils.isEmpty(marker.getTitle())) {
                return NOTHING;
            }
            if (Character.isDigit(marker.getTitle().charAt(0))) {
                return EVENT;
            } else {
                return marker.getTitle();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return NOTHING;
        }
    }
}
