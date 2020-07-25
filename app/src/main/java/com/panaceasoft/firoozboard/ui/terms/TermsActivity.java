package com.panaceasoft.firoozboard.ui.terms;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityTermsBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class TermsActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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
