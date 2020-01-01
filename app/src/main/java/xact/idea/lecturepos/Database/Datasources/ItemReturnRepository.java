package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ItemReturn;

public class ItemReturnRepository implements IItemReturnDataSources {
    public IItemReturnDataSources IItemsDataSources;
    public ItemReturnRepository(IItemReturnDataSources IItemsDataSources){
        this.IItemsDataSources=IItemsDataSources;
    }
    private static  ItemReturnRepository instance;

    public static ItemReturnRepository getInstance(IItemReturnDataSources iCartDataSource){
        if(instance==null)
            instance= new ItemReturnRepository(iCartDataSource);
        return instance;

    }
    @Override
    public void emptyItemsById(int id) {
        IItemsDataSources.emptyItemsById(id);
    }

    @Override
    public int valueSum() {
        return IItemsDataSources.valueSum();
    }

    @Override
    public Flowable<List<ItemReturn>> getItemItems() {
        return IItemsDataSources.getItemItems();
    }

    @Override
    public Flowable<List<ItemReturn>> getItemItemById(int ItemsItemsId) {
        return IItemsDataSources.getItemItemById(ItemsItemsId);
    }

    @Override
    public ItemReturn getItems(String ItemsItems) {
        return IItemsDataSources.getItems(ItemsItems);
    }


    @Override
    public void emptyItem() {
        IItemsDataSources.emptyItem();
    }

    @Override
    public int size() {
        return IItemsDataSources.size();
    }

    @Override
    public void insertToItem(ItemReturn... Itemss) {
        IItemsDataSources.insertToItem(Itemss);
    }

    @Override
    public int wrongItem(String stock) {
        return IItemsDataSources.wrongItem(stock);
    }

    @Override
    public void updateItem(ItemReturn... Itemss) {
        IItemsDataSources.updateItem(Itemss);
    }

    @Override
    public void deleteItemItem(ItemReturn... Itemss) {
        IItemsDataSources.deleteItemItem(Itemss);
    }
}
