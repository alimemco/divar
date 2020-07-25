package com.panaceasoft.firoozboard.ui.item.myitem;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityLoginUserItemListBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MyItemListActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginUserItemListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login_user_item_list);
        
        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityLoginUserItemListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getString(R.string.profile__my_listing));

        // setup Fragment
        setupFragment(new MyItemFragment());

    }

}
