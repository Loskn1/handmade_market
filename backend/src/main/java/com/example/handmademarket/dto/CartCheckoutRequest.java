package com.example.handmademarket.dto;

import java.util.List;

public class CartCheckoutRequest {

    private List<Integer> cartIds;
    private String deliveryAddress;
    private String payType;
    private String remark;

    public List<Integer> getCartIds() { return cartIds; }
    public void setCartIds(List<Integer> cartIds) { this.cartIds = cartIds; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPayType() { return payType; }
    public void setPayType(String payType) { this.payType = payType; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
