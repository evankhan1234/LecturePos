package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.SalesDetails;
import xact.idea.lecturepos.Model.SalesDetailPrintModel;

public interface ISalesDetailsDataSource {
  Flowable<List<SalesDetails>> getSalesDetailsItems();


  Flowable<List<SalesDetails>> getSalesDetailsItemById(String SalesDetailsItemId);

  SalesDetails getSalesMaster(String SalesDetailsItemId);
  Flowable<List<SalesDetails>> getSalesDetails(int favoriteid);

  void emptySalesDetails();
  int size();
  Flowable<List<SalesDetailPrintModel>> getBookStockModel(String SalesDetailsItemId);

  void insertToSalesDetails(SalesDetails... SalesDetailss);


  void updateSalesDetails(SalesDetails... SalesDetailss);


  void deleteSalesDetailsItem(SalesDetails... SalesDetailss);
}