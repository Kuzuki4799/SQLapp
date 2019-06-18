package android.trithe.sqlapp.config;

public class Config {

    public static final int TIME_OUT = 30000;
    private static final String IP_CONFIG = "192.168.0.117";
    public static final String API_URL = "http://" + IP_CONFIG + "/RetrofitvsmSql/";

    private static final String LOVE_CATE = "love/";
    private static final String SAVE_CATE = "save/";
    private static final String CAST_CATE = "cast/";
    private static final String KIND_CATE = "kind/";
    private static final String FILM_CATE = "film/";
    private static final String RAT_CATE = "rat/";
    private static final String LOGIN_CATE = "login/";
    private static final String FEED_BACK_CATE = "feedback/";
    private static final String NOTIFICATION_CATE = "notification/";
    private static final String COMMENT_FILM_CATE = "comment_film/";

    public static final String API_LOGIN = LOGIN_CATE + "login.php";
    public static final String API_REGISTER = LOGIN_CATE + "register.php";
    public static final String API_FORGET_PASS = LOGIN_CATE + "forget_pass.php";
    public static final String API_CHANGE_PASS = LOGIN_CATE + "change_password.php";
    public static final String API_CHANGE_NAME = LOGIN_CATE + "change_info_user.php";
    public static final String API_CHANGE_IMAGE = LOGIN_CATE + "change_image_user.php";
    public static final String API_GET_USER_BY_ID = LOGIN_CATE + "get_user_by_id.php";

    public static final String LINK_LOAD_IMAGE = API_URL + "uploads/";
    public static final String API_UPLOAD_IMG = "upload.php";

    public static final String API_COMMENT_FILM = COMMENT_FILM_CATE + "get_comment_by_film.php";
    public static final String API_SEND_COMMENT_FILM = COMMENT_FILM_CATE + "send_comment_film.php";

    public static final String API_FEED_BACK = FEED_BACK_CATE + "feedback.php";
    public static final String API_CHECK_FEED_BACK = FEED_BACK_CATE + "check_feedback.php";

    public static final String API_RATING_FILM = RAT_CATE + "rating.php";
    public static final String API_CHECK_RAT = RAT_CATE + "check_rat.php";
    public static final String API_PUSH_RAT = RAT_CATE + "insert_rat.php";

    public static final String API_GET_ALL_CAST = CAST_CATE + "get_all_cast.php";
    public static final String API_GET_ALL_CAST_BY_LOVED = CAST_CATE + "get_cast_by_loved.php";
    public static final String API_SEARCH_CAST = CAST_CATE + "search_cast.php";
    public static final String API_CAST_DETAIL = CAST_CATE + "cast_detail.php";
    public static final String API_CAST_JOB_LIST = CAST_CATE + "get_job_list.php";
    public static final String API_CAST_COUNTRY_LIST = CAST_CATE + "get_country_list.php";
    public static final String API_CAST = CAST_CATE + "cast.php";
    public static final String API_GET_FILM_BY_CAST = CAST_CATE + "get_film_by_cast.php";
    public static final String UPDATE_VIEWS_CAST = CAST_CATE + "update_views.php";

    public static final String API_KIND = KIND_CATE + "kind.php";
    public static final String API_KIND_DETAIL = KIND_CATE + "kind_detail.php";
    public static final String API_KIND_FILM_DETAIL = KIND_CATE + "get_kind_detail.php";
    public static final String API_DATA_KIND = KIND_CATE + "get_data_kind.php";

    public static final String API_INSERT_SAVED = SAVE_CATE + "insert_saved.php";
    public static final String API_DELETE_SAVED = SAVE_CATE + "delete_saved.php";

    public static final String API_COUNT_LOVE_CAST = LOVE_CATE + "get_count_love.php";
    public static final String API_INSERT_LOVE_CAST = LOVE_CATE + "insert_loved.php";
    public static final String API_DELETE_LOVE_CAST = LOVE_CATE + "delete_loved.php";

    public static final String API_FILM = FILM_CATE + "film.php";
    public static final String API_SEARCH_FILM = FILM_CATE + "search_film.php";
    public static final String API_GET_FILM_BY_ID = FILM_CATE + "get_fim_by_id.php";
    public static final String API_GET_SERIES = FILM_CATE + "series_film.php";
    public static final String API_GET_FILM_SAVED = FILM_CATE + "get_film_by_saved.php";

    public static final String LOAD_VIDEO_STORAGE = "https://firebasestorage.googleapis.com/v0/b/apps-f7451.appspot.com/o/Videos%2F";
    public static final String END_PART_VIDEO_STORAGE = "?alt=media&token=fb4c0b8a-0c53-469a-b375-83c544d7a14f";

    public static final String API_NOTIFICATION = NOTIFICATION_CATE + "notification.php";
    public static final String API_GET_COUNT_NOTIFICATION = NOTIFICATION_CATE + "get_count_notification.php";
    public static final String API_SEEN_NOTIFICATION = NOTIFICATION_CATE + "seen_notification.php";

    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";
}