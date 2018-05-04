package com.miaxis.escortcompany.model;

import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.model.local.greenDao.gen.CarDao;
import com.miaxis.escortcompany.model.local.greenDao.gen.EscortDao;

import java.util.List;

/**
 * Created by 一非 on 2018/5/4.
 */

public class CarModel {
    public List<Car> loadCar(Company company) {
        return EscortCompanyApp.getInstance().getDaoSession().getCarDao().queryBuilder()
                .where(CarDao.Properties.Compid.eq(company.getId())).list();
    }
}
