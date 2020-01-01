package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;

public interface IItemAdjustmentDatasources {
    Flowable<List<ItemAdjustment>> getItemItems();


    Flowable<List<ItemAdjustment>> getItemItemById(int ItemItemId);

    ItemAdjustment getItems(String ItemItem);

    void emptyItem();
    int size();
    void emptyItemsById(int id);
    int valueSum();
    void insertToItem(ItemAdjustment... ItemAdjustment);
    int wrongItem(String stock);
    void updateItem(ItemAdjustment... ItemAdjustment);


    void deleteItemItem(ItemAdjustment... ItemAdjustment);
}
