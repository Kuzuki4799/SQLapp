package android.trithe.sqlapp.widget.PullToRefresh;

import android.content.Context;
import android.trithe.sqlapp.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pnikosis.materialishprogress.ProgressWheel;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


public class MyPullToRefresh extends PtrClassicFrameLayout {

    private OnRefreshBegin onRefreshBegin;

    public MyPullToRefresh(Context context) {
        super(context);
    }

    public MyPullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPullToRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnRefreshBegin(View view, PullToRefreshHeader headerView, OnRefreshBegin onRefreshBegin) {
        this.onRefreshBegin = onRefreshBegin;
        if (headerView != null) {
            this.setHeaderView(headerView);
            this.addPtrUIHandler(headerView);
        } else {
            PtrClassicDefaultHeader defaultHeader = new PtrClassicDefaultHeader(getContext());
            this.setHeaderView(defaultHeader);
            this.addPtrUIHandler(defaultHeader);
        }
        this.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                try {
                    return headerView.getCurrentPosY() < 1.5 * frame.getHeaderHeight()
                            && PtrDefaultHandler.checkContentCanBePulledDown(frame, view, header);
                } catch (Exception e) {
                    return true;
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefreshBegin.refresh();
            }
        });
    }

    public void refresh() {

    }

    public interface OnRefreshBegin {
        void refresh();
    }

    public static class PullToRefreshHeader extends RelativeLayout implements PtrUIHandler {

        private ProgressWheel progressWheel;
        private LayoutParams layoutParams;

        private int currentPosY;

        public int getCurrentPosY() {
            return currentPosY;
        }

        public PullToRefreshHeader(Context context) {
            super(context);
            initViews();
        }

        public PullToRefreshHeader(Context context, AttributeSet attrs) {
            super(context, attrs);
            initViews();
        }

        public PullToRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initViews();
        }

        protected void initViews() {
            View header = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header,
                    this);
            progressWheel = header.findViewById(R.id.progress_wheel);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        }

        @Override
        public void onUIReset(PtrFrameLayout frame) {
            progressWheel.setVisibility(VISIBLE);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        }

        @Override
        public void onUIRefreshPrepare(PtrFrameLayout frame) {
        }

        @Override
        public void onUIRefreshBegin(PtrFrameLayout frame) {
            progressWheel.spin();
        }

        @Override
        public void onUIRefreshComplete(PtrFrameLayout frame) {
            progressWheel.setVisibility(GONE);
        }

        @Override
        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch,
                                       byte status, PtrIndicator ptrIndicator) {
            currentPosY = ptrIndicator.getCurrentPosY();
            float percent = Math.min(1f, ptrIndicator.getCurrentPercent());
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                progressWheel.setProgress(percent);

            }
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE
                    || status == PtrFrameLayout.PTR_STATUS_LOADING) {
                if (ptrIndicator.getCurrentPercent() > 1f) {
                    layoutParams.bottomMargin =
                            ptrIndicator.getCurrentPosY() - ptrIndicator.getHeaderHeight();
                    layoutParams.topMargin =
                            ptrIndicator.getHeaderHeight() - ptrIndicator.getCurrentPosY();
                    setLayoutParams(layoutParams);
                }
            }
            invalidate();
        }
    }

}
