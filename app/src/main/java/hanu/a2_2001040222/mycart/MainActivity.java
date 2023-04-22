package hanu.a2_2001040222.mycart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import hanu.a2_2001040222.mycart.adapters.ProductAdapter;
import hanu.a2_2001040222.mycart.db.CartController;
import hanu.a2_2001040222.mycart.models.Cart_Item;
import hanu.a2_2001040222.mycart.models.Product;
import hanu.a2_2001040222.mycart.models.Constants;

public class MainActivity extends AppCompatActivity {
    List <Product> products = new ArrayList <> ();


    ProductAdapter productAdapter;
    RecyclerView listProduct;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        listProduct = findViewById(R.id.rvItem);

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());



        //get name and price
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                String api = "https://hanu-congnv.github.io/mpr-cart-api/products.json";
                String data = getJSON(api);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(data == null) {
                            Toast.makeText(MainActivity.this, "cant get api", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object =jsonArray.getJSONObject(i);
                                    //get product id
                                    int productID = object.getInt("id");

                                    //get product name
                                    String nameProduct = object.getString("name");

                                    //get product price
                                    double priceProduct =Double.parseDouble(object.getString("unitPrice"));

                                    //get image link
                                    String thumbnail = object.getString("thumbnail");

                                    //add product into the product list
                                    products.add(new Product(nameProduct, productID, priceProduct, thumbnail));


                                    GridLayoutManager gridLayoutManager =new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
                                    listProduct.setLayoutManager(gridLayoutManager);
                                    productAdapter = new ProductAdapter(products);

                                    productAdapter.setData(products, new ProductAdapter.clickAddToCart() {
                                        @Override
                                        public void onClickAdd(ImageButton btnAddProduct, Product product, View view) {
                                            Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
                                            CartController cartManager = CartController.getInstance(view.getContext());
                                            boolean exist = false;

                                            for (Cart_Item item : cartManager.getAll()) {

                                                //if have same id => quantity + 1
                                                if(item.getProductId() == product.getId()){
                                                    //set quantity
                                                    item.setQuantity(item.getQuantity() + 1);

                                                    //change exist to true
                                                    exist= true;

                                                    //update in database
                                                    cartManager.update(item.getProductId(), item.getQuantity());

                                                }
                                            }
                                            //if did not exist in cart -> create first cart
                                            if (!exist) {
                                                Cart_Item cartItem = new Cart_Item(product.getName(),product.getPrice(),product.getId(),product.getImgUrl(), 1);
                                                cartManager.add(cartItem);
                                            }


                                        }
                                    });
                                    listProduct.setAdapter(productAdapter);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }
        });

        search();




    }

    public void search() {
        searchView = findViewById(R.id.viewSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Product> rs = new ArrayList<>();
                for (Product product : products) {
                    if(product.getName().toLowerCase().contains(newText.toLowerCase())){
                        rs.add(product);
                    }
                }
                productAdapter = new ProductAdapter(rs);
                listProduct.setAdapter(productAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    public String getJSON(String api) {
        URL url;
        HttpsURLConnection httpsURLConnection;
        String line;
        try {
            url = new URL(api);
            //open connection
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();

            //get api
            InputStream getApi = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(getApi);
            StringBuilder result = new StringBuilder();
            //using iterator to check
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                result.append(line);
            }
            return result.toString();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void changeToShoppingCart() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnCart) {
            changeToShoppingCart();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}