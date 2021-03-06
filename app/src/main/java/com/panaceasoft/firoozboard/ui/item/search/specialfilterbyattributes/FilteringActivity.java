package com.panaceasoft.firoozboard.ui.item.search.specialfilterbyattributes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityFilteringBinding;
import com.panaceasoft.firoozboard.ui.category.categoryfilter.CategoryFilterFragment;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class FilteringActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFilteringBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_filtering);

        // Init all UI
        initUI(binding);

    }



    //endregion


    //region Private Methods

    private void initUI(ActivityFilteringBinding binding) {

        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.FILTERING_FILTER_NAME);

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {

            // Toolbar
            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI_Type__Button));

            // setup Fragment
            setupFragment(new CategoryFilterFragment());
            // Or you can call like this
            //setupFragment(new NewsListFragment(), R.id.content_frame);
        } else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI_Tune__Button));

            setupFragment(new FilteringFragment());
        }

    }

    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
    }
}
