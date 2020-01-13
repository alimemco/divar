package com.panaceasoft.psbuyandsell.ui.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityLocationBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.ui.item.itemlocation.ItemLocationFragment;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LocationActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLocationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityLocationBinding binding) {

        // Toolbar
        //initToolbar(binding.toolbar, "Location");

        setupFragment(new ItemLocationFragment());

    }

}