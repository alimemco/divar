package com.panaceasoft.psbuyandsell.ui.user.verifyemail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityVerifyEmailBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VerifyEmailActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityVerifyEmailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_email);
        // Init all UI
        initUI(binding);
        
    }



    //endregion


    //region Private Methods

    private void initUI(ActivityVerifyEmailBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getString(R.string.history__title));

        // setup Fragment
        setupFragment(new VerifyEmailFragment());

    }

}
