package com.panaceasoft.firoozboard.ui.item.search.searchlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivitySettingBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SearchListActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

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

    private void initUI(ActivitySettingBinding binding) {

        String title = getIntent().getStringExtra(Constants.ITEM_NAME);

        if (title != null) {
            initToolbar(binding.toolbar, title);
        } else {
            initToolbar(binding.toolbar, getString(R.string.item_list_title));
        }

        setupFragment(new SearchListFragment());

        // setup Fragment

        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

}
