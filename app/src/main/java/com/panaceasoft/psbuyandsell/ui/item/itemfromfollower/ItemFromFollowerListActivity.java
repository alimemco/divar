package com.panaceasoft.psbuyandsell.ui.item.itemfromfollower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityItemFromFollowerListBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ItemFromFollowerListActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityItemFromFollowerListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_item_from_follower_list);

        // Init all UI
        initUI(binding);
    }


    //endregion

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //region Private Methods

    private void initUI(ActivityItemFromFollowerListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.item_from_follower_toolbar));

        // setup Fragment
        setupFragment(new ItemFromFollowerListFragment());

    }

    //endregion
}
