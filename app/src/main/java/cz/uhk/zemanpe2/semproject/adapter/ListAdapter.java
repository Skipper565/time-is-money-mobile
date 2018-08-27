package cz.uhk.zemanpe2.semproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cz.uhk.zemanpe2.semproject.R;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.FinancialEntity;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private List<FinancialEntity> list;

    public ListAdapter(Context context, List<FinancialEntity> list) {
        myInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        convertView = myInflater.inflate(R.layout.row_item, null);
        holder.date = (TextView) convertView.findViewById(R.id.date);
        holder.note = (TextView) convertView.findViewById(R.id.note);
        holder.value = (TextView) convertView.findViewById(R.id.value);

        convertView.setTag(holder);

        holder.date.setText(list.get(position).getDate().toString());
        holder.note.setText(list.get(position).getNote());
        holder.value.setText(String.valueOf(list.get(position).getValue()));

        return convertView;
    }

    static class ViewHolder {
        TextView date;
        TextView note;
        TextView value;
    }

}