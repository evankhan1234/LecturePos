package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ISalesDetailsDataSource;
import xact.idea.lecturepos.Database.Model.SalesDetails;

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
    public Flowable<List<SalesDetails>> getSalesDetailsItemById(int SalesDetailsItemId) {
        return SaleDetailsDao.getSalesDetailsItemById(SalesDetailsItemId);
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