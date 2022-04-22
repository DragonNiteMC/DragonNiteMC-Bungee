package com.ericlam.mc.bungee.dnmc.builders;

import com.ericlam.mc.bungee.dnmc.builders.function.Calculation;

/**
 * 計算機
 */
public class CalculationBuilder {
    private double result;

    public CalculationBuilder(double result) {
        this.result = result;
    }

    /**
     * @param add +
     * @return this
     */
    public CalculationBuilder add(double add) {
        this.result += add;
        return this;
    }

    /**
     * @param minus -
     * @return this
     */
    public CalculationBuilder minus(double minus) {
        this.result -= minus;
        return this;
    }

    /**
     * @param multiply *
     * @return this
     */
    public CalculationBuilder multiply(double multiply) {
        this.result *= multiply;
        return this;
    }

    /**
     * @param divide /
     * @return this
     */
    public CalculationBuilder divide(double divide) {
        this.result /= divide;
        return this;
    }

    /**
     * @param pow ^
     * @return this
     */
    public CalculationBuilder pow(double pow) {
        this.result = Math.pow(this.result, pow);
        return this;
    }

    /**
     * @return √x
     */
    public CalculationBuilder sqrt() {
        this.result = Math.sqrt(this.result);
        return this;
    }

    /**
     * @return *3.14
     */
    public CalculationBuilder pi() {
        this.result *= Math.PI;
        return this;
    }

    /**
     * @return ⌊x⌋
     */
    public CalculationBuilder roundDown() {
        this.result = Math.floor(this.result);
        return this;
    }

    /**
     * @return 四捨五入
     */
    public CalculationBuilder round() {
        this.result = Math.round(this.result);
        return this;
    }

    /**
     * @return ⌈x⌉
     */
    public CalculationBuilder roundUp() {
        this.result = Math.ceil(this.result);
        return this;
    }

    /**
     * @param calculate 計算函式
     * @return this
     * @see Calculation
     */
    public CalculationBuilder doOther(Calculation calculate) {
        this.result = calculate.cal(this.result);
        return this;
    }

    /**
     * @return 計算結果
     */
    public double getResult() {
        return result;
    }
}
// 如何使用
/*
class CalculationUse{
    double result = new CalculationBuilder(5).add(7).divide(6).minus(2).multiply(10).pow(2).round().getResult();
    // ((((5 + 7) / 6 ) - 2 ) * 10 ) ^ 2
}

 */
