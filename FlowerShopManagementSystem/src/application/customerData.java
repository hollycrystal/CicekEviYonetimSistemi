package application;

import java.sql.Date;

public class customerData {
    private Integer customerID;
    private Integer flowerID;
    private String name;
    private Integer quantity;
    private Double price;
    private Date date;

    public customerData(Integer customerID, Integer flowerID, String name,
                        Integer quantity, Double price, Date date) {
        this.customerID = customerID;
        this.flowerID = flowerID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public Integer getFlowerID() {
        return flowerID;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
}
