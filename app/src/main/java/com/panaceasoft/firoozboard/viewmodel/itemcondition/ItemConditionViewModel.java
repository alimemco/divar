package com.panaceasoft.firoozboard.viewmodel.itemcondition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.firoozboard.repository.itemcondition.ItemConditionRepository;
import com.panaceasoft.firoozboard.utils.AbsentLiveData;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewmodel.common.PSViewModel;
import com.panaceasoft.firoozboard.viewobject.ItemCondition;
import com.panaceasoft.firoozboard.viewobject.common.Resource;
import com.panaceasoft.firoozboard.viewobject.holder.CategoryParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemConditionViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemCondition>>> itemTypeListData;
    private MutableLiveData<ItemConditionViewModel.TmpDataHolder> itemTypeListObj = new MutableLiveData<>();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemConditionViewModel(ItemConditionRepository repository) {

        Utils.psLog("ItemConditionViewModel");

        itemTypeListData = Transformations.switchMap(itemTypeListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemConditionViewModel : categories");
            return repository.getAllItemConditionList(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setItemConditionListObj(String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            itemTypeListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemCondition>>> getItemConditionListData() {
        return itemTypeListData;
    }


    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String cityId = "";
    }
}
