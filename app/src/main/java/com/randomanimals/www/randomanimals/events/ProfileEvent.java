package com.randomanimals.www.randomanimals.events;

import com.randomanimals.www.randomanimals.models.Animal;

import java.util.List;

/**
 * Created by cbuonocore on 3/11/17.
 */

public class ProfileEvent {

    public List<Animal> animals;

    @Override
    public String toString() {
        String res = "";
        for (Animal a : animals) {
            res += a.animal + ": " + a.count  + "\n";
        }
        return res;
    }

    public ProfileEvent(List<Animal> animals) {
        this.animals = animals;
    }
}
