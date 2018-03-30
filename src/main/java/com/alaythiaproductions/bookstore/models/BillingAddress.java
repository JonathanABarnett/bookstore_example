package com.alaythiaproductions.bookstore.models;

import javax.persistence.*;

@Entity
public class BillingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String billingAddressName;
    private String billingAddressStreet1;
    private String billingAddressStreet2;
    private String billingAddressCity;
    private String billingAddressState;
    private String billingAddressCountry = "USA";
    private String billingAddressZipcode;

    @OneToOne
    private Order order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getbillingAddressName() {
        return billingAddressName;
    }

    public void setbillingAddressName(String billingAddressName) {
        this.billingAddressName = billingAddressName;
    }

    public String getbillingAddressStreet1() {
        return billingAddressStreet1;
    }

    public void setbillingAddressStreet1(String billingAddressStreet1) {
        this.billingAddressStreet1 = billingAddressStreet1;
    }

    public String getbillingAddressStreet2() {
        return billingAddressStreet2;
    }

    public void setbillingAddressStreet2(String billingAddressStreet2) {
        this.billingAddressStreet2 = billingAddressStreet2;
    }

    public String getbillingAddressCity() {
        return billingAddressCity;
    }

    public void setbillingAddressCity(String billingAddressCity) {
        this.billingAddressCity = billingAddressCity;
    }

    public String getbillingAddressState() {
        return billingAddressState;
    }

    public void setbillingAddressState(String billingAddressState) {
        this.billingAddressState = billingAddressState;
    }

    public String getbillingAddressCountry() {
        return billingAddressCountry;
    }

    public void setbillingAddressCountry(String billingAddressCountry) {
        this.billingAddressCountry = billingAddressCountry;
    }

    public String getbillingAddressZipcode() {
        return billingAddressZipcode;
    }

    public void setbillingAddressZipcode(String billingAddressZipcode) {
        this.billingAddressZipcode = billingAddressZipcode;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
