package android.trithe.sqlapp.widget.CustomJzvd;

import android.content.Context;
import android.trithe.sqlapp.widget.Jz.JZDataSource;
import android.trithe.sqlapp.widget.Jz.JZUtils;
import android.trithe.sqlapp.widget.Jz.Jzvd;
import android.trithe.sqlapp.widget.Jz.JzvdStd;
import android.util.AttributeSet;

public class JzvdStdList extends JzvdStd {
    public JzvdStdList(Context context) {
        super(context);
    }

    public JzvdStdList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterface) {
        super.setUp(jzDataSource, screen, mediaInterface);

        if (jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
            long position = 0;
            try {
                position = Jzvd.CURRENT_JZVD.mediaInterface.getCurrentPosition();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            if (position != 0) {
                JZUtils.saveProgress(getContext(), Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl(), position);
            }
            Jzvd.releaseAllVideos();
        }

    }
}
