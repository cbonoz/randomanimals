package com.randomanimals.www.randomanimals.events;

import com.randomanimals.www.randomanimals.models.Animal;

import java.util.List;

/**
 * Created by cbuonocore on 3/11/17.
 */

public class LeaderEvent {

    public List<Animal> animals;

    @Override
    public String toString() {
        String res = "";
        for (Animal a : animals) {
            res += a.username + ": " + a.count  + "\n";
        }
        return res;
    }

    public LeaderEvent(List<Animal> animals) {
        this.animals = animals;
    }
}
