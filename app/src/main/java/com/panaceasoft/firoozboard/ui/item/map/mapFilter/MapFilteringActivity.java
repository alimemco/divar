package com.panaceasoft.firoozboard.ui.item.map.mapFilter;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityMapFilteringBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MapFilteringActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapFilteringBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_map_filtering);

        // Init all UI
        initUI(binding);
    }

//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Utils.psLog("Inside Result MainActivity");
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//
//    }
//        Fragment fragment = getSupportFragmentManager().findFragmentBDouble.valueOf(lngValue)yId(R.id.content_frame);
//        fragment.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        navigationController.navigateBackToSearchFromMapFiltering(this, null);
    }

    //region Private Methods

    private void initUI(ActivityMapFilteringBinding binding) {
        initToolbar(binding.toolbar, getResources().getString(R.string.map_filter__map_title));
        setupFragment(new MapFilteringFragment());
    }
}
