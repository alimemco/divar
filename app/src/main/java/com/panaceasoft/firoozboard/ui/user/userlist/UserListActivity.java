package com.panaceasoft.firoozboard.ui.user.userlist;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityUserListBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.viewobject.holder.UserParameterHolder;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UserListActivity extends PSAppCompactActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    UserParameterHolder userHolder = new UserParameterHolder();

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);
        
        // Init all UI
        initUI(binding);

    }



    //endregion


    //region Private Methods

    private void initUI(ActivityUserListBinding binding) {

        userHolder = (UserParameterHolder) getIntent().getSerializableExtra(Constants.USER_PARAM_HOLDER_KEY);

        // Toolbar
        if(userHolder.return_types.equals("follower")) {
            initToolbar(binding.toolbar, getString(R.string.user_follower_list_toolbar_name));
        }else {
            initToolbar(binding.toolbar, getString(R.string.user_following_list_toolbar_name));
        }

        // setup Fragment
        setupFragment(new UserListFragment());

    }

}
