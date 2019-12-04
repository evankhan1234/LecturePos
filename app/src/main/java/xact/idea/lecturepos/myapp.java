package xact.idea.lecturepos;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("App","Started");

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}