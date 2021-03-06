package com.panaceasoft.firoozboard.ui.chat.chatimage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityChatImageFullScreenBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ChatImageFullScreenActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatImageFullScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_image_full_screen);

        // Init all UI
        initUI(binding);
    }




    //region Private Methods

    private void initUI(ActivityChatImageFullScreenBinding binding) {

        // Toolbar
//        initToolbar(binding.toolbar, getResources().getString(R.string.comment__detail_title));

        // setup Fragment
        ChatImageFullScreenFragment chatFragment = new ChatImageFullScreenFragment();
        setupFragment(chatFragment);
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}
