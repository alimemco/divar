package com.panaceasoft.firoozboard.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.firoozboard.viewobject.Noti;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Noti noti);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Noti noti);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotificationList(List<Noti> NotiList);

    @Query("DELETE FROM Noti")
    void deleteAllNotificationList();

    @Query("SELECT * FROM Noti order by addedDate desc")
    LiveData<List<Noti>> getAllNotificationList();

    @Query("DELETE FROM Noti WHERE id = :id")
    void deleteNotificationById(String id);

    @Query("SELECT * FROM Noti WHERE id = :id")
    LiveData<Noti> getNotificationById(String id);

}
