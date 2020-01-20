package xact.idea.lecturepos.Retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  Context context;
  private static Retrofit retrofit = null;

  public static Retrofit getClient(String baseUrl){
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)

            .readTimeout(100,TimeUnit.SECONDS).build();

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    if (retrofit == null) {

      retrofit = new Retrofit.Builder().baseUrl(baseUrl).client(client).
              addConverterFactory(GsonConverterFactory.create(gson))
              .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();

    }
    return retrofit;
  }
}