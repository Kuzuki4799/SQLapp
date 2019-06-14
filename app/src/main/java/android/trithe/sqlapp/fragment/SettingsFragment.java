package android.trithe.sqlapp.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.LockPassActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private Switch swNotification;
    private RelativeLayout llLockApp;
    public static final int REQUEST_LOCK_PASS = 999;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        llLockApp.setOnClickListener(v -> {
            Intent intents = new Intent(getContext(), LockPassActivity.class);
            startActivityForResult(intents, REQUEST_LOCK_PASS);
        });
        return view;
    }

    private void initView(View view) {
        swNotification = view.findViewById(R.id.swNotification);
        llLockApp = view.findViewById(R.id.llLockApp);

        swNotification.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swNotification) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
