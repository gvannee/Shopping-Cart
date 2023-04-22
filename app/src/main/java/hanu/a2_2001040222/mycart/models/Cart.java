package hanu.a2_2001040222.mycart.models;

import java.util.List;

public class Cart {
    private List<Cart_Item> list;

    public Cart(List<Cart_Item> list) {
        this.list = list;
    }

    public List<Cart_Item> getList() {
        return list;
    }

    public void setList(List<Cart_Item> list) {
        this.list = list;
    }
}
