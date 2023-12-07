package com.afdul.belajar.springboot.learningmanagementsystem.auth.util;

import java.util.Random;

public class ActivationCodeGenerator {
    public static int generateActivationCode() {
        // Generate a random 6-digit number
        int min = 100000;
        int max = 999999;
        return new Random().nextInt((max - min) + 1) + min;
    }
}
