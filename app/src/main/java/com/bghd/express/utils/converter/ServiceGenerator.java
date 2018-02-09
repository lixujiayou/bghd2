package com.bghd.express.utils.converter;








import com.bghd.express.core.AllUrl;
import com.bghd.express.utils.converter.string.StringConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jiang.xu on 2015/11/7.
 */
public class ServiceGenerator {
    public static OkHttpClient sOkHttpClient = new OkHttpClient.Builder().
    connectTimeout(60, TimeUnit.SECONDS).
    readTimeout(60, TimeUnit.SECONDS).
    writeTimeout(60, TimeUnit.SECONDS).build();

    public static Retrofit.Builder mBuilder = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())   //加入Rxjava
            .addConverterFactory(GsonConverterFactory.create())
            ;

    private static Retrofit.Builder mStringBuilder = new Retrofit.Builder()
            .baseUrl(AllUrl.mainUrl)
            .addConverterFactory(StringConverterFactory.create());




    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit =mBuilder
                .baseUrl(AllUrl.mainUrl)
                .client(sOkHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }




    public static <S> S createService2(Class<S> serviceClass) {
        Retrofit retrofit = mStringBuilder.client(sOkHttpClient).build();
        return retrofit.create(serviceClass);
    }
}
