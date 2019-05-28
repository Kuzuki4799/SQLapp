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

    public static final String API_LOGIN = "login.php";
    public static final String API_REGISTER = "register.php";

    public static final String LINK_LOAD_IMAGE = API_URL + "uploads/";
    public static final String API_UPLOAD_IMG = "upload.php";
    public static final String API_POSTER = "poster.php";

    public static final String API_RATING_FILM = "rating.php";

    public static final String API_CAST_DETAIL = CAST_CATE + "cast_detail.php";
    public static final String API_CAST_JOB_LIST = CAST_CATE + "get_job_list.php";
    public static final String API_CAST_COUNTRY_LIST = CAST_CATE + "get_country_list.php";
    public static final String API_CAST = CAST_CATE + "cast.php";
    public static final String API_GET_FILM_BY_CAST = CAST_CATE + "get_film_by_cast.php";

    public static final String API_KIND = KIND_CATE + "kind.php";
    public static final String API_KIND_DETAIL = KIND_CATE + "kind_detail.php";
    public static final String API_KIND_FILM_DETAIL = KIND_CATE + "get_kind_detail.php";
    public static final String API_DATA_KIND = KIND_CATE + "get_data_kind.php";

    public static final String API_CHECK_SAVED = SAVE_CATE + "check_saved.php";
    public static final String API_INSERT_SAVED = SAVE_CATE + "insert_saved.php";
    public static final String API_DELETE_SAVED = SAVE_CATE + "delete_saved.php";

    public static final String API_LOVE_CAST = LOVE_CATE + "check_loved.php";
    public static final String API_COUNT_LOVE_CAST = LOVE_CATE + "get_count_love.php";
    public static final String API_INSERT_LOVE_CAST = LOVE_CATE + "insert_loved.php";
    public static final String API_DELETE_LOVE_CAST = LOVE_CATE + "delete_loved.php";

    public static final String API_FILM = FILM_CATE + "film.php";
    public static final String API_SEARCH_FILM = FILM_CATE + "search_film.php";

}