package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ISalesMasterDataSource;
import xact.idea.lecturepos.Database.Model.SalesMaster;

public class SalesMasterDataSources implements ISalesMasterDataSource {

    private SaleMastersDao SaleMastersDao;
    private static SalesMasterDataSources instance;

    public SalesMasterDataSources(SaleMastersDao SaleMastersDao){
        this.SaleMastersDao=SaleMastersDao;
    }
    public static SalesMasterDataSources getInstance(SaleMastersDao SaleMastersDao){
        if(instance==null)
            instance = new SalesMasterDataSources(SaleMastersDao);
        return instance;

    }



    @Override
    public Flowable<List<SalesMaster>> getSalesMasterItems() {
        return SaleMastersDao.getSalesDetailsItems();
    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMasterItemById(int SalesMasterItemId) {
        return SaleMastersDao.getSalesDetailsItemById(SalesMasterItemId);
    }

    @Override
    public Flowable<List<SalesMaster>> getSalesMaster(int favoriteid) {
        return SaleMastersDao.getSalesDetails();
    }

    @Override
    public void emptySalesMaster() {
        SaleMastersDao.emptySalesDetails();
    }

    @Override
    public int size() {
        return SaleMastersDao.value();
    }

    @Override
    public void insertToSalesMaster(SalesMaster... carts) {

        SaleMastersDao.insertToSalesDetails(carts);
    }

    @Override
    public void updateSalesMaster(SalesMaster... carts) {

        SaleMastersDao.updateSalesDetails(carts);
    }

    @Override
    public void deleteSalesMasterItem(SalesMaster... carts) {

        SaleMastersDao.deleteSalesDetailsItem(carts);
    }
}
