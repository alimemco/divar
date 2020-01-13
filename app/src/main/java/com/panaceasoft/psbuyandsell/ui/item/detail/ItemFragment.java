package com.panaceasoft.psbuyandsell.ui.item.detail;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.panaceasoft.psbuyandsell.Config;
import com.panaceasoft.psbuyandsell.R;
import com.panaceasoft.psbuyandsell.binding.FragmentDataBindingComponent;
import com.panaceasoft.psbuyandsell.databinding.FragmentItemBinding;
import com.panaceasoft.psbuyandsell.databinding.ItemEntryBottomBoxBinding;
import com.panaceasoft.psbuyandsell.databinding.ItemInfoBottomBoxBinding;
import com.panaceasoft.psbuyandsell.ui.common.PSFragment;
import com.panaceasoft.psbuyandsell.ui.gallery.detail.GalleryDetailFragment;
import com.panaceasoft.psbuyandsell.utils.AutoClearedValue;
import com.panaceasoft.psbuyandsell.utils.Constants;
import com.panaceasoft.psbuyandsell.utils.JalaliCalendar;
import com.panaceasoft.psbuyandsell.utils.PSDialogMsg;
import com.panaceasoft.psbuyandsell.utils.TouchImageView;
import com.panaceasoft.psbuyandsell.utils.Utils;
import com.panaceasoft.psbuyandsell.utils.ViewAnimationUtil;
import com.panaceasoft.psbuyandsell.viewmodel.image.ImageViewModel;
import com.panaceasoft.psbuyandsell.viewmodel.item.FavouriteViewModel;
import com.panaceasoft.psbuyandsell.viewmodel.item.ItemViewModel;
import com.panaceasoft.psbuyandsell.viewmodel.item.SpecsViewModel;
import com.panaceasoft.psbuyandsell.viewmodel.item.TouchCountViewModel;
import com.panaceasoft.psbuyandsell.viewmodel.rating.RatingViewModel;
import com.panaceasoft.psbuyandsell.viewobject.Image;
import com.panaceasoft.psbuyandsell.viewobject.Item;
import com.panaceasoft.psbuyandsell.viewobject.common.Resource;
import com.panaceasoft.psbuyandsell.viewobject.common.Status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private ImageViewModel imageViewModel;
    private TouchCountViewModel touchCountViewModel;
    private FavouriteViewModel favouriteViewModel;
    private SpecsViewModel specsViewModel;
    private RatingViewModel ratingViewModel;
    private PSDialogMsg psDialogMsg;
    private ImageView imageView;
    private Item item;
    @VisibleForTesting
    private AutoClearedValue<FragmentItemBinding> binding;
    private AutoClearedValue<ProgressDialog> prgDialog;
    private AutoClearedValue<BottomSheetDialog> mBottomSheetDialog;
    private AutoClearedValue<ItemInfoBottomBoxBinding> bottomBoxLayoutBinding;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);


        item = binding.get().getItem();

        return binding.get().getRoot();
    }


    //region Override Methods

    private static void fixLeakCanary696(Context context) {

        //https://github.com/square/leakcanary/issues/696
        try {
            Class clazz = Class.forName(Constants.GALLERY_BOOST);
            Utils.psLog("clazz " + clazz);

            Field _sGestureBoostManager = clazz.getDeclaredField(Constants.GALLERY_GESTURE);
            _sGestureBoostManager.setAccessible(true);
            Field _mContext = clazz.getDeclaredField(Constants.GALLERY_CONTEXT);
            _mContext.setAccessible(true);

            Object sGestureBoostManager = _sGestureBoostManager.get(null);
            if (sGestureBoostManager != null) {
                _mContext.set(sGestureBoostManager, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUIAndActions() {


        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        if (getContext() != null) {

            BottomSheetDialog mBottomSheetDialog2 = new BottomSheetDialog(getContext());
            mBottomSheetDialog = new AutoClearedValue<>(this, mBottomSheetDialog2);

            ItemInfoBottomBoxBinding bottomBoxLayoutBinding2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_info_bottom_box, null, false);
            bottomBoxLayoutBinding = new AutoClearedValue<>(this, bottomBoxLayoutBinding2);
            mBottomSheetDialog.get().setContentView(bottomBoxLayoutBinding.get().getRoot());

        }
        binding.get().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int arg0) {

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
            public void onPageSelected(int currentPage) {

                if (imageViewModel.newsImageList != null) {
                    if (currentPage >= imageViewModel.newsImageList.size()) {
                        currentPage = currentPage % imageViewModel.newsImageList.size();
                    }

                    binding.get().imgDesc.setText(imageViewModel.newsImageList.get(currentPage).imgDesc);
                }

            }

        });


        binding.get().pageIndicatorView.setViewPager(binding.get().viewPager);

        //  binding.get().toolbar.setTitleTextColor(getResources().getColor(R.color.text__white));

        // ((MainActivity)getActivity()).updateMenuIconWhite();


        binding.get().backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        binding.get().backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });


        binding.get().constraintLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psDialogMsg.showConfirmDialog(getString(R.string.item_detail__confirm_report), getString(R.string.app__ok), getString(R.string.message__cancel_close));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v12 -> {
                    itemViewModel.setReportItemStatusObj(itemViewModel.itemId, loginUserId);

                    psDialogMsg.cancel();
                });

                psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

            }
        });

        binding.get().userCardView.setOnClickListener(v -> navigationController.navigateToUserDetail(getActivity(), itemViewModel.otherUserId, itemViewModel.otherUserName));

