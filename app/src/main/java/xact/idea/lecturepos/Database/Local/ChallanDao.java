package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Challan;

@Dao
public interface ChallanDao {
    @Query("SELECT * FROM challan")
    Flowable<List<Challan>> getChallanItems();

    @Query("SELECT * FROM challan WHERE id=:ChallanItemId")
    Flowable<List<Challan>> getChallanItemById(int ChallanItemId);
    @Query("SELECT * FROM challan WHERE CHALLAN_DATE=:ChallanItem")
    Challan getChallan(String ChallanItem);
    @Query("SELECT * FROM challan WHERE Date BETWEEN :from AND :to")
    Flowable<List<Challan>> getChallanActivityItemByDate(Date from, Date to);
    @Query("Select Count(id)  FROM challan")
    int value();

    @Query("SELECT * FROM challan WHERE IS_RECEIVE=:ChallanItemId")
    Flowable<List<Challan>> getList(String ChallanItemId);
    @Query("DELETE  FROM challan")
    void emptyChallan();
    @Query("UPDATE  challan SET IS_RECEIVE=:value , receive_date=:date where CHALLAN_NO=:ChallanNo")
    void updateReciver(String value,String ChallanNo,String date);

    @Insert
    void insertToChallan(Challan...challan);

    @Update
    void updateChallan(Challan...challan);

    @Delete
    void deleteChallanItem(Challan...challan);

    @Query("SELECT * from challan")
        //@Query("SELECT * challan Challan as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Challan>> getChallan();
}
