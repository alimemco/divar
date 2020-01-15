package com.panaceasoft.firoozboard.repository.itempricetype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.panaceasoft.firoozboard.AppExecutors;
import com.panaceasoft.firoozboard.Config;
import com.panaceasoft.firoozboard.api.ApiResponse;
import com.panaceasoft.firoozboard.api.PSApiService;
import com.panaceasoft.firoozboard.db.ItemPriceTypeDao;
import com.panaceasoft.firoozboard.db.PSCoreDb;
import com.panaceasoft.firoozboard.repository.common.NetworkBoundResource;
import com.panaceasoft.firoozboard.repository.common.PSRepository;
import com.panaceasoft.firoozboard.utils.Utils;
import com.panaceasoft.firoozboard.viewobject.ItemPriceType;
import com.panaceasoft.firoozboard.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemPriceTypeRepository extends PSRepository {
    private ItemPriceTypeDao itemPriceTypeDao;

    @Inject
    ItemPriceTypeRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemPriceTypeDao itemPriceTypeDao) {

        super(psApiService, appExecutors, db);
        this.itemPriceTypeDao = itemPriceTypeDao;
    }

    public LiveData<Resource<List<ItemPriceType>>> getAllItemPriceTypeList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemPriceType>, List<ItemPriceType>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemPriceType> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");


                try {
                    db.beginTransaction();

                    itemPriceTypeDao.deleteAllItemPriceType();

                    itemPriceTypeDao.insertAll(itemTypeList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllCategoriesWithUserId", e);
                } finally {
                    db.endTransaction();
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemPriceType> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemPriceType>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemPriceTypeDao.getAllItemPriceType();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemPriceType>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemPriceTypeList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }
}