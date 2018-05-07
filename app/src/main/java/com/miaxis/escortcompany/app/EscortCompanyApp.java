package com.miaxis.escortcompany.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;

import com.miaxis.escortcompany.model.local.GreenDaoContext;
import com.miaxis.escortcompany.model.local.greenDao.gen.DaoMaster;
import com.miaxis.escortcompany.model.local.greenDao.gen.DaoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 一非 on 2018/5/3.
 */

public class EscortCompanyApp extends Application {

    private static EscortCompanyApp escortApp;

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Map<String, Object> map;

    @Override
    public void onCreate() {
        super.onCreate();
        escortApp = this;
        map = new HashMap<>();
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public static EscortCompanyApp getInstance() {
        return escortApp;
    }

    /**
     * 由于运行时权限依赖于LoginActivity，数据库初始化在拿到权限后进行
     */
    public void initDbHelp() {
        helper = new DaoMaster.DevOpenHelper(new GreenDaoContext(this), "EscortCompany.db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        daoSession.clear();
        return daoSession;
    }

    public void clearAndRebuildDatabase() {
        DaoMaster.dropAllTables(daoMaster.getDatabase(),true);
        DaoMaster.createAllTables(daoMaster.getDatabase(),true);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

}
