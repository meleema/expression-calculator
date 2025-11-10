package com.calculator.calculator.functions;

/**
 * Интерфейс для математических функций
 */
@FunctionalInterface
public interface MathFunction {
    /**
     * Вычисляет значение функции
     * 
     * @param args аргументы функции
     * @return результат вычисления
     */
    double apply(double... args);
}
