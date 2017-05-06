package com.randomanimals.www.randomanimals.services;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class NumberUtil {

    private NumberUtil() {
    }

    private static WeightedCollection<Integer> initBonusDist() {
        WeightedCollection<Integer> bonusCollection = new WeightedCollection<>();
        bonusCollection.add(6, 1);
        bonusCollection.add(2, 2);
        bonusCollection.add(1, 5);
        bonusCollection.add(1, 10);
        return  bonusCollection;
    }

    private static final WeightedCollection<Integer> bonusCollection = initBonusDist();

    private static final NumberFormat numberFormat
            = NumberFormat.getNumberInstance(Locale.getDefault());

    public static String getCountStringFromCount(int count) {
        final String countString;
        String numberAsString = numberFormat.format(count);
        if (count > 1) {
            countString = numberAsString + " points";
        } else {
            countString = numberAsString + " point";
        }
        return countString;
    }

    public static int getRandomIndex(int n) {
        Random rand = new Random();
        if (n > 0) {
            return rand.nextInt(n);
        } else {
            return 0;
        }
    }

    public static int getRandomNumberOfRotations() {
        Random rand = new Random();
        return rand.nextInt(10) + 5;
    }

    public static Integer getNextBonus() {
        return bonusCollection.next();
    }

    private static class WeightedCollection<E> {

        private NavigableMap<Integer, E> map = new TreeMap<Integer, E>();
        private Random random;
        private int total = 0;

        public WeightedCollection() {
            this(new Random());
        }

        public WeightedCollection(Random random) {
            this.random = random;
        }

        public void add(int weight, E object) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, object);
        }

        public E next() {
            int value = random.nextInt(total) + 1; // Can also use floating-point weights
            return map.ceilingEntry(value).getValue();
        }

    }

}
