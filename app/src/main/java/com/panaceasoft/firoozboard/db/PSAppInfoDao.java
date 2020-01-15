package com.panaceasoft.firoozboard.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.firoozboard.viewobject.PSAppInfo;

@Dao
public interface PSAppInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PSAppInfo PSAppInfo);

    @Query("DELETE FROM PSAppInfo")
    void deleteAll();
}
