package net.liuxuan.msgdelete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import net.liuxuan.msgdelete.R;
import net.liuxuan.msgdelete.model.FilterBean;

import java.util.List;

/**
 * 筛选器
 * Created by hanj on 14-10-30.
 */
public class FilterAdapter extends BaseAdapter {
    private Context context;
    private List<FilterBean> list;

    public FilterAdapter(Context context, List<FilterBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pop_item, parent, false);
            holder = new ViewHolder();

            holder.textView = (TextView) convertView.findViewById(R.id.pop_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).getTitle());
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
    }
}
