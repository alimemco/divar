package com.panaceasoft.firoozboard.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.R;
import com.panaceasoft.firoozboard.binding.FragmentDataBindingComponent;
import com.panaceasoft.firoozboard.databinding.FragmentSearchCategoryBinding;
import com.panaceasoft.firoozboard.ui.category.adapter.SearchCategoryAdapter;
import com.panaceasoft.firoozboard.ui.common.PSFragment;
import com.panaceasoft.firoozboard.utils.AutoClearedValue;
import com.panaceasoft.firoozboard.utils.Constants;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.itemcategory.ItemCategoryViewModel;
import com.panaceasoft.firoozboard.viewobject.ItemCategory;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.panaceasoft.firoozboard.viewobject.common.Status;

import java.util.List;

public class DashBoardSearchCategoryFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCategoryViewModel itemCategoryViewModel;
    public String catId;

    @VisibleForTesting
    private AutoClearedValue<FragmentSearchCategoryBinding> binding;
    private AutoClearedValue<SearchCategoryAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSearchCategoryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_category, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            this.catId = intent.getStringExtra(Constants.CATEGORY_ID);
        }

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.clear_button, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clear) {
            this.catId = "";

            initAdapters();

            initData();

            //todo ali change price
            navigationController.navigateBackToSearchFragment(this.getActivity(), this.catId, "", "0");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewModels() {

        itemCategoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemCategoryViewModel.class);
    }

    @Override
    protected void initAdapters() {


        SearchCategoryAdapter nvadapter = new SearchCategoryAdapter(dataBindingComponent,
                (category, id) -> {

                    navigationController.navigateBackToSearchFragment(this.getActivity(), category.id, category.name, category.price);

                    if (getActivity() != null) {
                        this.getActivity().finish();
                    }
                }, this.catId);
        this.adapter = new AutoClearedValue<>(this, nvadapter);
        binding.get().searchCategoryRecyclerView.setAdapter(nvadapter);

    }

    @Override
    protected void initData() {
        loadCategory();
    }

    private void loadCategory() {

        // Load Category List
        itemCategoryViewModel.categoryParameterHolder.cityId = selectedCityId;

        itemCategoryViewModel.setCategoryListObj("", String.valueOf(itemCategoryViewModel.offset));

        LiveData<Resource<List<ItemCategory>>> news = itemCategoryViewModel.getCategoryListData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            itemCategoryViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            itemCategoryViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (itemCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemCategoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        itemCategoryViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemCategoryViewModel.setLoadingState(false);
                    itemCategoryViewModel.forceEndLoading = true;
                }
            }
        });

    }

    private void replaceData(List<ItemCategory> categoryList) {

        adapter.get().replace(categoryList);
        binding.get().executePendingBindings();

    }
}

