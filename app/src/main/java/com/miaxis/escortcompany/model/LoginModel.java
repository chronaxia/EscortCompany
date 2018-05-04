package com.miaxis.escortcompany.model;

import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;

import java.util.List;

/**
 * Created by 一非 on 2018/5/3.
 */

public class LoginModel {

    public Config loadConfig() {
        return EscortCompanyApp.getInstance().getDaoSession().getConfigDao().load(1L);
    }

    public void saveConfig(Config config) {
        EscortCompanyApp.getInstance().getDaoSession().getConfigDao().deleteAll();
        EscortCompanyApp.getInstance().getDaoSession().getConfigDao().insert(config);
    }

    public void saveUsername(String username) {
        Config config = EscortCompanyApp.getInstance().getDaoSession().getConfigDao().load(1L);
        config.setUsername(username);
        EscortCompanyApp.getInstance().getDaoSession().getConfigDao().update(config);
    }

    public void saveCompanyList(List<Company> companyList) {
        EscortCompanyApp.getInstance().getDaoSession().getCompanyDao().deleteAll();
        EscortCompanyApp.getInstance().getDaoSession().getCompanyDao().insertInTx(companyList);
    }

}
