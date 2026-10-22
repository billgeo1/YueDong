package com.xiangmu.agent;

public class AgentProductView {
    private int id;
    private int leibieId;
    private String title;
    private String description;
    private String image;
    private int price;
    private int stock;
    private String categoryName;
    private int sales;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeibieId() {
        return leibieId;
    }

    public void setLeibieId(int leibieId) {
        this.leibieId = leibieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getDetailUrl() {
        return "/view/product/productDetailQian.html?id=" + id;
    }
}
