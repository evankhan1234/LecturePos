package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public interface ISalesMasterDataSource {
    Flowable<List<SalesMaster>> getSalesMasterItems();


    Flowable<List<SalesMaster>> getSalesMasterItemById(int SalesMasterItemId);


    Flowable<List<SalesMaster>> getSalesMaster(int favoriteid);

    void emptySalesMaster();
    int size();


    void insertToSalesMaster(SalesMaster... SalesMasters);


    void updateSalesMaster(SalesMaster... SalesMasters);


    void deleteSalesMasterItem(SalesMaster... SalesMasters);
}
