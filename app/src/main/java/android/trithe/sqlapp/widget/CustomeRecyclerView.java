package android.trithe.sqlapp.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class CustomeRecyclerView extends RecyclerView {

    Context context;

    public CustomeRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public CustomeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX*= 0.7;
        return super.fling(velocityX, velocityY);
    }

}
