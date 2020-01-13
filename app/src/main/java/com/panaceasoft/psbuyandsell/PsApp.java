package com.panaceasoft.psbuyandsell;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.multidex.MultiDexApplication;

import com.panaceasoft.psbuyandsell.di.AppInjector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;

import javax.inject.Inject;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.params.ConnRoutePNames;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class PsApp extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppInjector.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
