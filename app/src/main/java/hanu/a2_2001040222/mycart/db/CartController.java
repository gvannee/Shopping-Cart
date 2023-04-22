package hanu.a2_2001040222.mycart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040222.mycart.models.Cart_Item;

public class CartController {
    private static  CartController instance;

    private static final String DB_INSERT = "INSERT INTO ListItem"
            + "(productId,name,price,thumbnail,quantity)VALUES(?,?,?,?,?)";
    private SQLiteDatabase db;
    private ItemHelper itemHelper;

    public static CartController getInstance(Context context) {
        if(instance == null) {
            instance = new CartController(context);
        }
        return instance;
    }

    public CartController(Context context) {
        itemHelper = new ItemHelper(context);

        //set database to writable
        db =  itemHelper.getWritableDatabase();

    }

    //get all item
    public List<Cart_Item> getAll() {
        //query to select all data
        Cursor cursor = db.rawQuery("SELECT * FROM ListItem",null);

        CartCursorWrapper cartCursorWrapper = new CartCursorWrapper(cursor);

        return  cartCursorWrapper.getCart();
    }

    public boolean delete (Long id) {
        int rs = db.delete(ItemSchema.ShoppingCartTable.NAME, "id=?", new String[]{id+""});

        //return true
        return rs > 0;
    }

    //update db
    public void update (int productId, int quantity) {
        ContentValues contentValues = new ContentValues();

        //change quantity
        contentValues.put("quantity", quantity);

        //update product id
        db.update(ItemSchema.ShoppingCartTable.NAME,contentValues, "productId = ?", new String[]{productId+""});
    }

    public boolean add (Cart_Item cartItem) {
        SQLiteStatement stm = db.compileStatement(DB_INSERT);

        //bind column 1
        stm.bindLong(1,cartItem.getProductId());

        //bind column 2
        stm.bindString(2,cartItem.getName());

        //bind column 3
        stm.bindDouble(3,cartItem.getPrice());

        //bind column 4
        stm.bindString(4,cartItem.getThumbnail());

        //bind column 5
        stm.bindLong(5,cartItem.getQuantity());

        long id = stm.executeInsert();


        if (id > 0 ) {
            cartItem.setId(id);
            return  true;
        }
        return  false;
    }
}
