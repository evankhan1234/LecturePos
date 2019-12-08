package xact.idea.lecturepos.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Datasources.ILoginDataSource;
import xact.idea.lecturepos.Database.Model.Login;

public class LoginDataSource implements ILoginDataSource {

    private LoginDao LoginDao;
    private static LoginDataSource instance;

    public LoginDataSource(LoginDao LoginDao){
        this.LoginDao=LoginDao;
    }
    public static LoginDataSource getInstance(LoginDao LoginDao){
        if(instance==null)
            instance = new LoginDataSource(LoginDao);
        return instance;

    }

    @Override
    public Flowable<List<Login>> getLoginItems() {
        return LoginDao.getLoginItems();
    }

    @Override
    public Flowable<List<Login>> getLoginItemById(int LoginItemId) {
        return LoginDao.getLoginItemById(LoginItemId);
    }



    @Override
    public Login getLoginUser(String user_id, String user_pass) {
        return LoginDao.getLoginUser(user_id, user_pass);
    }

    @Override
    public void emptyLogin() {
        LoginDao.emptyLogin();
    }

    @Override
    public int size() {
        return LoginDao.value();
    }

    @Override
    public void insertToLogin(Login... Logins) {
        LoginDao.insertToLogin(Logins);
    }

    @Override
    public void updateLogin(Login... Logins) {
        LoginDao.updateLogin(Logins);
    }

    @Override
    public void deleteLoginItem(Login... Logins) {
        LoginDao.deleteLoginItem(Logins);
    }
}
