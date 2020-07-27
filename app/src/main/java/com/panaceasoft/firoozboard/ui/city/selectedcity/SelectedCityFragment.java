package com.panaceasoft.firoozboard.ui.city.selectedcity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentSelectedCityBinding;
import com.panaceasoft.firoozboard.ui.category.adapter.CityCategoryAdapter;
import com.panaceasoft.firoozboard.ui.common.DataBoundListAdapter;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.ui.item.adapter.ItemHorizontalListAdapter;
import com.panaceasoft.firoozboard.ui.item.adapter.ItemVerticalListAdapter;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.PSDialogMsg;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.firoozboard.viewmodel.item.ItemViewModel;
import com.panaceasoft.firoozboard.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.firoozboard.viewmodel.itemfromfollower.ItemFromFollowerViewModel;
import com.panaceasoft.firoozboard.viewmodel.psappinfo.PSAppInfoViewModel;
import com.panaceasoft.firoozboard.viewobject.Item;
import com.panaceasoft.firoozboard.viewobject.ItemCategory;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.panaceasoft.firoozboard.viewobject.common.Status;
import com.panaceasoft.firoozboard.viewobject.holder.ItemParameterHolder;

import java.util.List;

public class SelectedCityFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCategoryViewModel itemCategoryViewModel;
    // private RecentItemViewModel recentItemViewModel;
    private boolean layoutDone = false;
    private int loadingCount = 0;
    private PSDialogMsg psDialogMsg;
    private PSAppInfoViewModel psAppInfoViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;
    private ItemFromFollowerViewModel itemFromFollowerViewModel;
    private ItemParameterHolder searchItemParameterHolder = new ItemParameterHolder().getRecentItem();

    private ItemViewModel itemViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentSelectedCityBinding> binding;
    private AutoClearedValue<ItemVerticalListAdapter> adapter;
    private AutoClearedValue<ItemHorizontalListAdapter> followerItemListAdapter;
    private AutoClearedValue<CityCategoryAdapter> cityCategoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSelectedCityBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_city, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.layout__primary_background));
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.GRAY);
            //   ((MainActivity) getActivity()).updateMenuIconGrey();
        }

        getIntentData();

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest2 = new AdRequest.Builder()
                    .build();
            binding.get().adView2.loadAd(adRequest2);
        } else {
            binding.get().adView2.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);


        binding.get().followerViewAllTextView.setOnClickListener(v -> navigationController.navigateToItemListFromFollower(getActivity()));

        //  binding.get().recentItemViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), recentItemViewModel.recentItemParameterHolder, getString(R.string.selected_city_recent),  psAppInfoViewModel.appSettingLat, psAppInfoViewModel.appSettingLng,Constants.MAP_MILES));

        binding.get().categoryViewAllTextView.setOnClickListener(v -> navigationController.navigateToCategoryActivity(getActivity()));


        binding.get().locationTextView.setOnClickListener(v -> navigationController.navigateToLocationActivity(getActivity(), Constants.LOCATION_NOT_CLEAR_ICON));

        binding.get().searchBoxEditText.setOnKeyListener((v, keyCode, event) -> {

            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                callSearchList();
                return false;
            }
            return false;
        });
        binding.get().searchImageButton.setOnClickListener(v -> SelectedCityFragment.this.callSearchList());


        if (force_update) {
            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_title, force_update_msg);
        }


        binding.get().recentItemRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !itemViewModel.forceEndLoading) {

                            itemViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.ITEM_COUNT;

                            itemViewModel.offset = itemViewModel.offset + limit;

                            loadNextPageItemList(String.valueOf(itemViewModel.offset));

                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemViewModel.forceEndLoading = false;

            itemViewModel.loadingDirection = Utils.LoadingDirection.top;

            loadItemList();

        });

    }

    private void callSearchList() {
        if (binding.get().searchBoxEditText.getText().toString().length() > 0) {
            searchItemParameterHolder.keyword = binding.get().searchBoxEditText.getText().toString();

            navigationController.navigateToHomeFilteringActivity(getActivity(), searchItemParameterHolder, searchItemParameterHolder.keyword, psAppInfoViewModel.appSettingLat, psAppInfoViewModel.appSettingLng, Constants.MAP_MILES,selected_location_id);
        } else {
            Toast.makeText(getContext(), R.string.error_message__blank_keyword, Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    protected void initViewModels() {
        itemCategoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemCategoryViewModel.class);
        itemViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemViewModel.class);
        //  recentItemViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecentItemViewModel.class);
        itemFromFollowerViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemFromFollowerViewModel.class);
        psAppInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(PSAppInfoViewModel.class);
        clearAllDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(ClearAllDataViewModel.class);
    }

    @Override
    protected void initAdapters() {


        CityCategoryAdapter cityCategoryAdapter = new CityCategoryAdapter(dataBindingComponent,
                category -> navigationController.navigateToSubCategoryActivity(getActivity(), category.id, category.name), this);

        this.cityCategoryAdapter = new AutoClearedValue<>(this, cityCategoryAdapter);
        binding.get().cityCategoryRecyclerView.setAdapter(cityCategoryAdapter);


        ItemHorizontalListAdapter followerItemListAdapter = new ItemHorizontalListAdapter(dataBindingComponent, item -> navigationController.navigateToItemDetailActivity(SelectedCityFragment.this.getActivity(), item.id, item.title), this);
        this.followerItemListAdapter = new AutoClearedValue<>(this, followerItemListAdapter);

        binding.get().followerRecyclerView.setAdapter(followerItemListAdapter);

/*        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent, new ItemVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(getActivity(), item.id, item.title);
            }
        }, this);*/


        //  this.adapter = new AutoClearedValue<>(this, nvAdapter);

        //   binding.get().recentItemRecyclerView.setAdapter(nvAdapter);
        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent, new ItemVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(SelectedCityFragment.this.getActivity(), item.id, item.title);
            }
        }, this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().recentItemRecyclerView.setAdapter(nvAdapter);

    }

    private void replaceItemFromFollowerList(List<Item> itemList) {
        this.followerItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceRecentItemList(List<Item> itemList) {
        this.adapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceCityCategory(List<ItemCategory> categories) {
        cityCategoryAdapter.get().replace(categories);
        binding.get().executePendingBindings();
    }


    @Override
    protected void initData() {
        if (getActivity() != null) {


            if (((MainActivity) this.getActivity()).selectedLocationId != null) {
                itemViewModel.holder.location_id = ((MainActivity) this.getActivity()).selectedLocationId;

            }

            binding.get().locationTextView.setText(((MainActivity) this.getActivity()).selectedLocationName.toString());


            initItemData();
        }
        showItemFromFollower();

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        break;
                }
            }
        });

        loadProducts();
    }

    private void initItemData() {

        loadItemList();

        LiveData<Resource<List<Item>>> news = itemViewModel.getItemListByKeyData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {
                    switch (listResource.status) {
                        case SUCCESS:

                            itemViewModel.setLoadingState(false);

                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {

                                    if (!binding.get().getLoadingMore()) {
                                        //  binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {

                                    //   binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                }

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                onDispatched();
                            }
                            break;

                        case LOADING:
                            if (listResource.data != null) {

                                //  binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                itemViewModel.setLoadingState(true);

                                if (itemViewModel.forceEndLoading) {
                                    itemViewModel.forceEndLoading = false;
                                }

                                onDispatched();
                            }
                            break;

                        case ERROR:

                            itemViewModel.setLoadingState(false);
                            itemViewModel.forceEndLoading = true;

                            if (itemViewModel.getItemListByKeyData() != null) {
                                if (itemViewModel.getItemListByKeyData().getValue() != null) {
                                    if (itemViewModel.getItemListByKeyData().getValue().data != null) {
                                        if (!binding.get().getLoadingMore() && itemViewModel.getItemListByKeyData().getValue().data.size() == 0) {
                                            //    binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }


                            break;

                        default:
                            break;
                    }
                }
            });
        }

        itemViewModel.getNextPageItemListByKeyData().observe(this, state -> {

            if (state != null) {
                if (state.status == Status.ERROR) {

                    itemViewModel.setLoadingState(false);
                    itemViewModel.forceEndLoading = true;

                }
            }
        });

        itemViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(itemViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void replaceData(List<Item> newsList) {
        adapter.get().replace(newsList);
        binding.get().executePendingBindings();
    }


    private void showItemFromFollower() {
        if (loginUserId.isEmpty()) {
            hideForFollower();
        } else {
            showForFollower();
        }
    }

    private void showForFollower() {
        binding.get().itemFromFollowerCardView.setVisibility(View.VISIBLE);
        binding.get().followerConstraintLayout.setVisibility(View.VISIBLE);
        binding.get().followerTitleTextView.setVisibility(View.VISIBLE);
        binding.get().followerViewAllTextView.setVisibility(View.VISIBLE);
        //  binding.get().followerDescTextView.setVisibility(View.VISIBLE);
        binding.get().followerRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideForFollower() {
        binding.get().itemFromFollowerCardView.setVisibility(View.GONE);
        binding.get().followerConstraintLayout.setVisibility(View.GONE);
        binding.get().followerTitleTextView.setVisibility(View.GONE);
        binding.get().followerViewAllTextView.setVisibility(View.GONE);
        //  binding.get().followerDescTextView.setVisibility(View.GONE);
        binding.get().followerRecyclerView.setVisibility(View.GONE);
    }

    private void getIntentData() {

/*        if (getActivity() != null) {
            recentItemViewModel.locationId  = getActivity().getIntent().getStringExtra(Constants.SELECTED_LOCATION_ID);
            recentItemViewModel.locationName = getActivity().getIntent().getStringExtra(Constants.SELECTED_LOCATION_NAME);

            if(getArguments() != null){
                recentItemViewModel.locationId  = getArguments().getString(Constants.SELECTED_LOCATION_ID);
                recentItemViewModel.locationName = getArguments().getString(Constants.SELECTED_LOCATION_NAME);
                recentItemViewModel.locationLat  = getArguments().getString(Constants.LAT);
                recentItemViewModel.locationLng  = getArguments().getString(Constants.LNG);
            }

            recentItemViewModel.recentItemParameterHolder.location_id = recentItemViewModel.locationId;
            searchItemParameterHolder.location_id = recentItemViewModel.locationId;

            binding.get().locationTextView.setText(recentItemViewModel.locationName);

        }*/
    }

    private void loadProducts() {


        // Category

        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT), Constants.ZERO);

        itemCategoryViewModel.getCategoryListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });




/*
        //Recent Item

        recentItemViewModel.setRecentItemListByKeyObj(Utils.checkUserId(loginUserId), Config.LIMIT_FROM_DB_COUNT, Constants.ZERO, recentItemViewModel.recentItemParameterHolder);

        recentItemViewModel.getRecentItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                SelectedCityFragment.this.replaceRecentItemList(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                SelectedCityFragment.this.replaceRecentItemList(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });
*/

        // Item from follower

        itemFromFollowerViewModel.setItemFromFollowerListObj(Utils.checkUserId(loginUserId), Config.LIMIT_FROM_DB_COUNT, Constants.ZERO);

        itemFromFollowerViewModel.getItemFromFollowerListData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceItemFromFollowerList(listResource.data);
                                showForFollower();
                            }
                        } else {
                            hideForFollower();
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceItemFromFollowerList(listResource.data);
                                showForFollower();
                            }
                        } else {
                            hideForFollower();
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //endregion

    }

    @Override
    public void onDispatched() {

        if (itemViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get() != null) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        binding.get().recentItemRecyclerView.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        }
    }

