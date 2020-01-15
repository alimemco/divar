package com.panaceasoft.firoozboard.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivitySearchByCategoryBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardSearchByCategoryActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchByCategoryBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_category);

        initUI(databinding);

    }


    protected void initUI(ActivitySearchByCategoryBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.CATEGORY_FLAG);

        if (fragName.equals(Constants.CATEGORY)) {
            setupFragment(new DashBoardSearchCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_cat_title));
        } else if (fragName.equals(Constants.SUBCATEGORY)) {
            setupFragment(new DashBoardSearchSubCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_sub_cat_title));
        }


    }
}
