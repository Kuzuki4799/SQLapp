package android.trithe.sqlapp.widget.CustomJzvd;

import android.content.Context;
import android.trithe.sqlapp.widget.Jz.JZDataSource;
import android.trithe.sqlapp.widget.Jz.JZUtils;
import android.trithe.sqlapp.widget.Jz.JzvdStd;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class JzvdStdTinyWindow extends JzvdStd {
    public JzvdStdTinyWindow(Context context) {
        super(context);
    }

    public JzvdStdTinyWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen, Class mediaInterface) {
        super.setUp(jzDataSource, screen, mediaInterface);
    }

    public void gotoScreenTiny() {
        Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");
        if (state == STATE_NORMAL || state == STATE_ERROR || state == STATE_AUTO_COMPLETE)
            return;
        ViewGroup vg = (ViewGroup) getParent();
        vg.removeView(this);
        cloneAJzvd(vg);
        CONTAINER_LIST.add(vg);
        ViewGroup vgg = (ViewGroup) (JZUtils.scanForActivity(getContext())).getWindow().getDecorView();//和他也没有关系
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        vgg.addView(this, lp);
        setScreenTiny();
    }


}
