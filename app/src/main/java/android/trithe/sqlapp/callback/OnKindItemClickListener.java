package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.rest.model.KindModel;
import android.widget.ImageView;

public interface OnKindItemClickListener {
    void onKind(KindModel kindModel, ImageView imageView);
}
