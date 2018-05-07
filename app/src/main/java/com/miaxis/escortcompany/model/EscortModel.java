package com.miaxis.escortcompany.model;

import android.database.Cursor;

import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.model.local.greenDao.gen.EscortDao;
import com.miaxis.escortcompany.util.DateUtil;
import com.miaxis.escortcompany.util.StaticVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一非 on 2018/5/4.
 */

public class EscortModel {
    public String queryEscortLastestOpdate(Company company) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MAX(OPDATE) FROM ESCORT WHERE COMID=?");
        // 执行sql
        Cursor cursor = EscortCompanyApp.getInstance().getDb().rawQuery(sql.toString(), new String[]{"" + company.getId()});
        // 获取查询结果
        String opdate = "";
        while (cursor.moveToNext()) {
            opdate = cursor.getString(0);
        }
        cursor.close();
        return opdate;
    }

    public void saveEscort(List<Escort> escortList) {
        EscortCompanyApp.getInstance().getDaoSession().getEscortDao().insertOrReplaceInTx(escortList);
    }

    public List<Escort> loadEscort(Company company) {
        return EscortCompanyApp.getInstance().getDaoSession().getEscortDao().queryBuilder()
                .where(EscortDao.Properties.Comid.eq(company.getId()))
                .where(EscortDao.Properties.Esstatus.eq(0)).list();
    }

    public void deleteEscort(Escort escort) {
        escort.setEsstatus(2);
        escort.setOpdate(DateUtil.getCurDateTime24());
        EscortCompanyApp.getInstance().getDaoSession().getEscortDao().update(escort);
    }

}
