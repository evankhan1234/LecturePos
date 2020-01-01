package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemReturn;

@Dao
public interface ItemReturnDao {
    @Query("SELECT * FROM itemReturn")
    Flowable<List<ItemReturn>> getItemsItems();

    @Query("SELECT * FROM itemReturn WHERE id=:ItemsItemsId")
    Flowable<List<ItemReturn>> getItemsItemsById(int ItemsItemsId);
    @Query("SELECT * FROM itemReturn WHERE BookName=:ItemsItems")
    ItemReturn getItems(String ItemsItems);

    @Query("Select Count(id)  FROM itemReturn")
    int value();
    @Query("Select Sum(Amount)  FROM itemReturn")
    int valueSum();

    @Query("Select Count(id) FROM itemReturn where Stock=:stock")
    int wrongItem(String stock);
    @Query("DELETE  FROM itemReturn")
    void emptyItems();
    @Query("DELETE  FROM itemReturn WHERE id=:id")
    void emptyItemsById(int id);

    @Insert
    void insertToItems(ItemReturn...ItemReturn);

    @Update
    void updateItems(ItemReturn...ItemReturn);

    @Delete
    void deleteItemsItems(ItemReturn...ItemReturn);

    @Query("SELECT * from itemReturn")
        //@Query("SELECT * ItemReturn Itemss as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<ItemReturn>> getItems();
}
