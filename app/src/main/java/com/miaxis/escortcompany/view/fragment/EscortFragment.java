package com.miaxis.escortcompany.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.adapter.CompanyAdapter;
import com.miaxis.escortcompany.adapter.EscortAdapter;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.presenter.EscortPresenter;
import com.miaxis.escortcompany.presenter.contract.EscortContract;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class EscortFragment extends BaseFragment implements EscortContract.View{

    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.srl_escort)
    SwipeRefreshLayout srlEscort;
    @BindView(R.id.smrv_escort)
    SwipeMenuRecyclerView smrvEscort;

    private OnFragmentInteractionListener mListener;
    private EscortAdapter escortAdapter;

    private EscortContract.Presenter presenter;
    private Company selcectCompany;

    public EscortFragment() {
        // Required empty public constructor
    }

    public static EscortFragment newInstance() {
        return new EscortFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_escort;
    }

    @Override
    protected void initData() {
        presenter = new EscortPresenter(this, this);
        escortAdapter = new EscortAdapter(getContext(), new ArrayList<Escort>());
    }

    @Override
    protected void initView() {
        smrvEscort.setLayoutManager(new LinearLayoutManager(getContext()));
        smrvEscort.setSwipeItemClickListener(new SwipeItemClickListener() {
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
        smrvEscort.setSwipeMenuCreator(mSwipeMenuCreator);
        SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                if (menuPosition == 0) {
                    Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "点击删除", 0, true).show();
                } else if (menuPosition == 1) {
                    Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "点击修改", 0, true).show();
                }
            }
        };
        // 菜单点击监听。
        smrvEscort.setSwipeMenuItemClickListener(mMenuItemClickListener);
        smrvEscort.setAdapter(escortAdapter);
        srlEscort.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.downEscort(selcectCompany);
            }
        });
    }

    @Override
    public void updateEscort(List<Escort> escortList) {
        srlEscort.setRefreshing(false);
        if (escortList.size() != 0 && escortList.get(0).getComid().equals("" + selcectCompany.getId())) {
            escortAdapter.setDataList(escortList);
            escortAdapter.notifyDataSetChanged();
        } else {
            Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), "该机构下未找到操作员", 0, true).show();
            escortAdapter.setDataList(new ArrayList<Escort>());
            escortAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadEscortFailed(String message) {
        srlEscort.setRefreshing(false);
        Toasty.info(EscortCompanyApp.getInstance().getApplicationContext(), message, 0, true).show();
    }

    @Override
    public void downEscortSuccess(Company company) {
        presenter.loadEscort(company);
    }

    @Override
    public void downEscortFailed(String message, Company company) {
        presenter.loadEscort(company);
    }

    public void downEscort(Company company) {
        selcectCompany = company;
        presenter.downEscort(company);
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
        void onEscort();
    }
}
