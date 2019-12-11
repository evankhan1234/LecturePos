package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Sync;

public interface ISyncDataSource {
    Flowable<List<Sync>> getSyncItems();


    Flowable<List<Sync>> getSyncItemById(int SyncItemId);

    Sync getSync(String SyncItem);
    Sync valueFor(String name);
    int maxValue(String name);
    void emptySync();
    int size();


    void insertToSync(Sync... Syncs);


    void updateSync(Sync... Syncs);


    void deleteSyncItem(Sync... Syncs);
}
