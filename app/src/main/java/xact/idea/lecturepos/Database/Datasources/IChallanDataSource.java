package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Challan;

public interface IChallanDataSource {
    Flowable<List<Challan>> getChallanItems();


    Flowable<List<Challan>> getChallanItemById(int ChallanItemId);

    Challan getChallans(String ChallanItem);
    Flowable<List<Challan>> getChallan(int favoriteid);

    void emptyChallan();
    int size();
    void updateReciver(String value,String ChallanNo);
    Flowable<List<Challan>> getList(String ChallanItemId);
    void insertToChallan(Challan... Challans);


    void updateChallan(Challan... Challans);


    void deleteChallanItem(Challan... Challans);
}
