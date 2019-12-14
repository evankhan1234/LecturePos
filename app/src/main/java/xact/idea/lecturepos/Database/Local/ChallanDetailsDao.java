package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.ChallanDetails;

@Dao
public interface ChallanDetailsDao {
    @Query("SELECT * FROM challan_details")
    Flowable<List<ChallanDetails>> getChallanDetailsItems();

    @Query("SELECT * FROM challan_details WHERE F_CHALLAN_NO=:ChallanDetailsItemId")
    Flowable<List<ChallanDetails>> getChallanDetailsItemById(int ChallanDetailsItemId);
    @Query("SELECT * FROM challan_details WHERE F_CHALLAN_NO=:ChallanDetailsItem")
    ChallanDetails getChallanDetails(String ChallanDetailsItem);

    @Query("Select Count(id)  FROM challan_details")
    int value();


    @Query("DELETE  FROM challan_details")
    void emptyChallanDetails();

    @Insert
    void insertToChallanDetails(ChallanDetails...challan_details);

    @Update
    void updateChallanDetails(ChallanDetails...challan_details);

    @Delete
    void deleteChallanDetailsItem(ChallanDetails...challan_details);

    @Query("SELECT * from challan_details")
        //@Query("SELECT * challan_details ChallanDetails as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<ChallanDetails>> getChallanDetails();
}
