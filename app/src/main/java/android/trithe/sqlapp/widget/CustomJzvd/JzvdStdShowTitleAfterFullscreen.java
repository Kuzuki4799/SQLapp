package android.trithe.sqlapp.widget.CustomJzvd;

import android.content.Context;
import android.trithe.sqlapp.widget.Jz.JZDataSource;
import android.trithe.sqlapp.widget.Jz.JzvdStd;
import android.util.AttributeSet;
import android.view.View;


public class JzvdStdShowTitleAfterFullscreen extends JzvdStd {
    public JzvdStdShowTitleAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdShowTitleAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        if (this.screen == SCREEN_FULLSCREEN) {
            titleTextView.setVisibility(View.VISIBLE);
        } else {
            titleTextView.setVisibility(View.INVISIBLE);
        }
    }
}
