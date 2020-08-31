package com.example.bachelorthesisclient.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.Performer;
import com.example.bachelorthesisclient.util.DateTimeUtil;

import java.util.Date;
import java.util.List;

public class StagePerformersAdapter extends ArrayAdapter<Performer> {
    private Context context;
    private List<Performer> performerList;

    public StagePerformersAdapter(@NonNull Context context, List<Performer> list) {
        super(context, 0, list);
        this.context = context;
        this.performerList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Performer performer = performerList.get(position);

        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_list_stage_performers, parent, false);
        }

        TextView performerName = listItem.findViewById(R.id.tv_performer_name);
        TextView performerDate = listItem.findViewById(R.id.tv_performer_date);

        performerName.setText(performer.getName());
        performerDate.setText(DateTimeUtil.formatDate(performer.getStartTime()));

        if (new Date().compareTo(performer.getStartTime()) > 0) {
            performerName.setEnabled(false);
            performerDate.setEnabled(false);
        } else {
            performerName.setEnabled(true);
            performerDate.setEnabled(true);
        }

        return listItem;
    }

    public void setPerformers(List<Performer> performerList) {
        this.performerList.clear();
        this.performerList.addAll(performerList);
        notifyDataSetChanged();
    }
}
