package hanu.a2_2001040222.mycart.models;

import java.util.Objects;

public class Cart_Item {
    private Long id;
    private String name;
    private double price;
    private int productId;
    private String thumbnail;
    private int quantity;

    public Cart_Item(String name, double price, int productId, String thumbnail, int quantity) {
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
    }

    public Cart_Item(Long id, String name, double price, int productId, String thumbnail, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public boolean equals(Object o) {
//        if (this == o){
//            return true;
//        }
//
//        if (o == null || getClass() != o.getClass()){
//            return false;
//        }
//
//        Cart_Item cartItem = (Cart_Item) o;
//        return Objects.equals(id, cartItem.id)
//                && productId == cartItem.productId
//                && quantity == cartItem.quantity
//                && Objects.equals(name, cartItem.name)
//                && Objects.equals(price, cartItem.price)
//                && Objects.equals(thumbnail, cartItem.thumbnail);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, productId, name, price, thumbnail, quantity);
//    }
}

