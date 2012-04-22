package com.testapp.app;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DataManipulator
{
    private static final  String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2;
    static final String TABLE_NAME = "newtable";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PICTURE = "picture";
    private static Context context;
    static SQLiteDatabase db;
    private SQLiteStatement insertStmt;
        
    private static final String INSERT = "insert into " + TABLE_NAME + " (name,picture) values (?,?)";
    public DataManipulator(Context context) {
        DataManipulator.context = context;
        OpenHelper openHelper = new OpenHelper(DataManipulator.context);
        DataManipulator.db = openHelper.getWritableDatabase();
        this.insertStmt = DataManipulator.db.compileStatement(INSERT);
    }
//    public long insert(String name,String number,String skypeId,byte[] bytes) {
//        this.insertStmt.bindString(1, name);
//        this.insertStmt.bindString(2, number);
//        this.insertStmt.bindString(3, skypeId);
//        this.insertStmt.bindBlob(4, bytes);
//        return this.insertStmt.executeInsert();
//    }
//    
//    
//    public long insert(String name,String number,String skypeId,String address) {
//        this.insertStmt.bindString(1, name);
//        this.insertStmt.bindString(2, number);
//        this.insertStmt.bindString(3, skypeId);
//        this.insertStmt.bindString(4, address);
//        return this.insertStmt.executeInsert();
//    }
//    
    public long insert(String name, byte[] picture) {
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindBlob(2, picture);
        return this.insertStmt.executeInsert();
    }
    
    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }
    public List<String[]> selectAll()
    {
        List<String[]> list = new ArrayList<String[]>();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id","name","picture" }, null, null, null, null, "name asc"); 
        int x=0;
        if (cursor.moveToFirst()) {
           do {
                String[] b1=new String[]{cursor.getString(0),cursor.getString(1),cursor.getBlob(2).toString()};//Have this be a dictionary
                list.add(b1);
                x=x+1;
                
           } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
           cursor.close();
        } 
        cursor.close();
        return list;
    }
    
    public List<Hashtable> selectHashtable()
    {
        List<Hashtable> list = new ArrayList<Hashtable>();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id","name","picture" }, null, null, null, null, "name asc"); 
        if (cursor.moveToFirst()) {
           do {
           	Hashtable values = new Hashtable();
           	values.put("id", cursor.getString(0));
           	values.put("name", cursor.getString(1));
           	values.put("picture", cursor.getBlob(2));
           	list.add(values);
           } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
           cursor.close();
        } 
        cursor.close();
        
        return list;
    }
    
    //---updates a title---
    public boolean update(long rowId, String name) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        return db.update(TABLE_NAME, args, 
                         KEY_ID + "=" + rowId, null) > 0;
    }
    
    
   public void delete(int rowId) {
        db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null); 
   }
   
   private static class OpenHelper extends SQLiteOpenHelper {
        OpenHelper(Context context) {
             super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
             db.execSQL("CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY, name TEXT, picture BLOB)");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
             db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
             onCreate(db);
        }
   }

}