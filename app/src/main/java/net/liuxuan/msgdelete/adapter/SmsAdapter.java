package net.liuxuan.msgdelete.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.liuxuan.msgdelete.R;
import net.liuxuan.msgdelete.model.SmsBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 主页面短信内容的adapter
 * Created by hanj on 14-10-30.
 */
public class SmsAdapter extends BaseAdapter {
    private Context context;
    private List<SmsBean> list;

    private SparseBooleanArray selectedMap;

    public SmsAdapter(Context context, List<SmsBean> list) {
        this.context = context;
        this.list = list;

        selectedMap = new SparseBooleanArray();
    }

    public SparseBooleanArray getSelectedMap() {
        return selectedMap;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.sms_layout, parent, false);
            holder = new ViewHolder();

            holder.selectCB = (CheckBox) convertView.findViewById(R.id.sms_cb);
            holder.fromNumTV = (TextView) convertView.findViewById(R.id.sms_fromNum);
            holder.contentTV = (TextView) convertView.findViewById(R.id.sms_content);
            holder.timeTV = (TextView) convertView.findViewById(R.id.sms_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fromNumTV.setText(list.get(position).getFromNum());
        holder.contentTV.setText(list.get(position).getContent());
        holder.timeTV.setText(getFormatTime(list.get(position).getTime()));

        holder.selectCB.setTag(position);
        holder.selectCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer position = (Integer) buttonView.getTag();
                if (isChecked) {
                    selectedMap.put(position, true);
                } else {
                    selectedMap.put(position, false);
                }

                if (listener != null) {
                    listener.onSelectAll(hasSelectAll());
                }
            }
        });

        holder.selectCB.setChecked(selectedMap.get(position));

        return convertView;
    }

    private boolean hasSelectAll() {
        for (int i = 0; i < getCount(); i++) {
            if (!selectedMap.get(i)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasNoneSelected() {
        for (int i = 0; i < getCount(); i++) {ta
            if (selectedMap.get(i)) {
                return false;
            }
        }

        return true;
    }


    private class ViewHolder {
        TextView fromNumTV, contentTV, timeTV;
        CheckBox selectCB;
    }

    private String getFormatTime(long ms) {
        SimpleDateFormat f = new SimpleDateFormat("MM/dd");
        return f.format(new Date(ms));
    }

    public void setList(List<SmsBean> list) {
        this.list = list;
    }

    public List<SmsBean> getList() {
        return list;
    }

    public interface OnSelectAllListener {
        void onSelectAll(boolean selectAll);
    }

    private OnSelectAllListener listener;

    public void setOnSelectAllListener(OnSelectAllListener listener) {
        this.listener = listener;
    }


}
