package com.miaxis.escortcompany.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.model.entity.Car;
import com.miaxis.escortcompany.model.entity.Escort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 一非 on 2018/5/7.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private List<Car> dataList;

    private LayoutInflater layoutInflater;

    public CarAdapter(Context context, List<Car> dataList) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_car, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Car car = dataList.get(position);
        holder.tvCar.setText(car.getPlateno());
        Glide.with(layoutInflater.getContext())
                .load(car.getCarphoto())
                .into(holder.civCarPhoto);
    }

    public void setDataList(List<Car> escortList) {
        this.dataList = escortList;
    }

    public List<Car> getDataList() {
        return dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_car_photo)
        CircleImageView civCarPhoto;
        @BindView(R.id.tv_car)
        TextView tvCar;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

