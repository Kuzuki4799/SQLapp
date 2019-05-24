package android.trithe.sqlapp.config;

public class Config {

    public static final int TIME_OUT = 30000;

    public static final String LOVE_CATE = "love/";
    public static final String SAVE_CATE = "save/";
    public static final String CAST_CATE = "cast/";

    public static final String IP_CONFIG = "192.168.0.117";
    public static final String API_URL = "http://" + IP_CONFIG + "/RetrofitvsmSql/";

    public static final String API_LOGIN = "login.php";
    public static final String API_REGISTER = "register.php";

    public static final String LINK_LOAD_IMAGE = API_URL + "uploads/";
    public static final String API_UPLOAD_IMG = "upload.php";
    public static final String API_POSTER = "poster.php";

    public static final String API_FILM = "film.php";
    public static final String API_RATING_FILM = "rating.php";
    public static final String API_SEARCH_FILM = "search_film.php";

    public static final String API_CAST_DETAIL = CAST_CATE + "cast_detail.php";
    public static final String API_CAST_JOB_LIST = CAST_CATE + "get_job_list.php";
    public static final String API_CAST = CAST_CATE + "cast.php";

    public static final String API_KIND = "kind.php";
    public static final String API_KIND_DETAIL = "kind_detail.php";
    public static final String API_KIND_FILM_DETAIL = "get_kind_detail.php";
    public static final String API_DATA_KIND = "get_data_kind.php";

    public static final String API_CHECK_SAVED = SAVE_CATE + "check_saved.php";
    public static final String API_INSERT_SAVED = SAVE_CATE + "insert_saved.php";
    public static final String API_DELETE_SAVED = SAVE_CATE + "delete_saved.php";

    public static final String API_LOVE_CAST = LOVE_CATE + "check_loved.php";
    public static final String API_INSERT_LOVE_CAST = LOVE_CATE + "insert_loved.php";
    public static final String API_DELETE_LOVE_CAST = LOVE_CATE + "delete_loved.php";
}