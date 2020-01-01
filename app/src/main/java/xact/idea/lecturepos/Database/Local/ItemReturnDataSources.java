package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;

import xact.idea.lecturepos.Database.Datasources.IItemReturnDataSources;
import xact.idea.lecturepos.Database.Model.ItemReturn;

public class ItemReturnDataSources implements IItemReturnDataSources {
    private ItemReturnDao ItemReturnDao;
    private static ItemReturnDataSources instance;

    public ItemReturnDataSources(ItemReturnDao ItemReturnDao){
        this.ItemReturnDao=ItemReturnDao;
    }
    public static IItemReturnDataSources getInstance(ItemReturnDao ItemReturnDao){
        if(instance==null)
            instance = new ItemReturnDataSources(ItemReturnDao);
        return instance;

    }
    @Override
    public Flowable<List<ItemReturn>> getItemItems() {
        return ItemReturnDao.getItems();
    }

    @Override
    public Flowable<List<ItemReturn>> getItemItemById(int ItemItemId) {
        return ItemReturnDao.getItemsItemsById(ItemItemId);
    }

    @Override
    public ItemReturn getItems(String ItemItem) {
        return ItemReturnDao.getItems(ItemItem);
    }


    @Override
    public void emptyItem() {
        ItemReturnDao.emptyItems();
    }

    @Override
    public int size() {
        return ItemReturnDao.value();
    }

    @Override
    public void emptyItemsById(int id) {
        ItemReturnDao.emptyItemsById(id);
    }

    @Override
    public int valueSum() {
        return  ItemReturnDao.valueSum();
    }

    @Override
    public void insertToItem(ItemReturn... ItemReturn) {
        ItemReturnDao.insertToItems(ItemReturn);
    }

    @Override
    public int wrongItem(String stock) {
        return ItemReturnDao.wrongItem(stock);
    }

    @Override
    public void updateItem(ItemReturn... ItemReturn) {
        ItemReturnDao.updateItems(ItemReturn);
    }

    @Override
    public void deleteItemItem(ItemReturn... ItemReturn) {
        ItemReturnDao.deleteItemsItems(ItemReturn);
    }
}