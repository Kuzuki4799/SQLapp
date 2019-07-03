package android.trithe.sqlapp.aplication;

import android.location.Location;
import android.trithe.sqlapp.utils.MapMode;

public class MapApplicationData {
    public static Double latitude;              // kinh do hien tai cua nguoi dung
    public static Double longitude;             // vi do hien tai cua nguoi dung
    public static float heading;                // huong hien tai
    public static double distance = 0;          // khoang cach cua lo trinh hien tai
    public static double walkingDistance = 0;   // khoang cach da di duoc tren lo trinh hien tai

    public static MapMode mapModeMainFragment = MapMode.NORMAL_MODE; // Trang thai map cua map chinh

    public static Location location;
}
