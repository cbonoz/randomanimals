package com.randomanimals.www.randomanimals.models;

import java.util.Date;

/**
 * Created by cbuonocore on 3/11/17.
 */

public class Animal {

    public String userid;
    public String username;
    public String animal;
    public int count = 0;
    public Date created;

    public Animal(String userid, String username, String animal, int count, Date created) {
        this.userid = userid;
        this.username = username;
        this.animal = animal;
        this.count = count;
        this.created = created;
    }

//    public String printUserCount() {
//        return username + ": " + count + " times";
//    }
//
//    public String printAnimalCount() {
//        return animal + ": " + count + " times";
//    }
}
