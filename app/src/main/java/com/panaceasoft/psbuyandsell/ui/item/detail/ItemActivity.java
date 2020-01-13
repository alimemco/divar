package com.panaceasoft.psbuyandsell.ui.item.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityItemBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;
import com.panaceasoft.psbuyandsell.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ItemActivity extends PSAppCompactActivity {
    public ActivityItemBinding binding;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding = DataBindingUtil.setContentView(this, R.layout.activity_item);


        initUI(binding);

    }


    private void initUI(ActivityItemBinding binding) {

/*       // String title = getIntent().getStringExtra(Constants.ITEM_NAME);
        binding.toolbar.setBackgroundColor(getResources().getColor(R.color.md_grey_50));
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.md_black_1000));
       // initToolbar(binding.toolbar, title);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try {
            setSupportActionBar(binding.toolbar);
        }catch (Exception e) {
            Utils.psErrorLog("Error in set support action bar.", e);
        }

        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }catch (Exception e) {
            Utils.psErrorLog("Error in set display home as up enabled.", e);
        }*/
        // setup Fragment
        setupFragment(new ItemFragment());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
