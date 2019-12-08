package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Login;

public interface ILoginDataSource {
    Flowable<List<Login>> getLoginItems();


    Flowable<List<Login>> getLoginItemById(int LoginItemId);




    Login getLoginUser(String user_id, String user_pass);

    void emptyLogin();

    int size();


    void insertToLogin(Login... Logins);


    void updateLogin(Login... Logins);


    void deleteLoginItem(Login... Logins);
}
