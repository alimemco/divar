package com.panaceasoft.firoozboard.ui.subcategory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivitySubCategoryBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubCategoryActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySubCategoryBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category);

        initUI(activityFilteringBinding);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI(ActivitySubCategoryBinding binding) {

        initToolbar(binding.toolbar, getIntent().getStringExtra(Constants.CATEGORY_NAME));

        setupFragment(new SubCategoryFragment());

    }

}
