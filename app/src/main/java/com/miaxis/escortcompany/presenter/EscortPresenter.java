package com.miaxis.escortcompany.presenter;

import android.app.Fragment;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miaxis.escortcompany.app.EscortCompanyApp;
import com.miaxis.escortcompany.model.EscortModel;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Config;
import com.miaxis.escortcompany.model.entity.Escort;
import com.miaxis.escortcompany.model.retrofit.EscortNet;
import com.miaxis.escortcompany.model.retrofit.ResponseEntity;
import com.miaxis.escortcompany.presenter.contract.EscortContract;
import com.miaxis.escortcompany.util.StaticVariable;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 一非 on 2018/5/4.
 */

public class EscortPresenter extends BaseFragmentPresenter implements EscortContract.Presenter{

    private EscortContract.View view;
    private EscortModel model;

    public EscortPresenter(LifecycleProvider<FragmentEvent> provider, EscortContract.View view) {
        super(provider);
        this.view = view;
        model = new EscortModel();
    }

    @Override
    public void downEscort(final Company company) {
        Observable.create(new ObservableOnSubscribe<Config>() {
            @Override
            public void subscribe(ObservableEmitter<Config> e) throws Exception {
                e.onNext(EscortCompanyApp.getInstance().getDaoSession().getConfigDao().load(1L));
            }
        })
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Config>bindToLifecycle())
                .observeOn(Schedulers.io())
                .flatMap(new Function<Config, ObservableSource<ResponseEntity<Escort>>>() {
                    @Override
                    public ObservableSource<ResponseEntity<Escort>> apply(Config config) throws Exception {
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  //适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                                .baseUrl("http://" + config.getIp() + ":" + config.getPort())
                                .build();
                        EscortNet escortNet = retrofit.create(EscortNet.class);
                        return escortNet.downEscortByCompId(company.getId(), model.queryEscortLastestOpdate(company));
                    }
                })
                .doOnNext(new Consumer<ResponseEntity<Escort>>() {
                    @Override
                    public void accept(ResponseEntity<Escort> escortResponseEntity) throws Exception {
                        if (StaticVariable.SUCCESS.equals(escortResponseEntity.getCode())) {
                            model.saveEscort(escortResponseEntity.getListData());
                        } else {
                            throw new Exception(escortResponseEntity.getMessage());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseEntity<Escort>>() {
                    @Override
                    public void accept(ResponseEntity<Escort> escortResponseEntity) throws Exception {
                        if (view != null) {
                            view.downEscortSuccess(company);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.downEscortFailed(throwable.getMessage(), company);
                        }
                    }
                });
    }

    @Override
    public void loadEscort(Company company) {
        Observable.just(company)
                .subscribeOn(Schedulers.io())
                .compose(getProvider().<Company>bindToLifecycle())
                .observeOn(Schedulers.io())
                .map(new Function<Company, List<Escort>>() {
                    @Override
                    public List<Escort> apply(Company company) throws Exception {
                        return model.loadEscort(company);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Escort>>() {
                    @Override
                    public void accept(List<Escort> escorts) throws Exception {
                        if (view != null) {
                            view.updateEscort(escorts);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (view != null) {
                            view.loadEscortFailed(throwable.getMessage());
                        }
                    }
                });
    }

    @Override
    public void doDestroy() {
        view = null;
    }
}
