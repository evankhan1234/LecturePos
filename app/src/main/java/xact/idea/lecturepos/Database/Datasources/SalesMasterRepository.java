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
    public Flowable<List<SalesMaster>> getSalesMaster(int favoriteid) {
        return ISalesMasterDataSource.getSalesMaster(favoriteid);
    }

    @Override
    public int maxValue() {
        return ISalesMasterDataSource.maxValue();
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
    public Flowable<List<SalesMaster>> getInvoiceActivityItemByDate(Date from, Date to) {
        return ISalesMasterDataSource.getInvoiceActivityItemByDate(from, to);
    }

    @Override
    public void insertToSalesMaster(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.insertToSalesMaster(SalesMasters);
    }

    @Override
    public void updateSalesMaster(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.updateSalesMaster(SalesMasters);
    }

    @Override
    public void deleteSalesMasterItem(SalesMaster... SalesMasters) {
        ISalesMasterDataSource.deleteSalesMasterItem(SalesMasters);
    }
}