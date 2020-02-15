package com.panaceasoft.firoozboard.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.PsApp;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentUserRegisterBinding;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.ui.user.verifyemail.VerifyEmailActivity;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * UserRegisterFragment
 */
public class UserRegisterFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserRegisterBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    private String code;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserRegisterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_register, container, false, dataBindingComponent);

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

            if (connectivity.isConnected()) {

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
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));

                psDialogMsg.show();
            }

        });

        binding.get().TermsOpen.setOnClickListener(view -> navigationController.navigateToTermsActivity(getActivity()));

        binding.get().registerButton.setOnClickListener(view -> UserRegisterFragment.this.registerUser());

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

        bindingData();

        // TODO ali diabled
/*
        userViewModel.getRegisterUser().observe(this, listResource -> {
            //TODO ali register received data
            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            if (getActivity() != null) {
                                pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                pref.edit().putString(Constants.USER_NAME, listResource.data.userName).apply();
                                //TODO ali
                                pref.edit().putString(Constants.USER_PHONE, listResource.data.userPhone).apply();
                                pref.edit().putString(Constants.USER_EMAIL, listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_PASSWORD, binding.get().passwordEditText.getText().toString()).apply();

                                //TODO ali
                                pref.edit().putString(Constants.USER_OLD_PHONE, listResource.data.userPhone).apply();
                                pref.edit().putString(Constants.USER_OLD_EMAIL, listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_OLD_PASSWORD, binding.get().passwordEditText.getText().toString()).apply();
                                pref.edit().putString(Constants.USER_OLD_NAME, listResource.data.userName).apply();
                                //  pref.edit().putString(Constants.USER_OLD_ID, listResource.data.userId).apply();
                                pref.edit().putString(Constants.USER_OLD_ID, Constants.EMPTY_STRING).apply();
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();
                            updateRegisterBtnStatus();


                            updateRegisterBtnStatus();
                            psDialogMsg.showSuccessDialog(getString(R.string.message__register_success), getString(R.string.login__login));

                            psDialogMsg.show();
                            psDialogMsg.okButton.setOnClickListener(v -> {

                                psDialogMsg.cancel();
                                if (getActivity() instanceof MainActivity) {

                                    navigationController.navigateToUserLogin((MainActivity) getActivity());

                                    // ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.verify_email));
                                    //   navigationController.navigateToVerifyEmail((MainActivity) getActivity());

                                } else {

                                    navigationController.navigateToUserLoginActivity(getActivity());
                                    try {
                                        if (getActivity() != null) {
                                            getActivity().finish();
                                        }
                                    } catch (Exception e) {
                                        Utils.psErrorLog("Error in closing parent activity.", e);
                                    }
                                }

                            });


                        }

                        break;
                    case ERROR:
                        // Error State

                        //TODO ali error
                        psDialogMsg.showWarningDialog(getString(R.string.error_message__email_exists), getString(R.string.app__ok));
                        binding.get().registerButton.setText(getResources().getString(R.string.register__register));
                        psDialogMsg.show();

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

    private void bindingData() {

/*        if (!userOldEmail.isEmpty()) {
            binding.get().emailEditText.setText(userOldEmail);
        }
        if (!userOldPassword.isEmpty()) {
            binding.get().passwordEditText.setText(userOldPassword);
        }
        if (!userOldName.isEmpty()) {
            binding.get().nameEditText.setText(userOldName);
        }*/
    }

    //endregion


    //region Private Methods

    private void updateRegisterBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().registerButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().registerButton.setText(getResources().getString(R.string.register__register));
        }
    }

    private void registerUser() {

        //TODO ali start registering

        Utils.hideKeyboard(getActivity());

        String userName = binding.get().nameEditText.getText().toString().trim();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));

            psDialogMsg.show();

            return;
        }


        //TODO ali phone
        String userEmail = binding.get().emailEditText.getText().toString().trim();
        if (userEmail.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        String userPassword = binding.get().passwordEditText.getText().toString().trim();
        if (userPassword.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }

        if (!userPassword.equals(binding.get().passwordagainEditText.getText().toString().trim())) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password_agian), getString(R.string.app__ok));

            psDialogMsg.show();
            return;
        }


        userViewModel.isLoading = true;
        updateRegisterBtnStatus();

        //  String token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        code = String.valueOf(Utils.randInt(111111, 999999));

        //TODO ali send sms
        PsApp.getApi().sendSms(userEmail, code, "Verify", Config.API_KEY_SMS).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {

                            Toast.makeText(getContext(), response.body().string(), Toast.LENGTH_LONG).show();
                            Utils.log(getClass(), response.body().string());

                            Intent intent = new Intent(getActivity(), VerifyEmailActivity.class);
                            intent.putExtra(Constants.VALIDATION_CODE, code);
                            intent.putExtra(Constants.USER_NAME, userName);
                            intent.putExtra(Constants.USER_EMAIL, userEmail);
                            intent.putExtra(Constants.USER_PASSWORD, userPassword);

                            startActivity(intent);

                        } catch (IOException e) {
                            Utils.log(getClass(), e.toString());
                        }
                    } else {
                        Utils.log(getClass(), "response.body() is empty");
                    }
                } else {
                    Utils.log(getClass(), "response is not successful");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.log(getClass(), t.getMessage());
            }
        });

/*
        //TODO Register user *important*
        userViewModel.setRegisterUser(new User(
                "",
                "",
                "",
                "",
                userName,
                userEmail,
                "0916",
                userPassword,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                token,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null));*/
    }

    //endregion

}

