package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Customer;

@Dao
public interface CustomerDao {
    @Query("SELECT * FROM RETAIL_CUSTOMERS")
    Flowable<List<Customer>> getCustomerItems();

    @Query("SELECT * FROM RETAIL_CUSTOMERS WHERE id=:CustomerItemId")
    Flowable<List<Customer>> getCustomerItemById(int CustomerItemId);

    @Query("SELECT * FROM RETAIL_CUSTOMERS WHERE ShopName=:Name")
    Customer getCustomerss(String Name);

    @Query("Select Count(id)  FROM RETAIL_CUSTOMERS")
    int value();


    @Query("DELETE  FROM RETAIL_CUSTOMERS")
    void emptyCustomer();

    @Insert
    void insertToCustomer(Customer...Customers);

    @Update
    void updateCustomer(Customer...Customers);

    @Delete
    void deleteCustomerItem(Customer...Customers);

    @Query("SELECT * from RETAIL_CUSTOMERS")
        //@Query("SELECT * from Customer as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Customer>> getCustomer();
}
