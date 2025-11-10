package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;
import com.calculator.calculator.functions.MathFunction;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.List;

/**
 * Основной класс калькулятора математических выражений.
 * Координирует работу парсера, вычислителя и реестра функций.
 * Предоставляет высокоуровневый API для вычисления выражений.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
public class Calculator {
    private final FunctionRegistry functionRegistry;
    
    /**
     * Конструктор калькулятора. Инициализирует реестр математических функций.
     * Создает экземпляр с предустановленным набором стандартных функций.
     */
    public Calculator() {
        this.functionRegistry = new FunctionRegistry();
    }
    
    /**
     * Вычисляет значение математического выражения без переменных.
     * Выражение может содержать числа, операторы, скобки и функции.
     *
     * @param expression математическое выражение для вычисления
     * @return результат вычисления выражения как значение типа double
     * @throws ExpressionException если выражение содержит синтаксические ошибки,
     *                            неизвестные функции или деление на ноль
     * @see #calculate(String, Map)
     */
    public double calculate(String expression) throws ExpressionException {
        return calculate(expression, null);
    }
    
    /**
     * Вычисляет значение математического выражения с поддержкой переменных.
     * Если выражение содержит переменные, их значения должны быть предоставлены в карте variables.
     * Процесс вычисления включает парсинг выражения и его последующую оценку.
     *
     * @param expression математическое выражение для вычисления
     * @param variables карта значений переменных, где ключ - имя переменной,
     *                  значение - числовое значение переменной
     * @return результат вычисления выражения как значение типа double
     * @throws ExpressionException если выражение содержит синтаксические ошибки,
     *                            неизвестные функции, деление на ноль или
     *                            используются неопределенные переменные
     * @see ExpressionParser
     * @see ExpressionEvaluator
     */
    public double calculate(String expression, Map<String, Double> variables) 
            throws ExpressionException {
        
        ExpressionParser parser = new ExpressionParser(expression, functionRegistry);
        List<Token> tokens = parser.parse();
        
        ExpressionEvaluator evaluator = new ExpressionEvaluator(tokens, functionRegistry, variables);
        return evaluator.evaluate();
    }
    
    /**
     * Анализирует выражение и извлекает все имена переменных.
     * Полезно для определения, какие значения переменных нужно запросить у пользователя.
     *
     * @param expression математическое выражение для анализа
     * @return множество уникальных имен переменных, найденных в выражении
     * @throws ExpressionException если выражение содержит синтаксические ошибки
     * @see #getVariableValues(Set, Scanner)
     */
    public Set<String> extractVariables(String expression) throws ExpressionException {
        ExpressionParser parser = new ExpressionParser(expression, functionRegistry);
        List<Token> tokens = parser.parse();
        
        Set<String> variables = new HashSet<>();
        for (Token token : tokens) {
            if (token.getType() == TokenType.VARIABLE) {
                variables.add(token.getValue());
            }
        }
        
        return variables;
    }
    
    /**
     * Запрашивает у пользователя значения для указанных переменных.
     * Обеспечивает корректный ввод числовых значений с обработкой ошибок.
     * Цикл продолжается до тех пор, пока для каждой переменной не будет введено корректное число.
     *
     * @param variables множество имен переменных, для которых нужно запросить значения
     * @param scanner объект Scanner для чтения ввода пользователя
     * @return карта, содержащая значения переменных, где ключ - имя переменной,
     *         значение - введенное пользователем число
     */
    public Map<String, Double> getVariableValues(Set<String> variables, Scanner scanner) {
        Map<String, Double> values = new HashMap<>();
        
        for (String varName : variables) {
            while (true) {
                System.out.print("Введите значение переменной " + varName + ": ");
                try {
                    String input = scanner.nextLine().trim();
                    double value = Double.parseDouble(input);
                    values.put(varName, value);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введите корректное число");
                }
            }
        }
        
        return values;
    }
    
    /**
     * Регистрирует пользовательскую математическую функцию в калькуляторе.
     * Зарегистрированная функция становится доступной для использования в выражениях.
     * Имя функции регистрируется в нижнем регистре для регистронезависимого доступа.
     *
     * @param name имя функции для использования в выражениях
     * @param function реализация функции в виде лямбда-выражения или объекта MathFunction
     * @see FunctionRegistry#registerFunction(String, MathFunction)
     */
    public void registerFunction(String name, MathFunction function) {
        functionRegistry.registerFunction(name, function);
    }
}