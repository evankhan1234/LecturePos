package xact.idea.lecturepos.Database.Datasources;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Challan;

public class ChallanRepositoy implements IChallanDataSource {
    public IChallanDataSource IChallanDataSource;
    public ChallanRepositoy(IChallanDataSource IChallanDataSource){
        this.IChallanDataSource=IChallanDataSource;
    }
    private static  ChallanRepositoy instance;

    public static ChallanRepositoy getInstance(IChallanDataSource iCartDataSource){
        if(instance==null)
            instance= new ChallanRepositoy(iCartDataSource);
        return instance;

    }
    
    @Override
    public Flowable<List<Challan>> getChallanItems() {
        return IChallanDataSource.getChallanItems();
    }

    @Override
    public Flowable<List<Challan>> getChallanItemById(int ChallanItemId) {
        return IChallanDataSource.getChallanItemById(ChallanItemId);
    }

    @Override
    public Challan getChallans(String ChallanItem) {
        return IChallanDataSource.getChallans(ChallanItem);
    }


    @Override
    public Flowable<List<Challan>> getChallan(int favoriteid) {
        return IChallanDataSource.getChallan(favoriteid);
    }

    @Override
    public void emptyChallan() {
        IChallanDataSource.emptyChallan();
    }

    @Override
    public int size() {
        return IChallanDataSource.size();
    }

    @Override
    public void updateReciver(String value, String ChallanNo,String datetime,String date) {
        IChallanDataSource.updateReciver(value, ChallanNo,datetime,date);
    }

    @Override
    public Flowable<List<Challan>> getList(String ChallanItemId) {
        return IChallanDataSource.getList(ChallanItemId);
    }

    @Override
    public void insertToChallan(Challan... Challans) {
        IChallanDataSource.insertToChallan(Challans);
    }

    @Override
    public Flowable<List<Challan>> getChallanActivityItemByDate(Date from, Date to,String ChallanItemId) {
        return IChallanDataSource.getChallanActivityItemByDate(from, to,ChallanItemId);
    }

    @Override
    public void updateChallan(Challan... Challans) {
        IChallanDataSource.updateChallan(Challans);
    }

    @Override
    public void deleteChallanItem(Challan... Challans) {
        IChallanDataSource.deleteChallanItem(Challans);
    }
}