/*
                if (item.getTitle().toString().equals(getString(R.string.menu__report_item))) {

                    itemViewModel.setReportItemStatusObj(itemViewModel.itemId, loginUserId);
                } else {//share

                    Bitmap bitmap = getBitmapFromView(getCurrentImageView());
                    shareImageUri(saveImageExternal(bitmap));
                }
*/


        binding.get().editButton.setOnClickListener(v -> navigationController.navigateToItemEntryActivity(getActivity(), itemViewModel.itemId, itemViewModel.locationId, itemViewModel.locationName));

        binding.get().soldTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.get().soldTextView.getText().equals(getResources().getString(R.string.item_detail__mark_sold))) {
                    psDialogMsg.showConfirmDialog(getString(R.string.item_detail__confirm_sold_out), getString(R.string.app__ok), getString(R.string.message__cancel_close));
                    psDialogMsg.show();

                    psDialogMsg.okButton.setOnClickListener(v12 -> {
                        itemViewModel.setMarkAsSoldOutItemObj(itemViewModel.itemId, loginUserId);

                        psDialogMsg.cancel();
                    });

                    psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

                }
            }
        });


        binding.get().deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psDialogMsg.showConfirmDialog(getString(R.string.item_detail__confirm_delete), getString(R.string.app__ok), getString(R.string.message__cancel_close));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v12 -> {
                    itemViewModel.setDeleteItemObj(itemViewModel.itemId, loginUserId);

                    psDialogMsg.cancel();
                });

                psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

            }
        });


        binding.get().statisticDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().statisticTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().statisticDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().meetTheSellerDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().userImageView);
                ViewAnimationUtil.expand(binding.get().mailImageView);
                ViewAnimationUtil.expand(binding.get().userNameTextView);
                ViewAnimationUtil.expand(binding.get().joinedTextView);
                ViewAnimationUtil.expand(binding.get().verifiedTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().userImageView);
                ViewAnimationUtil.collapse(binding.get().mailImageView);
                ViewAnimationUtil.collapse(binding.get().userNameTextView);
                ViewAnimationUtil.collapse(binding.get().joinedTextView);
                ViewAnimationUtil.collapse(binding.get().verifiedTextView);
            }
        });

        binding.get().meetTheSellerTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().meetTheSellerDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().userImageView);
                ViewAnimationUtil.expand(binding.get().mailImageView);
                ViewAnimationUtil.expand(binding.get().userNameTextView);
                ViewAnimationUtil.expand(binding.get().joinedTextView);
                ViewAnimationUtil.expand(binding.get().verifiedTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().userImageView);
                ViewAnimationUtil.collapse(binding.get().mailImageView);
                ViewAnimationUtil.collapse(binding.get().userNameTextView);
                ViewAnimationUtil.collapse(binding.get().joinedTextView);
                ViewAnimationUtil.collapse(binding.get().verifiedTextView);
            }
        });


        binding.get().chatButton.setOnClickListener(v -> {
         //   Toast.makeText(getContext(), "Clicked chat button", Toast.LENGTH_SHORT).show();

            if (loginUserId.equals("")) {
                navigationController.navigateToUserLoginActivity(getActivity());
            } else if (itemViewModel.currentItem.user.userId.isEmpty()) {
                psDialogMsg.showWarningDialog(getString(R.string.item_entry_user_not_exit), getString(R.string.app__ok));
                psDialogMsg.show();
            } else {
                navigationController.navigateToChatActivity(getActivity(),
                        itemViewModel.currentItem.id,
                        itemViewModel.currentItem.user.userId,
                        itemViewModel.currentItem.user.userName,
                        itemViewModel.currentItem.defaultPhoto.imgPath,
                        itemViewModel.currentItem.title,
                        itemViewModel.currentItem.price,
                        itemViewModel.currentItem.itemCondition.name,
                        Constants.CHAT_FROM_SELLER,
                        itemViewModel.currentItem.user.userProfilePhoto,
                        0
                );
            }


        });

    }


    @Override
    protected void initViewModels() {
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel.class);
        ratingViewModel = ViewModelProviders.of(this, viewModelFactory).get(RatingViewModel.class);
        specsViewModel = ViewModelProviders.of(this, viewModelFactory).get(SpecsViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        getIntentData();

        getItemDetail();

        getMarkAsSoldOutData();

        getFavData();

        getTouchCount();

        try {

            if (getActivity() != null) {

                imageViewModel.imgType = Constants.IMAGE_TYPE_PRODUCT;
                imageViewModel.id = itemViewModel.itemId;
                imageViewModel.imgId = "1";
            }

        } catch (Exception e) {
            Utils.psErrorLog("Error in getting intent.", e);
        }

        LiveData<Resource<List<Image>>> imageListLiveData = imageViewModel.getImageListLiveData();
        imageViewModel.setImageParentId(imageViewModel.imgType, imageViewModel.id);
        imageListLiveData.observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");

                // Update the data
                imageViewModel.newsImageList = listResource.data;

                int selectedItemPosition = 0;

                for (int i = 0; i < imageViewModel.newsImageList.size(); i++) {
                    if (imageViewModel.newsImageList.get(i).imgId.equals(imageViewModel.imgId)) {
                        selectedItemPosition = i;
                        break;
                    }
                }

                binding.get().viewPager.setAdapter(new ItemFragment.TouchImageAdapter());

                try {
                    binding.get().viewPager.setCurrentItem(selectedItemPosition);

                    binding.get().imgDesc.setText(imageViewModel.newsImageList.get(selectedItemPosition).imgDesc);
                } catch (Exception e) {
                    Utils.psErrorLog("", e);
                }

            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");
            }
        });

        getFavData();

        getReportItemStatus();

        getDeleteItemStatus();

    }
    class TouchImageAdapter extends PagerAdapter {

        private TouchImageAdapter() {
        }

        @Override
        public int getCount() {
            if (imageViewModel.newsImageList != null) {
                return imageViewModel.newsImageList.size();
            }

            return 0;

        }

        @Override
        @NonNull
        public View instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imgView = new ImageView(container.getContext());
            if (imageViewModel.newsImageList != null) {
                if (position >= imageViewModel.newsImageList.size()) {
                    position = position % imageViewModel.newsImageList.size();
                }

                if (getActivity() != null) {
                    imgView.setRotationX(180);
                    imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    dataBindingComponent.getFragmentBindingAdapters().bindFullImage(imgView,  imageViewModel.newsImageList.get(position).imgPath);

                    container.addView(imgView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int finalPosition = position;
                    imgView.setOnClickListener(v -> {
                        navigationController.navigateToDetailGalleryActivity(getActivity(), Constants.IMAGE_TYPE_PRODUCT,  itemViewModel.itemId, imageViewModel.newsImageList.get(finalPosition).imgId);
                    });
                }
            }
            return imgView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

    private void getDeleteItemStatus() {
        itemViewModel.getDeleteItemStatus().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "آگهی با موفقیت حذف گردید.", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().finish();
                        }

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "مشکل در حذف آگهی!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getMarkAsSoldOutData() {
        LiveData<Resource<Item>> itemDetail = itemViewModel.getMarkAsSoldOutItemData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                Toast.makeText(getContext(), "مورد به فروخته شده تغییر یافت", Toast.LENGTH_SHORT).show();

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);
//                            binding.get().markAsSoldButton.setVisibility(View.VISIBLE);

                            break;

                        default:
                            // Default

                            break;
                    }

                } else {

                    itemViewModel.setLoadingState(false);

                }
            });
        }
    }

    private void getReportItemStatus() {

        itemViewModel.getReportItemStatusData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "آگهی با موفقیت گزارش شد", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "مشکل در گزارش آگهی!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getTouchCount() {

        //get touch count post method
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void getFavData() {
        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void getIntentData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemViewModel.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    itemViewModel.historyFlag = getActivity().getIntent().getExtras().getString(Constants.HISTORY_FLAG);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    private void shareImageUri(Uri uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                Objects.requireNonNull(getContext()).startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmapFromView(ImageView view) {
        Drawable drawable = view.getDrawable();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private ImageView getCurrentImageView() {
        return imageView;
    }

    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void getItemDetail() {

        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

        LiveData<Resource<Item>> itemDetail = itemViewModel.getItemDetailData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);
                                itemViewModel.userId = listResource.data.user.userId;
                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);
                                bindingCountData(listResource.data);
                                bindingNumberData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);

                                // Update the data
                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);
                                itemViewModel.userId = listResource.data.user.userId;
//                                if (itemViewModel.userId != null){
                                callTouchCount();
//                                }
                                bindingCountData(listResource.data);
                                bindingNumberData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);
                                itemViewModel.locationId = listResource.data.itemLocation.id;
                                itemViewModel.locationName = listResource.data.itemLocation.name;
                                itemViewModel.otherUserId = listResource.data.user.userId;
                                itemViewModel.otherUserName = listResource.data.user.userName;
