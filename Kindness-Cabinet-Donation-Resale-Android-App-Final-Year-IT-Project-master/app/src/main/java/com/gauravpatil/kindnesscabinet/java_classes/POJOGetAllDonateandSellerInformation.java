package com.gauravpatil.kindnesscabinet.java_classes;

public class POJOGetAllDonateandSellerInformation {
   String id,name,mobile_no,product_cat,product_image,product_name,paid_status,productrating,quantity,
           descrition,pickup_location,pickup_option,role;

    public POJOGetAllDonateandSellerInformation(String id, String name, String mobile_no, String product_cat,
                                                String product_image, String product_name, String paid_status,
                                                String productrating,
                                                String quantity, String descrition, String pickup_location,
                                                String pickup_option, String role) {
        this.id = id;
        this.name = name;
        this.mobile_no = mobile_no;
        this.product_cat = product_cat;
        this.product_image = product_image;
        this.product_name = product_name;
        this.paid_status = paid_status;
        this.productrating = productrating;
        this.quantity = quantity;
        this.descrition = descrition;
        this.pickup_location = pickup_location;
        this.pickup_option = pickup_option;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
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

    public String getDescrition() {
        return descrition;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
