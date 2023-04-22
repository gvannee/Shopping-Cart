package hanu.a2_2001040222.mycart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hanu.a2_2001040222.mycart.adapters.CartAdapter;
import hanu.a2_2001040222.mycart.db.CartController;
import hanu.a2_2001040222.mycart.models.Cart_Item;

public class CartActivity extends AppCompatActivity implements CartAdapter.ItemsChangeListener{
    public static final int CART_ADDED = 1;

    private RecyclerView rvCart;

    private List<Cart_Item> items;

    private CartAdapter cartAdapter;

    private CartController cartManager;

    long finalPrice= 0 ;

    int count =0;

    TextView tvCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        //data set
        cartManager = CartController.getInstance(CartActivity.this);

        //get all carts
        items = cartManager.getAll();


        tvCheckout = findViewById(R.id.tvPrice);

        //adapter
        cartAdapter = new CartAdapter(items,this);

        //recycleview
        rvCart = findViewById(R.id.rvCart);
        cartAdapter.notifyDataSetChanged();


        for (Cart_Item item : items) {

            finalPrice = (long) ( finalPrice + item.getPrice() * item.getQuantity());

        }

        //set final price
        tvCheckout.setText("VND" + finalPrice);

        //set adapter
        GridLayoutManager gridLayoutManager =new GridLayoutManager(CartActivity.this, 1, GridLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(gridLayoutManager);
        rvCart.setAdapter(cartAdapter);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CART_ADDED) {
            items.clear();

            //add all cart
            items.addAll(cartManager.getAll());
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(long totalPrice) {

        tvCheckout.setText("VND "+totalPrice);
    }
}


