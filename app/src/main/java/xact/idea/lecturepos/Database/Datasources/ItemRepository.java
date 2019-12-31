package xact.idea.lecturepos.Database.Datasources;


import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Items;

public class ItemRepository implements IItemDataSources {
    public IItemDataSources IItemsDataSources;
    public ItemRepository(IItemDataSources IItemsDataSources){
        this.IItemsDataSources=IItemsDataSources;
    }
    private static  ItemRepository instance;

    public static ItemRepository getInstance(IItemDataSources iCartDataSource){
        if(instance==null)
            instance= new ItemRepository(iCartDataSource);
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
    public Flowable<List<Items>> getItemItems() {
        return IItemsDataSources.getItemItems();
    }

    @Override
    public Flowable<List<Items>> getItemItemById(int ItemsItemsId) {
        return IItemsDataSources.getItemItemById(ItemsItemsId);
    }

    @Override
    public Items getItems(String ItemsItems) {
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
    public void insertToItem(Items... Itemss) {
        IItemsDataSources.insertToItem(Itemss);
    }

    @Override
    public int wrongItem(String stock) {
        return IItemsDataSources.wrongItem(stock);
    }

    @Override
    public void updateItem(Items... Itemss) {
        IItemsDataSources.updateItem(Itemss);
    }

    @Override
    public void deleteItemItem(Items... Itemss) {
        IItemsDataSources.deleteItemItem(Itemss);
    }
}
