package android.trithe.sqlapp.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.databinding.LayoutCcSliderBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

public class CustomSliderView extends BaseSliderView {
    public CustomSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        LayoutCcSliderBinding viewBinding;
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_cc_slider, null, false);
        bindEventAndShow(viewBinding.getRoot(), viewBinding.ivImage);
        return viewBinding.getRoot();
    }
}