//    @Override
    // public void onDispatched() {

//        if (homeLatestProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            LinearLayoutManager layoutManager = (LinearLayoutManager)
//                    binding.get().productList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
//
//        if (homeSearchProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            GridLayoutManager layoutManager = (GridLayoutManager)
//                    binding.get().discountList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
//
//        if (homeTrendingProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            GridLayoutManager layoutManager = (GridLayoutManager)
//                    binding.get().trendingList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
    //  }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if(data != null) {
            if (requestCode == Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE) {

                recentItemViewModel.locationId = data.getStringExtra(Constants.ITEM_LOCATION_TYPE_ID);
                recentItemViewModel.locationName = data.getStringExtra(Constants.ITEM_LOCATION_TYPE_NAME);
                recentItemViewModel.locationLat = data.getStringExtra(Constants.LAT);
                recentItemViewModel.locationLng = data.getStringExtra(Constants.LNG);

                pref.edit().putString(Constants.SELECTED_LOCATION_ID, recentItemViewModel.locationId).apply();
                pref.edit().putString(Constants.SELECTED_LOCATION_NAME,recentItemViewModel.locationName).apply();
                pref.edit().putString(Constants.LAT, recentItemViewModel.locationLat).apply();
                pref.edit().putString(Constants.LNG,recentItemViewModel.locationLng).apply();


                if(getActivity() != null) {

                    navigationController.navigateToHome((MainActivity)getActivity(),true,recentItemViewModel.locationId,
                            recentItemViewModel.locationName);
                }
            }
        }*/
    }

    private void resetLimitAndOffset() {
        itemViewModel.offset = 0;
    }

    private void loadNextPageItemList(String offset) {

        itemViewModel.setNextPageItemListByKeyObj(String.valueOf(Config.ITEM_COUNT), offset, Utils.checkUserId(loginUserId), itemViewModel.holder);


    }

    private void loadItemList() {

        resetLimitAndOffset();

        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

    }

    @Override
    public void onResume() {
        loadLoginUserId();
        super.onResume();
    }

}
