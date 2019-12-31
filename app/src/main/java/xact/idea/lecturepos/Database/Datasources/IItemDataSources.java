package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Items;

public interface IItemDataSources {
    Flowable<List<Items>> getItemItems();


    Flowable<List<Items>> getItemItemById(int ItemItemId);

    Items getItems(String ItemItem);

    void emptyItem();
    int size();
    void emptyItemsById(int id);
    int valueSum();
    void insertToItem(Items... Items);

    int wrongItem(String stock);
    void updateItem(Items... Items);


    void deleteItemItem(Items... Items);
}
