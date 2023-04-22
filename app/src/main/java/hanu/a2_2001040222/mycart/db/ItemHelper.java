package hanu.a2_2001040222.mycart.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

public class ItemHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "listItem.db";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "ListItem";

    public ItemHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String ex =  "CREATE TABLE " + TABLE_NAME + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "productId INTEGER NOT NULL,"
                + "name TEXT NOT NULL,"
                + "price DOUBLE NOT NULL,"
                + "thumbnail TEXT NOT NULL,"
                + "quantity INTEGER" + ")";

        sqLiteDatabase.execSQL(ex);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        //query use to delete table
        String ex = "DROP TABLE " + TABLE_NAME;
        sqLiteDatabase.execSQL(ex);

        //create new table
        onCreate(sqLiteDatabase);
    }
}
