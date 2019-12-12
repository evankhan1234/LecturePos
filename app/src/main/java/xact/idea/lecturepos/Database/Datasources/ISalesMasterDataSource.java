package xact.idea.lecturepos.Database.Datasources;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public interface ISalesMasterDataSource {
    Flowable<List<SalesMaster>> getSalesMasterItems();


    Flowable<List<SalesMaster>> getSalesMasterItemById(int SalesMasterItemId);


    Flowable<List<SalesMaster>> getSalesMaster(int favoriteid);
    int maxValue();
    void emptySalesMaster();
    int size();
    SalesMaster invoice(int id);
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to);
    void insertToSalesMaster(SalesMaster... SalesMasters);


    void updateSalesMaster(SalesMaster... SalesMasters);


    void deleteSalesMasterItem(SalesMaster... SalesMasters);
}
