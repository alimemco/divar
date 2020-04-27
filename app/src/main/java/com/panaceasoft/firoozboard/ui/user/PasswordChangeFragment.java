package com.panaceasoft.firoozboard.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.PsApp;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentPasswordChangeBinding;
import com.panaceasoft.firoozboard.edit.model.QueryModel;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * PasswordChangeFragment
 */
public class PasswordChangeFragment extends PSFragment {

    private static final String TAG = "PasswordChangeLog";


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentPasswordChangeBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    private String code;
    private String phone;


    public static PasswordChangeFragment newInstance(String phone, String code) {

        Bundle args = new Bundle();
        args.putString(Constants.USER_PHONE, phone);
        args.putString(Constants.VALIDATION_CODE, code);

        PasswordChangeFragment fragment = new PasswordChangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = getArguments().getString(Constants.VALIDATION_CODE, "-1");
        phone = getArguments().getString(Constants.USER_PHONE, "-1");
        Log.i(TAG, "onCreate: " + code);

    }

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentPasswordChangeBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_password_change, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.ROBOTO)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().saveButton.setOnClickListener(view -> passwordUpdate());


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
            binding.get().saveButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().saveButton.setText(getResources().getString(R.string.password_change__save));
        }

    }

    private void passwordUpdate() {

        Utils.hideKeyboard(getActivity());

        if (!connectivity.isConnected()) {

            psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String password = binding.get().passwordEditText.getText().toString().trim();
        String confirmPassword = binding.get().confirmPasswordEditText.getText().toString().trim();
        String codeEditText = binding.get().tokenEditText.getText().toString().trim();

        if (!code.equals(codeEditText)) {
            psDialogMsg.showWarningDialog("کد اعتبار سنجی غلط است", getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }


        if (password.equals("") || confirmPassword.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        if (!(password.equals(confirmPassword))) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__password_not_equal), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }


        userViewModel.isLoading = true;
        prgDialog.get().show();
        updateForgotBtnStatus();

        PsApp.getApi().updatePassword(Config.API_KEY, phone, password).enqueue(new Callback<QueryModel>() {
            @Override
            public void onResponse(Call<QueryModel> call, Response<QueryModel> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        boolean success = Boolean.parseBoolean(response.body().getSuccess());
                        if (success) {
                            psDialogMsg.showSuccessDialog("با موفقیت تغییر کرد", getString(R.string.app__ok));
                            psDialogMsg.show();
                            prgDialog.get().cancel();

                            userViewModel.isLoading = false;

                            updateForgotBtnStatus();

                            if (getActivity() != null)
                                getActivity().finish();

                        } else {
                            showError(response.body().getMessage());
                        }
                    } else {
                        showError("response body empty");
                    }

                } else {
                    showError("response is un Successful");
                }

            }

            @Override
            public void onFailure(Call<QueryModel> call, Throwable t) {
                showError(t.getMessage());
            }
        });



/*
        userViewModel.passwordUpdate(loginUserId, password).observe(this, listResource -> {

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
                            prgDialog.get().cancel();

                            userViewModel.isLoading = false;

                            updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        binding.get().saveButton.setText(getResources().getString(R.string.password_change__save));
                        userViewModel.isLoading = false;

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });
*/

    }

    //endregion
    private void showError(String message) {
        psDialogMsg.showErrorDialog(message, getString(R.string.app__ok));
        psDialogMsg.show();

        binding.get().saveButton.setText(getResources().getString(R.string.password_change__save));
        userViewModel.isLoading = false;
        updateForgotBtnStatus();
        prgDialog.get().dismiss();
    }
}