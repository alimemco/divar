package com.panaceasoft.firoozboard.ui.user;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityPasswordChangeBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PasswordChangeActivity extends PSAppCompactActivity {

    private static final String TAG = "PasswordChangeLog";

    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods
    private String code;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPasswordChangeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_password_change);

        code = getIntent().getStringExtra(Constants.VALIDATION_CODE);
        phone = getIntent().getStringExtra(Constants.USER_PHONE);

        // Init all UI
        initUI(binding);


    }


    //endregion


    //region Private Methods

    private void initUI(ActivityPasswordChangeBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.password_change__password_change));

        // setup Fragment
        setupFragment(PasswordChangeFragment.newInstance(phone, code));
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}
