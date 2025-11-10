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
 * Основной класс калькулятора
 */
public class Calculator {
    private final FunctionRegistry functionRegistry;
    
    /**
     * Конструктор калькулятора
     */
    public Calculator() {
        this.functionRegistry = new FunctionRegistry();
    }
    
    /**
     * Вычисляет значение выражения
     * 
     * @param expression математическое выражение
     * @return результат вычисления
     * @throws ExpressionException если выражение содержит ошибки
     */
    public double calculate(String expression) throws ExpressionException {
        return calculate(expression, null);
    }
    
    /**
     * Вычисляет значение выражения с переменными
     * 
     * @param expression математическое выражение
     * @param variables значения переменных
     * @return результат вычисления
     * @throws ExpressionException если выражение содержит ошибки
     */
    public double calculate(String expression, Map<String, Double> variables) 
            throws ExpressionException {
        
        ExpressionParser parser = new ExpressionParser(expression, functionRegistry);
        List<Token> tokens = parser.parse();
        
        ExpressionEvaluator evaluator = new ExpressionEvaluator(tokens, functionRegistry, variables);
        return evaluator.evaluate();
    }
    
    /**
     * Извлекает имена переменных из выражения
     * 
     * @param expression математическое выражение
     * @return множество имен переменных
     * @throws ExpressionException если выражение содержит ошибки
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
     * Получает значения переменных от пользователя
     * 
     * @param variables множество имен переменных
     * @param scanner сканер для ввода
     * @return карта значений переменных
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
     * Регистрирует пользовательскую функцию
     * 
     * @param name имя функции
     * @param function реализация функции
     */
    public void registerFunction(String name, MathFunction function) {
        functionRegistry.registerFunction(name, function);
    }
}
