package com.panaceasoft.psbuyandsell.ui.terms;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.binding.FragmentDataBindingComponent;
import com.panaceasoft.psbuyandsell.databinding.FragmentTermsBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSFragment;
import com.panaceasoft.psbuyandsell.utils.AutoClearedValue;
import com.panaceasoft.psbuyandsell.utils.Utils;
import com.panaceasoft.psbuyandsell.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.psbuyandsell.viewobject.AboutUs;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsFragment extends PSFragment {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private AboutUsViewModel aboutUsViewModel;

    private static final int REQUEST_CALL = 1;

    @VisibleForTesting
    private AutoClearedValue<FragmentTermsBinding> binding;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentTermsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

    }

    @Override
    protected void initViewModels() {
        aboutUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(AboutUsViewModel.class);
    }

    @Override
    protected void initAdapters() {
    }

    //  private  void replaceAboutUsData()
    @Override
    protected void initData() {

        aboutUsViewModel.setAboutUsObj("about us");
        aboutUsViewModel.getAboutUsData().observe(this, resource -> {

            if (resource != null) {

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            setAboutUsData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.");


            } else {
                //noinspection ConstantConditions
                Utils.psLog("No Data of About Us.");
            }
        });
    }

    private void setAboutUsData(AboutUs aboutUs) {

        binding.get().setAboutUs(aboutUs);


        if (aboutUs.aboutTitle.equals("")) {

            binding.get().terms.setVisibility(View.GONE);

        }else{
            binding.get().terms.setText(aboutUs.aboutTitle);
        }




    }
    //endregion

}