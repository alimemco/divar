package com.panaceasoft.firoozboard.ui.item.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityItemEntryBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

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

