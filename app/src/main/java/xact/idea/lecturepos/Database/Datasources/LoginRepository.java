package xact.idea.lecturepos.Database.Datasources;

import java.util.List;

import io.reactivex.Flowable;
import xact.idea.lecturepos.Database.Model.Login;

public class LoginRepository implements ILoginDataSource {
    public ILoginDataSource ILoginDataSource;
    public LoginRepository(ILoginDataSource ILoginDataSource){
        this.ILoginDataSource=ILoginDataSource;
    }
    private static  LoginRepository instance;

    public static LoginRepository getInstance(ILoginDataSource iCartDataSource){
        if(instance==null)
            instance= new LoginRepository(iCartDataSource);
        return instance;

    }

    @Override
    public Flowable<List<Login>> getLoginItems() {
        return ILoginDataSource.getLoginItems();
    }

    @Override
    public Flowable<List<Login>> getLoginItemById(int LoginItemId) {
        return ILoginDataSource.getLoginItemById(LoginItemId);
    }



    @Override
    public Login getLoginUser(String user_id, String user_pass) {
        return ILoginDataSource.getLoginUser(user_id, user_pass);
    }

    @Override
    public void emptyLogin() {
        ILoginDataSource.emptyLogin();
    }

    @Override
    public int size() {
        return ILoginDataSource.size();
    }

    @Override
    public void insertToLogin(Login... Logins) {
        ILoginDataSource.insertToLogin(Logins);
    }

    @Override
    public void updateLogin(Login... Logins) {
        ILoginDataSource.updateLogin(Logins);
    }

    @Override
    public void deleteLoginItem(Login... Logins) {
        ILoginDataSource.deleteLoginItem(Logins);
    }
}
