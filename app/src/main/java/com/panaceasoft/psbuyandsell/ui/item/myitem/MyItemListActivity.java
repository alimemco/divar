package com.panaceasoft.psbuyandsell.ui.item.myitem;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityLoginUserItemListBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyItemListActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
