package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Sync;

public class SyncRepository implements ISyncDataSource {
    public ISyncDataSource ISyncDataSource;
    public SyncRepository(ISyncDataSource ISyncDataSource){
        this.ISyncDataSource=ISyncDataSource;
    }
    private static  SyncRepository instance;

    public static SyncRepository getInstance(ISyncDataSource iCartDataSource){
        if(instance==null)
            instance= new SyncRepository(iCartDataSource);
        return instance;

    }
    @Override
    public Flowable<List<Sync>> getSyncItems() {
        return ISyncDataSource.getSyncItems();
    }

    @Override
    public Flowable<List<Sync>> getSyncItemById(int SyncItemId) {
        return ISyncDataSource.getSyncItemById(SyncItemId);
    }

    @Override
    public Sync getSync(String SyncItem) {
        return ISyncDataSource.getSync(SyncItem);
    }

    @Override
    public Sync valueFor(String name) {
        return ISyncDataSource.valueFor(name);
    }

    @Override
    public int maxValue(String name) {
        return ISyncDataSource.maxValue(name);
    }




    @Override
    public void emptySync() {
        ISyncDataSource.emptySync();
    }

    @Override
    public int size() {
        return ISyncDataSource.size();
    }

    @Override
    public void insertToSync(Sync... Syncs) {
        ISyncDataSource.insertToSync(Syncs);
    }

    @Override
    public void updateSync(Sync... Syncs) {
        ISyncDataSource.updateSync(Syncs);
    }

    @Override
    public void deleteSyncItem(Sync... Syncs) {
        ISyncDataSource.deleteSyncItem(Syncs);
    }
}
