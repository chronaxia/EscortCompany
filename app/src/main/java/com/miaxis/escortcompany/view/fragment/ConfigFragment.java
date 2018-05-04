package com.miaxis.escortcompany.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.presenter.ConfigPresenter;
import com.miaxis.escortcompany.presenter.contract.ConfigContract;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends BaseFragment implements ConfigContract.View{

    private ConfigContract.Presenter presenter;
    private OnConfigClickListener mListener;

    private MaterialDialog materialDialog;

    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.et_orgCode)
    EditText etOrgCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    public ConfigFragment() {
        // Required empty public constructor
    }

    public static ConfigFragment newInstance() {
        return new ConfigFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ConfigFragment.OnConfigClickListener) {
            mListener = (ConfigFragment.OnConfigClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfigClickListener");
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_config;
    }

    @Override
    protected void initData() {
        presenter = new ConfigPresenter(this, this);
        presenter.loadConfig();
    }

    @Override
    protected void initView() {
        materialDialog = new MaterialDialog.Builder(this.getActivity())
                .title("请稍后...")
                .content("")
                .progress(true, 100)
                .cancelable(false)
                .build();
        RxView.clicks(btnCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        mListener.onConfigCancel();
                    }
                });
        Observable<CharSequence> observableIp = RxTextView.textChanges(etIp);
        Observable<CharSequence> observablePort = RxTextView.textChanges(etPort);
        Observable<CharSequence> observableOrgCode = RxTextView.textChanges(etOrgCode);
        Observable.combineLatest(observableIp, observablePort, observableOrgCode, new Function3<CharSequence, CharSequence, CharSequence, Boolean>(){
            @Override
            public Boolean apply(CharSequence ip, CharSequence port, CharSequence orgCode) throws Exception {
                return !ip.toString().isEmpty() && !port.toString().isEmpty()&& !orgCode.toString().isEmpty();
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.<Boolean>bindToLifecycle())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) throws Exception {
                        if (b != null) {
                            btnConfirm.setEnabled(b.booleanValue());
                        }
                    }
                });
        RxView.clicks(btnConfirm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        materialDialog.setContent("正在清空历史数据...");
                        materialDialog.show();
                        presenter.configSave(etIp.getText().toString(),
                                etPort.getText().toString(),
                                etOrgCode.getText().toString());
                    }
                });
    }

    @Override
    public void configSaveSuccess() {
        materialDialog.dismiss();
        mListener.onConfigSave();
    }

    @Override
    public void configSaveFailed() {
        materialDialog.setCancelable(true);
    }

    @Override
    public void fetchConfig(Config config) {
        if (config == null) {
            return;
        }
        etIp.setText(config.getIp());
        etPort.setText(config.getPort());
        etOrgCode.setText(config.getOrgCode());
    }

    @Override
    public void setProgressDialogMessage(String message) {
        materialDialog.setContent(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.doDestroy();
    }

    public interface OnConfigClickListener {
        void onConfigSave();
        void onConfigCancel();
    }

}
