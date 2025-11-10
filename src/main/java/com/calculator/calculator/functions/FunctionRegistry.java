package com.calculator.calculator.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр математических функций для калькулятора.
 * Предоставляет механизм регистрации и выполнения математических функций.
 * Содержит предустановленный набор основных математических функций.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
public class FunctionRegistry {
    private final Map<String, MathFunction> functions;
    
    /**
     * Конструктор по умолчанию. Инициализирует реестр и регистрирует стандартные функции.
     */
    public FunctionRegistry() {
        functions = new HashMap<>();
        registerDefaultFunctions();
    }
    
    /**
     * Регистрирует стандартный набор математических функций.
     * Включает тригонометрические, логарифмические и другие основные функции.
     */
    private void registerDefaultFunctions() {
        // Тригонометрические функции
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
        
        // Логарифмические функции
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
        
        // Алгебраические функции
        functions.put("sqrt", args -> {
            validateArgs("sqrt", args, 1);
            if (args[0] < 0) throw new IllegalArgumentException("Корень из отрицательного числа");
            return Math.sqrt(args[0]);
        });
        
        functions.put("abs", args -> {
            validateArgs("abs", args, 1);
            return Math.abs(args[0]);
        });
        
        // Функции для работы с числами
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
    
    /**
     * Проверяет корректность количества аргументов для функции.
     *
     * @param functionName имя функции для сообщения об ошибке
     * @param args массив аргументов функции
     * @param expectedCount ожидаемое количество аргументов
     * @throws IllegalArgumentException если количество аргументов не соответствует ожидаемому
     */
    private void validateArgs(String functionName, double[] args, int expectedCount) {
        if (args.length != expectedCount) {
            throw new IllegalArgumentException(
                String.format("Функция '%s' требует %d аргументов, получено %d", 
                            functionName, expectedCount, args.length)
            );
        }
    }
    
    /**
     * Регистрирует пользовательскую функцию в реестре.
     * Имя функции приводится к нижнему регистру для регистронезависимого доступа.
     *
     * @param name имя функции для регистрации
     * @param function реализация функции в виде лямбда-выражения или объекта MathFunction
     */
    public void registerFunction(String name, MathFunction function) {
        functions.put(name.toLowerCase(), function);
    }
    
    /**
     * Проверяет существование функции в реестре.
     * Поиск осуществляется без учета регистра.
     *
     * @param name имя функции для проверки
     * @return true если функция зарегистрирована, false в противном случае
     */
    public boolean hasFunction(String name) {
        return functions.containsKey(name.toLowerCase());
    }
    
    /**
     * Выполняет математическую функцию с заданными аргументами.
     * Перед выполнением проверяет существование функции и корректность аргументов.
     *
     * @param name имя выполняемой функции
     * @param args аргументы функции в виде переменного числа параметров типа double
     * @return результат вычисления функции
     * @throws IllegalArgumentException если функция не найдена или аргументы некорректны
     */
    public double executeFunction(String name, double... args) {
        MathFunction function = functions.get(name.toLowerCase());
        if (function == null) {
            throw new IllegalArgumentException("Неизвестная функция: " + name);
        }
        return function.apply(args);
    }
}