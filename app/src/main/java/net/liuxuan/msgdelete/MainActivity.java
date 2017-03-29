package net.liuxuan.msgdelete;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import net.liuxuan.msgdelete.adapter.FilterAdapter;
import net.liuxuan.msgdelete.adapter.SmsAdapter;
import net.liuxuan.msgdelete.model.FilterBean;
import net.liuxuan.msgdelete.model.SmsBean;
import net.liuxuan.msgdelete.tools.DatabaseHelper;
import net.liuxuan.msgdelete.tools.ScreenUtils;
import net.liuxuan.msgdelete.tools.SmsWriteOpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView titleTV;

    private CheckBox selectAllCB;
    private boolean ignoreChange;

    private List<SmsBean> smsList;
    private SmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTV = (TextView) findViewById(R.id.main_title);
        ImageView filterIV = (ImageView) findViewById(R.id.main_filter);
        ListView listView = (ListView) findViewById(R.id.main_listView);
        selectAllCB = (CheckBox) findViewById(R.id.main_selectAll_cb);
        Button deleteBtn = (Button) findViewById(R.id.main_delete);

        filterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        smsList = getAllSms();
        adapter = new SmsAdapter(this, smsList);
        listView.setAdapter(adapter);

        adapter.setOnSelectAllListener(new SmsAdapter.OnSelectAllListener() {
            @Override
            public void onSelectAll(boolean selectAll) {
                ignoreChange = true;
                selectAllCB.setChecked(selectAll);
                ignoreChange = false;
            }
        });

        selectAllCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!ignoreChange) {
                    SparseBooleanArray map = adapter.getSelectedMap();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        map.put(i, isChecked);
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });

        //批量删除
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.hasNoneSelected()) {
                    Toast.makeText(MainActivity.this, "请选择要删除的短信。",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                deleteSelectedSms();
                //刷新页面
                adapter.notifyDataSetChanged();
            }
        });
    }

    //显示filter列表PopupWindow
    private void showPopupWindow(View anchor) {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
        ListView listView = (ListView) view.findViewById(R.id.pop_listView);
        TextView allSmsTV = (TextView) view.findViewById(R.id.pop_all_sms);
        TextView newFilterTV = (TextView) view.findViewById(R.id.pop_new_filter);
        final PopupWindow popupWindow = new PopupWindow(view, ScreenUtils.getScreenW(this) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        final List<FilterBean> filterList = getFilters();
        listView.setAdapter(new FilterAdapter(this, filterList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();

                filterSms(filterList.get(position));
            }
        });

        //长按删除此filter
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();

                showDeleteFilterDialog(filterList.get(position).getId(),
                        filterList.get(position).getTitle());
                return true;
            }
        });

        //全部短信
        allSmsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                String title = MainActivity.this.getResources().getString(R.string.main_sms_all);
                filterSms(new FilterBean(1, title, "", 1));
            }
        });

        //新建过滤器
        newFilterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showAddFilterDialog();
            }
        });

        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(anchor);
    }

    //删除过滤器的对话框
    private void showDeleteFilterDialog(final int filterId, String filterTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("删除过滤器 " + filterTitle + " ？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                helper.deleteFilter(filterId);
            }
        });

        builder.create().show();
    }

    //新建过滤器的对话框
    private void showAddFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加过滤器");

        View view = LayoutInflater.from(this).inflate(R.layout.add_filter_dialog, null);
        final EditText titleET = (EditText) view.findViewById(R.id.add_filter_dialog_title);
        final EditText contentET = (EditText) view.findViewById(R.id.add_filter_dialog_content);
        final RadioButton filterPhoneNum = (RadioButton) view.findViewById(R.id.add_filter_dialog_phoneNum);

        filterPhoneNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contentET.setText("");
                if (isChecked) {
                    contentET.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                } else {
                    contentET.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                }
            }
        });

        builder.setView(view);
        builder.setPositiveButton("确认", null);
        builder.setNegativeButton("取消", null);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //点击确定按钮时，如果输入不正确，则进行提示，不关闭对话框.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString().trim();
                String content = contentET.getText().toString().trim();
                if (title.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (content.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入筛选内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();

                //添加至数据库
                DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                //type:筛选手机号-1，筛选短信内容-2
                int type = filterPhoneNum.isChecked() ? 1 : 2;
                FilterBean filterBean = new FilterBean(-1, title, content, type);
                helper.addFilter(filterBean);
                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                //筛选
                filterSms(filterBean);
            }
        });
    }

    //从数据库中读取所有的filter
    private List<FilterBean> getFilters() {
        DatabaseHelper helper = new DatabaseHelper(this);
        return helper.getFilters();
    }

    //获取系统全部短信
    private List<SmsBean> getAllSms() {
        final String SMS_URI_ALL = "content://sms/";
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[]{"_id", "address", "body", "date"};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, "date desc");

        int index_Id = cursor.getColumnIndex("_id");
        int index_Address = cursor.getColumnIndex("address");
        int index_Body = cursor.getColumnIndex("body");
        int index_Date = cursor.getColumnIndex("date");

        List<SmsBean> smsList = new ArrayList<SmsBean>();
        while (cursor.moveToNext()) {
            SmsBean bean = new SmsBean();
            bean.setId(cursor.getInt(index_Id));
            bean.setFromNum(cursor.getString(index_Address));
            bean.setContent(cursor.getString(index_Body));
            bean.setTime(cursor.getLong(index_Date));
            smsList.add(bean);
        }

        cursor.close();
        return smsList;
    }

    //筛选符合条件的发件人手机号码
    private void filterSms(FilterBean filterBean) {
        if (smsList == null) {
            return;
        }

        titleTV.setText(filterBean.getTitle());
        List<SmsBean> adapterSmsList = new ArrayList<SmsBean>();
        for (SmsBean sms : smsList) {
            String filterContent = (filterBean.getType() == 1 ? sms.getFromNum() : sms.getContent())
                    .replace(" ", "").replace("-", "");
            if (checkSms(filterContent, filterBean.getContent())) {
                adapterSmsList.add(sms);
            }
        }

        adapter.setList(adapterSmsList);
        adapter.getSelectedMap().clear();
        adapter.notifyDataSetChanged();

        ignoreChange = true;
        selectAllCB.setChecked(false);
        ignoreChange = false;
    }

    //模糊搜索，匹配手机号
    private boolean checkSms(String content, String filter) {
        Pattern pattern = Pattern.compile(".*" + filter + ".*");
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }

    //删除已选择的短信
    private void deleteSelectedSms() {
        List<SmsBean> adapterList = adapter.getList();
        SparseBooleanArray map = adapter.getSelectedMap();
        final StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = adapterList.size() - 1; i >= 0; i--) {
            if (map.get(i)) {
                sb.append(adapterList.get(i).getId());
                sb.append(",");

                //更新页面
                smsList.remove(adapterList.get(i));
                adapterList.remove(i);
                map.delete(i);

                count++;
            }
        }

        Toast.makeText(this, "成功删除了" + count + "条短信", Toast.LENGTH_SHORT).show();

        //删除
        if (!SmsWriteOpUtil.isWriteEnabled(getApplicationContext())) {
            SmsWriteOpUtil.setWriteEnabled(
                    getApplicationContext(), true);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String SMS_URI_ALL = "content://sms/";
                Uri uri = Uri.parse(SMS_URI_ALL);
                String whereClause = "_id in(" + sb.substring(0, sb.length() - 1) + ")";
                int count = getContentResolver().delete(uri, whereClause, null);
                System.out.println("实际删除短信" + count + "条");
            }
        }).start();
    }
}
