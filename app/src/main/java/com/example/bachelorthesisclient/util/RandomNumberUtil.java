package com.example.bachelorthesisclient.util;

import java.util.Random;

public class RandomNumberUtil {
    private Random random;
    private final int upperBound = 10000;

    private static RandomNumberUtil instance;

    public static RandomNumberUtil getInstance() {
        if (instance == null) {
            instance = new RandomNumberUtil();
        }

        return instance;
    }

    private RandomNumberUtil() {
        this.random = new Random();
    }

    public int getRandomNumber() {
        return this.random.nextInt(upperBound);
    }
}
