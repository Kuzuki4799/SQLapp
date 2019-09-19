package android.trithe.sqlapp.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.view.View;
import android.widget.ProgressBar;

import io.reactivex.annotations.NonNull;


public class LoadingViewHolder extends RecyclerView.ViewHolder {

    ProgressBar progressBar;
    public static final int LAYOUT_ID = R.layout.item_progress;
    public static final int LAYOUT_ID_HORIZONTAL = R.layout.item_progress_hozentical;
    public static final int LAYOUT_ID_HORIZONTAL_TWO = R.layout.item_progress_hozentical_two;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.pBar);
    }

    public void isHide() {
        progressBar.setVisibility(View.GONE);
    }
}