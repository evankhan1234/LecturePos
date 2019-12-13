package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ChallanDetails;

public class ChallanDetailsRepository implements IChallanDetailsDataSources {
    public IChallanDetailsDataSources IChallanDetailsDataSources;
    public ChallanDetailsRepository(IChallanDetailsDataSources IChallanDetailsDataSources){
        this.IChallanDetailsDataSources=IChallanDetailsDataSources;
    }
    private static  ChallanDetailsRepository instance;

    public static ChallanDetailsRepository getInstance(IChallanDetailsDataSources iCartDataSource){
        if(instance==null)
            instance= new ChallanDetailsRepository(iCartDataSource);
        return instance;

    }

    
    @Override
    public Flowable<List<ChallanDetails>> getChallanDetailsItems() {
        return IChallanDetailsDataSources.getChallanDetailsItems();
    }

    @Override
    public Flowable<List<ChallanDetails>> getChallanDetailsItemById(int ChallanDetailsItemId) {
        return IChallanDetailsDataSources.getChallanDetailsItemById(ChallanDetailsItemId);
    }

    @Override
    public ChallanDetails getChallanDetails(String ChallanDetailsItem) {
        return IChallanDetailsDataSources.getChallanDetails(ChallanDetailsItem);
    }



    @Override
    public void emptyChallanDetails() {
        IChallanDetailsDataSources.emptyChallanDetails();
    }

    @Override
    public int size() {
        return IChallanDetailsDataSources.size();
    }

    @Override
    public void insertToChallanDetails(ChallanDetails... ChallanDetailss) {
        IChallanDetailsDataSources.insertToChallanDetails(ChallanDetailss);
    }

    @Override
    public void updateChallanDetails(ChallanDetails... ChallanDetailss) {
        IChallanDetailsDataSources.updateChallanDetails(ChallanDetailss);
    }

    @Override
    public void deleteChallanDetailsItem(ChallanDetails... ChallanDetailss) {
        IChallanDetailsDataSources.deleteChallanDetailsItem(ChallanDetailss);
    }
}
