package com.miaxis.escortcompany.view.activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.adapter.CompanyAdapter;
import com.miaxis.escortcompany.adapter.MainFragmentAdapter;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.view.custom.CustomViewPager;
import com.miaxis.escortcompany.view.fragment.CarFragment;
import com.miaxis.escortcompany.view.fragment.EscortFragment;
import com.miaxis.escortcompany.view.fragment.SystemFragment;

import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements EscortFragment.OnFragmentInteractionListener,
                                                          CarFragment.OnFragmentInteractionListener,
                                                          SystemFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vp_main)
    CustomViewPager vpMain;
    @BindView(R.id.tl_main)
    TabLayout tlMain;
    @BindView(R.id.tv_company_select)
    TextView tvCompanySelect;

    private MainFragmentAdapter adapter;

    private List<Drawable> normalIconList;
    private List<Drawable> pressedIconList;

    private EscortFragment escortFragment;
    private CarFragment carFragment;
    private SystemFragment systemFragment;

    private MaterialDialog materialDialog;
    private CompanyAdapter companyAdapter;
    private Company selcectCompany;
    private List<Company> companyList;
    private String baseCompanyName = "";

    public static final String[] TITLES = {"员工管理", "车辆管理", "系统设置"};

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        normalIconList = new ArrayList<>();
        normalIconList.add(getResources().getDrawable(R.drawable.tab_uptask_normal));
        normalIconList.add(getResources().getDrawable(R.drawable.tab_mytask_normal));
        normalIconList.add(getResources().getDrawable(R.drawable.tab_icon_setting_normal));
        pressedIconList = new ArrayList<>();
        pressedIconList.add(getResources().getDrawable(R.drawable.tab_uptask_pressed));
        pressedIconList.add(getResources().getDrawable(R.drawable.tab_mytask_pressd));
        pressedIconList.add(getResources().getDrawable(R.drawable.tab_icon_setting_pressed));
        List<Fragment> fragmentList = new ArrayList<>();
        escortFragment = EscortFragment.newInstance();
        fragmentList.add(escortFragment);
        carFragment = CarFragment.newInstance();
        fragmentList.add(carFragment);
        systemFragment = SystemFragment.newInstance();
        fragmentList.add(systemFragment);
        adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragmentList);
        companyList = EscortCompanyApp.getInstance().getDaoSession().getCompanyDao().loadAll();
        companyAdapter = new CompanyAdapter(this, companyList);
    }

    @Override
    protected void initView() {
        //禁用左右滑动
        vpMain.setSlide(false);
        vpMain.setAdapter(adapter);
        vpMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlMain));
        tlMain.setupWithViewPager(vpMain, true);
        vpMain.setOffscreenPageLimit(20);
        initTabLayout();
        vpMain.setCurrentItem(1);
        tlMain.getTabAt(0).select();
        materialDialog = new MaterialDialog.Builder(this)
                .title("请选择")
                .iconRes(R.mipmap.ic_launcher)
                .adapter(companyAdapter, new LinearLayoutManager(this))
                .limitIconToDefaultSize()
                .negativeText("取消")
                .build();
        RxView.clicks(tvCompanySelect)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        materialDialog.show();
                    }
                });
        companyAdapter.setOnItemClickListener(new CompanyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selcectCompany = companyAdapter.getDataList().get(position);
                tvCompanySelect.setText(baseCompanyName + " ➧ " + selcectCompany.getCompname());
                escortFragment.downEscort(selcectCompany);
                materialDialog.dismiss();
            }
        });
        baseCompanyName = companyList.get(0).getCompname();
        tvCompanySelect.setText(baseCompanyName);
    }

    private void initTabLayout() {

        for (int i = 0; i < tlMain.getTabCount(); i++) {
            TabLayout.Tab tab = tlMain.getTabAt(i);
            if (tab != null) {
                tab.setIcon(normalIconList.get(i));
                tab.setText(TITLES[i]);
            }
        }
        tlMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (int i = 0; i < tlMain.getTabCount(); i++) {
                    TabLayout.Tab mTab = tlMain.getTabAt(i);
                    if (mTab != null) {
                        mTab.setIcon(normalIconList.get(i));
                    }
                }
                int position = tab.getPosition();
                toolbar.setVisibility(View.VISIBLE);
                tvCompanySelect.setVisibility(View.VISIBLE);
                tab.setIcon(pressedIconList.get(position));
                toolbar.setTitle(TITLES[position]);
                tab.setText(TITLES[position]);
                if (position == 2) {
                    toolbar.setVisibility(View.GONE);
                    tvCompanySelect.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onCar() {
    }

    @Override
    public void onEscort() {
    }

    @Override
    public void onSystem() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
