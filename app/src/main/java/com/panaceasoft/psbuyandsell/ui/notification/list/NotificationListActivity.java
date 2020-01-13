package com.panaceasoft.psbuyandsell.ui.notification.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.fragment.app.Fragment;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityNotificationListBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationListActivity extends PSAppCompactActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityNotificationListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_list);

        // Init all UI
        initUI(binding);

    }

    //endregion


    //region Private Methods

    private void initUI(ActivityNotificationListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.notification__title));

        // setup Fragment
        setupFragment(new NotificationListFragment());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //endregion


}