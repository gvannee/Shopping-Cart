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
import hanu.a2_2001040222.mycart.db.CartController;
import hanu.a2_2001040222.mycart.models.Cart_Item;
import hanu.a2_2001040222.mycart.models.Constants;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    List<Cart_Item> item;
    private Context context;

    ItemsChangeListener listener;

    public CartAdapter(List<Cart_Item> item, ItemsChangeListener listener) {
        this.item = item;
        this.listener = listener;
        notifyDataSetChanged();
    }


//    void setOnItemClickListener(OnItemsClickListener listener){
//        this.listener = listener;
//    }



    //view holder
    protected  class CartHolder extends RecyclerView.ViewHolder  {

        int productId;
        String name;
        double price;
        int quantity;
        long finalPrice = 0 ;
        CartController managerCart;

        ItemsChangeListener listener;

        //get ref
        ImageButton btnAdd = itemView.findViewById(R.id.btnAdd);
        ImageButton btnDelete = itemView.findViewById(R.id.btnDelete);
        TextView tvName = itemView.findViewById(R.id.tvCartName);
        TextView tvPrice = itemView.findViewById(R.id.tvCartPrice);
        TextView tvSumPrice = itemView.findViewById(R.id.tvSumPrice);
        TextView tvQuantity = itemView.findViewById(R.id.tvQuantity);
        ImageView imgThumbnail = itemView.findViewById(R.id.imgItemCart);



        public CartHolder(@NonNull View itemView, ItemsChangeListener listener) {
            super(itemView);
            this.listener = listener;

        }

        public void bind(Cart_Item cartItem) {
            //get product id
             productId = cartItem.getProductId();

             //get product name
             name = cartItem.getName();

             //get product price
             price = cartItem.getPrice();

             //calculate total price
             double priceSum = cartItem.getPrice() * cartItem.getQuantity();

             //get quantity
             quantity = cartItem.getQuantity();

            managerCart = CartController.getInstance(itemView.getContext());

            //handle click event for add button
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //increase the quality
                    cartItem.setQuantity(cartItem.getQuantity()+1);

                    //update database
                    managerCart.update(cartItem.getProductId(), cartItem.getQuantity());

                    //set value to display quantity
                    tvQuantity.setText(String.valueOf(quantity));

                    notifyDataSetChanged();
                    finalPrice = 0;

                    //final price for all product
                    for (Cart_Item cart : managerCart.getAll()) {
                        finalPrice = (long) ( finalPrice + cart.getPrice() * cart.getQuantity());
                    }

                    //handle click
                    listener.onItemClick(finalPrice);
                }
            });

            //handle click event when click decrease button
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //decrease the quantity
                    cartItem.setQuantity(cartItem.getQuantity()-1);

                    //update in database
                    managerCart.update(cartItem.getProductId(), cartItem.getQuantity());

                    //set text to display quantity
                    tvQuantity.setText(String.valueOf(quantity));

                    //delete in database
                    if (cartItem.getQuantity() == 0 ) {
                        managerCart.delete(cartItem.getId());

                        //remove
                        item.remove(cartItem);
                    }

                    notifyDataSetChanged();
                    finalPrice = 0;
                    for (Cart_Item cart : managerCart.getAll()) {
                        finalPrice = (long) ( finalPrice + cart.getPrice() * cart.getQuantity());
                    }

                    //handle click event
                    listener.onItemClick(finalPrice);
                }

            });

            //set image url
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    //using bitmap to handle image
                    Bitmap bm = loadImg(cartItem.getThumbnail());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //set url for image
                            imgThumbnail.setImageBitmap(bm);
                        }
                    });
                }
            });
                //set name
                tvName.setText(name);

                //set price
                tvPrice.setText("VND " +  (long)price);

                //set sum price for product
                tvSumPrice.setText("VND " + (long)priceSum );

                //set quantity
                tvQuantity.setText(String.valueOf(quantity));


        }

    }

    public interface ItemsChangeListener{
        void onItemClick(long totalPrice);
    }

    //data set
    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_cart,  parent, false);
        return new CartHolder(item,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {

        Cart_Item cartItem = item.get(position);

        holder.bind(cartItem);

    }



    public Bitmap loadImg(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream a = httpURLConnection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(a);
            return  bm;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


}


