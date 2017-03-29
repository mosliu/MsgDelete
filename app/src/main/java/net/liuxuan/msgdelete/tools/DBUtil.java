package net.liuxuan.msgdelete.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对SQLite数据库增删改查的封装。
 * @author hanj
 *
 */
public class DBUtil {
	/**插入数据*/
	public static long insertData(SQLiteDatabase db, String tableName,ContentValues values) {
		return db.insert(tableName, null, values);
	}
	 
	/**删除数据*/
	public static int deleteData(SQLiteDatabase db,String tableName,String whereClause){
		return db.delete(tableName, whereClause, null);
	}
	
	/**更新数据*/
	public static int updateData(SQLiteDatabase db,String tableName,ContentValues values,String whereClause){
		return db.update(tableName, values, whereClause, null);
	}
	
	/**有条件查询*/
	public static Cursor selectData(SQLiteDatabase db,String tableName,String[] columns,String whereClause){  
		return db.query(tableName, columns, whereClause, null,null,null,null);
	}
	
	/**无条件查询*/
	public static Cursor selectData(SQLiteDatabase db,String tableName,String[] columns){
		return db.query(tableName, columns, null, null,null,null,null);
	}


    //释放资源
    public static void release(Cursor cursor, SQLiteDatabase db) {
        try {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (db != null && db.isOpen()) {
                    db.close();
                }
                db = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
//
//	/**
//	 * 直接运行sql语句执行数据插入操作。原始SQL示例：insert into students values(19,'han','tianjin',2013);
//	 * @param db
//	 * @param tableName
//	 * @param values_str 19,'han','tianjin',2013   &nbsp;&nbsp;  注意：_id字段需插入null值，即可保证_id自增长。
//
//	 * @note 使用此方法直接执行原始sql语句，相对于使用Android封装的方法具有更高的效率。
//	 */
//	public static void insertDataSQL(SQLiteDatabase db,String tableName,String str_values){
//		db.execSQL("insert into "+tableName+" values("+str_values+")");
//	}
//
//	/**
//	 * 直接运行sql语句执行删除数据操作。原始SQL语句示例：delete from student where age<18;
//	 * @param db
//	 * @param tableName
//	 * @param str_where age<18  &nbsp;&nbsp; 没有where语句时输入null
//	 */
//	public static void deleteDataSQL(SQLiteDatabase db,String tableName,String str_where){
//		String sql;
//		if(str_where==null){
//			sql="delete from "+tableName;
//		}else{
//			sql="delete from "+tableName+" where "+str_where;
//		}
//		db.execSQL(sql);
//	}
//
//	/**
//	 * 直接运行sql语句执行更新数据操作。原始SQL示例： update student set name='lisi',age=21 where _id=2;
//	 * @param tableName
//	 * @param db
//	 * @param str_set name='lisi',age=21
//	 * @param str_where _id=2 &nbsp;&nbsp; 没有where语句时输入null
//	 */
//	public static void updateDataSQL(String tableName,SQLiteDatabase db,
//			String str_set,String str_where){
//		String sql;
//		if(str_where==null){
//			sql="update "+tableName+" set "+str_set;
//		}else{
//			sql="update "+tableName+" set "+str_set+" where "+str_where;
//		}
//		db.execSQL(sql);
//	}
//
//	/**
//	 * 直接运行sql语句执行查询操作。原始SQL示例：select name,age from student where num>1;
//	 * @param db
//	 * @param tableName
//	 * @param str_selection  name,age   &nbsp;&nbsp;  查询全部数据时输入 *
//	 * @param str_where num>1  &nbsp;&nbsp;  没有where语句时输入null
//	 * @return 查询结果的Cursor对象
//	 */
//	public static Cursor selectDataSQL(SQLiteDatabase db,String tableName,
//			String str_selection,String str_where){
//		String sql;
//		if(str_where==null){
//			sql="select "+str_selection+" from "+tableName;
//		}else{
//			sql="select "+str_selection+" from "+tableName+" where "+str_where;
//		}
//		return db.rawQuery(sql, null);
//	}
	

}
