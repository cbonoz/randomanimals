package com.randomanimals.www.randomanimals.events;

import com.randomanimals.www.randomanimals.models.Animal;

import java.util.List;

/**
 * Created by cbuonocore on 3/11/17.
 */

public class ProfileEvent {

    public List<Animal> animals;
    public String username;

    public ProfileEvent() {
        animals = null;
    }

    public ProfileEvent(List<Animal> animals, String username) {
        this.animals = animals;
        this.username = username;
    }

    @Override
    public String toString() {
        String res = "";
        for (Animal a : animals) {
            res += a.animal + ": " + a.count  + "\n";
        }
        return res;
    }
}
