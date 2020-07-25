package com.panaceasoft.firoozboard.ui.item.readmore;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.databinding.ActivityReadMoreBinding;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.Constants;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ReadMoreActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReadMoreBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_read_more);

        // Init all UI
        initUI(binding);

    }


    //endregion

    //region Private Methods

    private void initUI(ActivityReadMoreBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getIntent().getStringExtra(Constants.ITEM_TITLE));

        // setup Fragment
        setupFragment(new ReadMoreFragment());

    }

    //endregion


}