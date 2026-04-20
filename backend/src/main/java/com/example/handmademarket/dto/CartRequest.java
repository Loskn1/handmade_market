package com.example.handmademarket.dto;

public class CartRequest {

    private Integer goodsId;
    private Integer quantity;
    private String spec;
    private String goodsImage;

    public Integer getGoodsId() { return goodsId; }
    public void setGoodsId(Integer goodsId) { this.goodsId = goodsId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }

    public String getGoodsImage() { return goodsImage; }
    public void setGoodsImage(String goodsImage) { this.goodsImage = goodsImage; }
}
