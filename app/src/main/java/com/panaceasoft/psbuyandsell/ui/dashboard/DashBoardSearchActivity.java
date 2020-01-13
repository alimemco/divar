package com.panaceasoft.psbuyandsell.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.databinding.ActivityCommentDetailBinding;
import com.panaceasoft.psbuyandsell.databinding.ActivityDashBoardSearchBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSAppCompactActivity;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class DashBoardSearchActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDashBoardSearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board_search);

        // Init all UI
        initUI(binding);
    }




    //region Private Methods

    private void initUI(ActivityDashBoardSearchBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.menu__search));

        // setup Fragment
        DashBoardSearchFragment dashboardsearchfragment = new DashBoardSearchFragment();
        setupFragment(dashboardsearchfragment);
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion

}
