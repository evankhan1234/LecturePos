package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;


@Dao
public interface ItemAdjustmentDao {
    @Query("SELECT * FROM itemAdjustment")
    Flowable<List<ItemAdjustment>> getItemAdjustmentItemAdjustment();

    @Query("SELECT * FROM itemAdjustment WHERE id=:ItemAdjustmentItemAdjustmentId")
    Flowable<List<ItemAdjustment>> getItemAdjustmentItemAdjustmentById(int ItemAdjustmentItemAdjustmentId);
    @Query("SELECT * FROM itemAdjustment WHERE BookName=:ItemAdjustmentItemAdjustment")
    ItemAdjustment getItemAdjustment(String ItemAdjustmentItemAdjustment);

    @Query("Select Count(id)  FROM itemAdjustment")
    int value();
    @Query("Select Sum(id)  FROM itemAdjustment")
    int valueSum();

    @Query("Select Count(id) FROM itemAdjustment where InOut=:stock")
    int wrongitemAdjustment(String stock);
    @Query("DELETE  FROM itemAdjustment")
    void emptyItemAdjustment();
    @Query("DELETE  FROM itemAdjustment WHERE id=:id")
    void emptyItemAdjustmentById(int id);

    @Insert
    void insertToItemAdjustment(ItemAdjustment...ItemAdjustment);

    @Update
    void updateItemAdjustment(ItemAdjustment...ItemAdjustment);

    @Delete
    void deleteItemAdjustmentItemAdjustment(ItemAdjustment...ItemAdjustment);

    @Query("SELECT * from itemAdjustment")
        //@Query("SELECT * ItemAdjustment ItemAdjustments as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<ItemAdjustment>> getItemAdjustment();
}
