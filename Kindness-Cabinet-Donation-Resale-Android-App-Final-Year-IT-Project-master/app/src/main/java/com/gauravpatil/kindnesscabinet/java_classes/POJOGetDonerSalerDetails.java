package com.gauravpatil.kindnesscabinet.java_classes;

public class POJOGetDonerSalerDetails {
    // POJO => Plain old java object
    // reusebility
    // POJO class multiple data get and set

    String id;
    String product_cat;
    String product_image;
    String product_name;
    String paid_status;
    String productrating;
    String quantity;
    String description;
    String pickup_location;
    String pickup_option;

    public POJOGetDonerSalerDetails(String id, String pickup_option, String pickup_location, String description, String quantity, String paid_status, String productrating, String product_name, String product_image, String product_cat) {
        this.id = id;
        this.pickup_option = pickup_option;
        this.pickup_location = pickup_location;
        this.description = description;
        this.quantity = quantity;
        this.paid_status = paid_status;
        this.productrating = productrating;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_cat = product_cat;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getProduct_cat() {
        return product_cat;
    }

    public void setProduct_cat(String product_cat) {
        this.product_cat = product_cat;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getProductrating() {
        return productrating;
    }

    public void setProductrating(String productrating) {
        this.productrating = productrating;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getPickup_option() {
        return pickup_option;
    }

    public void setPickup_option(String pickup_option) {
        this.pickup_option = pickup_option;
    }





}
