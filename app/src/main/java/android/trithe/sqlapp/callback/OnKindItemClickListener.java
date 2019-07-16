package android.trithe.sqlapp.callback;

import android.support.v7.widget.CardView;
import android.trithe.sqlapp.rest.model.KindModel;

public interface OnKindItemClickListener {
    void onKind(KindModel kindModel, CardView cardView);
}
