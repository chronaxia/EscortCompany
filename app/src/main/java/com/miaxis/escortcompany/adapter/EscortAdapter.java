package com.miaxis.escortcompany.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miaxis.escortcompany.R;
import com.miaxis.escortcompany.model.entity.Company;
import com.miaxis.escortcompany.model.entity.Escort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 一非 on 2018/5/3.
 */

public class EscortAdapter extends RecyclerView.Adapter<EscortAdapter.MyViewHolder> {

    private List<Escort> dataList;

    private LayoutInflater layoutInflater;

    public EscortAdapter(Context context, List<Escort> dataList) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_escort, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Escort escort = dataList.get(position);
        holder.tvEscort.setText(escort.getEsname());
    }

    public void setDataList(List<Escort> escortList) {
        this.dataList = escortList;
    }

    public List<Escort> getDataList() {
        return dataList;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_escort)
        TextView tvEscort;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
