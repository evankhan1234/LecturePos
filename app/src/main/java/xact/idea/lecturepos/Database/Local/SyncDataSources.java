package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ISyncDataSource;
import xact.idea.lecturepos.Database.Model.Sync;

public class SyncDataSources implements ISyncDataSource {
    private SyncDao SyncDao;
    private static SyncDataSources instance;

    public SyncDataSources(SyncDao SyncDao){
        this.SyncDao=SyncDao;
    }
    public static SyncDataSources getInstance(SyncDao SyncDao){
        if(instance==null)
            instance = new SyncDataSources(SyncDao);
        return instance;

    }
    
    @Override
    public Flowable<List<Sync>> getSyncItems() {
        return SyncDao.getSyncItems();
    }

    @Override
    public Flowable<List<Sync>> getSyncItemById(int SyncItemId) {
        return SyncDao.getSyncItemById(SyncItemId);
    }

    @Override
    public Sync getSync(String SyncItem) {
        return SyncDao.getSync(SyncItem);
    }

    @Override
    public Sync valueFor(String name) {
        return SyncDao.valueFor(name);
    }

    @Override
    public int maxValue(String name) {
        return SyncDao.maxValue(name);
    }




    @Override
    public void emptySync() {
        SyncDao.emptySync();
    }

    @Override
    public int size() {
        return SyncDao.value();
    }

    @Override
    public void insertToSync(Sync... Syncs) {
        SyncDao.insertToSync(Syncs);
    }

    @Override
    public void updateSync(Sync... Syncs) {
        SyncDao.updateSync(Syncs);
    }

    @Override
    public void deleteSyncItem(Sync... Syncs) {
        SyncDao.deleteSyncItem(Syncs);
    }
}
