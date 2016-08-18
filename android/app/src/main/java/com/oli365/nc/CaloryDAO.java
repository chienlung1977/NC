package com.oli365.nc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvinlin on 2016/5/6.
 */
public class CaloryDAO {


    // 表格名稱
    public static final String TABLE_NAME = "CALORY";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String DATETIME_COLUMN = "CREATE_TIME";
    public static final String BREAKFAST_COLUMN = "BREAKFAST";
    public static final String LUNCH_COLUMN = "LUNCH";
    public static final String DINNER_COLUMN = "DINNER";
    public static final String DESSERT_COLUMN = "DESSERT";
    public static final String SPORT_COLUMN = "SPORT";
    public static final String MEMO_COLUMN = "MEMO";
    // public static final String LASTMODIFY_COLUMN = "lastmodify";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    BREAKFAST_COLUMN + " INTEGER , " +
                    LUNCH_COLUMN + " INTEGER ," +
                    DINNER_COLUMN + " INTEGER ," +
                    DESSERT_COLUMN + " INTEGER," +
                    SPORT_COLUMN + " INTEGER," +
                    MEMO_COLUMN + " TEXT )";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public CaloryDAO(Context context) {
        db = DbHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    // 新增參數指定的物件
    public Calory insert(Calory item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(DATETIME_COLUMN, item.getCreatedate());
        cv.put(BREAKFAST_COLUMN, item.getBreakfast());
        cv.put(LUNCH_COLUMN, item.getLunch());
        cv.put(DINNER_COLUMN, item.getDinner());
        cv.put(DESSERT_COLUMN, item.getDessert());
        cv.put(SPORT_COLUMN, item.getSport());
        cv.put(MEMO_COLUMN, item.getMemo());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }

    public boolean delete(long id ){
        return db.delete(TABLE_NAME,"_id=" + id,null)>0;
    }

    public boolean update(Calory item){

        ContentValues cv = new ContentValues();
        return  db.update(TABLE_NAME,cv,"_id=" + item.getId(),null)>0;
    }


    // 讀取所有記事資料
    public List<Calory> getAll() {
        List<Calory> result = new ArrayList<Calory>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getCalory(cursor));
        }

        cursor.close();
        return result;
    }

    //取得今天往前推30天的記錄
    public List<Calory> getLatest30(){

        List<Calory> result = new ArrayList<Calory>();

        //Log.i(TAG,)
        String sql ="SELECT * FROM " + TABLE_NAME + " WHERE CREATE_TIME>date('now','-30 day') ORDER BY CREATE_TIME DESC";
        Cursor cursor = db.rawQuery(sql,null);

        while (cursor.moveToNext()) {
            result.add(getCalory(cursor));
        }

        cursor.close();
        return result;

    }

    // 取得指定編號的資料物件
    public Calory get(long id) {
        // 準備回傳結果用的物件
        Calory item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getCalory(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }


    // 把Cursor目前的資料包裝為物件
    public Calory getCalory(Cursor cursor) {
        // 準備回傳結果用的物件
        Calory result = new Calory();

        result.setId(cursor.getLong(0));
        result.setCreatedate(cursor.getString(1));
        result.setBreakfast(cursor.getInt(2));
        result.setLunch(cursor.getInt(3));
        result.setDinner(cursor.getInt(4));
        result.setDessert(cursor.getInt(5));
        result.setSport(cursor.getInt(6));
        result.setMemo(cursor.getString(7));
        //result.setLastModify(cursor.getLong(8));

        // 回傳結果
        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }


}
