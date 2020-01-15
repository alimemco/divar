package com.panaceasoft.firoozboard.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.panaceasoft.firoozboard.viewobject.DeletedObject;

@Dao
public interface DeletedObjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DeletedObject deletedObject);

    @Query("DELETE FROM DeletedObject")
    void deleteAll();
}
