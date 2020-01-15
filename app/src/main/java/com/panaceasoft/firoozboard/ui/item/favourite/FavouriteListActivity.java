package com.panaceasoft.firoozboard.ui.item.favourite;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityFavouriteListBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavouriteListActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityFavouriteListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_list);

        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityFavouriteListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.favourite__title));

        // setup Fragment
        setupFragment(new FavouriteListFragment());

    }

    //endregion

}