package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Items;


@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    Flowable<List<Items>> getItemsItems();

    @Query("SELECT * FROM item WHERE id=:ItemsItemsId")
    Flowable<List<Items>> getItemsItemsById(int ItemsItemsId);
    @Query("SELECT * FROM item WHERE BookName=:ItemsItems")
    Items getItems(String ItemsItems);

    @Query("Select Count(id)  FROM item")
    int value();
    @Query("Select Sum(Amount)  FROM item")
    int valueSum();


    @Query("DELETE  FROM item")
    void emptyItems();
    @Query("DELETE  FROM item WHERE id=:id")
    void emptyItemsById(int id);

    @Insert
    void insertToItems(Items...Items);

    @Update
    void updateItems(Items...Items);

    @Delete
    void deleteItemsItems(Items...Items);

    @Query("SELECT * from item")
        //@Query("SELECT * Items Itemss as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Items>> getItems();
}
