package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IItemDataSources;
import xact.idea.lecturepos.Database.Model.Items;

public class ItemDataSources implements IItemDataSources {
    private ItemDao ItemDao;
    private static ItemDataSources instance;

    public ItemDataSources(ItemDao ItemDao){
        this.ItemDao=ItemDao;
    }
    public static ItemDataSources getInstance(ItemDao ItemDao){
        if(instance==null)
            instance = new ItemDataSources(ItemDao);
        return instance;

    }
    @Override
    public Flowable<List<Items>> getItemItems() {
        return ItemDao.getItems();
    }

    @Override
    public Flowable<List<Items>> getItemItemById(int ItemItemId) {
        return ItemDao.getItemsItemsById(ItemItemId);
    }

    @Override
    public Items getItems(String ItemItem) {
        return ItemDao.getItems(ItemItem);
    }


    @Override
    public void emptyItem() {
        ItemDao.emptyItems();
    }

    @Override
    public int size() {
        return ItemDao.value();
    }

    @Override
    public void emptyItemsById(int id) {
        ItemDao.emptyItemsById(id);
    }

    @Override
    public int valueSum() {
        return  ItemDao.valueSum();
    }

    @Override
    public void insertToItem(Items... Items) {
        ItemDao.insertToItems(Items);
    }

    @Override
    public int wrongItem(String stock) {
        return ItemDao.wrongItem(stock);
    }

    @Override
    public void updateItem(Items... Items) {
        ItemDao.updateItems(Items);
    }

    @Override
    public void deleteItemItem(Items... Items) {
        ItemDao.deleteItemsItems(Items);
    }
}
