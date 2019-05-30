package cz.uhk.zemanpe2.semproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cz.uhk.zemanpe2.semproject.R;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.FinancialEntity;

import java.text.SimpleDateFormat;
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
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                ViewHolder holder = new ViewHolder();
        convertView = myInflater.inflate(R.layout.row_item, null);
        holder.date = convertView.findViewById(R.id.date);
        holder.value = convertView.findViewById(R.id.value);
        holder.note = convertView.findViewById(R.id.note);
        holder.latitude = convertView.findViewById(R.id.latitude);
        holder.longitude = convertView.findViewById(R.id.longitude);

        convertView.setTag(holder);

        holder.date.setText(df.format(list.get(position).getDate()));
        String sign = "";
        if (list.get(position).getType().equals("cost")) {
            sign = "- ";
        }
        holder.value.setText(sign + list.get(position).getValue());
        if (list.get(position).getNote() != null) {
            holder.note.setVisibility(View.VISIBLE);
            holder.note.setText(list.get(position).getNote());
        }

        if (list.get(position).getLatitude() != null) {
            holder.latitude.setVisibility(View.VISIBLE);
            holder.latitude.setText("Latitude: " + list.get(position).getLatitude());
        }

        if ( list.get(position).getLongitude() != null) {
            holder.longitude.setVisibility(View.VISIBLE);
            holder.longitude.setText("Longitude: " + list.get(position).getLongitude());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView date;
        TextView value;
        TextView note;
        TextView latitude;
        TextView longitude;
    }

}