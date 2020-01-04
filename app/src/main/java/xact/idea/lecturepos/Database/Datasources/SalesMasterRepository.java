package xact.idea.lecturepos.Database.Datasources;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public class SalesMasterRepository implements ISalesMasterDataSource {
    public ISalesMasterDataSource ISalesMasterDataSource;
    public SalesMasterRepository(ISalesMasterDataSource ISalesMasterDataSource){
        this.ISalesMasterDataSource=ISalesMasterDataSource;
    }
    private static  SalesMasterRepository instance;

    public static SalesMasterRepository getInstance(ISalesMasterDataSource iCartDataSource){
        if(instance==null)
            instance= new SalesMasterRepository(iCartDataSource);
        return instance;

    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMasterItems() {
        return ISalesMasterDataSource.getSalesMasterItems();
    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMasterItemById(int SalesMasterItemId) {
        return ISalesMasterDataSource.getSalesMasterItemById(SalesMasterItemId);
    }

    @Override
    public SalesMaster getSalesMaster(String SalesDetailsItemId) {
        return ISalesMasterDataSource.getSalesMaster(SalesDetailsItemId);
    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMaster(int favoriteid) {
        return ISalesMasterDataSource.getSalesMaster(favoriteid);
    }

    @Override
    public int maxValue(String date,String trnType) {
        return ISalesMasterDataSource.maxValue(date,trnType);
    }

    @Override
    public void emptySalesMaster() {
        ISalesMasterDataSource.emptySalesMaster();
    }

    @Override
    public int size() {
        return ISalesMasterDataSource.size();
    }

    @Override
    public SalesMaster invoice(int id) {
        return ISalesMasterDataSource.invoice(id);
    }

    @Override
    public Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to,String Trn) {
        return ISalesMasterDataSource.getInvoiceActivityItemByDate(from, to,Trn);
    }

    @Override
    public void insertToSalesMaster(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.insertToSalesMaster(SalesMasters);
    }

    @Override
    public Flowable<List<SalesMaster>> getInvoiceActivityItemByDateByName(Date from, Date to, String Name,String Trn) {
        return ISalesMasterDataSource.getInvoiceActivityItemByDateByName(from, to, Name,Trn);
    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMasterList(String SalesDetailsItemId) {
        return ISalesMasterDataSource.getSalesMasterList(SalesDetailsItemId);
    }

    @Override
    public void updateSalesMaster(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.updateSalesMaster(SalesMasters);
    }

    @Override
    public Flowable<List<SalesMaster>> getDetailsActivityItemByDateByName(Date from, Date to, String Name) {
        return ISalesMasterDataSource.getDetailsActivityItemByDateByName(from, to, Name);
    }

    @Override
    public void deleteSalesMasterItem(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.deleteSalesMasterItem(SalesMasters);
    }
}