package android.trithe.sqlapp.callback;

import android.support.v7.widget.CardView;

public interface OnRemoveItemClickListener {
    void onCheckItem(int position, CardView cardView);

    void onRemoveItem(int position);
}
