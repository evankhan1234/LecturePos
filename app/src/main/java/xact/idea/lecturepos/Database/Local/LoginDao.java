package xact.idea.lecturepos.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Login;

@Dao
public interface LoginDao {
    @Query("SELECT * FROM login")
    Flowable<List<Login>> getLoginItems();

    @Query("SELECT * FROM login WHERE id=:LoginItemId")
    Flowable<List<Login>> getLoginItemById(int LoginItemId);
    @Query("SELECT * FROM login WHERE id=:LoginItem")
    Login getLogin(int LoginItem);

    @Query("Select Count(id)  FROM login")
    int value();

    @Query("Select  * FROM login where USER_ID=:UserID AND PASSWORD=:Password")
    Login getLoginUser(String UserID,String Password);
    @Query("DELETE  FROM login")
    void emptyLogin();

    @Insert
    void insertToLogin(Login...login);

    @Update
    void updateLogin(Login...login);

    @Delete
    void deleteLoginItem(Login...login);

    @Query("SELECT * from login")
        //@Query("SELECT * login Login as c Inner  JOIN Favorite as f ON c.Id = f.id  WHERE f.id=:favoriteid")
    Flowable<List<Login>> getLogin();
}
