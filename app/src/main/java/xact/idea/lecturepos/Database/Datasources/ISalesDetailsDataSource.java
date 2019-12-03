package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;

public interface ISalesDetailsDataSource {
    Flowable<List<SalesDetails>> getSalesDetailsItems();


    Flowable<List<SalesDetails>> getSalesDetailsItemById(int SalesDetailsItemId);


    Flowable<List<SalesDetails>> getSalesDetails(int favoriteid);

    void emptySalesDetails();
    int size();


    void insertToSalesDetails(SalesDetails... SalesDetailss);


    void updateSalesDetails(SalesDetails... SalesDetailss);


    void deleteSalesDetailsItem(SalesDetails... SalesDetailss);
}
