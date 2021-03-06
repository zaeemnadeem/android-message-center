package com.usepace.android.messagingcenter.screens.sendbird;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.sendbird.android.SendBird;
import com.usepace.android.messagingcenter.R;
import com.usepace.android.messagingcenter.clients.connection_client.MessageCenter;
import com.usepace.android.messagingcenter.utils.PreferenceUtils;


public class SendBirdChatActivity extends AppCompatActivity{


    private onBackPressedListener mOnBackPressedListener;
    private Toolbar toolbar;
    private TextView toolbarSubtitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);
        MessageCenter.clearNotificationInboxMessages();
        init();
    }

    private void init() {
        SendBird.setAutoBackgroundDetection(true);
        PreferenceUtils.init(this);
        initToolBar();
        String channelUrl = getIntent().getStringExtra("CHANNEL_URL");

        if(channelUrl != null) {
            Fragment fragment = SendBirdChatFragment.newInstance(channelUrl);
            Bundle bundle = new Bundle();
            bundle.putString("CHANNEL_URL", channelUrl);
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();
            manager.beginTransaction().replace(R.id.container_group_channel, fragment).commit();
        }
        else {
            finish();
        }
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.my_toolbar);
        toolbarSubtitle = findViewById(R.id.toolbar_subtitle);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().hasExtra("TITLE") ? getIntent().getStringExtra("TITLE") : getString(R.string.message_center_toolbar_title));
        }
        toolbarSubtitle.setText(getIntent().hasExtra("SUBTITLE") ? getIntent().getStringExtra("SUBTITLE") : "");
    }

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("close") && intent.getBooleanExtra("close", false)) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.init(this);
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SendBird.setAutoBackgroundDetection(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface onBackPressedListener {
        boolean onBack();
    }
}