package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IChallanDetailsDataSources;
import xact.idea.lecturepos.Database.Model.ChallanDetails;

public class ChallanDetailsDataSources implements IChallanDetailsDataSources {
    private ChallanDetailsDao ChallanDetailsDao;
    private static ChallanDetailsDataSources instance;

    public ChallanDetailsDataSources(ChallanDetailsDao ChallanDetailsDao){
        this.ChallanDetailsDao=ChallanDetailsDao;
    }
    public static ChallanDetailsDataSources getInstance(ChallanDetailsDao ChallanDetailsDao){
        if(instance==null)
            instance = new ChallanDetailsDataSources(ChallanDetailsDao);
        return instance;

    }
    @Override
    public Flowable<List<ChallanDetails>> getChallanDetailsItems() {
        return ChallanDetailsDao.getChallanDetailsItems();
    }

    @Override
    public Flowable<List<ChallanDetails>> getChallanDetailsItemById(int ChallanDetailsItemId) {
        return ChallanDetailsDao.getChallanDetailsItemById(ChallanDetailsItemId);
    }

    @Override
    public ChallanDetails getChallanDetails(String ChallanDetailsItem) {
        return ChallanDetailsDao.getChallanDetails(ChallanDetailsItem);
    }


    @Override
    public void emptyChallanDetails() {
        ChallanDetailsDao.emptyChallanDetails();
    }

    @Override
    public int size() {
        return ChallanDetailsDao.value();
    }

    @Override
    public void insertToChallanDetails(ChallanDetails... ChallanDetailss) {
        ChallanDetailsDao.insertToChallanDetails(ChallanDetailss);
    }

    @Override
    public void updateChallanDetails(ChallanDetails... ChallanDetailss) {
        ChallanDetailsDao.updateChallanDetails(ChallanDetailss);
    }

    @Override
    public void deleteChallanDetailsItem(ChallanDetails... ChallanDetailss) {
        ChallanDetailsDao.deleteChallanDetailsItem(ChallanDetailss);
    }
}
