package net.liuxuan.msgdelete.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.liuxuan.msgdelete.model.FilterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanj on 14-10-30.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SmsFilter.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "sms_filter";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + "(_id integer primary key autoincrement," +
                "title text,content text,type integer)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFilter(FilterBean filterBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", filterBean.getTitle());
        values.put("content", filterBean.getContent());
        values.put("type", filterBean.getType());

        DBUtil.insertData(db, TABLE_NAME, values);
        DBUtil.release(null, db);
    }

    public List<FilterBean> getFilters() {
        SQLiteDatabase db = getWritableDatabase();
        List<FilterBean> list = new ArrayList<FilterBean>();

        Cursor cursor = DBUtil.selectData(db, TABLE_NAME, null);
        while (cursor.moveToNext()) {
            FilterBean bean = new FilterBean();
            bean.setId(cursor.getInt(0));
            bean.setTitle(cursor.getString(1));
            bean.setContent(cursor.getString(2));
            bean.setType(cursor.getInt(3));
            list.add(bean);
        }

        DBUtil.release(cursor, db);
        return list;
    }

    public void deleteFilter(int filterId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id=" + filterId;
        DBUtil.deleteData(db, TABLE_NAME, whereClause);
        DBUtil.release(null, db);
    }
}
