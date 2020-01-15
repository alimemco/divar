package com.panaceasoft.firoozboard.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.firoozboard.viewobject.CityMap;

@Dao
public interface CityMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CityMap cityMap);

    @Query("DELETE FROM CityMap WHERE mapKey = :key")
    void deleteByMapKey(String key);

    @Query("SELECT max(sorting) from CityMap WHERE mapKey = :value ")
    int getMaxSortingByValue(String value);

    @Query("DELETE FROM CityMap")
    void deleteAll();
}
