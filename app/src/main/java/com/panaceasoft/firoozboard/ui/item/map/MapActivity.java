package com.panaceasoft.firoozboard.ui.item.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityMapBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.Utils;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MapActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map);

        // Init all UI
        initUI(binding);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.psLog("Inside Result MainActivity");
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //region Private Methods

    private void initUI(ActivityMapBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.MAP_FLAG);

        switch (fragName) {
            case Constants.MAP:
                setupFragment(new MapFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.map_filter__map_title));
                break;
            case Constants.MAP_PICK:
                setupFragment(new PickMapFragment());
                initToolbar(binding.toolbar, getResources().getString(R.string.map_filter__map_title2));
                break;
        }
    }
}
