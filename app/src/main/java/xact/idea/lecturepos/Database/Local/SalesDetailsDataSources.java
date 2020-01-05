package xact.idea.lecturepos.Database.Local;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ISalesDetailsDataSource;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;

public class SalesDetailsDataSources implements ISalesDetailsDataSource {

    private SaleDetailsDao SaleDetailsDao;
    private static SalesDetailsDataSources instance;

    public SalesDetailsDataSources(SaleDetailsDao SaleDetailsDao){
        this.SaleDetailsDao=SaleDetailsDao;
    }
    public static SalesDetailsDataSources getInstance(SaleDetailsDao SaleDetailsDao){
        if(instance==null)
            instance = new SalesDetailsDataSources(SaleDetailsDao);
        return instance;

    }



    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItems() {
        return SaleDetailsDao.getSalesDetailsItems();
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItemById(String SalesDetailsItemId) {
        return SaleDetailsDao.getSalesDetailsItemById(SalesDetailsItemId);
    }

    @Override
    public SalesDetails getSalesMaster(String SalesDetailsItemId) {
        return SaleDetailsDao.getSalesMaster(SalesDetailsItemId);
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetails(int favoriteid) {
        return SaleDetailsDao.getSalesDetails();
    }

    @Override
    public void emptySalesDetails() {
        SaleDetailsDao.emptySalesDetails();
    }

    @Override
    public int size() {
        return SaleDetailsDao.value();
    }

    @Override
    public Flowable<List<SalesDetailPrintModel>> getBookStockModel(String SalesDetailsItemId) {
        return SaleDetailsDao.getBookStockModel(SalesDetailsItemId);
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItemByDate(String SalesDetailsItemId, Date from, Date to) {
        return SaleDetailsDao.getSalesDetailsItemByDate(SalesDetailsItemId,from,to);
    }

    @Override
    public void insertToSalesDetails(SalesDetails... carts) {

        SaleDetailsDao.insertToSalesDetails(carts);
    }

    @Override
    public void updateSalesDetails(SalesDetails... carts) {

        SaleDetailsDao.updateSalesDetails(carts);
    }

    @Override
    public void deleteSalesDetailsItem(SalesDetails... carts) {

        SaleDetailsDao.deleteSalesDetailsItem(carts);
    }
}