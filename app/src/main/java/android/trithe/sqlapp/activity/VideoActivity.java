package android.trithe.sqlapp.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String url = getIntent().getStringExtra(Constant.VIDEO);
        VideoView videoView = findViewById(R.id.videoView);
        if (getIntent().getStringExtra(Constant.TYPE).equals(Constant.TYPE_FILM)) {
            videoView.setVideoURI(Uri.parse(url));
        } else {
            videoView.setVideoURI(Uri.parse(Config.LINK_LOAD_IMAGE + url));
        }
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
