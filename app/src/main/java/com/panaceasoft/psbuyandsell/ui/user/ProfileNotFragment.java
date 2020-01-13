package com.panaceasoft.psbuyandsell.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.panaceasoft.psbuyandsell.MainActivity;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.binding.FragmentDataBindingComponent;
import com.panaceasoft.psbuyandsell.databinding.FragmentProfileBinding;
import com.panaceasoft.psbuyandsell.databinding.FragmentProfileNotBinding;
import com.panaceasoft.psbuyandsell.ui.common.DataBoundListAdapter;
import com.panaceasoft.psbuyandsell.ui.common.PSFragment;
import com.panaceasoft.psbuyandsell.utils.AutoClearedValue;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.PSDialogMsg;
import com.panaceasoft.psbuyandsell.utils.Utils;
import com.panaceasoft.psbuyandsell.viewmodel.user.UserViewModel;
import com.panaceasoft.psbuyandsell.viewobject.User;
import com.panaceasoft.psbuyandsell.viewobject.common.Resource;
import com.panaceasoft.psbuyandsell.viewobject.holder.UserParameterHolder;

/**
 * ProfileNotFragment
 */
public class ProfileNotFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileNotBinding> binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileNotBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_not, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            //((MainActivity) getActivity()).updateMenuIconWhite();
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().RateThisApp.setOnClickListener(view -> navigationController.navigateToPlayStore(getActivity()));
        binding.get().Settings.setOnClickListener(view -> navigationController.navigateToSettingActivity(getActivity()));
        binding.get().InfoApp.setOnClickListener(view -> navigationController.navigateToAppInfoActivity(getActivity()));

        binding.get().TermsApp.setOnClickListener(view -> navigationController.navigateToTermsActivity(getActivity()));

        binding.get().ContactUs.setOnClickListener(view -> navigationController.navigateToContactUs(getActivity()));


        binding.get().LogIn.setOnClickListener(v -> {
            if (loginUserId.isEmpty()) {

                navigationController.navigateToUserLoginActivity(getActivity());

            }
        });


    }

    @Override
    protected void initViewModels() {
        // itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();

        //   itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

    }
}
