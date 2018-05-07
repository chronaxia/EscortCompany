package com.miaxis.escortcompany.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.adapter.CarAdapter;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.presenter.CarPresenter;
import com.miaxis.escortcompany.presenter.contract.CarContract;
import com.miaxis.escortcompany.view.activity.AddCarActivity;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class CarFragment extends BaseFragment implements CarContract.View{

    @BindView(R.id.srl_car)
    SwipeRefreshLayout srlCar;
    @BindView(R.id.smrv_car)
    SwipeMenuRecyclerView smrvCar;

    private CarContract.Presenter presenter;
    private CarAdapter carAdapter;
    private OnFragmentInteractionListener mListener;
    private Company selcectCompany;

    public CarFragment() {
        // Required empty public constructor
    }

    public static CarFragment newInstance() {
        return new CarFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_car;
    }

    @Override
    protected void initData() {
        presenter = new CarPresenter(this, this);
        carAdapter = new CarAdapter(getContext(), new ArrayList<Car>());
    }

    @Override
    protected void initView() {
        smrvCar.setLayoutManager(new LinearLayoutManager(getContext()));
        smrvCar.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                //不想禁用viewpager的左右滑动，可把滑动菜单添加到右边
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getResources().getColor(R.color.firebrick))
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth(200)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                rightMenu.addMenuItem(deleteItem);
                SwipeMenuItem editItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getResources().getColor(R.color.darkorange))
                        .setText("修改")
                        .setTextColor(Color.WHITE)
                        .setTextSize(16)
                        .setWidth(200)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                rightMenu.addMenuItem(editItem);
            }
        };
        // 设置监听器。
        smrvCar.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    presenter.deleteCar(carAdapter.getDataList().get(adapterPosition));
                } else if (menuPosition == 1) {
                    Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "点击修改", 0, true).show();
                }
            }
        };
        // 菜单点击监听。
        smrvCar.setSwipeMenuItemClickListener(mMenuItemClickListener);
        smrvCar.setAdapter(carAdapter);
        srlCar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (selcectCompany != null) {
                    presenter.downCar(selcectCompany);
                } else {
                    srlCar.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void updateCar(List<Car> carList) {
        srlCar.setRefreshing(false);
        if (carList.size() != 0 && ("" + carList.get(0).getCompid()).equals("" + selcectCompany.getId())) {
            carAdapter.setDataList(carList);
            carAdapter.notifyDataSetChanged();
        } else {
            Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "该机构下未找到车辆", 0, true).show();
            carAdapter.setDataList(new ArrayList<Car>());
            carAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadCarFailed(String message) {
        srlCar.setRefreshing(false);
        Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), message, 0, true).show();
    }

    @Override
    public void downCarSuccess(Company company) {
        presenter.loadCar(company);
    }

    @Override
    public void downCarFailed(String message, Company company) {
        presenter.loadCar(company);
    }

    public void downCar(Company company) {
        selcectCompany = company;
        presenter.downCar(company);
    }

    public void addCar() {
        Intent intent = new Intent(CarFragment.this.getContext(), AddCarActivity.class);
        intent.putExtra("company", selcectCompany);
        startActivity(intent);
    }

    @Override
    public void deleteCarSuccess() {
        presenter.loadCar(selcectCompany);
        Toasty.success(EscortCompanyApp.getInstance().getApplicationContext(), "删除成功", 0, true).show();
    }

    @Override
    public void deleteCarFailed(String message) {
        Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "删除操作失败", 0, true).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onCar();
    }
}
