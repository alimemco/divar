package com.panaceasoft.firoozboard.ui.item.entry;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.SharedPrefManager;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentItemEntryBinding;
import com.panaceasoft.firoozboard.databinding.ItemEntryBottomBoxBinding;
import com.panaceasoft.firoozboard.edit.Detail;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.ui.payment.PaymentFragment;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.image.ImageViewModel;
import com.panaceasoft.firoozboard.viewmodel.item.ItemViewModel;
import com.panaceasoft.firoozboard.viewobject.Image;
import com.panaceasoft.firoozboard.viewobject.Item;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemEntryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private static final String TAG = "ItemEntryFragmentLog";
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String catId = Constants.EMPTY_STRING;
    private String subCatId = Constants.EMPTY_STRING;
    private String typeId = Constants.EMPTY_STRING;
    private String priceTypeId = Constants.EMPTY_STRING;
    private String dealOptionId = Constants.EMPTY_STRING;
    private String conditionId = Constants.EMPTY_STRING;
    private String locationId = Constants.EMPTY_STRING;
    private String businessMode = Constants.EMPTY_STRING;
    private String itemId;

    private String firstImageId = Constants.EMPTY_STRING;
    private String secImageId = Constants.EMPTY_STRING;
    private String thirdImageId = Constants.EMPTY_STRING;
    private String fouthImageId = Constants.EMPTY_STRING;
    private String fifthImageId = Constants.EMPTY_STRING;

    private boolean isFirstImageSelected = false;
    private boolean isSecImageSelected = false;
    private boolean isThirdImageSelected = false;
    private boolean isFouthImageSelected = false;
    private boolean isFifthImageSelected = false;
    private String locationName;
    //TODO ali
    private boolean mNeedToPay = false;

    private PSDialogMsg psDialogMsg;
    private ItemViewModel itemViewModel;
    private ImageViewModel imageViewModel;
    private String imagePath = "";
    private GoogleMap map;
    private Marker marker;
    private List<String> imagePathList = new ArrayList<>();
    private boolean selected = false;
    private int imageCount = 0;
    private ProgressDialog progressDialog;
    private long mLastClickTime = 0;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemEntryBinding> binding;
    private AutoClearedValue<BottomSheetDialog> mBottomSheetDialog;
    private AutoClearedValue<ItemEntryBottomBoxBinding> bottomBoxLayoutBinding;
    private boolean mPaid;
    private Context mContext;
    private SharedPrefManager sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate: ");

        if (savedInstanceState == null) return;

        itemId = savedInstanceState.getString(Constants.ITEM_ID);
        locationId = savedInstanceState.getString(Constants.ITEM_LOCATION_TYPE_ID);
        locationName = savedInstanceState.getString(Constants.ITEM_LOCATION_TYPE_NAME);

        getIntentData();
        getItemDetail();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");

        FragmentItemEntryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_entry, container, false, dataBindingComponent);

        sharedPreferences = new SharedPrefManager(mContext);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);
        initializeMap(savedInstanceState);


        Uri data = getActivity().getIntent().getData();
        ZarinPal.getPurchase(mContext).verificationPayment(data, (isPaymentSuccess, refID, paymentRequest) -> {


            if (isPaymentSuccess) {
                mPaid = true;
                // String message = "Your Payment is Success :) " + refID;
                // Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                sendDetail();

            } else {
                mPaid = false;
                String message = "پرداخت انجام نشد";
                Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            sharedPreferences.empty();
        });


        return binding.get().getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        Log.i(TAG, "onAttach: ");
    }


    private void pay() {

        ZarinPal purchase = ZarinPal.getPurchase(mContext);
        //TODO pay SandBox

        PaymentRequest payment = ZarinPal.getPaymentRequest();
        // PaymentRequest payment = ZarinPal.getSandboxPaymentRequest();

        payment.setMerchantID(Config.MerchantID);
        // payment.setMerchantID("71c705f8-bd37-11e6-aa0c-000c295eb8fc");
        payment.setAmount(101);
        payment.isZarinGateEnable(true);  // If you actived `ZarinGate`, can handle payment by `ZarinGate`
        payment.setDescription("پرداخت برای آگهی غیر رایگان");
        payment.setCallbackURL("divar://app");     /* Your App Scheme */
        //  payment.setMobile("09355106005");            /* Optional Parameters */
        // payment.setEmail("imannamix@gmail.com");     /* Optional Parameters */


        purchase.startPayment(payment, (status, authority, paymentGatewayUri, intent) -> {

            if (status == 100) {

                if (getFragmentManager() == null) return;

                intent.putExtra(Constants.ITEM_LOCATION_TYPE_NAME, locationName);
                PaymentFragment paymentFragment = new PaymentFragment(intent, itemId, locationId, locationName);
                paymentFragment.show(getFragmentManager(), "PaymentFragment");

                //  navigationController.navigateToPaymentActivity(getActivity(), intent, itemId, locationId, locationName);
            } else {
                Toast.makeText(mContext, "!! Your Payment Failure :(", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void setDataToSharedPref() {

        if (!itemViewModel.itemId.equals(Constants.ADD_NEW_ITEM)) {//edit
            sharedPreferences.save(new Detail(this.catId, this.subCatId, this.typeId, this.priceTypeId, this.conditionId, this.locationId,
                    binding.get().remarkEditText.getText().toString(), binding.get().descEditText.getText().toString(),
                    binding.get().highlightInfoEditText.getText().toString(), binding.get().priceEditText.getText().toString(), this.dealOptionId,
                    binding.get().brandEditText.getText().toString(), businessMode, itemViewModel.is_sold_out, binding.get().titleEditText.getText().toString(), binding.get().addressEditText.getText().toString(),
                    itemViewModel.latValue, itemViewModel.lngValue, itemViewModel.itemId, loginUserId));

        } else {//add new item

            sharedPreferences.save(new Detail(this.catId, this.subCatId, this.typeId, this.priceTypeId, this.conditionId, this.locationId,
                    binding.get().remarkEditText.getText().toString(), binding.get().descEditText.getText().toString(),
                    binding.get().highlightInfoEditText.getText().toString(), binding.get().priceEditText.getText().toString(), this.dealOptionId,
                    binding.get().brandEditText.getText().toString(), businessMode, "", binding.get().titleEditText.getText().toString(), binding.get().addressEditText.getText().toString(),
                    itemViewModel.latValue, itemViewModel.lngValue, "", loginUserId));
        }

    }


    private void initializeMap(Bundle savedInstanceState) {
        try {
            if (this.getActivity() != null) {
                MapsInitializer.initialize(this.getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.get().mapView.onCreate(savedInstanceState);
        bindMap();

    }

    private void bindMap() {
        binding.get().mapView.onResume();

        binding.get().mapView.getMapAsync(googleMap -> {
            map = googleMap;

            try {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(itemViewModel.latValue), Double.valueOf(itemViewModel.lngValue)))
                        .title("نام شهر"));

                //zoom
                if (!itemViewModel.latValue.isEmpty() && !itemViewModel.lngValue.isEmpty()) {
                    int zoomlevel = 8;
                    // Animating to the touched position
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(itemViewModel.latValue), Double.parseDouble(itemViewModel.lngValue)), zoomlevel));
                }
            } catch (Exception e) {
                Utils.psErrorLog("", e);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ");

        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CATEGORY) {

            String categoryName = data.getStringExtra(Constants.CATEGORY_NAME);
            this.catId = data.getStringExtra(Constants.CATEGORY_ID);

            binding.get().categoryTextView.setText(categoryName);

            itemViewModel.holder.cat_id = this.catId;
            this.subCatId = "";
            itemViewModel.holder.sub_cat_id = this.subCatId;
            binding.get().subCategoryTextView.setText("");
            if (!this.catId.isEmpty()) {
                binding.get().subProductTypeTextView.setVisibility(View.VISIBLE);
                binding.get().subCategorySelectionView.setVisibility(View.VISIBLE);

            } else {
                binding.get().subProductTypeTextView.setVisibility(View.GONE);
                binding.get().subCategorySelectionView.setVisibility(View.GONE);
            }

            //TODO changed
            if (categoryName != null)
                if (categoryName.contains("غیر رایگان")) {
                    Log.i(TAG, "onActivityResult: PRICE");
                    mNeedToPay = true;
                } else {
                    Log.i(TAG, "onActivityResult: FREE");
                    mNeedToPay = false;
                }


        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY) {

            this.subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID);
            binding.get().subCategoryTextView.setText(data.getStringExtra(Constants.SUBCATEGORY_NAME));
            itemViewModel.holder.sub_cat_id = this.subCatId;
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_TYPE) {

            this.typeId = data.getStringExtra(Constants.ITEM_TYPE_ID);
            binding.get().typeTextView.setText(data.getStringExtra(Constants.ITEM_TYPE_NAME));
            itemViewModel.holder.type_id = this.typeId;
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_PRICE_TYPE) {

            this.priceTypeId = data.getStringExtra(Constants.ITEM_PRICE_TYPE_ID);
            binding.get().priceTypeTextView.setText(data.getStringExtra(Constants.ITEM_PRICE_TYPE_NAME));

            if (this.priceTypeId.toString().equals("7") || this.priceTypeId.toString().equals("1") || this.priceTypeId.toString().equals("8")) {
                binding.get().priceTitleTextView.setVisibility(View.VISIBLE);
                // binding.get().starTextView8.setVisibility(View.VISIBLE);
                binding.get().priceEditText.setVisibility(View.VISIBLE);
                binding.get().priceEditText.setText("");
            } else {
                binding.get().priceTitleTextView.setVisibility(View.GONE);
                // binding.get().starTextView8.setVisibility(View.GONE);
                binding.get().priceEditText.setVisibility(View.GONE);
                binding.get().priceEditText.setText("0");
            }

            itemViewModel.holder.price_type_id = this.priceTypeId;

        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_CONDITION_TYPE) {

            this.conditionId = data.getStringExtra(Constants.ITEM_CONDITION_TYPE_ID);
            binding.get().itemConditionTextView.setText(data.getStringExtra(Constants.ITEM_CONDITION_TYPE_NAME));
            itemViewModel.holder.condition_id = this.conditionId;
        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE) {

            this.locationId = data.getStringExtra(Constants.ITEM_LOCATION_TYPE_ID);
            itemViewModel.latValue = data.getStringExtra(Constants.LAT);
            itemViewModel.lngValue = data.getStringExtra(Constants.LNG);

            binding.get().locationTextView.setText(data.getStringExtra(Constants.ITEM_LOCATION_TYPE_NAME));
            itemViewModel.holder.location_id = this.locationId;

            bindMap();
        } else if (requestCode == Constants.RESULT_CODE__TO_MAP_VIEW && resultCode == Constants.RESULT_CODE__FROM_MAP_VIEW) {

            itemViewModel.latValue = data.getStringExtra(Constants.LAT);
            itemViewModel.lngValue = data.getStringExtra(Constants.LNG);

            changeCamera();

            bindingLatLng(itemViewModel.latValue, itemViewModel.lngValue);
        } else if (requestCode == Constants.REQUEST_CODE__PAYMENT) {
            //TODO ali payment
            Toast.makeText(mContext, "OnResultPay", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onActivityResult: " + Constants.REQUEST_CODE__PAYMENT);
        }

        //image  gallery upload

        if ((requestCode == Constants.REQUEST_CODE__FIRST_GALLERY || requestCode == Constants.REQUEST_CODE__SEC_GALLERY || requestCode == Constants.REQUEST_CODE__THIRD_GALLERY ||
                requestCode == Constants.REQUEST_CODE__FOURTH_GALLERY || requestCode == Constants.REQUEST_CODE__FIFTH_GALLERY)
                && resultCode == Constants.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (requestCode == Constants.REQUEST_CODE__FIRST_GALLERY) {
                dataBindingComponent.getFragmentBindingAdapters().bindFullImageUri(binding.get().firstImageView, selectedImage);
                itemViewModel.firstImagePath = convertToImagePath(selectedImage, filePathColumn);
                isFirstImageSelected = true;
            }
            if (requestCode == Constants.REQUEST_CODE__SEC_GALLERY) {
                dataBindingComponent.getFragmentBindingAdapters().bindFullImageUri(binding.get().secImageView, selectedImage);
                itemViewModel.secImagePath = convertToImagePath(selectedImage, filePathColumn);
                isSecImageSelected = true;
            }
            if (requestCode == Constants.REQUEST_CODE__THIRD_GALLERY) {
                dataBindingComponent.getFragmentBindingAdapters().bindFullImageUri(binding.get().thirdImageView, selectedImage);
                itemViewModel.thirdImagePath = convertToImagePath(selectedImage, filePathColumn);
                isThirdImageSelected = true;
            }
            if (requestCode == Constants.REQUEST_CODE__FOURTH_GALLERY) {
                dataBindingComponent.getFragmentBindingAdapters().bindFullImageUri(binding.get().fouthImageView, selectedImage);
                itemViewModel.fouthImagePath = convertToImagePath(selectedImage, filePathColumn);
                isFouthImageSelected = true;
            }
            if (requestCode == Constants.REQUEST_CODE__FIFTH_GALLERY) {
                dataBindingComponent.getFragmentBindingAdapters().bindFullImageUri(binding.get().fifthImageView, selectedImage);
                itemViewModel.fifthImagePath = convertToImagePath(selectedImage, filePathColumn);
                isFifthImageSelected = true;
            }


        }
        //image camera

        if ((requestCode == Constants.REQUEST_CODE__FIRST_CAMERA || requestCode == Constants.REQUEST_CODE__SEC_CAMERA || requestCode == Constants.REQUEST_CODE__THIRD_CAMERA ||
                requestCode == Constants.REQUEST_CODE__FOURTH_CAMERA || requestCode == Constants.REQUEST_CODE__FIFTH_CAMERA) && resultCode == Constants.RESULT_OK) {

            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (imageBitmap != null) {
                    if (requestCode == Constants.REQUEST_CODE__FIRST_CAMERA) {
                        dataBindingComponent.getFragmentBindingAdapters().bindFullImageBitMap(binding.get().firstImageView, imageBitmap);
                        itemViewModel.firstImagePath = convertToImagePath(convertBitmapToUri(imageBitmap), filePathColumn);
                    }
                    if (requestCode == Constants.REQUEST_CODE__SEC_CAMERA) {
                        dataBindingComponent.getFragmentBindingAdapters().bindFullImageBitMap(binding.get().secImageView, imageBitmap);
                        itemViewModel.secImagePath = convertToImagePath(convertBitmapToUri(imageBitmap), filePathColumn);
                    }
                    if (requestCode == Constants.REQUEST_CODE__THIRD_CAMERA) {
                        dataBindingComponent.getFragmentBindingAdapters().bindFullImageBitMap(binding.get().thirdImageView, imageBitmap);
                        itemViewModel.thirdImagePath = convertToImagePath(convertBitmapToUri(imageBitmap), filePathColumn);
                    }
                    if (requestCode == Constants.REQUEST_CODE__FOURTH_CAMERA) {
                        dataBindingComponent.getFragmentBindingAdapters().bindFullImageBitMap(binding.get().fouthImageView, imageBitmap);
                        itemViewModel.fouthImagePath = convertToImagePath(convertBitmapToUri(imageBitmap), filePathColumn);
                    }
                    if (requestCode == Constants.REQUEST_CODE__FIFTH_CAMERA) {
                        dataBindingComponent.getFragmentBindingAdapters().bindFullImageBitMap(binding.get().fifthImageView, imageBitmap);
                        itemViewModel.fifthImagePath = convertToImagePath(convertBitmapToUri(imageBitmap), filePathColumn);
                    }
                }
            }
        }

        //custom camera

        if ((requestCode == Constants.REQUEST_CODE__FIRST_CUSTOM_CAMERA || requestCode == Constants.REQUEST_CODE__SEC_CUSTOM_CAMERA || requestCode == Constants.REQUEST_CODE__THIRD_CUSTOM_CAMERA ||
                requestCode == Constants.REQUEST_CODE__FOURTH_CUSTOM_CAMERA || requestCode == Constants.REQUEST_CODE__FIFTH_CUSTOM_CAMERA) && resultCode == Constants.RESULT_CODE__ITEM_ENTRY_WITH_CUSTOM_CAMERA) {

            itemViewModel.customImageUri = data.getStringExtra(Constants.IMAGE_PATH);
            selected = true;

            if (requestCode == Constants.REQUEST_CODE__FIRST_CUSTOM_CAMERA) {
                dataBindingComponent.getFragmentBindingAdapters().bindStorageImageUri(binding.get().firstImageView, itemViewModel.customImageUri);
                itemViewModel.firstImagePath = itemViewModel.customImageUri;
            }
            if (requestCode == Constants.REQUEST_CODE__SEC_CUSTOM_CAMERA) {
                dataBindingComponent.getFragmentBindingAdapters().bindStorageImageUri(binding.get().secImageView, itemViewModel.customImageUri);
                itemViewModel.secImagePath = itemViewModel.customImageUri;
            }
            if (requestCode == Constants.REQUEST_CODE__THIRD_CUSTOM_CAMERA) {
                dataBindingComponent.getFragmentBindingAdapters().bindStorageImageUri(binding.get().thirdImageView, itemViewModel.customImageUri);
                itemViewModel.thirdImagePath = itemViewModel.customImageUri;
            }
            if (requestCode == Constants.REQUEST_CODE__FOURTH_CUSTOM_CAMERA) {
                dataBindingComponent.getFragmentBindingAdapters().bindStorageImageUri(binding.get().fouthImageView, itemViewModel.customImageUri);
                itemViewModel.fouthImagePath = itemViewModel.customImageUri;
            }
            if (requestCode == Constants.REQUEST_CODE__FIFTH_CUSTOM_CAMERA) {
                dataBindingComponent.getFragmentBindingAdapters().bindStorageImageUri(binding.get().fifthImageView, itemViewModel.customImageUri);
                itemViewModel.fifthImagePath = itemViewModel.customImageUri;
            }
        }
        //endregion
    }

    private Uri convertBitmapToUri(Bitmap bitmapImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = "";
        if (getActivity() != null) {
            path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapImage, "Title", null);
        }
        return Uri.parse(path);
    }

    private String convertToImagePath(Uri selectedImage, String[] filePathColumn) {

        if (getActivity() != null && selectedImage != null) {
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            selected = true;
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                cursor.close();
            }
        }
        return imagePath;
    }

    @Override
    public void onDispatched() {


    }

    @Override
    protected void initUIAndActions() {

        itemViewModel.latValue = selectedLat;
        itemViewModel.lngValue = selectedLng;

        if (getActivity() instanceof MainActivity) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            // ((MainActivity) getActivity()).updateMenuIconWhite();
        }

        if (getContext() != null) {

            BottomSheetDialog mBottomSheetDialog2 = new BottomSheetDialog(getContext());
            mBottomSheetDialog = new AutoClearedValue<>(this, mBottomSheetDialog2);

            ItemEntryBottomBoxBinding bottomBoxLayoutBinding2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_entry_bottom_box, null, false);
            bottomBoxLayoutBinding = new AutoClearedValue<>(this, bottomBoxLayoutBinding2);
            mBottomSheetDialog.get().setContentView(bottomBoxLayoutBinding.get().getRoot());

        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message__loading));
        progressDialog.setCancelable(false);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().titleEditText.setHint(R.string.search__notSet);

        binding.get().subCategoryTextView.setHint(R.string.search__notSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        AutoClearedValue<AlertDialog.Builder> alertDialog = new AutoClearedValue<>(this, builder);
        alertDialog.get().setTitle(getResources().getString(R.string.Feature_UI__search_alert_cat_title));

        binding.get().categoryTextView.setText("");

        binding.get().subCategoryTextView.setText("");

        binding.get().categorySelectionView.setOnClickListener(view -> navigationController.navigateToSearchActivityCategoryFragment(this.getActivity(), Constants.CATEGORY, catId, subCatId));

        binding.get().subCategorySelectionView.setOnClickListener(view -> {

            if (catId.equals(Constants.NO_DATA) || catId.isEmpty()) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_category), getString(R.string.app__ok));

                psDialogMsg.show();

            } else {
                navigationController.navigateToSearchActivityCategoryFragment(this.getActivity(), Constants.SUBCATEGORY, catId, subCatId);

            }
        });

        binding.get().typeCardView.setOnClickListener(view -> navigationController.navigateToSearchViewActivity(this.getActivity(), Constants.ITEM_TYPE, typeId, priceTypeId, conditionId, dealOptionId, locationId));

        //   binding.get().itemConditionCardView.setOnClickListener(view -> navigationController.navigateToSearchViewActivity(this.getActivity(), Constants.ITEM_CONDITION_TYPE, typeId, priceTypeId, conditionId, dealOptionId,locationId));

        binding.get().priceTypeCardView.setOnClickListener(view -> navigationController.navigateToSearchViewActivity(this.getActivity(), Constants.ITEM_PRICE_TYPE, typeId, priceTypeId, conditionId, dealOptionId, locationId));

        //binding.get().dealOptionCardView.setOnClickListener(view -> navigationController.navigateToSearchViewActivity(this.getActivity(), Constants.ITEM_DEAL_OPTION_TYPE, typeId, priceTypeId, conditionId, dealOptionId,  locationId));

        binding.get().locationCardView.setOnClickListener(view -> navigationController.navigateToSearchViewActivity(this.getActivity(), Constants.ITEM_LOCATION_TYPE, typeId, priceTypeId, conditionId, dealOptionId, locationId));

        binding.get().mapViewButton.setOnClickListener(v -> {
            if (itemViewModel.lngValue.isEmpty() && itemViewModel.latValue.toString().isEmpty()) {
                String latValue = "28.841507";
                String lngValue = "52.563298";

                map.clear();
                navigationController.navigateToMapActivity(ItemEntryFragment.this.getActivity(), lngValue, latValue, Constants.MAP_PICK);

            } else {

                map.clear();
                navigationController.navigateToMapActivity(ItemEntryFragment.this.getActivity(), itemViewModel.lngValue, itemViewModel.latValue, Constants.MAP_PICK);

            }

        });

        binding.get().mapTitleTextView.setOnClickListener(view -> {

            binding.get().mapTitleTextView.setVisibility(View.GONE);
            binding.get().mapView.setVisibility(View.VISIBLE);
            binding.get().mapViewButton.setVisibility(View.VISIBLE);
            binding.get().latitudeTitleTextView.setVisibility(View.VISIBLE);
            binding.get().latitudeEditText.setVisibility(View.VISIBLE);
            binding.get().lngTextView.setVisibility(View.VISIBLE);
            binding.get().lngEditText.setVisibility(View.VISIBLE);

        });

        binding.get().submitButton.setOnClickListener(view -> {
            //TODO pay validation


            if (itemViewModel.firstImagePath == null && itemViewModel.secImagePath == null && itemViewModel.thirdImagePath == null && itemViewModel.fouthImagePath == null && itemViewModel.fifthImagePath == null) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_image), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().titleEditText.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_list_title), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().categoryTextView.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_category), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().subCategoryTextView.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_subcategory), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().typeTextView.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_type), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().brandEditText.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_item_brand), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().priceTypeTextView.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_price_type), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().priceEditText.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_price), getString(R.string.app__ok));
                psDialogMsg.show();
                //           } else if (binding.get().priceEditText.getText().toString().equals("0")) {
                //             psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_price_not_zero), getString(R.string.app__ok));
                //             psDialogMsg.show();
            } else if (binding.get().descEditText.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_description), getString(R.string.app__ok));
                psDialogMsg.show();
            } else if (binding.get().locationTextView.getText().toString().isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_need_item_city), getString(R.string.app__ok));
                psDialogMsg.show();

            } else if (mNeedToPay && !mPaid) {
                setDataToSharedPref();
                pay();

            } else {
                setDataToSharedPref();
                sendDetail();
            }


        });

        binding.get().firstImageView.setOnClickListener(v -> {
            mBottomSheetDialog.get().show();
            ButtonSheetClick(Constants.ONE);
        });

        binding.get().secImageView.setOnClickListener(v -> {
            mBottomSheetDialog.get().show();
            ButtonSheetClick(Constants.TWO);
        });

        binding.get().thirdImageView.setOnClickListener(v -> {
            mBottomSheetDialog.get().show();
            ButtonSheetClick(Constants.THREE);
        });

        binding.get().fouthImageView.setOnClickListener(v -> {
            mBottomSheetDialog.get().show();
            ButtonSheetClick(Constants.FOUR);
        });

        binding.get().fifthImageView.setOnClickListener(v -> {
            mBottomSheetDialog.get().show();
            ButtonSheetClick(Constants.FIVE);
        });


    }

    private void sendDetail() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1500)
            return;

        mLastClickTime = SystemClock.elapsedRealtime();

        getImagePathList();

        //   checkIsShop();
        businessMode = Constants.ZERO;

        if (itemViewModel.itemId != null) {

            Detail detail = sharedPreferences.get();

            itemViewModel.setUploadItemObj(
                    detail.getCatId(),
                    detail.getSubCatId(),
                    detail.getItemTypeId(),
                    detail.getItemPriceTypeId(),
                    detail.getConditionId(),
                    detail.getLocationId(),
                    detail.getRemark(),
                    detail.getDescription(),
                    detail.getHighlightInfo(),
                    detail.getPrice(),
                    detail.getDealOptionId(),
                    detail.getBrand(),
                    detail.getBusinessMode(),
                    detail.getIsSoldOut(),
                    detail.getTitle(),
                    detail.getAddress(),
                    detail.getLat(),
                    detail.getLng(),
                    detail.getItemId(),
                    detail.getUserId()
            );

            sharedPreferences.empty();

        }

        progressDialog.show();
    }

    private void getImagePathList() {

        if (!itemViewModel.firstImagePath.isEmpty()) {
            imagePathList.add(itemViewModel.firstImagePath);
        }
        if (!itemViewModel.secImagePath.isEmpty()) {
            imagePathList.add(itemViewModel.secImagePath);
        }
        if (!itemViewModel.thirdImagePath.isEmpty()) {
            imagePathList.add(itemViewModel.thirdImagePath);
        }
        if (!itemViewModel.fouthImagePath.isEmpty()) {
            imagePathList.add(itemViewModel.fouthImagePath);
        }
        if (!itemViewModel.fifthImagePath.isEmpty()) {
            imagePathList.add(itemViewModel.fifthImagePath);
        }
    }

    private void checkIsShop() {

        if (binding.get().isShopCheckBox.isChecked()) {
            businessMode = Constants.ONE;
        } else {
            businessMode = Constants.ZERO;
        }
    }

    private void ButtonSheetClick(String flag) {
        bottomBoxLayoutBinding.get().cameraButton.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity() != null) {
                    if ((getActivity()).checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            ((getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, Constants.REQUEST_CODE__PERMISSION_CODE);
                    } else {
                        //granted
//                        navigationController.navigateToCamera(getActivity());
                        openCamera(flag);
                    }
                }
            }

            mBottomSheetDialog.get().dismiss();
        });

        bottomBoxLayoutBinding.get().galleryButton.setOnClickListener(v -> {

//            bottomBoxLayoutBinding.get().galleryButton.setBackgroundResource(R.color.button__select_green);
//            bottomBoxLayoutBinding.get().cameraButton.setBackgroundResource(R.color.md_white_1000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity() != null) {
                    if ((getActivity()).checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            ((getActivity()).checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, Constants.REQUEST_CODE__PERMISSION_CODE);
                    } else {
                        //granted
                        navigationController.navigateToGallery(getActivity(), flag);
                    }
                }
            }

            mBottomSheetDialog.get().dismiss();

        });


    }

    private void openCamera(String flag) {
        if (getActivity() != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, "تصویر جدید");
            contentValues.put(MediaStore.Images.Media.DESCRIPTION, "از دوربین");

            if (cameraType.equals(Constants.DEFAULT_CAMERA)) {
                navigationController.navigateToCamera(getActivity(), flag);
            } else {
                navigationController.navigateToCustomCamera(getActivity(), flag);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE__PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //open camera
//                navigationController.navigateToCamera(getActivity());
                openCamera(Constants.ZERO);
            } else {
                //permission denied
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void initViewModels() {
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel.class);

        Log.i(TAG, "initViewModels: ");

    }

    @Override
    protected void initAdapters() {
        Log.i(TAG, "initAdapters: ");
    }

    @Override
    protected void initData() {

        Log.i(TAG, "initData: ");

        getIntentData();

        bindingLatLng(itemViewModel.latValue, itemViewModel.lngValue);

        getItemDetail();

        getImageList();

        itemViewModel.getUploadItemData().observe(this, result -> {

            if (result != null) {

                switch (result.status) {
                    case SUCCESS:
                        if (result.data != null) {
                            if (selected) {

                                progressDialog.cancel();
                                itemViewModel.itemId = result.data.id;

                                if (isFirstImageSelected) {//reload
                                    itemViewModel.setUploadItemImageObj(imagePathList.get(0), result.data.id, firstImageId);
                                    progressDialog.show();
                                    isFirstImageSelected = false;
                                } else if (isSecImageSelected) {
                                    itemViewModel.setUploadItemImageObj(imagePathList.get(0), itemViewModel.itemId, secImageId);
                                    progressDialog.show();
                                    isSecImageSelected = false;
                                } else if (isThirdImageSelected) {
                                    itemViewModel.setUploadItemImageObj(imagePathList.get(0), itemViewModel.itemId, thirdImageId);
                                    progressDialog.show();
                                    isThirdImageSelected = false;
                                } else if (isFouthImageSelected) {
                                    itemViewModel.setUploadItemImageObj(imagePathList.get(0), itemViewModel.itemId, fouthImageId);
                                    progressDialog.show();
                                    isFouthImageSelected = false;
                                } else if (isFifthImageSelected) {
                                    itemViewModel.setUploadItemImageObj(imagePathList.get(0), itemViewModel.itemId, fifthImageId);
                                    progressDialog.show();
                                    isFifthImageSelected = false;
                                }


                            } else {
                                Toast.makeText(getActivity(), "آگهی با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                                if (getActivity() != null) {
                                    getActivity().finish();
                                }

                            }

                        }

                        break;

                    case ERROR:
                        progressDialog.cancel();
                        psDialogMsg.showErrorDialog(getString(R.string.error_message__item_cannot_upload), getString(R.string.app__ok));
                        psDialogMsg.show();
                        break;
                }
            }

        });

        itemViewModel.getUploadItemImageData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        progressDialog.cancel();
                        Toast.makeText(ItemEntryFragment.this.getActivity(), "آگهی با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();

                        imageCount += 1;

                        if (imagePathList.size() > imageCount) {
                            ItemEntryFragment.this.callImageUpload(imageCount);//first is one
                        }

                        if (getActivity() != null) {
                            getActivity().finish();
                        }

//                        else {
//                            if (ItemEntryFragment.this.getActivity() != null) {
//                                ItemEntryFragment.this.getActivity().finish();
//                            }
//                        }

                        break;

                    case ERROR:

                        Toast.makeText(ItemEntryFragment.this.getActivity(), "خطا!", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        psDialogMsg.showErrorDialog(ItemEntryFragment.this.getString(R.string.error_message__image_cannot_upload), ItemEntryFragment.this.getString(R.string.app__ok));
                        psDialogMsg.show();
                        break;
                }

            }
        });
    }

    private void getImageList() {
        LiveData<Resource<List<Image>>> imageListLiveData = imageViewModel.getImageListLiveData();
        imageViewModel.setImageParentId(Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId);
        imageListLiveData.observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                // Update the data
                bindingImageListData(listResource.data);
//                this.binding.get().executePendingBindings();

            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");
            }
        });
    }

    private void bindingImageListData(List<Image> imageList) {

        if (imageList.size() != 0) {
            if (imageList.size() == 1) {
                firstImageId = imageList.get(0).imgId;
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().firstImageView, imageList.get(0).imgPath);
            }
            if (imageList.size() == 2) {
                firstImageId = imageList.get(0).imgId;
                secImageId = imageList.get(1).imgId;
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().firstImageView, imageList.get(0).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().secImageView, imageList.get(1).imgPath);
            }
            if (imageList.size() == 3) {
                firstImageId = imageList.get(0).imgId;
                secImageId = imageList.get(1).imgId;
                thirdImageId = imageList.get(2).imgId;
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().firstImageView, imageList.get(0).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().secImageView, imageList.get(1).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().thirdImageView, imageList.get(2).imgPath);
            }
            if (imageList.size() == 4) {
                firstImageId = imageList.get(0).imgId;
                secImageId = imageList.get(1).imgId;
                thirdImageId = imageList.get(2).imgId;
                fouthImageId = imageList.get(3).imgId;
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().firstImageView, imageList.get(0).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().secImageView, imageList.get(1).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().thirdImageView, imageList.get(2).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().fouthImageView, imageList.get(3).imgPath);
            }
            if (imageList.size() == 5) {
                firstImageId = imageList.get(0).imgId;
                secImageId = imageList.get(1).imgId;
                thirdImageId = imageList.get(2).imgId;
                fouthImageId = imageList.get(3).imgId;
                fifthImageId = imageList.get(4).imgId;
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().firstImageView, imageList.get(0).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().secImageView, imageList.get(1).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().thirdImageView, imageList.get(2).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().fouthImageView, imageList.get(3).imgPath);
                dataBindingComponent.getFragmentBindingAdapters().bindFullImage(binding.get().fifthImageView, imageList.get(4).imgPath);
            }
        }
    }

    private void getItemDetail() {

        LiveData<Item> historyItemList = itemViewModel.getItemDetailFromDBByIdData();
        if (historyItemList != null) {
            historyItemList.observe(this, listResource -> {
                if (listResource != null) {
                    bindingItemDetailData(listResource);
                }

            });

        }
    }

    private void bindingItemDetailData(Item item) {
        binding.get().titleEditText.setText(item.title);
        itemViewModel.holder.cat_id = item.catId;
        itemViewModel.holder.sub_cat_id = item.subCatId;
        itemViewModel.holder.type_id = item.itemTypeId;
        itemViewModel.holder.condition_id = item.conditionOfItem;
        itemViewModel.holder.price_type_id = item.itemPriceTypeId;
        itemViewModel.holder.currency_id = item.itemCurrencyId;
        itemViewModel.holder.location_id = item.itemLocation.id;
        itemViewModel.holder.deal_option_id = item.dealOptionId;
        itemViewModel.is_sold_out = item.isSoldOut;
        this.catId = item.catId;
        this.subCatId = item.subCatId;
        this.typeId = item.itemTypeId;
        this.conditionId = item.conditionOfItem;
        this.priceTypeId = item.itemPriceTypeId;
        binding.get().priceTypeTextView.setText(item.itemPriceType.name);

        if (this.priceTypeId.toString().equals("7") || this.priceTypeId.toString().equals("1") || this.priceTypeId.toString().equals("8")) {
            binding.get().priceTitleTextView.setVisibility(View.VISIBLE);
            // binding.get().starTextView8.setVisibility(View.VISIBLE);
            binding.get().priceEditText.setVisibility(View.VISIBLE);
            binding.get().priceEditText.setText(item.price);
        } else {
            binding.get().priceTitleTextView.setVisibility(View.GONE);
            // binding.get().starTextView8.setVisibility(View.GONE);
            binding.get().priceEditText.setVisibility(View.GONE);
            binding.get().priceEditText.setText("0");
        }

        this.locationId = item.itemLocation.id;
        this.dealOptionId = item.dealOptionId;
        binding.get().dealOptionTextView.setText(item.itemDealOption.name);

        binding.get().categoryTextView.setText(item.category.name);


        binding.get().subCategoryTextView.setText(item.subCategory.name);
        binding.get().typeTextView.setText(item.itemType.name);
        binding.get().itemConditionTextView.setText(item.itemCondition.name);
        binding.get().locationTextView.setText(item.itemLocation.name);
        bindMap();
        bindingLatLng(item.lat, item.lng);
        binding.get().brandEditText.setText(item.brand);

        binding.get().highlightInfoEditText.setText(item.highlightInfo);
        binding.get().descEditText.setText(item.description);
        binding.get().remarkEditText.setText(item.dealOptionRemark);
        bindingIsShop(item.businessMode);
        binding.get().addressEditText.setText(item.address);

    }

    private void bindingIsShop(String businessMode) {
        if (businessMode.equals(Constants.ONE)) {
            binding.get().isShopCheckBox.setChecked(true);
        } else {
            binding.get().isShopCheckBox.setChecked(false);
        }
    }


    private void getIntentData() {
        try {
            //TODO ali changed
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {

                    this.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    if (this.itemId == null)
                        this.itemId = Constants.ADD_NEW_ITEM;

                    if (this.itemId.equals(Constants.EMPTY_STRING))
                        this.itemId = Constants.ADD_NEW_ITEM;

                    itemViewModel.itemId = this.itemId;

                    this.locationId = getActivity().getIntent().getExtras().getString(Constants.SELECTED_LOCATION_ID);
                    itemViewModel.holder.location_id = this.locationId;

                    locationName = getActivity().getIntent().getExtras().getString(Constants.SELECTED_LOCATION_NAME);

                    assert locationName != null;
                    if (!locationName.equals(getString(R.string.location_see_all))) {
                        binding.get().locationTextView.setText(locationName);
                    } else {
                        binding.get().locationTextView.setText("");
                    }


                    if (itemViewModel.itemId != null) {
                        if (!itemViewModel.itemId.equals(Constants.ADD_NEW_ITEM)) {//edit

                            itemViewModel.setItemDetailFromDBById(itemViewModel.itemId);

                        }
                    }
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    private void callImageUpload(int imageCount) {

        if (isSecImageSelected) {
            itemViewModel.setUploadItemImageObj(imagePathList.get(imageCount), itemViewModel.itemId, secImageId);
            isSecImageSelected = false;
        } else if (isThirdImageSelected) {
            itemViewModel.setUploadItemImageObj(imagePathList.get(imageCount), itemViewModel.itemId, thirdImageId);
            isThirdImageSelected = false;
        } else if (isFouthImageSelected) {
            itemViewModel.setUploadItemImageObj(imagePathList.get(imageCount), itemViewModel.itemId, fouthImageId);
            isFouthImageSelected = false;
        } else if (isFifthImageSelected) {
            itemViewModel.setUploadItemImageObj(imagePathList.get(imageCount), itemViewModel.itemId, fifthImageId);
            isFifthImageSelected = false;
        }

    }

    private void bindingLatLng(String latValue, String lngValue) {
        binding.get().latitudeEditText.setText(latValue);
        binding.get().lngEditText.setText(lngValue);
    }

    private void changeCamera() {

        if (marker != null) {
            marker.remove();
        }

        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(Double.valueOf(itemViewModel.latValue), Double.valueOf(itemViewModel.lngValue))).zoom(10).bearing(10).tilt(10).build()));

        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(itemViewModel.latValue), Double.valueOf(itemViewModel.lngValue)))
                .title("نام"));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");

        outState.putString(Constants.ITEM_ID, itemId);
        outState.putString(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        outState.putString(Constants.ITEM_LOCATION_TYPE_NAME, locationName);

        itemViewModel.setItemDetailFromDBById(itemId);


    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored: ");
        if (savedInstanceState == null) return;

        itemId = savedInstanceState.getString(Constants.ITEM_ID);
        locationId = savedInstanceState.getString(Constants.ITEM_LOCATION_TYPE_ID);
        locationName = savedInstanceState.getString(Constants.ITEM_LOCATION_TYPE_NAME);

        //   getDataFromSharedPef();

    }

}
