package com.panaceasoft.psbuyandsell.ui.contactus;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityContactUsBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContactUsActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityContactUsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);

        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityContactUsBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.menu__contact_us));

        // setup Fragment
        setupFragment(new ContactUsFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}
