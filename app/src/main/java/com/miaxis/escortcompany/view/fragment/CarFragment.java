package com.miaxis.escortcompany.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.presenter.contract.CarContract;

import java.util.List;

public class CarFragment extends BaseFragment implements CarContract.View{

    private OnFragmentInteractionListener mListener;

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

    }

    @Override
    protected void initView() {

    }

    @Override
    public void updateCar(List<Car> escortList) {
    }

    @Override
    public void loadCarFailed(String message) {
    }

    @Override
    public void downCarSuccess(Company company) {
    }

    @Override
    public void downCarFailed(String message, Company company) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
