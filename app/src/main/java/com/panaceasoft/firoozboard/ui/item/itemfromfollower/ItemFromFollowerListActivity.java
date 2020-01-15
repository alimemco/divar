package com.panaceasoft.firoozboard.ui.item.itemfromfollower;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityItemFromFollowerListBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

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
