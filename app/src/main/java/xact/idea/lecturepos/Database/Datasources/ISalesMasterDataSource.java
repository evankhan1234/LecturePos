package xact.idea.lecturepos.Database.Datasources;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public interface ISalesMasterDataSource {
    Flowable<List<SalesMaster>> getSalesMasterItems();


    Flowable<List<SalesMaster>> getSalesMasterItemById(int SalesMasterItemId);
    SalesMaster getSalesMaster(String SalesDetailsItemId);

    Flowable<List<SalesMaster>> getSalesMaster(int favoriteid);
    int maxValue(String date,String trnType);
    void emptySalesMaster();
    int size();
    SalesMaster invoice(int id);
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to,String Trn);
    void insertToSalesMaster(SalesMaster... SalesMasters);
    Flowable<List<SalesMaster>> getInvoiceActivityItemByDateByName(Date from, Date to,String Name,String Trn);
    Flowable<List<SalesMaster>> getSalesMasterList(String SalesDetailsItemId);
    void updateSalesMaster(SalesMaster... SalesMasters);

    Flowable<List<SalesMaster>> getDetailsActivityItemByDateByName(Date from, Date to,String Name);
    void deleteSalesMasterItem(SalesMaster... SalesMasters);
}
