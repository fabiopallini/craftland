package com.craftland.engine.core;

public class Percentage
{
    public static double of(double per_cent, double of)
    {
        double result = (per_cent / 100) * of;
        result = (double) Math.round(result * 100.0) / 100.0;
        return result;
    }

    public static double compare(double value, double maxValue) {
        return (value * 100) / maxValue;
    }
}
