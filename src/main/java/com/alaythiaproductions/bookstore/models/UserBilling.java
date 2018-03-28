package com.alaythiaproductions.bookstore.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    

}
