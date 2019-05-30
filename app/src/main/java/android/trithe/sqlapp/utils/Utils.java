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
import android.support.v4.content.FileProvider;
import android.trithe.sqlapp.BuildConfig;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static android.support.constraint.Constraints.TAG;

public class Utils {
    static final String strFull = "－＋＊／！＃＄％＆’（）＝＜＞「」【】";
    static final String strHalf = "-+*/!#$%&'()=<>｢｣[]";
    static final String strGameFull = "\n※";
    static final String strGameHalf = "\n\n※";
    static final String strDesStart1 = "「";
    static final String strDesStartConvert1 = "「<strong>";
    static final String strDesEnd1 = "」";
    static final String strDesEndConvert1 = "</strong>」";
    static final String strDesStart2 = "『";
    static final String strDesStartConvert2 = "『<strong>";
    static final String strDesEnd2 = "』";
    static final String strDesEndConvert2 = "</strong>』";
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

    //ダイアログ警告閉じる
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

    //MailAddress Check
    public final static boolean isValidEmail(CharSequence target) {
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

    /**
     * 全文字の半角を全角変換.
     *
     * @param string 変換対象の文字列を設定します.
     * @return String 変換された文字列が返却されます.
     */
    public static final String toBig(String string) {
        string = toBigAscii(string);
        return toBigJp(string);
    }

    /**
     * カナ半角を全角変換.
     *
     * @param string 変換対象の文字列を設定します.
     * @return String 変換された文字列が返されます.
     */
    public static final String toBigJp(String string) {
        if (string == null || string.length() <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int len = string.length();
        char[] o = new char[1];
        for (int i = 0; i < len; i++) {
            i += toBigJpOne(o, string, i);
            buf.append(o[0]);
        }
        return buf.toString();
    }

    /**
     * 英語半角を全角変換.
     *
     * @param string 変換対象の文字列を設定します.
     * @return String 変換された文字列が返されます.
     */
    public static final String toBigAscii(String string) {
        if (string == null || string.length() <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            buf.append(toBigAsciiOne(string.charAt(i)));
        }
        return buf.toString();
    }

    /**
     * 英語のみ、半角を全角に変換.
     */
    private static final String toBigAsciiOne(char code) {
        switch (code) {
            case '!':
                return "！";
            case '\"':
                return "”";
            case '#':
                return "＃";
            case '$':
                return "＄";
            case '\\':
                return "￥";
            case '%':
                return "％";
            case '&':
                return "＆";
            case '\'':
                return "’";
            case '(':
                return "（";
            case ')':
                return "）";
            case '*':
                return "＊";
            case '+':
                return "＋";
            case ',':
                return "，";
            case '-':
                return "－";
            case '.':
                return "．";
            case '/':
                return "／";
            case '0':
                return "０";
            case '1':
                return "１";
            case '2':
                return "２";
            case '3':
                return "３";
            case '4':
                return "４";
            case '5':
                return "５";
            case '6':
                return "６";
            case '7':
                return "７";
            case '8':
                return "８";
            case '9':
                return "９";
            case ':':
                return "：";
            case ';':
                return "；";
            case '<':
                return "＜";
            case '=':
                return "＝";
            case '>':
                return "＞";
            case '?':
                return "？";
            case '@':
                return "＠";
            case 'A':
                return "Ａ";
            case 'B':
                return "Ｂ";
            case 'C':
                return "Ｃ";
            case 'D':
                return "Ｄ";
            case 'E':
                return "Ｅ";
            case 'F':
                return "Ｆ";
            case 'G':
                return "Ｇ";
            case 'H':
                return "Ｈ";
            case 'I':
                return "Ｉ";
            case 'J':
                return "Ｊ";
            case 'K':
                return "Ｋ";
            case 'L':
                return "Ｌ";
            case 'M':
                return "Ｍ";
            case 'N':
                return "Ｎ";
            case 'O':
                return "Ｏ";
            case 'P':
                return "Ｐ";
            case 'Q':
                return "Ｑ";
            case 'R':
                return "Ｒ";
            case 'S':
                return "Ｓ";
            case 'T':
                return "Ｔ";
            case 'U':
                return "Ｕ";
            case 'V':
                return "Ｖ";
            case 'W':
                return "Ｗ";
            case 'X':
                return "Ｘ";
            case 'Y':
                return "Ｙ";
            case 'Z':
                return "Ｚ";
            case '^':
                return "＾";
            case '_':
                return "＿";
            case '`':
                return "‘";
            case 'a':
                return "ａ";
            case 'b':
                return "ｂ";
            case 'c':
                return "ｃ";
            case 'd':
                return "ｄ";
            case 'e':
                return "ｅ";
            case 'f':
                return "ｆ";
            case 'g':
                return "ｇ";
            case 'h':
                return "ｈ";
            case 'i':
                return "ｉ";
            case 'j':
                return "ｊ";
            case 'k':
                return "ｋ";
            case 'l':
                return "ｌ";
            case 'm':
                return "ｍ";
            case 'n':
                return "ｎ";
            case 'o':
                return "ｏ";
            case 'p':
                return "ｐ";
            case 'q':
                return "ｑ";
            case 'r':
                return "ｒ";
            case 's':
                return "ｓ";
            case 't':
                return "ｔ";
            case 'u':
                return "ｕ";
            case 'v':
                return "ｖ";
            case 'w':
                return "ｗ";
            case 'x':
                return "ｘ";
            case 'y':
                return "ｙ";
            case 'z':
                return "ｚ";
            case '{':
                return "｛";
            case '|':
                return "｜";
            case '}':
                return "｝";
            case '｡':
                return "。";
            case '｢':
                return "「";
            case '｣':
                return "」";
            case '､':
                return "、";
            case '･':
                return "・";
            case ' ':
                return "　";
        }
        return new String(new char[]{code});
    }

    /**
     * (カナ)半角から全角変換.
     */
    private static final int toBigJpOne(char[] out, String str, int index) {
        char code = str.charAt(index);
        if (index + 1 >= str.length()) {
            switch (code) {
                case 'ｧ':
                    out[0] = 'ァ';
                    return 0;
                case 'ｨ':
                    out[0] = 'ィ';
                    return 0;
                case 'ｩ':
                    out[0] = 'ゥ';
                    return 0;
                case 'ｪ':
                    out[0] = 'ェ';
                    return 0;
                case 'ｫ':
                    out[0] = 'ォ';
                    return 0;
                case 'ｬ':
                    out[0] = 'ャ';
                    return 0;
                case 'ｭ':
                    out[0] = 'ュ';
                    return 0;
                case 'ｮ':
                    out[0] = 'ョ';
                    return 0;
                case 'ｯ':
                    out[0] = 'ッ';
                    return 0;
                case 'ｰ':
                    out[0] = 'ー';
                    return 0;
                case 'ｱ':
                    out[0] = 'ア';
                    return 0;
                case 'ｲ':
                    out[0] = 'イ';
                    return 0;
                case 'ｳ':
                    out[0] = 'ウ';
                    return 0;
                case 'ｴ':
                    out[0] = 'エ';
                    return 0;
                case 'ｵ':
                    out[0] = 'オ';
                    return 0;
                case 'ｶ':
                    out[0] = 'カ';
                    return 0;
                case 'ｷ':
                    out[0] = 'キ';
                    return 0;
                case 'ｸ':
                    out[0] = 'ク';
                    return 0;
                case 'ｹ':
                    out[0] = 'ケ';
                    return 0;
                case 'ｺ':
                    out[0] = 'コ';
                    return 0;
                case 'ｻ':
                    out[0] = 'サ';
                    return 0;
                case 'ｼ':
                    out[0] = 'シ';
                    return 0;
                case 'ｽ':
                    out[0] = 'ス';
                    return 0;
                case 'ｾ':
                    out[0] = 'セ';
                    return 0;
                case 'ｿ':
                    out[0] = 'ソ';
                    return 0;
                case 'ﾀ':
                    out[0] = 'タ';
                    return 0;
                case 'ﾁ':
                    out[0] = 'チ';
                    return 0;
                case 'ﾂ':
                    out[0] = 'ツ';
                    return 0;
                case 'ﾃ':
                    out[0] = 'テ';
                    return 0;
                case 'ﾄ':
                    out[0] = 'ト';
                    return 0;
                case 'ﾅ':
                    out[0] = 'ナ';
                    return 0;
                case 'ﾆ':
                    out[0] = 'ニ';
                    return 0;
                case 'ﾇ':
                    out[0] = 'ヌ';
                    return 0;
                case 'ﾈ':
                    out[0] = 'ネ';
                    return 0;
                case 'ﾉ':
                    out[0] = 'ノ';
                    return 0;
                case 'ﾊ':
                    out[0] = 'ハ';
                    return 0;
                case 'ﾋ':
                    out[0] = 'ヒ';
                    return 0;
                case 'ﾌ':
                    out[0] = 'フ';
                    return 0;
                case 'ﾍ':
                    out[0] = 'ヘ';
                    return 0;
                case 'ﾎ':
                    out[0] = 'ホ';
                    return 0;
                case 'ﾏ':
                    out[0] = 'マ';
                    return 0;
                case 'ﾐ':
                    out[0] = 'ミ';
                    return 0;
                case 'ﾑ':
                    out[0] = 'ム';
                    return 0;
                case 'ﾒ':
                    out[0] = 'メ';
                    return 0;
                case 'ﾓ':
                    out[0] = 'モ';
                    return 0;
                case 'ﾔ':
                    out[0] = 'ヤ';
                    return 0;
                case 'ﾕ':
                    out[0] = 'ユ';
                    return 0;
                case 'ﾖ':
                    out[0] = 'ヨ';
                    return 0;
                case 'ﾗ':
                    out[0] = 'ラ';
                    return 0;
                case 'ﾘ':
                    out[0] = 'リ';
                    return 0;
                case 'ﾙ':
                    out[0] = 'ル';
                    return 0;
                case 'ﾚ':
                    out[0] = 'レ';
                    return 0;
                case 'ﾛ':
                    out[0] = 'ロ';
                    return 0;
                case 'ﾜ':
                    out[0] = 'ワ';
                    return 0;
                case 'ｦ':
                    out[0] = 'ヲ';
                    return 0;
                case 'ﾝ':
                    out[0] = 'ン';
                    return 0;
                case 'ﾞ':
                    out[0] = 'ﾞ';
                    return 0;
                case 'ﾟ':
                    out[0] = 'ﾟ';
                    return 0;
            }
            out[0] = code;
            return 0;
        } else {
            int type = 0;
            char code2 = str.charAt(index + 1);
            if (code2 == 'ﾞ') {
                type = 1;
            } else if (code2 == 'ﾟ') {
                type = 2;
            }
            switch (code) {
                case 'ｧ':
                    out[0] = 'ァ';
                    return 0;
                case 'ｨ':
                    out[0] = 'ィ';
                    return 0;
                case 'ｩ':
                    out[0] = 'ゥ';
                    return 0;
                case 'ｪ':
                    out[0] = 'ェ';
                    return 0;
                case 'ｫ':
                    out[0] = 'ォ';
                    return 0;
                case 'ｬ':
                    out[0] = 'ャ';
                    return 0;
                case 'ｭ':
                    out[0] = 'ュ';
                    return 0;
                case 'ｮ':
                    out[0] = 'ョ';
                    return 0;
                case 'ｯ':
                    out[0] = 'ッ';
                    return 0;
                case 'ｰ':
                    out[0] = 'ー';
                    return 0;
                case 'ｱ':
                    out[0] = 'ア';
                    return 0;
                case 'ｲ':
                    out[0] = 'イ';
                    return 0;
                case 'ｳ':
                    if (type == 1) {
                        out[0] = 'ヴ';
                        return 1;
                    } else {
                        out[0] = 'ウ';
                        return 0;
                    }
                case 'ｴ':
                    out[0] = 'エ';
                    return 0;
                case 'ｵ':
                    out[0] = 'オ';
                    return 0;
                case 'ｶ':
                    if (type == 1) {
                        out[0] = 'ガ';
                        return 1;
                    } else {
                        out[0] = 'カ';
                        return 0;
                    }
                case 'ｷ':
                    if (type == 1) {
                        out[0] = 'ギ';
                        return 1;
                    } else {
                        out[0] = 'キ';
                        return 0;
                    }
                case 'ｸ':
                    if (type == 1) {
                        out[0] = 'グ';
                        return 1;
                    } else {
                        out[0] = 'ク';
                        return 0;
                    }

                case 'ｹ':
                    if (type == 1) {
                        out[0] = 'ゲ';
                        return 1;
                    } else {
                        out[0] = 'ケ';
                        return 0;
                    }
                case 'ｺ':
                    if (type == 1) {
                        out[0] = 'ゴ';
                        return 1;
                    } else {
                        out[0] = 'コ';
                        return 0;
                    }
                case 'ｻ':
                    if (type == 1) {
                        out[0] = 'ザ';
                        return 1;
                    } else {
                        out[0] = 'サ';
                        return 0;
                    }
                case 'ｼ':
                    if (type == 1) {
                        out[0] = 'ジ';
                        return 1;
                    } else {
                        out[0] = 'シ';
                        return 0;
                    }
                case 'ｽ':
                    if (type == 1) {
                        out[0] = 'ズ';
                        return 1;
                    } else {
                        out[0] = 'ス';
                        return 0;
                    }
                case 'ｾ':
                    if (type == 1) {
                        out[0] = 'ゼ';
                        return 1;
                    } else {
                        out[0] = 'セ';
                        return 0;
                    }
                case 'ｿ':
                    if (type == 1) {
                        out[0] = 'ゾ';
                        return 1;
                    } else {
                        out[0] = 'ソ';
                        return 0;
                    }
                case 'ﾀ':
                    if (type == 1) {
                        out[0] = 'ダ';
                        return 1;
                    } else {
                        out[0] = 'タ';
                        return 0;
                    }
                case 'ﾁ':
                    if (type == 1) {
                        out[0] = 'ヂ';
                        return 1;
                    } else {
                        out[0] = 'チ';
                        return 0;
                    }
                case 'ﾂ':
                    if (type == 1) {
                        out[0] = 'ヅ';
                        return 1;
                    } else {
                        out[0] = 'ツ';
                        return 0;
                    }
                case 'ﾃ':
                    if (type == 1) {
                        out[0] = 'デ';
                        return 1;
                    } else {
                        out[0] = 'テ';
                        return 0;
                    }
                case 'ﾄ':
                    if (type == 1) {
                        out[0] = 'ド';
                        return 1;
                    } else {
                        out[0] = 'ト';
                        return 0;
                    }
                case 'ﾅ':
                    out[0] = 'ナ';
                    return 0;
                case 'ﾆ':
                    out[0] = 'ニ';
                    return 0;
                case 'ﾇ':
                    out[0] = 'ヌ';
                    return 0;
                case 'ﾈ':
                    out[0] = 'ネ';
                    return 0;
                case 'ﾉ':
                    out[0] = 'ノ';
                    return 0;
                case 'ﾊ':
                    if (type == 1) {
                        out[0] = 'バ';
                        return 1;
                    } else if (type == 2) {
                        out[0] = 'パ';
                        return 1;
                    } else {
                        out[0] = 'ハ';
                        return 0;
                    }
                case 'ﾋ':
                    if (type == 1) {
                        out[0] = 'ビ';
                        return 1;
                    } else if (type == 2) {
                        out[0] = 'ピ';
                        return 1;
                    } else {
                        out[0] = 'ヒ';
                        return 0;
                    }
                case 'ﾌ':
                    if (type == 1) {
                        out[0] = 'ブ';
                        return 1;
                    } else if (type == 2) {
                        out[0] = 'プ';
                        return 1;
                    } else {
                        out[0] = 'フ';
                        return 0;
                    }
                case 'ﾍ':
                    if (type == 1) {
                        out[0] = 'ベ';
                        return 1;
                    } else if (type == 2) {
                        out[0] = 'ペ';
                        return 1;
                    } else {
                        out[0] = 'ヘ';
                        return 0;
                    }
                case 'ﾎ':
                    if (type == 1) {
                        out[0] = 'ボ';
                        return 1;
                    } else if (type == 2) {
                        out[0] = 'ポ';
                        return 1;
                    } else {
                        out[0] = 'ホ';
                        return 0;
                    }
                case 'ﾏ':
                    out[0] = 'マ';
                    return 0;
                case 'ﾐ':
                    out[0] = 'ミ';
                    return 0;
                case 'ﾑ':
                    out[0] = 'ム';
                    return 0;
                case 'ﾒ':
                    out[0] = 'メ';
                    return 0;
                case 'ﾓ':
                    out[0] = 'モ';
                    return 0;
                case 'ﾔ':
                    out[0] = 'ヤ';
                    return 0;
                case 'ﾕ':
                    out[0] = 'ユ';
                    return 0;
                case 'ﾖ':
                    out[0] = 'ヨ';
                    return 0;
                case 'ﾗ':
                    out[0] = 'ラ';
                    return 0;
                case 'ﾘ':
                    out[0] = 'リ';
                    return 0;
                case 'ﾙ':
                    out[0] = 'ル';
                    return 0;
                case 'ﾚ':
                    out[0] = 'レ';
                    return 0;
                case 'ﾛ':
                    out[0] = 'ロ';
                    return 0;
                case 'ﾜ':
                    out[0] = 'ワ';
                    return 0;
                case 'ｦ':
                    out[0] = 'ヲ';
                    return 0;
                case 'ﾝ':
                    out[0] = 'ン';
                    return 0;
                case 'ﾞ':
                    out[0] = 'ﾞ';
                    return 0;
                case 'ﾟ':
                    out[0] = 'ﾟ';
                    return 0;
            }
        }
        out[0] = code;
        return 0;
    }

    /**
     * 全角文字内容をすべて半角文字内容に変換.
     *
     * @param mode   [true]の場合HTML形式で変換します.
     * @param string 変換対象情報を設定します.
     * @return String 変換された内容が返されます.
     */
    public static final String toSmall(boolean mode, String string) {
        if (string == null || string.length() <= 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int len = string.length();
        for (int i = 0; i < len; i++) {
            buf.append(toSmallOne(mode, string.charAt(i)));
        }
        return buf.toString();
    }

    /**
     * 全角文字を半角文字に変更.
     */
    private static final String toSmallOne(boolean mode, char code) {
        switch (code) {
            case 'ァ':
                return "ｧ";
            case 'ィ':
                return "ｨ";
            case 'ゥ':
                return "ｩ";
            case 'ェ':
                return "ｪ";
            case 'ォ':
                return "ｫ";
            case 'ャ':
                return "ｬ";
            case 'ュ':
                return "ｭ";
            case 'ョ':
                return "ｮ";
            case 'ッ':
                return "ｯ";
            case 'ー':
                return "ｰ";
            case 'ア':
                return "ｱ";
            case 'イ':
                return "ｲ";
            case 'ウ':
                return "ｳ";
            case 'エ':
                return "ｴ";
            case 'オ':
                return "ｵ";
            case 'カ':
                return "ｶ";
            case 'キ':
                return "ｷ";
            case 'ク':
                return "ｸ";
            case 'ケ':
                return "ｹ";
            case 'コ':
                return "ｺ";
            case 'サ':
                return "ｻ";
            case 'シ':
                return "ｼ";
            case 'ス':
                return "ｽ";
            case 'セ':
                return "ｾ";
            case 'ソ':
                return "ｿ";
            case 'タ':
                return "ﾀ";
            case 'チ':
                return "ﾁ";
            case 'ツ':
                return "ﾂ";
            case 'テ':
                return "ﾃ";
            case 'ト':
                return "ﾄ";
            case 'ナ':
                return "ﾅ";
            case 'ニ':
                return "ﾆ";
            case 'ヌ':
                return "ﾇ";
            case 'ネ':
                return "ﾈ";
            case 'ノ':
                return "ﾉ";
            case 'ハ':
                return "ﾊ";
            case 'ヒ':
                return "ﾋ";
            case 'フ':
                return "ﾌ";
            case 'ヘ':
                return "ﾍ";
            case 'ホ':
                return "ﾎ";
            case 'マ':
                return "ﾏ";
            case 'ミ':
                return "ﾐ";
            case 'ム':
                return "ﾑ";
            case 'メ':
                return "ﾒ";
            case 'モ':
                return "ﾓ";
            case 'ヤ':
                return "ﾔ";
            case 'ユ':
                return "ﾕ";
            case 'ヨ':
                return "ﾖ";
            case 'ラ':
                return "ﾗ";
            case 'リ':
                return "ﾘ";
            case 'ル':
                return "ﾙ";
            case 'レ':
                return "ﾚ";
            case 'ロ':
                return "ﾛ";
            case 'ワ':
                return "ﾜ";
            case 'ヲ':
                return "ｦ";
            case 'ン':
                return "ﾝ";
            case 'ガ':
                return "ｶﾞ";
            case 'ギ':
                return "ｷﾞ";
            case 'グ':
                return "ｸﾞ";
            case 'ゲ':
                return "ｹﾞ";
            case 'ゴ':
                return "ｺﾞ";
            case 'ザ':
                return "ｻﾞ";
            case 'ジ':
                return "ｼﾞ";
            case 'ズ':
                return "ｽﾞ";
            case 'ゼ':
                return "ｾﾞ";
            case 'ゾ':
                return "ｿﾞ";
            case 'ダ':
                return "ﾀﾞ";
            case 'ヂ':
                return "ﾁﾞ";
            case 'ヅ':
                return "ﾂﾞ";
            case 'デ':
                return "ﾃﾞ";
            case 'ド':
                return "ﾄﾞ";
            case 'パ':
                return "ﾊﾟ";
            case 'ピ':
                return "ﾋﾟ";
            case 'プ':
                return "ﾌﾟ";
            case 'ペ':
                return "ﾍﾟ";
            case 'ポ':
                return "ﾎﾟ";
            case 'バ':
                return "ﾊﾞ";
            case 'ビ':
                return "ﾋﾞ";
            case 'ブ':
                return "ﾌﾞ";
            case 'ベ':
                return "ﾍﾞ";
            case 'ボ':
                return "ﾎﾞ";
            case 'ヴ':
                return "ｳﾞ";
            case '！':
                return "!";
            case '”':
                return (mode) ? "&quot;" : "\"";
            case '＃':
                return "#";
            case '＄':
                return "$";
            case '￥':
                return "\\";
            case '％':
                return "%";
            case '＆':
                return "&";
            case '’':
                return "\'";
            case '（':
                return "(";
            case '）':
                return ")";
            case '＊':
                return "*";
            case '＋':
                return "+";
            case '，':
                return ",";
            case '－':
                return "-";
            case '．':
                return ".";
            case '／':
                return "/";
            case '０':
                return "0";
            case '１':
                return "1";
            case '２':
                return "2";
            case '３':
                return "3";
            case '４':
                return "4";
            case '５':
                return "5";
            case '６':
                return "6";
            case '７':
                return "7";
            case '８':
                return "8";
            case '９':
                return "9";
            case '：':
                return ":";
            case '；':
                return ";";
            case '＜':
                return (mode) ? "&lt;" : "<";
            case '＝':
                return "=";
            case '＞':
                return (mode) ? "&gt;" : ">";
            case '？':
                return "?";
            case '＠':
                return "@";
            case 'Ａ':
                return "A";
            case 'Ｂ':
                return "B";
            case 'Ｃ':
                return "C";
            case 'Ｄ':
                return "D";
            case 'Ｅ':
                return "E";
            case 'Ｆ':
                return "F";
            case 'Ｇ':
                return "G";
            case 'Ｈ':
                return "H";
            case 'Ｉ':
                return "I";
            case 'Ｊ':
                return "J";
            case 'Ｋ':
                return "K";
            case 'Ｌ':
                return "L";
            case 'Ｍ':
                return "M";
            case 'Ｎ':
                return "N";
            case 'Ｏ':
                return "O";
            case 'Ｐ':
                return "P";
            case 'Ｑ':
                return "Q";
            case 'Ｒ':
                return "R";
            case 'Ｓ':
                return "S";
            case 'Ｔ':
                return "T";
            case 'Ｕ':
                return "U";
            case 'Ｖ':
                return "V";
            case 'Ｗ':
                return "W";
            case 'Ｘ':
                return "X";
            case 'Ｙ':
                return "Y";
            case 'Ｚ':
                return "Z";
            case '＾':
                return "^";
            case '＿':
                return "_";
            case '‘':
                return "`";
            case 'ａ':
                return "a";
            case 'ｂ':
                return "b";
            case 'ｃ':
                return "c";
            case 'ｄ':
                return "d";
            case 'ｅ':
                return "e";
            case 'ｆ':
                return "f";
            case 'ｇ':
                return "g";
            case 'ｈ':
                return "h";
            case 'ｉ':
                return "i";
            case 'ｊ':
                return "j";
            case 'ｋ':
                return "k";
            case 'ｌ':
                return "l";
            case 'ｍ':
                return "m";
            case 'ｎ':
                return "n";
            case 'ｏ':
                return "o";
            case 'ｐ':
                return "p";
            case 'ｑ':
                return "q";
            case 'ｒ':
                return "r";
            case 'ｓ':
                return "s";
            case 'ｔ':
                return "t";
            case 'ｕ':
                return "u";
            case 'ｖ':
                return "v";
            case 'ｗ':
                return "w";
            case 'ｘ':
                return "x";
            case 'ｙ':
                return "y";
            case 'ｚ':
                return "z";
            case '｛':
                return "{";
            case '｜':
                return "|";
            case '｝':
                return "}";
            case '。':
                return "｡";
            case '「':
                return "｢";
            case '」':
                return "｣";
            case '、':
                return "､";
            case '・':
                return "･";
            case '　':
                return (mode) ? "&nbsp;" : " ";
        }
        return new String(new char[]{code});
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
}
