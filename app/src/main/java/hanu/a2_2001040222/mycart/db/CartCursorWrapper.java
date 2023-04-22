package hanu.a2_2001040222.mycart.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040222.mycart.models.Cart_Item;

public class CartCursorWrapper extends CursorWrapper {

    public CartCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Cart_Item getItem() {
        //get from id column
        long id = getLong(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.ID));

        //get from product id column
        int productId = getInt(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.PRODUCT_ID));

        //get from name column
        String name = getString(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.NAME));

        //get from price column
        double price = getDouble(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.PRICE));

        //get from url column
        String url = getString(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.THUMBNAIL));

        //get from quantity column
        int quantity = getInt(getColumnIndex(ItemSchema.ShoppingCartTable.Columns.QUANTITY));

        //create product
        Cart_Item product = new Cart_Item(id, name, price, productId, url,quantity);


        //return product
        return product;
    }

    public List<Cart_Item> getCart () {
        List<Cart_Item> cart = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            Cart_Item item = getItem();

            //add into an array
            cart.add(item);
            moveToNext();
        }
        return  cart;
    }
}
