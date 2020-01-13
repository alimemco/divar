package com.panaceasoft.psbuyandsell.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityProfileEditBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileEditActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProfileEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit);

        // Init all UI
        initUI(binding);

    }



    //endregion


    //region Private Methods

    private void initUI(ActivityProfileEditBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.edit_profile__title));

        // setup Fragment
        setupFragment(new ProfileEditFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}