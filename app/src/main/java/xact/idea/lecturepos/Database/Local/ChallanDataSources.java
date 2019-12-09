package xact.idea.lecturepos.Database.Local;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.IChallanDataSource;
import xact.idea.lecturepos.Database.Model.Challan;

public class ChallanDataSources implements IChallanDataSource {
    private ChallanDao ChallanDao;
    private static ChallanDataSources instance;

    public ChallanDataSources(ChallanDao ChallanDao){
        this.ChallanDao=ChallanDao;
    }
    public static ChallanDataSources getInstance(ChallanDao ChallanDao){
        if(instance==null)
            instance = new ChallanDataSources(ChallanDao);
        return instance;

    }

    @Override
    public Flowable<List<Challan>> getChallanItems() {
        return ChallanDao.getChallanItems();
    }

    @Override
    public Flowable<List<Challan>> getChallanItemById(int ChallanItemId) {
        return ChallanDao.getChallanItemById(ChallanItemId);
    }

    @Override
    public Challan getChallans(String ChallanItem) {
        return ChallanDao.getChallan(ChallanItem);
    }

    @Override
    public Flowable<List<Challan>> getChallan(int favoriteid) {
        return ChallanDao.getChallan();
    }

    @Override
    public void emptyChallan() {
        ChallanDao.emptyChallan();
    }

    @Override
    public int size() {
        return ChallanDao.value();
    }

    @Override
    public void updateReciver(String value, String ChallanNo,String date) {
        ChallanDao.updateReciver(value, ChallanNo,date);
    }

    @Override
    public Flowable<List<Challan>> getList(String ChallanItemId) {
        return ChallanDao.getList(ChallanItemId);
    }

    @Override
    public void insertToChallan(Challan... Challans) {
        ChallanDao.insertToChallan(Challans);
    }

    @Override
    public Flowable<List<Challan>> getChallanActivityItemByDate(Date from, Date to) {
        return ChallanDao.getChallanActivityItemByDate(from, to);
    }

    @Override
    public void updateChallan(Challan... Challans) {
        ChallanDao.updateChallan(Challans);
    }

    @Override
    public void deleteChallanItem(Challan... Challans) {
        ChallanDao.deleteChallanItem(Challans);
    }
}
