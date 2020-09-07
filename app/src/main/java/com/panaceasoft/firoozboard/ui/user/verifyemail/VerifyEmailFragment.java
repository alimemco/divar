package com.panaceasoft.firoozboard.ui.user.verifyemail;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.PsApp;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentVerifyEmailBinding;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;
import com.panaceasoft.firoozboard.viewobject.User;
import com.panaceasoft.firoozboard.viewobject.UserLogin;
import com.panaceasoft.firoozboard.viewobject.common.Resource;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    @VisibleForTesting
    private AutoClearedValue<FragmentVerifyEmailBinding> binding;

    private String code;
    private String userName;
    private String userEmail;
    private String userPass;
    private String inviteCode;

    private AutoClearedValue<ProgressDialog> prgDialog;

    public static VerifyEmailFragment newInstance(String userName, String userEmail, String userPassword, String code, String inviteCode) {

        Bundle args = new Bundle();
        args.putString(Constants.VALIDATION_CODE, code);
        args.putString(Constants.USER_NAME, userName);
        args.putString(Constants.USER_EMAIL, userEmail);
        args.putString(Constants.USER_PASSWORD, userPassword);
        args.putString(Constants.USER_INVITE_CODE, inviteCode);

        VerifyEmailFragment fragment = new VerifyEmailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString(Constants.VALIDATION_CODE);
            userName = getArguments().getString(Constants.USER_NAME);
            userEmail = getArguments().getString(Constants.USER_EMAIL);
            userPass = getArguments().getString(Constants.USER_PASSWORD);
            inviteCode = getArguments().getString(Constants.USER_INVITE_CODE);
        }

    }

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentVerifyEmailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.setExpandedToolbar(getActivity());

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            // ((MainActivity)getActivity()).updateMenuIconWhite();
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        //TODO add dialog
        observeViewModel();
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);


        binding.get().emailTextView.setText(userOldEmail);
        // binding.get().enterCodeEditText.setText(code);

        binding.get().submitButton.setOnClickListener(v -> {
            //userViewModel.setEmailVerificationUser(Utils.checkUserId(userOldId), binding.get().enterCodeEditText.getText().toString());

            String enteredCode = binding.get().enterCodeEditText.getText().toString();
            if (enteredCode.equals(code)) {

                String token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);
                registerUser(userName, userEmail, userPass, token);
                //Toast.makeText(getContext(), "Try To Register", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), R.string.invalid_code, Toast.LENGTH_SHORT).show();
            }


        });


        binding.get().resentCodeButton.setOnClickListener(v -> userViewModel.setResentVerifyCodeObj(userOldEmail));

        binding.get().changeEmailButton.setOnClickListener(v -> navigationController.navigateToUserRegister((MainActivity) getActivity()));
    }

    private void observeViewModel() {
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

                            // TODO: 9/6/2020 IMPORTANT
                            Utils.psLog("SUCCESS REGISTER USER");

                            sendInviteCodeToServer(inviteCode, userEmail);

                            if (getActivity() != null) {
                                pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                pref.edit().putString(Constants.USER_NAME, listResource.data.userName).apply();
                                //TODO ali
                                pref.edit().putString(Constants.USER_PHONE, listResource.data.userPhone).apply();
                                pref.edit().putString(Constants.USER_EMAIL, listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_PASSWORD, listResource.data.userPassword).apply();

                                //TODO ali
                                pref.edit().putString(Constants.USER_OLD_PHONE, listResource.data.userPhone).apply();
                                pref.edit().putString(Constants.USER_OLD_EMAIL, listResource.data.userEmail).apply();
                                pref.edit().putString(Constants.USER_OLD_PASSWORD, listResource.data.userPassword).apply();
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
                        // binding.get().registerButton.setText(getResources().getString(R.string.register__register));
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
    }

    private void sendInviteCodeToServer(String inviteCode, String invitedUser) {
       if (inviteCode == null) return;
       if (inviteCode.equals("")) return;

        PsApp.getApi().sendInviteCode(inviteCode, invitedUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utils.psLog("INVITE" + response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utils.psLog(t.toString());
            }
        });
    }

    private void updateRegisterBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().submitButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().submitButton.setText(getResources().getString(R.string.send));
        }
    }

    private void registerUser(String userName, String userEmail, String userPassword, String token) {
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
                null));
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

        LiveData<Resource<UserLogin>> itemList = userViewModel.getEmailVerificationUser();

        if (itemList != null) {

            itemList.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                Toast.makeText(getContext(), "لطفا صبر کنید..", Toast.LENGTH_SHORT).show();

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                try {
                                    if (getActivity() != null) {
                                        pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();
                                        pref.edit().putString(Constants.USER_NAME, listResource.data.user.userName).apply();
                                        pref.edit().putString(Constants.USER_PHONE, listResource.data.user.userPhone).apply();
                                        pref.edit().putString(Constants.USER_EMAIL, listResource.data.user.userEmail).apply();
                                        pref.edit().putString(Constants.USER_PASSWORD, listResource.data.user.userPassword).apply();
                                        //TODO ali put email
                                        pref.edit().putString(Constants.USER_OLD_PHONE, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_EMAIL, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_PASSWORD, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_NAME, Constants.EMPTY_STRING).apply();
                                        pref.edit().putString(Constants.USER_OLD_ID, Constants.EMPTY_STRING).apply();

                                    }

                                } catch (NullPointerException ne) {
                                    Utils.psErrorLog("Null Pointer Exception.", ne);
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in getting notification flag data.", e);
                                }

                                if (getActivity() instanceof MainActivity) {
                                    ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                    ((MainActivity) getActivity()).refreshUserData();
                                    navigationController.navigateToUserProfile((MainActivity) VerifyEmailFragment.this.getActivity());

                                } else {
                                    try {
                                        if (getActivity() != null) {
                                            getActivity().finish();
                                        }
                                    } catch (Exception e) {
                                        Utils.psErrorLog("Error in closing parent activity.", e);
                                    }
                                }


//                                psDialogMsg.showSuccessDialog("Success Success", getString(R.string.app__ok));
//                                psDialogMsg.show();
//                                psDialogMsg.okButton.setOnClickListener(view -> {
//                                    psDialogMsg.cancel();
//
//
//                                    if(getActivity() != null) {
//                                        getActivity().finish();
//                                    }
//
//                                });
//

                            }

                            break;

                        case ERROR:
                            // Error State
                            psDialogMsg.showErrorDialog(getString(R.string.error_message__code_not_verify), getString(R.string.app__ok));
                            psDialogMsg.show();

                            break;
                        default:
                            // Default

                            break;
                    }

                }

            });
        }


        //For resent code
        userViewModel.getResentVerifyCodeData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "موفق", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "ناموفق", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }


}
