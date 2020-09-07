package com.panaceasoft.firoozboard.ui.user.verifyemail;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityVerifyEmailBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class VerifyEmailActivity extends PSAppCompactActivity {

    private String code;
    private String userName;
    private String userEmail;
    private String userPass;
    private String inviteCode;


    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {

            code = getIntent().getStringExtra(Constants.VALIDATION_CODE);
            userName = getIntent().getStringExtra(Constants.USER_NAME);
            userEmail = getIntent().getStringExtra(Constants.USER_EMAIL);
            userPass = getIntent().getStringExtra(Constants.USER_PASSWORD);
            inviteCode = getIntent().getStringExtra(Constants.USER_INVITE_CODE);
        }
        ActivityVerifyEmailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_email);
        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityVerifyEmailBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getString(R.string.verify_phone));

        // setup Fragment
        setupFragment(VerifyEmailFragment.newInstance(userName, userEmail, userPass, code, inviteCode));

    }

}
