package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemReturn;

public interface IItemReturnDataSources {
    Flowable<List<ItemReturn>> getItemItems();


    Flowable<List<ItemReturn>> getItemItemById(int ItemItemId);

    ItemReturn getItems(String ItemItem);

    void emptyItem();
    int size();
    void emptyItemsById(int id);
    int valueSum();
    void insertToItem(ItemReturn... ItemReturn);

    int wrongItem(String stock);
    void updateItem(ItemReturn... ItemReturn);


    void deleteItemItem(ItemReturn... ItemReturn);
}
