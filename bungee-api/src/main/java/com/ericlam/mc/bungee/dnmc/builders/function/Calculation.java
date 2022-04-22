package com.ericlam.mc.bungee.dnmc.builders.function;

/**
 * @see com.ericlam.mc.bungee.dnmc.builders.CalculationBuilder#doOther(Calculation)
 */
@FunctionalInterface
public interface Calculation {
    double cal(double result);
}
