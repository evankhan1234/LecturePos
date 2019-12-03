package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;

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
    public Flowable<List<SalesDetails>> getSalesDetailsItemById(int SalesDetailsItemId) {
        return ISalesDetailsDataSource.getSalesDetailsItemById(SalesDetailsItemId);
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