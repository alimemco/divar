package com.panaceasoft.psbuyandsell.ui.customcamera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityCameraBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CameraActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCameraBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);

        initUI(activityFilteringBinding);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        finish();
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
//        if (fragment != null) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void initUI(ActivityCameraBinding binding) {

//        initToolbar(binding.toolbar, getIntent().getStringExtra(Constants.CATEGORY_NAME));
        setupFragment(new CameraFragment());

    }



}
