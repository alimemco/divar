package com.panaceasoft.psbuyandsell.ui.item.entry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityItemEntryBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ItemEntryActivity extends PSAppCompactActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityItemEntryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_item_entry);

        // Init all UI
        initUI(binding);

    }



    //endregion


    //region Private Methods

    private void initUI(ActivityItemEntryBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getString(R.string.item_entry_toolbar));

        // setup Fragment
        setupFragment(new ItemEntryFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

