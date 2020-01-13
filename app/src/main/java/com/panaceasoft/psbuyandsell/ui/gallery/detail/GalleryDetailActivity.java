package com.panaceasoft.psbuyandsell.ui.gallery.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityGalleryDetailBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;
import com.panaceasoft.psbuyandsell.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GalleryDetailActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGalleryDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_detail);

        // Init all UI
        initUI(binding);

    }



    //endregion


    //region Private Methods

    private void initUI(ActivityGalleryDetailBinding binding) {

        // Toolbar
     //  initToolbar(binding.toolbar, getResources().getString(R.string.gallery__title));

        // setup Fragment
        setupFragment(new GalleryDetailFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}