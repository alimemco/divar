package com.panaceasoft.firoozboard.repository.itemtype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.firoozboard.AppExecutors;
import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.api.ApiResponse;
import com.panaceasoft.firoozboard.api.PSApiService;
import com.panaceasoft.firoozboard.db.ItemTypeDao;
import com.panaceasoft.firoozboard.db.PSCoreDb;
import com.panaceasoft.firoozboard.repository.common.NetworkBoundResource;
import com.panaceasoft.firoozboard.repository.common.PSRepository;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewobject.ItemType;
import com.panaceasoft.firoozboard.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemTypeRepository extends PSRepository {
    private ItemTypeDao itemTypeDao;

    @Inject
    ItemTypeRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemTypeDao itemTypeDao) {

        super(psApiService, appExecutors, db);
        this.itemTypeDao = itemTypeDao;
    }

    public LiveData<Resource<List<ItemType>>> getAllItemTypeList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemType>, List<ItemType>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemType> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");


                try {
                    db.beginTransaction();

                    itemTypeDao.deleteAllItemType();

                    itemTypeDao.insertAll(itemTypeList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e);
                } finally {
                    db.endTransaction();
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemType> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemType>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemTypeDao.getAllItemType();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemType>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemTypeList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }


}