//                                checkText(listResource.data);

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }

                } else {

                    itemViewModel.setLoadingState(false);

                }
            });
        }


        //get rating post method
        ratingViewModel.getRatingPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                        navigationController.navigateToRatingList(ItemFragment.this.getActivity(), item);
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }
                }
            }
        });


        //load product specs

//        LiveData<List<ItemSpecs>> itemSpecs = specsViewModel.getSpecsListData();
//        if(itemSpecs != null) {
//            itemSpecs.observe(this, listResource -> {
//                if (listResource != null && listResource.size() > 0) {
//
//                    ItemFragment.this.replaceItemSpecsData(listResource);
//                    specsViewModel.isSpecsData = true;
//
//                } else {
//                    specsViewModel.isSpecsData = false;
//                    binding.get().fiveCardView.setVisibility(View.GONE);
//
//                }
//                showOrHideSpecs();
//            });
//        }
//    }

//    private void bindingMapData(Item item) {
//        itemViewModel.latValue = item.lat;
//        itemViewModel.lngValue = item.lng;

//        //load product specs
//
//        LiveData<List<ItemSpecs>> itemSpecs = specsViewModel.getSpecsListData();
//        if(itemSpecs != null) {
//            itemSpecs.observe(this, listResource -> {
//                if (listResource != null && listResource.size() > 0) {
//
//                    ItemFragment.this.replaceItemSpecsData(listResource);
//                    specsViewModel.isSpecsData = true;
//
//                } else {
//                    specsViewModel.isSpecsData = false;
//                    binding.get().fiveCardView.setVisibility(View.GONE);
//
//                }
//                showOrHideSpecs();
//            });
//        }

    }

    private void callTouchCount() {
        if (!loginUserId.equals(itemViewModel.userId)) {
            if (connectivity.isConnected()) {
                touchCountViewModel.setTouchCountPostDataObj(loginUserId, itemViewModel.itemId);
            }
        }
    }

    private void replaceItemData(Item item) {
        itemViewModel.currentItem = item;
        binding.get().setItem(item);

    }

    private void bindingNumberData(Item item) {
        binding.get().InfoButton.setOnClickListener(v -> {

            mBottomSheetDialog.get().show();
            bottomBoxLayoutBinding.get().CallButton.setText(getResources().getString(R.string.info__item_call) + " " + item.brand.toString());
            bottomBoxLayoutBinding.get().SMSButton.setText(getResources().getString(R.string.info__item_sms) + " " + item.brand.toString());
            mBottomSheetDialog.get().show();
            bottomBoxLayoutBinding.get().CallButton.setOnClickListener(view -> {


                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + item.brand));
                startActivity(callIntent);

                mBottomSheetDialog.get().dismiss();
            });

            bottomBoxLayoutBinding.get().SMSButton.setOnClickListener(view -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+item.brand));
                startActivity(intent);

                mBottomSheetDialog.get().dismiss();
            });


        });
    }

    private void bindingCountData(Item item) {
        binding.get().favouriteCountTextView.setText(getString(R.string.item_detail__fav_count, item.favouriteCount));
        binding.get().viewCountTextView.setText(getString(R.string.item_detail__view_count, item.touchCount));
    }

    private void bindingCategoryNameAndSubCategoryName(Item item) {
        String categoryName = item.category.name;
        String subCategoryName = item.subCategory.name;

        if (categoryName.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setText(subCategoryName);
        } else if (subCategoryName.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setText(categoryName);
        } else {
            String name = categoryName + " / " + subCategoryName;
            binding.get().categoryAndSubCategoryTextView.setText(name);
        }

    }

    private void bindingPriceWithCurrencySymbol(Item item) {
        if (!item.price.toString().equals("0")) {
            if (item.itemPriceTypeId.toString().equals("7") || item.itemPriceTypeId.toString().equals("1") || item.itemPriceTypeId.toString().equals("8")) {
                String price = item.price;
                String currencyPrice = price + " " + getString(R.string.item_entry_detail__price_symbol) + " " + item.itemPriceType.name;
                binding.get().priceTextView.setText(currencyPrice);
            } else {
                String price = item.price;
                String currencyPrice = price + " " + getString(R.string.item_entry_detail__price_symbol);
                binding.get().priceTextView.setText(currencyPrice);
            }
        } else {
            binding.get().priceTextView.setText(item.itemPriceType.name.toString());
        }
    }


    private void bindingVerifiedData(Item item) {
        if (item.user.verifyTypes.equals("1")) {
            binding.get().mailImageView.setVisibility(View.VISIBLE);
        }
        if (item.user.verifyTypes.equals("2")) {
            binding.get().mailImageView.setVisibility(View.GONE);
        }
    }


    private void bindingFavoriteData(Item item) {

        if (item.isFavourited.equals(Constants.ONE)) {
            binding.get().fav.setText(getString(R.string.item_detail__like));
            binding.get().fav.setTextColor(getResources().getColor(R.color.global__primary));
            binding.get().constraintLayout3.setBackground(getResources().getDrawable(R.drawable.rounded_corner_shape_view_with_border_red));
            binding.get().imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_two));
            binding.get().constraintLayoutClick2.setOnClickListener(v -> {

                unFavFunction(item);

            });
        } else {
            binding.get().fav.setText(getString(R.string.item_detail__featured));
            binding.get().fav.setTextColor(getResources().getColor(R.color.text__primary));
            binding.get().constraintLayout3.setBackground(getResources().getDrawable(R.drawable.rounded_corner_shape_view_with_border));
            binding.get().imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_book_mark));
            binding.get().constraintLayoutClick2.setOnClickListener(v -> {

                favFunction(item);

            });
        }
    }

    private void bindingSoldData(Item item) {
        if (item.isSoldOut.equals(Constants.ONE)) {
            binding.get().soldTextView.setText(getString(R.string.item_detail__sold));
        } else {
            if (item.addedUserId.equals(loginUserId)) {
                binding.get().soldTextView.setText(R.string.item_detail__mark_sold);
            } else {
                binding.get().soldTextView.setVisibility(View.GONE);
            }
        }
    }

    private void bindingPhotoCount(Item item) {
        if (item.photoCount.toString().equals("0")) {
            binding.get().coverImageView.setVisibility(View.GONE);
            binding.get().toolbar1.setVisibility(View.GONE);
            binding.get().toolbar12.setVisibility(View.VISIBLE);
            // binding.get().toolbar.setAlpha(0f);
            /* set the scroll change listener on scrollview */
            binding.get().nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    /* get the maximum height which we have scroll before performing any action */
                    int maxDistance = binding.get().layoutnull.getHeight();
                    /* how much we have scrolled */
                    int movement = binding.get().nestedScrollView.getScrollY();
                    /*finally calculate the alpha factor and set on the view */
                    //   float alphaFactor = ((movement * 1.0f) / (maxDistance - binding.get().toolbar.getHeight()));

                    if (movement >= 0 && movement <= maxDistance) {
                        /*for image parallax with scroll */
                        //    binding.get().itemNameTextView.setTranslationY(-movement/2);
                        /* set visibility */
                        //   binding.get().toolbar.setAlpha(alphaFactor);
                        binding.get().toolbarTextView2.setText("");
                    } else {
                        binding.get().toolbarTextView2.setText(binding.get().itemNameTextView.getText());
                    }

                }
            });

        } else {
            binding.get().coverImageView.setVisibility(View.VISIBLE);
            binding.get().toolbar1.setVisibility(View.VISIBLE);
            binding.get().toolbar12.setVisibility(View.GONE);

           // binding.get().toolbar.setAlpha(0f);
            /* set the scroll change listener on scrollview */
            binding.get().nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    /* get the maximum height which we have scroll before performing any action */
                    int maxDistance = binding.get().coverImageView.getHeight();
                    /* how much we have scrolled */
                    int movement = binding.get().nestedScrollView.getScrollY();
                    /*finally calculate the alpha factor and set on the view */
                    float alphaFactor = ((movement * 1.0f) / (maxDistance - binding.get().toolbar.getHeight()));

                    if (movement >= 0 && movement <= maxDistance) {
                        /*for image parallax with scroll */
                        //    binding.get().itemNameTextView.setTranslationY(-movement/2);
                        /* set visibility */
                        binding.get().toolbar.setAlpha(alphaFactor);
                        binding.get().toolbarTextView.setText("");
                    } else {
                        binding.get().toolbarTextView.setText(binding.get().itemNameTextView.getText());
                    }

                }
            });
        }
        binding.get().photoCountTextView.setText(String.format("%s", item.photoCount));

    }


    private void bindindAddedDateUserName(Item item) {
        binding.get().activeHourTextView.setText(item.addedDateStr);
    }

    private void bindingBottomConstraintLayout(Item item) {
        if (item.isOwner.equals(Constants.ONE)) {
            binding.get().itemOwnerConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().secCardView.setVisibility(View.VISIBLE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.GONE);
        } else {
            binding.get().itemSupplierConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().itemOwnerConstraintLayout.setVisibility(View.GONE);
            binding.get().secCardView.setVisibility(View.GONE);
        }
    }

    private void unFavFunction(Item item) {
        if (loginUserId.equals("")) {
            navigationController.navigateToUserLoginActivity(getActivity());
            //   likeButton.setLiked(false);
        } else {
            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                binding.get().fav.setText(getString(R.string.item_detail__featured));
                binding.get().fav.setTextColor(getResources().getColor(R.color.text__primary));
                binding.get().constraintLayout3.setBackground(getResources().getDrawable(R.drawable.rounded_corner_shape_view_with_border_red));
                binding.get().imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_book_mark));
                //   likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }
        }
    }

    private void favFunction(Item item) {
        if (loginUserId.equals("")) {
            //  likeButton.setLiked(false);
            navigationController.navigateToUserLoginActivity(getActivity());
        } else {
            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                binding.get().fav.setText(getString(R.string.item_detail__like));
                binding.get().fav.setTextColor(getResources().getColor(R.color.global__primary));
                binding.get().constraintLayout3.setBackground(getResources().getDrawable(R.drawable.rounded_corner_shape_view_with_border));
                binding.get().imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_two));
                //  likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }
        }
    }

    private void showOrHide(Item item) {



        binding.get().Sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody =item.title.toString()+"\n\n"+getResources().getString(R.string.item_show)+"\n"+ Config.APP_IMAGES_URL.replace("uploads/","share/")+item.id;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            }
        });
        binding.get().Sharebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bitmap bitmap = getBitmapFromView(getCurrentImageView());
                //  shareImageUri(saveImageExternal(bitmap));
                String shareBody =item.title.toString()+"\n\n"+getResources().getString(R.string.item_show)+"\n"+ Config.APP_IMAGES_URL.replace("uploads/","share/")+item.id;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  getString(R.string.app_name));
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            }
        });
        if (item != null && item.user.addedDate != null && item.user.addedDate.equals("")) {

            binding.get().joinedTextView.setVisibility(View.GONE);
        } else {
            String startDateString = item.user.addedDate.toString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
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

            binding.get().joinedTextView.setText(timeClock + " " + jalaliDate.toString());
            binding.get().joinedTextView.setVisibility(View.VISIBLE);
        }

        if (item != null && item.addedDateStr != null && item.addedDateStr.equals("")) {
            binding.get().activeHourTextView.setVisibility(View.GONE);
            binding.get().imageView16.setVisibility(View.GONE);
        } else {
            binding.get().activeHourTextView.setVisibility(View.VISIBLE);
            binding.get().imageView16.setVisibility(View.VISIBLE);
        }
        if (item != null && item.price != null && item.price.equals("")) {
            binding.get().priceTextView.setVisibility(View.GONE);
            binding.get().imageView17.setVisibility(View.GONE);
        } else {
            binding.get().priceTextView.setVisibility(View.VISIBLE);
            binding.get().imageView17.setVisibility(View.VISIBLE);
        }
        if (item != null && item.favouriteCount != null && item.favouriteCount.equals("")) {
            binding.get().imageView22.setVisibility(View.GONE);
        } else {
            binding.get().imageView22.setVisibility(View.VISIBLE);
        }
        if (item != null && item.category.name != null && item.subCategory.name != null && item.category.name.equals("") && item.subCategory.name.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setVisibility(View.GONE);
            binding.get().imageView23.setVisibility(View.GONE);
        } else {
            binding.get().categoryAndSubCategoryTextView.setVisibility(View.VISIBLE);
            binding.get().imageView23.setVisibility(View.VISIBLE);
        }

