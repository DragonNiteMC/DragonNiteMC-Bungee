package com.ericlam.mc.bungee.dnmc.function;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResultParser {
    private boolean result;

    private ResultParser(Supplier<Boolean> result) {
        this.result = result.get();
    }


    public static ResultParser check(Supplier<Boolean> result) {
        return new ResultParser(result);
    }

    public ResultParser execute(Consumer<Boolean> resultRunner) {
        resultRunner.accept(result);
        return this;
    }

    public ResultParser ifTrue(Runnable runnable) {
        if (result) runnable.run();
        return this;
    }

    public ResultParser ifFalse(Runnable runnable) {
        if (!result) runnable.run();
        return this;
    }

    public boolean getResult() {
        return result;
    }
}
