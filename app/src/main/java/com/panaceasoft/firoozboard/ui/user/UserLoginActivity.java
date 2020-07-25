package com.panaceasoft.firoozboard.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityUserLoginBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UserLoginActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_login);

        // Init all UI
        initUI(binding);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }
    //endregion


    //region Private Methods

    private void initUI(ActivityUserLoginBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.login__login));

        // setup Fragment
        setupFragment(new UserLoginFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}