package com.panaceasoft.psbuyandsell.ui.terms;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityTermsBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TermsActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTermsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_terms);

        // Init all UI
        initUI(binding);

    }


    //endregion


    //region Private Methods

    private void initUI(ActivityTermsBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.about_us__terms));

        // setup Fragment

        setupFragment(new TermsFragment());
    }

    //endregion

}
