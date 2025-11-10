package com.calculator.calculator.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр математических функций
 */
public class FunctionRegistry {
    private final Map<String, MathFunction> functions;
    
    public FunctionRegistry() {
        functions = new HashMap<>();
        registerDefaultFunctions();
    }
    
    private void registerDefaultFunctions() {
        // Основные математические функции
        functions.put("sin", args -> {
            validateArgs("sin", args, 1);
            return Math.sin(args[0]);
        });
        
        functions.put("cos", args -> {
            validateArgs("cos", args, 1);
            return Math.cos(args[0]);
        });
        
        functions.put("tan", args -> {
            validateArgs("tan", args, 1);
            return Math.tan(args[0]);
        });
        
        functions.put("log", args -> {
            validateArgs("log", args, 1);
            if (args[0] <= 0) throw new IllegalArgumentException("Логарифм от неположительного числа");
            return Math.log(args[0]);
        });
        
        functions.put("log10", args -> {
            validateArgs("log10", args, 1);
            if (args[0] <= 0) throw new IllegalArgumentException("Логарифм от неположительного числа");
            return Math.log10(args[0]);
        });
        
        functions.put("sqrt", args -> {
            validateArgs("sqrt", args, 1);
            if (args[0] < 0) throw new IllegalArgumentException("Корень из отрицательного числа");
            return Math.sqrt(args[0]);
        });
        
        functions.put("abs", args -> {
            validateArgs("abs", args, 1);
            return Math.abs(args[0]);
        });
        
        functions.put("pow", args -> {
            validateArgs("pow", args, 2);
            return Math.pow(args[0], args[1]);
        });
        
        functions.put("max", args -> {
            validateArgs("max", args, 2);
            return Math.max(args[0], args[1]);
        });
        
        functions.put("min", args -> {
            validateArgs("min", args, 2);
            return Math.min(args[0], args[1]);
        });
    }
    
    private void validateArgs(String functionName, double[] args, int expectedCount) {
        if (args.length != expectedCount) {
            throw new IllegalArgumentException(
                String.format("Функция '%s' требует %d аргументов, получено %d", 
                            functionName, expectedCount, args.length)
            );
        }
    }
    
    /**
     * Регистрирует пользовательскую функцию
     * 
     * @param name имя функции
     * @param function реализация функции
     */
    public void registerFunction(String name, MathFunction function) {
        functions.put(name.toLowerCase(), function);
    }
    
    /**
     * Проверяет существование функции
     * 
     * @param name имя функции
     * @return true если функция существует
     */
    public boolean hasFunction(String name) {
        return functions.containsKey(name.toLowerCase());
    }
    
    /**
     * Выполняет функцию
     * 
     * @param name имя функции
     * @param args аргументы
     * @return результат вычисления
     */
    public double executeFunction(String name, double... args) {
        MathFunction function = functions.get(name.toLowerCase());
        if (function == null) {
            throw new IllegalArgumentException("Неизвестная функция: " + name);
        }
        return function.apply(args);
    }
}
