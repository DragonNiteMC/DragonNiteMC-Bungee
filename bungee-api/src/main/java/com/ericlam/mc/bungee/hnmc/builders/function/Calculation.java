package com.ericlam.mc.bungee.hnmc.builders.function;

/**
 * @see com.ericlam.mc.bungee.hnmc.builders.CalculationBuilder#doOther(Calculation)
 */
@FunctionalInterface
public interface Calculation {
    double cal(double result);
}
