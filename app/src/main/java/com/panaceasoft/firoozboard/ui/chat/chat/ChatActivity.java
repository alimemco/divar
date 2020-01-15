package com.panaceasoft.firoozboard.ui.chat.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityChatBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChatActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @VisibleForTesting
    ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        binding = dataBinding;

        // Init all UI
        initUI(dataBinding);
    }



    //region Private Methods

    private void initUI(ActivityChatBinding binding) {

        // setup Fragment
        setupFragment(new ChatFragment());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void changeText(String text) {
        initToolbar(binding.toolbar, text);
    }
}
