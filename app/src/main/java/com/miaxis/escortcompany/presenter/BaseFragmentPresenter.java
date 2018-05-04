package com.miaxis.escortcompany.presenter;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * Created by 一非 on 2018/4/8.
 */

public class BaseFragmentPresenter {

    private LifecycleProvider<FragmentEvent> provider;

    public BaseFragmentPresenter(LifecycleProvider<FragmentEvent> provider) {
        this.provider = provider;
    }

    public LifecycleProvider<FragmentEvent> getProvider() {
        return provider;
    }

}
