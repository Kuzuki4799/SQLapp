package android.trithe.sqlapp.widget.CustomJzvd;

import android.content.Context;
import android.trithe.sqlapp.widget.Jz.JzvdStd;
import android.util.AttributeSet;
import android.view.View;

public class JzvdStdShowTextureViewAfterAutoComplete extends JzvdStd {
    public JzvdStdShowTextureViewAfterAutoComplete(Context context) {
        super(context);
    }

    public JzvdStdShowTextureViewAfterAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        thumbImageView.setVisibility(View.GONE);
    }

}
