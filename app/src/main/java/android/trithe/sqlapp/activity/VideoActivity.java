package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.widget.CustomJzvd.MyJzvdStd;
import android.trithe.sqlapp.widget.Jz.Jzvd;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        MyJzvdStd videoView = findViewById(R.id.videoView);
        ImageView imgBack = findViewById(R.id.imgBack);

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
