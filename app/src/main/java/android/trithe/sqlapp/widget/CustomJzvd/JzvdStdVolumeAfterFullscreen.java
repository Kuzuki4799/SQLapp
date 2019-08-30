package android.trithe.sqlapp.widget.CustomJzvd;

import android.content.Context;
import android.trithe.sqlapp.widget.Jz.JzvdStd;
import android.util.AttributeSet;


public class JzvdStdVolumeAfterFullscreen extends JzvdStd {
    public JzvdStdVolumeAfterFullscreen(Context context) {
        super(context);
    }

    public JzvdStdVolumeAfterFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (screen == SCREEN_FULLSCREEN) {
            mediaInterface.setVolume(1f, 1f);
        } else {
            mediaInterface.setVolume(0f, 0f);
        }
    }

    @Override
    public void gotoScreenFullscreen() {
        super.gotoScreenFullscreen();
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (mediaInterface != null)
            mediaInterface.setVolume(1f, 1f);
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();
        if (mediaInterface != null)
            mediaInterface.setVolume(0f, 0f);
    }
}