/*
        if (item != null && item.brand != null && item.brand.equals("")) {
            binding.get().brandTextView.setVisibility(View.GONE);
            binding.get().imageView24.setVisibility(View.GONE);
        } else {
            binding.get().brandTextView.setVisibility(View.VISIBLE);
            binding.get().imageView24.setVisibility(View.VISIBLE);
        }
*/

        if (item != null && item.itemType.name != null && item.itemType.name.equals("")) {
            binding.get().saleBuyTextView.setVisibility(View.GONE);
            binding.get().imageView27.setVisibility(View.GONE);
        } else {
            binding.get().saleBuyTextView.setVisibility(View.VISIBLE);
            binding.get().imageView27.setVisibility(View.VISIBLE);
        }

        if (item != null && item.itemLocation.name != null && item.itemLocation.name.equals("")) {
            binding.get().cityTextView.setVisibility(View.GONE);
            binding.get().cityTextView2.setVisibility(View.GONE);
        } else {
            binding.get().cityTextView.setVisibility(View.VISIBLE);
            binding.get().cityTextView2.setVisibility(View.VISIBLE);
        }

        if (item != null && item.description != null && item.description.equals("")) {
            binding.get().informationTextView.setVisibility(View.GONE);
        } else {
            binding.get().informationTextView.setVisibility(View.VISIBLE);
        }

        if (item != null && item.address != null && item.address.equals("")) {
            binding.get().addressTextView.setVisibility(View.GONE);
            binding.get().addressTextView2.setVisibility(View.GONE);
            binding.get().line6.setVisibility(View.GONE);
            binding.get().imageView22.setVisibility(View.GONE);
        } else {
            binding.get().addressTextView.setVisibility(View.VISIBLE);
            binding.get().addressTextView2.setVisibility(View.VISIBLE);
            binding.get().line6.setVisibility(View.VISIBLE);
            binding.get().imageView22.setVisibility(View.VISIBLE);
        }

        if (item != null && item.addedUserId != null && item.addedUserId.equals(loginUserId)) {
//            if(item.isSoldOut.equals(Constants.ONE)){
//                binding.get().markAsSoldButton.setVisibility(View.GONE);
//            }else {
//                binding.get().markAsSoldButton.setVisibility(View.VISIBLE);
//            }
            binding.get().editButton.setVisibility(View.VISIBLE);
            binding.get().deleteButton.setVisibility(View.VISIBLE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.GONE);

        } else {
            binding.get().editButton.setVisibility(View.GONE);
            binding.get().deleteButton.setVisibility(View.GONE);
//            binding.get().markAsSoldButton.setVisibility(View.GONE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getActivity() != null) {
            fixLeakCanary696(getActivity().getApplicationContext());
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
        if (loginUserId != null) {
            itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
        }
        psDialogMsg.cancel();
//        binding.get().rating.setRating(0);
    }


}
