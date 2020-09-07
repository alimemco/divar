package com.panaceasoft.firoozboard.ui.user;

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
import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentProfileBinding;
import com.panaceasoft.firoozboard.ui.apploading.AppLoadingActivity;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.JalaliCalendar;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;
import com.panaceasoft.firoozboard.viewobject.User;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.panaceasoft.firoozboard.viewobject.holder.UserParameterHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    //  private ItemViewModel itemViewModel;
    private UserViewModel userViewModel;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileBinding> binding;
    // private AutoClearedValue<ItemVerticalListAdapter> adapter;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

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

        // binding.get().userOwnItemList.setNestedScrollingEnabled(false);
        binding.get().editProfile.setOnClickListener(view -> navigationController.navigateToProfileEditActivity(getActivity()));
        binding.get().seeAllItems.setOnClickListener(view -> navigationController.navigateToMyItemListActivity(getActivity(), loginUserId));
        binding.get().seeAllItemsHistory.setOnClickListener(view -> navigationController.navigateToHistoryActivity(getActivity()));
        binding.get().seeAllItemsFav.setOnClickListener(view -> navigationController.navigateToFavouriteActivity(getActivity()));
        binding.get().RateThisApp.setOnClickListener(view -> navigationController.navigateToPlayStore(getActivity()));
        binding.get().Settings.setOnClickListener(view -> navigationController.navigateToSettingActivity(getActivity()));
        binding.get().InfoApp.setOnClickListener(view -> navigationController.navigateToAppInfoActivity(getActivity()));

        binding.get().TermsApp.setOnClickListener(view -> navigationController.navigateToTermsActivity(getActivity()));

        binding.get().ContactUs.setOnClickListener(view -> navigationController.navigateToContactUs(getActivity()));

        binding.get().LogOut.setOnClickListener(view -> logout());

       /* navigationController.navigateToMainActivity(getActivity(), selected_location_id, selected_location_name, selectedLat, selectedLng);

        if (getActivity() != null) {
            getActivity().finish();
        }
        */
        binding.get().followingUserTextView.setOnClickListener(view -> navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowingUsers()));
        binding.get().followUserTextView.setOnClickListener(view -> navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowerUsers()));
        binding.get().deactivateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psDialogMsg.showConfirmDialog(getString(R.string.profile__confirm_deactivate), getString(R.string.app__ok), getString(R.string.message__cancel_close));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v12 -> {
                    userViewModel.setDeleteUserObj(loginUserId);

                    psDialogMsg.cancel();
                });

                psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

            }
        });


    }

    @Override
    protected void initViewModels() {
        // itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

   /*     ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent, new ItemVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title);
            }
        }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().userOwnItemList.setAdapter(nvAdapter);*/
    }

    @Override
    protected void initData() {

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    userViewModel.user = data.get(0).user;
                }
            }

        });

        //User
        userViewModel.setUserObj(loginUserId);
        userViewModel.getUserData().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> listResource) {

                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                ProfileFragment.this.fadeIn(binding.get().getRoot());

                                binding.get().setUser(listResource.data);
                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                                ProfileFragment.this.replaceUserData(listResource.data);


                            }

                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                binding.get().setUser(listResource.data);
                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                                ProfileFragment.this.replaceUserData(listResource.data);
                            }

                            break;
                        case ERROR:
                            // Error State

                            psDialogMsg.showErrorDialog(listResource.message, ProfileFragment.this.getString(R.string.app__ok));
                            psDialogMsg.show();

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

                // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
                // it won't call us if fragment is stopped or not started.
                if (listResource != null && listResource.data != null) {
                    Utils.psLog("Got Data");


                } else {
                    //noinspection Constant Conditions
                    Utils.psLog("Empty Data");

                }
            }
        });

        //delete user
        userViewModel.getDeleteUserStatus().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "حساب شما با موفقیت حذف شد.", Toast.LENGTH_SHORT).show();

                        logout();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "مشکلی در حذف حساب بوجود آمد!", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

     /*   //Item
        itemViewModel.holder.userId = loginUserId;

        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

        itemViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }
                            itemViewModel.setLoadingState(false);
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }

                        }

                        break;

                    case ERROR:
                        itemViewModel.setItemListFromDbByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

                        itemViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        itemViewModel.getItemListFromDbByKeyData().observe(this, listResource -> {

            if (listResource != null) {

                itemReplaceData(listResource);

            }
        });*/
    }

    private void logout() {


        userViewModel.deleteUserLogin(userViewModel.user).observe(this, status -> {
            if (status != null) {
//                    this.menuId = 0;

                startActivity(new Intent(getActivity(), AppLoadingActivity.class));

                //  ((MainActivity) getActivity()).isLogout = true;

                FacebookSdk.sdkInitialize(((MainActivity) Objects.requireNonNull(getActivity())).getApplicationContext());
                LoginManager.getInstance().logOut();

                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });

    }

    @Override
    public void onDispatched() {

    }


    private void replaceUserData(User user) {

        // binding.get().editTextView.setText(binding.get().editTextView.getText().toString());
//        binding.get().userNotificatinTextView.setText(binding.get().userNotificatinTextView.getText().toString());
//        binding.get().userHistoryTextView.setText(binding.get().userHistoryTextView.getText().toString());
        //  binding.get().favouriteTextView.setText(binding.get().favouriteTextView.getText().toString());
        //  binding.get().settingTextView.setText(binding.get().settingTextView.getText().toString());
        // binding.get().historyTextView.setText(binding.get().historyTextView.getText().toString());
        //  binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());

        String startDateString = user.addedDate.toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String timeClock = hour + ":" + minute + ":" + second;
        JalaliCalendar.gDate miladiiDate = new JalaliCalendar.gDate(year, month, day);
        JalaliCalendar.gDate jalaliDate = JalaliCalendar.MiladiToJalali(miladiiDate);

        binding.get().joinedDateTitleTextView.setText(binding.get().joinedDateTitleTextView.getText().toString());
        binding.get().joinedDateTextView.setText(timeClock + " " + jalaliDate.toString());
        binding.get().nameTextView.setText(user.userName);
        binding.get().overAllRatingTextView.setText(user.overallRating);
        binding.get().ratingBarInformation.setRating(user.ratingDetails.totalRatingValue);
        binding.get().ratingCountTextView.setText(String.format("%s%s%s", "(", user.ratingCount, ")"));
        binding.get().followUserTextView.setText(String.format("%s%s%s", user.followerCount, " ", getString(R.string.profile__followers)));
        binding.get().followingUserTextView.setText(String.format("%s%s%s", user.followingCount, " ", getString(R.string.profile__following)));

        if (user.userEmail != null) {
            String inviteCode = user.userEmail.substring(user.userEmail.length() - 6);

            binding.get().inviteCodeTextView.setText(String.format("کد دعوت شما : %s", inviteCode));
        }
        
        if (user.verifyTypes.equals("1")) {
            binding.get().facebookImage.setVisibility(View.GONE);
            binding.get().emailImage.setVisibility(View.VISIBLE);
        }
        if (user.verifyTypes.equals("2")) {
            binding.get().facebookImage.setVisibility(View.VISIBLE);
            binding.get().emailImage.setVisibility(View.GONE);
        }

    }

/*
    private void itemReplaceData(List<Item> itemList) {
        adapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }
*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.setUserObj(loginUserId);
        //   itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

    }
}
