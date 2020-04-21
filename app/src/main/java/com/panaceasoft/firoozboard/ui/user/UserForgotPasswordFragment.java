package com.panaceasoft.firoozboard.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.PsApp;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentUserForgotPasswordBinding;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.ui.user.sms.KavehNegar;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UserForgotPasswordFragment
 */
public class UserForgotPasswordFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserForgotPasswordBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserForgotPasswordBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_forgot_password, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);


        //fadeIn Animation
        fadeIn(binding.get().getRoot());


        binding.get().loginButton.setOnClickListener(view -> {

            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserLogin((MainActivity) getActivity());
            } else {

                navigationController.navigateToUserLoginActivity(getActivity());

                try {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in closing activity.", e);
                }
            }

        });

        binding.get().forgotPasswordButton.setOnClickListener(view -> {
            if (connectivity.isConnected()) {
                forgotPassword();
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        });
    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

    }

    //endregion


    //region Private Methods

    private void updateForgotBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
        }
    }

    private void forgotPassword() {

        Utils.hideKeyboard(getActivity());

        String phone = binding.get().emailEditText.getText().toString().trim();
        if (phone.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        if (!phone.matches("(\\+98|0)?9\\d{9}")) {
            psDialogMsg.showWarningDialog("شماره موبایل نامعتبر هست", getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        userViewModel.isLoading = true;
        prgDialog.get().show();
        updateForgotBtnStatus();

        String token = "app:url";
        PsApp.getApi().sendForgetPassword(phone, token, "forget", Config.API_KEY_KAVEH_NEGAR).enqueue(new Callback<KavehNegar>() {
            @Override
            public void onResponse(Call<KavehNegar> call, Response<KavehNegar> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        if (response.body().getReturn() != null) {
                            int status = response.body().getReturn().getStatus();
                            if (status == 200) {
                                Utils.log(getClass(), response.body().toString());

                                psDialogMsg.showSuccessDialog("کد بازیابی ارسال شد", getString(R.string.app__ok));
                                psDialogMsg.show();

                                userViewModel.isLoading = false;
                                prgDialog.get().cancel();

                                updateForgotBtnStatus();


                            } else {
                                String message = response.body().getReturn().getMessage();
                                showError(message);
                            }


                        } else {
                            showError("getReturn() is empty");

                        }

                    } else {
                        showError("response.body() is empty");

                    }
                } else {

                    showError("response is not successful");

                }


            }

            @Override
            public void onFailure(Call<KavehNegar> call, Throwable t) {

                showError(t.getMessage());
            }
        });


        /*

        userViewModel.forgotPassword(phone).observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();
                        updateForgotBtnStatus();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            psDialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.app__ok));
                            psDialogMsg.show();

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                            updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


        });
        */
    }

    private void showError(String message) {
        psDialogMsg.showErrorDialog(message, getString(R.string.app__ok));
        psDialogMsg.show();

        binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
        userViewModel.isLoading = false;
        prgDialog.get().cancel();
    }

    //endregion


}
