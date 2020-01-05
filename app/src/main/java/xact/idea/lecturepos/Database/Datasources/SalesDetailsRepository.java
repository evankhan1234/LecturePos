package xact.idea.lecturepos.Database.Datasources;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;

public class SalesDetailsRepository implements ISalesDetailsDataSource {
    public ISalesDetailsDataSource ISalesDetailsDataSource;
    public SalesDetailsRepository(ISalesDetailsDataSource ISalesDetailsDataSource){
        this.ISalesDetailsDataSource=ISalesDetailsDataSource;
    }
    private static  SalesDetailsRepository instance;

    public static SalesDetailsRepository getInstance(ISalesDetailsDataSource iCartDataSource){
        if(instance==null)
            instance= new SalesDetailsRepository(iCartDataSource);
        return instance;

    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItems() {
        return ISalesDetailsDataSource.getSalesDetailsItems();
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItemById(String SalesDetailsItemId) {
        return ISalesDetailsDataSource.getSalesDetailsItemById(SalesDetailsItemId);
    }

    @Override
    public SalesDetails getSalesMaster(String SalesDetailsItemId) {
        return ISalesDetailsDataSource.getSalesMaster(SalesDetailsItemId);
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetails(int favoriteid) {
        return ISalesDetailsDataSource.getSalesDetails(favoriteid);
    }

    @Override
    public void emptySalesDetails() {
        ISalesDetailsDataSource.emptySalesDetails();
    }

    @Override
    public int size() {
        return ISalesDetailsDataSource.size();
    }

    @Override
    public Flowable<List<SalesDetailPrintModel>> getBookStockModel(String SalesDetailsItemId) {
        return ISalesDetailsDataSource.getBookStockModel(SalesDetailsItemId);
    }

    @Override
    public Flowable<List<SalesDetails>> getSalesDetailsItemByDate(String SalesDetailsItemId, Date from, Date to) {
        return ISalesDetailsDataSource.getSalesDetailsItemByDate(SalesDetailsItemId, from, to);
    }

    @Override
    public void insertToSalesDetails(SalesDetails... SalesDetailss) {
        ISalesDetailsDataSource.insertToSalesDetails(SalesDetailss);
    }

    @Override
    public void updateSalesDetails(SalesDetails... SalesDetailss) {
        ISalesDetailsDataSource.updateSalesDetails(SalesDetailss);
    }

    @Override
    public void deleteSalesDetailsItem(SalesDetails... SalesDetailss) {
        ISalesDetailsDataSource.deleteSalesDetailsItem(SalesDetailss);
    }
}