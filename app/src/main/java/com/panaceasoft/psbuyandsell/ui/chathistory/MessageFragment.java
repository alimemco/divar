package com.panaceasoft.psbuyandsell.ui.chathistory;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.panaceasoft.psbuyandsell.MainActivity;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.binding.FragmentDataBindingComponent;
import com.panaceasoft.psbuyandsell.databinding.FragmentMessageBinding;
import com.panaceasoft.psbuyandsell.ui.chathistory.adapter.PagerAdapter;
import com.panaceasoft.psbuyandsell.ui.common.PSFragment;
import com.panaceasoft.psbuyandsell.utils.AutoClearedValue;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends PSFragment {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    private AutoClearedValue<FragmentMessageBinding> binding;

    private PagerAdapter pagerAdapter;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentMessageBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
           // ((MainActivity) getActivity()).updateMenuIconWhite();
        }

        if (getActivity() != null) {
            pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), binding.get().tabLayout.getTabCount());
            binding.get().tabViewPager.setAdapter(pagerAdapter);
            binding.get().tabViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.get().tabLayout));
        }
        setCustomFont();

        binding.get().tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.get().tabViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setCustomFont() {

        ViewGroup vg = (ViewGroup) binding.get().tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile_Medium.ttf"));
                }
            }
        }
    }
    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        getIntentData();

        bindingData();

    }

    private void bindingData() {

    }

    private void getIntentData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__BUYER_CHAT_FRAGMENT) {
            pagerAdapter.buyerFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            pagerAdapter.sellerFragment.onActivityResult(requestCode, resultCode, data);
        }

    }


}
