package hanu.a2_2001040222.mycart.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_2001040222.mycart.R;
import hanu.a2_2001040222.mycart.models.Product;
import hanu.a2_2001040222.mycart.models.Constants;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    List<Product> products;

    private clickAddToCart clickAddToCart;


    public ProductAdapter(List<Product> products) {
        this.products = products;

    }

    public void setData(List<Product> list, clickAddToCart listener) {
        this.products = list;
        this.clickAddToCart = listener;
        notifyDataSetChanged();
    }


    public interface clickAddToCart{
        void onClickAdd(ImageButton btnAddProduct, Product product, View view);
    }



    public class ProductHolder extends RecyclerView.ViewHolder {
        private ImageView imgThumbnail;
        private TextView nameProduct;
        private TextView priceProduct;
        private ImageButton btnAddToCart;


        public ProductHolder(@NonNull View itemView) {

            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgProduct);
            nameProduct = itemView.findViewById(R.id.tvName);
            priceProduct = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddCart);

        }

//        public void bind(Product product) {
//
//            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
//
//            Thread.executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    Bitmap bitmap = loadImg(product.getImgUrl());
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                        imgThumbnail.setImageBitmap(bitmap);
//                        }
//                    });
//                }
//            });
//
//            nameProduct.setText(product.getName());
//            priceProduct.setText("VND " + product.getPrice());
//
//        }

        public Bitmap loadImg(String imgUrl) {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                //hold raw data
                InputStream link = httpURLConnection.getInputStream();

                //decoded bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(link);
                return bitmap;

            }catch (MalformedURLException e) {
                //print stack trace of the errors
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        //take layout
        LayoutInflater inflater = LayoutInflater.from(context);

        //set layout
        View item = inflater.inflate(R.layout.item_product, parent, false);

        //return layout
        return new ProductHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = holder.loadImg(product.getImgUrl());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.imgThumbnail.setImageBitmap(bitmap);
                    }
                });
            }
        });

        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText("VND " + product.getPrice());
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddToCart.onClickAdd(holder.btnAddToCart,product, view);

            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}
