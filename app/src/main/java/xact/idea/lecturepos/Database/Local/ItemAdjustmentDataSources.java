package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IItemAdjustmentDatasources;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;

public class ItemAdjustmentDataSources implements IItemAdjustmentDatasources {
    private ItemAdjustmentDao ItemAdjustmentDao;
    private static ItemAdjustmentDataSources instance;

    public ItemAdjustmentDataSources(ItemAdjustmentDao ItemAdjustmentDao){
        this.ItemAdjustmentDao=ItemAdjustmentDao;
    }
    public static ItemAdjustmentDataSources getInstance(ItemAdjustmentDao ItemAdjustmentDao){
        if(instance==null)
            instance = new ItemAdjustmentDataSources(ItemAdjustmentDao);
        return instance;

    }

    @Override
    public Flowable<List<ItemAdjustment>> getItemItems() {
        return ItemAdjustmentDao.getItemAdjustment();
    }

    @Override
    public Flowable<List<ItemAdjustment>> getItemItemById(int ItemItemId) {
        return ItemAdjustmentDao.getItemAdjustmentItemAdjustmentById(ItemItemId);
    }

    @Override
    public ItemAdjustment getItems(String ItemItem) {
        return ItemAdjustmentDao.getItemAdjustment(ItemItem);
    }


    @Override
    public void emptyItem() {
        ItemAdjustmentDao.emptyItemAdjustment();
    }

    @Override
    public int size() {
        return ItemAdjustmentDao.value();
    }

    @Override
    public void emptyItemsById(int id) {
        ItemAdjustmentDao.emptyItemAdjustmentById(id);
    }


    @Override
    public int valueSum() {
        return  ItemAdjustmentDao.valueSum();
    }

    @Override
    public void insertToItem(ItemAdjustment... ItemAdjustment) {
        ItemAdjustmentDao.insertToItemAdjustment(ItemAdjustment);
    }

    @Override
    public int wrongItem(String stock) {
        return ItemAdjustmentDao.wrongitemAdjustment(stock);
    }

    @Override
    public void updateItem(ItemAdjustment... ItemAdjustment) {
        ItemAdjustmentDao.updateItemAdjustment(ItemAdjustment);
    }

    @Override
    public void deleteItemItem(ItemAdjustment... ItemAdjustment) {
        ItemAdjustmentDao.deleteItemAdjustmentItemAdjustment(ItemAdjustment);
    }
}