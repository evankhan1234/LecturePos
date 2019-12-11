package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Sync;

@Dao
public interface SyncDao {
    @Query("SELECT * FROM sync")
    Flowable<List<Sync>> getSyncItems();

    @Query("SELECT * FROM sync WHERE id=:SyncItemId")
    Flowable<List<Sync>> getSyncItemById(int SyncItemId);
    @Query("SELECT * FROM sync WHERE CUSTOMER_CODE=:SyncItem")
    Sync getSync(String SyncItem);

    @Query("Select Count(id)  FROM sync")
    int value();
    @Query("Select *  FROM sync where TABLE_NAME=:name")
    Sync valueFor(String name);

    @Query("Select MAX(id)  FROM sync where TABLE_NAME=:name")
    int maxValue(String name);
    @Query("DELETE  FROM sync")
    void emptySync();

    @Insert
    void insertToSync(Sync...sync);

    @Update
    void updateSync(Sync...sync);

    @Delete
    void deleteSyncItem(Sync...sync);

    @Query("SELECT * from sync")
        //@Query("SELECT * sync Sync as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Sync>> getSync();
}
