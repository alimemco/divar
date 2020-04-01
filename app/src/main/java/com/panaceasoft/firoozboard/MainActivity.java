package com.panaceasoft.firoozboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.android.material.internal.BaselineLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.panaceasoft.firoozboard.databinding.ActivityMainBinding;
import com.panaceasoft.firoozboard.edit.AlertModel;
import com.panaceasoft.firoozboard.edit.SuggestFragment;
import com.panaceasoft.firoozboard.ui.common.NavigationController;
import com.panaceasoft.firoozboard.ui.common.PSAppCompactActivity;
import com.panaceasoft.firoozboard.utils.AppLanguage;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.common.NotificationViewModel;
import com.panaceasoft.firoozboard.viewmodel.item.ItemViewModel;
import com.panaceasoft.firoozboard.viewmodel.user.UserViewModel;
import com.panaceasoft.firoozboard.viewobject.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * MainActivity of Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 *
 * @author Panacea-soft
 * @version 1.0
 * @since 11/15/17.
 */

public class MainActivity extends PSAppCompactActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //region Variables

    @Inject
    SharedPreferences pref;

    @Inject
    AppLanguage appLanguage;
    private Boolean notiSetting = false;
    private String token = "";
    private UserViewModel userViewModel;
    private ItemViewModel itemViewModel;
    private NotificationViewModel notificationViewModel;
    public User user;
    private PSDialogMsg psDialogMsg;
    public boolean isLogout = false;
    Drawable yourdrawable = null;
    Drawable yourdrawabletwo = null;
    private String token1;
    public String selectedLocationId, selectedLocationName, selected_lat, selected_lng;
    private String loginUserId;
    private String locationId;
    private String locationName;
    public String notiItemId, notibuyerId, notiSellerId, notiMsg, notisenderName, notiSenderUrl, userId;
    String receiverId = Constants.EMPTY_STRING;
    String receiverName = Constants.EMPTY_STRING;
    String receiverUrl = Constants.EMPTY_STRING;
    int requestCode = 0;
    String flag = Constants.EMPTY_STRING;


    private ConsentForm form;

    Dialog dialog;
    TextView titleTextView, msgTextView, subTitleTextView;
    Button openItemButton;
    ImageButton closeButton;
    private long mLastClickTime = 0;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    public ActivityMainBinding binding;

    //endregion


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_PSTheme);

        super.onCreate(savedInstanceState);

        // onNewIntent(getIntent());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        initUIAndActions();

        initModels();

        initData();

        // checkConsentStatus();

        getAlertFromServer();

    }

    private void getAlertFromServer() {

        if (!Utils.isNetworkAvailable(this)) return;

        PsApp.getApi().getAlert()
                .enqueue(new Callback<AlertModel>() {
                    @Override
                    public void onResponse(Call<AlertModel> call, Response<AlertModel> response) {

                        String message = "";
                        if (response.isSuccessful()) {
                            if (response.body() != null) {


                                    if ( response.body().getShow()) {
                                        SuggestFragment dialog = SuggestFragment.newInstance(response.body().getMessage());

                                        dialog.seOnButtonClickListener(view -> {
                                            switch (view.getId()){
                                                case R.id.fragment_suggest_insert:
//todo Arch
                                                  //  navigationController.navigateToImageFullScreen() ;
                                                    break;

                                                    case R.id.fragment_suggest_show:

                                                    break;
                                            }
                                        });

                                        dialog.show(getSupportFragmentManager(), "SuggestFragment");
                                        return;
                                    }


                            } else {
                                message = "response body is null";
                            }
                        } else {
                            message = "response is Not Successful";
                        }

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<AlertModel> call, Throwable t) {

                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();

        refreshUserData();
    }

    public void refreshUserData() {
        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            String message = getBaseContext().getString(R.string.message__want_to_quit);
            String okStr = getBaseContext().getString(R.string.message__ok_close);
            String cancelStr = getBaseContext().getString(R.string.message__cancel_close);

            psDialogMsg.showConfirmDialog(message, okStr, cancelStr);

            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(view -> {

                psDialogMsg.cancel();
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            });

            psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());
        }

        return true;
    }

    //endregion


    //region Private Methods

    /**
     * Initialize Models
     */
    private void initModels() {

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationViewModel.class);
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
    }


    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private void showAlertMessage(String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.ps_dialog, null);

        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null);

        TextView message = view.findViewById(R.id.messageTextView);

        message.setText(msg);

        builder.create();

        builder.show();

    }


    /**
     * This function will initialize UI and Event Listeners
     */
    private void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this, false);

        initToolbar(binding.toolbar, getString(R.string.app__app_name));


        navigationController.navigateToCityList(this);
        showBottomNavigation();

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getIntentData();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_menu:
                    //layout_scrollFlags
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    binding.addItemButton.setVisibility(View.VISIBLE);
                    navigationController.navigateToHome(MainActivity.this, false, selectedLocationId, selectedLocationName);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
