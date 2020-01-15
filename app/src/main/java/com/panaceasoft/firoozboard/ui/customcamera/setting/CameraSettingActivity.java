package com.panaceasoft.firoozboard.ui.customcamera.setting;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityCameraSettingBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CameraSettingActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCameraSettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_setting);

        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityCameraSettingBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, "Camera setting");//getResources().getString(R.string.notification_setting__title));

        // setup Fragment
        setupFragment(new CameraSettingFragment());

    }

    //endregion

}
