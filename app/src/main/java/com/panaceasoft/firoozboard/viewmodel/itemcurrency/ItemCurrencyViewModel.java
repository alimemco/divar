package com.panaceasoft.firoozboard.viewmodel.itemcurrency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.firoozboard.repository.itemcurrency.ItemCurrencyTypeRepository;
import com.panaceasoft.firoozboard.utils.AbsentLiveData;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.common.PSViewModel;
import com.panaceasoft.firoozboard.viewobject.ItemCurrency;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.panaceasoft.firoozboard.viewobject.holder.CategoryParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemCurrencyViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemCurrency>>> itemTypeListData;
    private MutableLiveData<ItemCurrencyViewModel.TmpDataHolder> itemTypeListObj = new MutableLiveData<>();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemCurrencyViewModel(ItemCurrencyTypeRepository repository) {

        Utils.psLog("ItemCurrencyViewModel");

        itemTypeListData = Transformations.switchMap(itemTypeListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemCurrencyViewModel : categories");
            return repository.getAllItemCurrencyList(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setItemCurrencyListObj(String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            itemTypeListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemCurrency>>> getItemCurrencyListData() {
        return itemTypeListData;
    }


    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String cityId = "";
    }
}
