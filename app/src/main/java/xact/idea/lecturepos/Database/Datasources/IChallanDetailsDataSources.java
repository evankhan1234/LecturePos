package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ChallanDetails;

public interface IChallanDetailsDataSources {
    Flowable<List<ChallanDetails>> getChallanDetailsItems();


    Flowable<List<ChallanDetails>> getChallanDetailsItemById(int ChallanDetailsItemId);

    ChallanDetails getChallanDetails(String ChallanDetailsItem);


    void emptyChallanDetails();
    int size();


    void insertToChallanDetails(ChallanDetails... ChallanDetailss);


    void updateChallanDetails(ChallanDetails... ChallanDetailss);


    void deleteChallanDetailsItem(ChallanDetails... ChallanDetailss);
}
