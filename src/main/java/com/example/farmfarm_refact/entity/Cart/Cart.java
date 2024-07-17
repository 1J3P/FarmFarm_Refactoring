package com.example.farmfarm_refact.entity.Cart;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Cart {
    private List<Item> itemList = new ArrayList<>();
    public List<Item> getItemList() {
        return itemList;
    }
    public void push(Item item) {
        for (Item i: itemList) {
            if (item.getProduct() == i.getProduct()) {
                i.setQuantity(i.getQuantity() + item.getQuantity());
                return;
            }
        }
        itemList.add(item);
    }

    public void delete(Long pId) {
        for (Item i : itemList) {
            if (i.getProduct().getPId() == pId) {
                itemList.remove(i);
                return;
            }
        }
    }
}