/*                case R.id.message_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    //  binding.addItemButton.setVisibility(View.GONE);
                    setToolbarText(binding.toolbar, getString(R.string.menu__chat));

                    chooseMassageFragment();


                    break;*/

                case R.id.interest_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    //   binding.addItemButton.setVisibility(View.GONE);
                    navigationController.navigateToInterest(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__interest));

                    break;

                case R.id.item_empty: {
                    return false;
                }

/*                case R.id.search_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                  //  binding.addItemButton.setVisibility(View.GONE);
                    navigationController.navigateToFilter(MainActivity.this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;*/

                case R.id.me_menu:
                    Utils.addToolbarScrollFlag(binding.toolbar);
                    //   binding.addItemButton.setVisibility(View.GONE);
                    setToolbarText(binding.toolbar, getString(R.string.profile__title));
                    chooseProfileFragment();


                    break;

                default:

//                    navigationController.navigateToShopProfile(this);
//                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
            }

            return true;
        });

        setNavigationTypeface();


        binding.addItemButton.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            if (loginUserId.isEmpty()) {

                psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToUserLoginActivity(this);
                });

            } else {

                try {
                    locationId = pref.getString(Constants.SELECTED_LOCATION_ID, Constants.EMPTY_STRING);
                    locationName = pref.getString(Constants.SELECTED_LOCATION_NAME, Constants.EMPTY_STRING);

                } catch (Exception e) {
                    Utils.psErrorLog("", e);
                }


                navigationController.navigateToItemEntryActivity(this, Constants.ADD_NEW_ITEM, locationId, locationName);

            }
        });


    }


    public void setNavigationTypeface() {
        final Typeface avenirHeavy = Typeface.createFromAsset(this.getAssets(), "fonts/IRANSansMobile_Medium.ttf"); //replace it with your own font
        ViewGroup navigationGroup = (ViewGroup) binding.bottomNavigationView.getChildAt(0);
        for (int i = 0; i < navigationGroup.getChildCount(); i++) {
            ViewGroup navUnit = (ViewGroup) navigationGroup.getChildAt(i);
            for (int j = 0; j < navUnit.getChildCount(); j++) {
                View navUnitChild = navUnit.getChildAt(j);
                if (navUnitChild instanceof BaselineLayout) {
                    BaselineLayout baselineLayout = (BaselineLayout) navUnitChild;
                    for (int k = 0; k < baselineLayout.getChildCount(); k++) {
                        View baselineChild = baselineLayout.getChildAt(k);
                        if (baselineChild instanceof TextView) {
                            TextView textView = (TextView) baselineChild;
                            textView.setTypeface(avenirHeavy);
                        }
                    }
                }
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        Bundle extra = intent.getExtras();
//        if(extra != null) {
//            notiItemId = extra.getString(Constants.NOTI_ITEM_ID);
//            notiMsg = extra.getString(Constants.NOTI_MSG);
//            notibuyerId = extra.getString(Constants.NOTI_BUYER_ID);
//            notiSellerId = extra.getString(Constants.NOTI_SELLER_ID);
//            notisenderName = extra.getString(Constants.NOTI_SENDER_NAME);
//            notiSenderUrl = extra.getString(Constants.NOTI_SENDER_URL);
//        }
//        userId = pref.getString(Constants.USER_ID,Constants.EMPTY_STRING);
//
//        checkUserId();
//
//        if(notiItemId != null) {
//            navigationController.navigateToChatActivity(MainActivity.this, notiItemId, receiverId, receiverName, "", "", "",
//                    "", "", flag, notiSenderUrl, requestCode);
//        }
//
//        //open from admin noti
//        if((notiItemId == null || notiItemId.isEmpty()) && (notiMsg != null ) ) {
//            showAlertMessage(notiMsg);
//        }
    }

    private void checkUserId() {
        if (!userId.isEmpty()) {
            if (userId.equals(notibuyerId)) {
                receiverId = notiSellerId;
                receiverName = notisenderName;
                receiverUrl = notiSenderUrl;
                requestCode = Constants.REQUEST_CODE__SELLER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_SELLER;
            }
            if (userId.equals(notiSellerId)) {
                receiverId = notibuyerId;
                receiverName = notisenderName;
                receiverUrl = notiSenderUrl;
                requestCode = Constants.REQUEST_CODE__BUYER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_BUYER;
            }

        }
    }

    private void getIntentData() {
        selectedLocationId = getIntent().getStringExtra(Constants.SELECTED_LOCATION_ID);
        selectedLocationName = getIntent().getStringExtra(Constants.SELECTED_LOCATION_NAME);
        selected_lat = getIntent().getStringExtra(Constants.LAT);
        selected_lng = getIntent().getStringExtra(Constants.LNG);

        pref.edit().putString(Constants.SELECTED_LOCATION_ID, selectedLocationId).apply();
        pref.edit().putString(Constants.SELECTED_LOCATION_NAME, selectedLocationName).apply();
        pref.edit().putString(Constants.LAT, selected_lat).apply();
        pref.edit().putString(Constants.LNG, selected_lng).apply();

        notiItemId = getIntent().getStringExtra(Constants.NOTI_ITEM_ID);
        notiMsg = getIntent().getStringExtra(Constants.NOTI_MSG);
        notibuyerId = getIntent().getStringExtra(Constants.NOTI_BUYER_ID);
        notiSellerId = getIntent().getStringExtra(Constants.NOTI_SELLER_ID);
        notisenderName = getIntent().getStringExtra(Constants.NOTI_SENDER_NAME);
        notiSenderUrl = getIntent().getStringExtra(Constants.NOTI_SENDER_URL);

        userId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);

        checkUserId();

        if (notiItemId != null) {
            navigationController.navigateToChatActivity(MainActivity.this, notiItemId, receiverId, receiverName, "", "", "",
                    "", flag, notiSenderUrl, requestCode);
        }

        //open from admin noti
        if ((notiItemId == null || notiItemId.isEmpty()) && (notiMsg != null)) {
            showAlertMessage(notiMsg);
        }


    }


    public void hideBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.GONE);
        binding.addItemButton.setVisibility(View.GONE);

        Utils.removeToolbarScrollFlag(binding.toolbar);

    }

    private void showBottomNavigation() {
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        binding.addItemButton.setVisibility(View.VISIBLE);

        Utils.addToolbarScrollFlag(binding.toolbar);

    }


    private void chooseProfileFragment() {
        String fragmentType = pref.getString(Constants.USER_OLD_ID, Constants.EMPTY_STRING);

        if (fragmentType.isEmpty()) {
            if (user == null) {
                navigationController.navigateToUserProfileNot(MainActivity.this);
            } else {
                navigationController.navigateToUserProfile(MainActivity.this);
            }
        } else {
            navigationController.navigateToVerifyEmail(MainActivity.this);
        }

    }


    private void chooseMassageFragment() {
        String fragmentType = pref.getString(Constants.USER_OLD_ID, Constants.EMPTY_STRING);

        if (fragmentType.isEmpty()) {
            if (user == null) {
                navigationController.navigateToChatNot(MainActivity.this);
            } else {
                navigationController.navigateToMessage(MainActivity.this);
            }
        } else {
            navigationController.navigateToVerifyEmail(MainActivity.this);
        }

    }

    /**
     * Initialize Data
     */
    private void initData() {

        try {
            notiSetting = pref.getBoolean(Constants.NOTI_SETTING, false);
            token = pref.getString(Constants.NOTI_TOKEN, "");

        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }

        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    user = data.get(0).user;

                    pref.edit().putString(Constants.USER_ID, user.userId).apply();
                    pref.edit().putString(Constants.USER_NAME, user.userName).apply();
                    pref.edit().putString(Constants.USER_PHONE, user.userPhone).apply();
                    pref.edit().putString(Constants.USER_EMAIL, user.userEmail).apply();
                    pref.edit().putString(Constants.USER_PASSWORD, user.userPassword).apply();

                } else {
                    user = null;

                    pref.edit().remove(Constants.USER_ID).apply();
                    pref.edit().remove(Constants.USER_NAME).apply();
                    pref.edit().remove(Constants.USER_PHONE).apply();
                    pref.edit().remove(Constants.USER_EMAIL).apply();
                    pref.edit().remove(Constants.USER_PASSWORD).apply();
                }

            } else {

                user = null;
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_PHONE).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();
                pref.edit().remove(Constants.USER_PASSWORD).apply();

            }


            if (isLogout) {
//                setToolbarText(binding.toolbar, getString(R.string.app__app_name));
//                showBottomNavigation();
                navigationController.navigateToHome(MainActivity.this, false, selectedLocationId, selectedLocationName);
                showBottomNavigation();
                isLogout = false;
            }

        });


        registerNotificationToken(); // Just send "" because don't have token to sent. It will get token itself.
    }

    /**
     * This function will change the menu based on the user is logged in or not.
     */

    private void registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notiSetting) {

            if (this.token.equals("")) {

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {

                                return;
                            }

                            // Get new Instance ID token
                            if (task.getResult() != null) {
                                token1 = task.getResult().getToken();
                            }

                            notificationViewModel.registerNotification(getBaseContext(), Constants.PLATFORM, token1);
                        });


            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.");
        }
    }

    private void getCustomLayoutDialog(String message, String itemId) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_noti_alert);

        titleTextView = dialog.findViewById(R.id.titleTextView);
        titleTextView.setText(R.string.notification__custom_alert);

        subTitleTextView = dialog.findViewById(R.id.subTitleTextView);
        subTitleTextView.setText(R.string.notification__custom_alert);

        msgTextView = dialog.findViewById(R.id.messageTextView);
        msgTextView.setText(message);

        openItemButton = dialog.findViewById(R.id.openItemButton);
        if (!itemId.isEmpty()) {
            openItemButton.setText(R.string.notification__custom_open);
        } else {
            openItemButton.setText(R.string.app__ok);
        }

        closeButton = dialog.findViewById(R.id.closeButton);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));
        }

        dialog.show();
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    //endregion

    public void updateToolbarIconColor(int color) {
        if (yourdrawable != null && yourdrawabletwo != null) {
            yourdrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            yourdrawabletwo.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        yourdrawable = menu.getItem(0).getIcon();
        yourdrawabletwo = menu.getItem(1).getIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_notification) {

            navigationController.navigateToNotificationList(this);
        } else if (item.getItemId() == R.id.action_filter) {
            navigationController.navigateToHomeFilteringActivity(MainActivity.this, itemViewModel.holder, null, itemViewModel.holder.lat, itemViewModel.holder.lng, Constants.MAP_MILES, selectedLocationId);
            // navigationController.navigateToDashBoardSearch(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void checkConsentStatus() {

        // For Testing Open this
        ConsentInformation.getInstance(this).
                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] publisherIds = {getString(R.string.stripe_publish_key)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.

                Utils.psLog(consentStatus.name());

                if (!consentStatus.name().equals(pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS)) || consentStatus.name().equals(Config.CONSENTSTATUS_UNKNOWN)) {
                    collectConsent();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.

                Utils.psLog("Failed to updateeee");
            }
        });
    }

    private void collectConsent() {

        URL privacyUrl = null;

        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if (form != null) {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }
    //endregion

}
