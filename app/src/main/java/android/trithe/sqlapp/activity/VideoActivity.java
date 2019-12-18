package android.trithe.sqlapp.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.widget.CustomJzvd.MyJzvdStd;
import android.trithe.sqlapp.widget.Jz.Jzvd;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class VideoActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        MyJzvdStd videoView = findViewById(R.id.videoView);
        ImageView imgBack = findViewById(R.id.imgBack);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.inters_video_ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        new Handler().postDelayed(() -> interstitialAd.show(), 10000);

        videoView.setUp(getIntent().getStringExtra(Constant.VIDEO), getIntent().getStringExtra(Constant.PREFERENCE_NAME));
        Glide.with(this).load(getIntent().getStringExtra(Constant.IMAGE)).into(videoView.thumbImageView);
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        finish();
        super.onBackPressed();
    }
}
