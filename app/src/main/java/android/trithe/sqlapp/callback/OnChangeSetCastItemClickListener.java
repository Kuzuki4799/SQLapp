package android.trithe.sqlapp.callback;

import android.support.v7.widget.CardView;

public interface OnChangeSetCastItemClickListener {
    void onCheckItemCast(int position, CardView cardView);

    void changSetDataCast(int position, String key);
}
