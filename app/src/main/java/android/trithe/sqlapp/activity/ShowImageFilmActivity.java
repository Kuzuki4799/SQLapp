package android.trithe.sqlapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.config.Config;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ShowImageFilmActivity extends AppCompatActivity {
    private ImageView img;
    private ImageView ivDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_film);
        initView();
        Glide.with(this).load(Config.LINK_LOAD_IMAGE + getIntent().getStringExtra("image")).into(img);
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        img = findViewById(R.id.img);
        ivDismiss = findViewById(R.id.ivDismiss);
    }
}
