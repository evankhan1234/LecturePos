package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemAdjustment;

public class ItemAdjustmentRepository implements IItemAdjustmentDatasources {
    public IItemAdjustmentDatasources IItemAdjustmentDataSources;
    public ItemAdjustmentRepository(IItemAdjustmentDatasources IItemAdjustmentDataSources){
        this.IItemAdjustmentDataSources=IItemAdjustmentDataSources;
    }
    private static  ItemAdjustmentRepository instance;

    public static ItemAdjustmentRepository getInstance(IItemAdjustmentDatasources iCartDataSource){
        if(instance==null)
            instance= new ItemAdjustmentRepository(iCartDataSource);
        return instance;

    }

    @Override
    public int valueSum() {
        return IItemAdjustmentDataSources.valueSum();
    }


    @Override
    public Flowable<List<ItemAdjustment>> getItemItems() {
        return IItemAdjustmentDataSources.getItemItems();
    }

    @Override
    public Flowable<List<ItemAdjustment>> getItemItemById(int ItemAdjustmentItemAdjustmentId) {
        return IItemAdjustmentDataSources.getItemItemById(ItemAdjustmentItemAdjustmentId);
    }

    @Override
    public ItemAdjustment getItems(String ItemItem) {
        return IItemAdjustmentDataSources.getItems(ItemItem);
    }


    @Override
    public void emptyItem() {
        IItemAdjustmentDataSources.emptyItem();
    }

    @Override
    public int size() {
        return IItemAdjustmentDataSources.size();
    }

    @Override
    public void emptyItemsById(int id) {
        IItemAdjustmentDataSources.emptyItemsById(id);
    }

    @Override
    public void insertToItem(ItemAdjustment... ItemAdjustments) {
        IItemAdjustmentDataSources.insertToItem(ItemAdjustments);
    }

    @Override
    public int wrongItem(String stock) {
        return IItemAdjustmentDataSources.wrongItem(stock);
    }

    @Override
    public void updateItem(ItemAdjustment... ItemAdjustments) {
        IItemAdjustmentDataSources.updateItem(ItemAdjustments);
    }

    @Override
    public void deleteItemItem(ItemAdjustment... ItemAdjustments) {
        IItemAdjustmentDataSources.deleteItemItem(ItemAdjustments);
    }
}
