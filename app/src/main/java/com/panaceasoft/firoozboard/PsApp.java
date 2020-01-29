package com.panaceasoft.firoozboard;

import android.app.Activity;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.panaceasoft.firoozboard.api.ApiService;
import com.panaceasoft.firoozboard.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class PsApp extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private static ApiService api;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppInjector.init(this);
    }

    public static ApiService getApi() {
        return api;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.APP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient.Builder().build())
                .build();


        api = retrofit.create(ApiService.class);


//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
        //LeakCanary.install(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile_Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

//        setupLeakCanary();

        //new ContextModule(getApplicationContext());

        //MultiDex.install(this);

    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

//    protected RefWatcher setupLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return RefWatcher.DISABLED;
//        }
//
//        ExcludedRefs excludedRefs = AndroidExcludedRefs.createAppDefaults()
//                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
//                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
//                .instanceField("com.android.internal.policy.PhoneWindow$DecorView", "mContext")
//                .instanceField("android.support.v7.widget.SearchView$SearchAutoComplete", "mContext")
//                .build();
//
//        return LeakCanary.refWatcher(this).excludedRefs(excludedRefs).buildAndInstall();
//                //.install(this);
//    }

}
