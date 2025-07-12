package com.github.liamsh.BetterArmedBedwars.utils;

public class Curves {
    public static float sigmoid(float x) {
        return 1.0F / (1.0F + (float)Math.exp(-x));
    }
}
