package com.gauravpatil.kindnesscabinet.Favorites;

public class POJOGetAllFavourite {
   String id,username,product_category,product_image, product_name,product_cost,product_rating,product_quantity,
           product_description,product_location,product_pickup_option,product_date_and_time,role;

    public POJOGetAllFavourite(String id, String username, String product_category, String product_image, String product_name, String product_cost, String product_rating, String product_quantity, String product_description, String product_location, String product_pickup_option, String product_date_and_time, String role) {
        this.id = id;
        this.username = username;
        this.product_category = product_category;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_cost = product_cost;
        this.product_rating = product_rating;
        this.product_quantity = product_quantity;
        this.product_description = product_description;
        this.product_location = product_location;
        this.product_pickup_option = product_pickup_option;
        this.product_date_and_time = product_date_and_time;
        this.role = role;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
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

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(String product_rating) {
        this.product_rating = product_rating;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_location() {
        return product_location;
    }

    public void setProduct_location(String product_location) {
        this.product_location = product_location;
    }

    public String getProduct_pickup_option() {
        return product_pickup_option;
    }

    public void setProduct_pickup_option(String product_pickup_option) {
        this.product_pickup_option = product_pickup_option;
    }

    public String getProduct_date_and_time() {
        return product_date_and_time;
    }

    public void setProduct_date_and_time(String product_date_and_time) {
        this.product_date_and_time = product_date_and_time;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
