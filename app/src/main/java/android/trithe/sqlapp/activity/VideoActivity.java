package android.trithe.sqlapp.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String url = getIntent().getStringExtra(Constant.VIDEO);
        videoView = findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(url));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
        videoView.start();
        if (SharedPrefUtils.getInt(Constant.POSITION, 0) != 0) {
            pos = SharedPrefUtils.getInt(Constant.POSITION, 0);
        } else {
            if (savedInstanceState != null) {
                pos = savedInstanceState.getInt(Constant.POSITION);
            }
        }
        videoView.seekTo(pos);
        SharedPrefUtils.putInt(Constant.POSITION, 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (videoView.isPlaying())
            outState.putInt(Constant.POSITION, videoView.getCurrentPosition());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
