package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса {@link ExpressionEvaluator}.
 * Проверяют корректность вычисления математических выражений различных типов.
 * Тестируют базовые операции, приоритет операторов, функции, переменные и обработку ошибок.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
class ExpressionEvaluatorTest {
    private FunctionRegistry functionRegistry;
    private Map<String, Double> variables;
    
    /**
     * Подготовка тестового окружения перед каждым тестом.
     * Инициализирует реестр функций и набор тестовых переменных.
     */
    @BeforeEach
    void setUp() {
        functionRegistry = new FunctionRegistry();
        variables = Map.of("x", 2.0, "y", 3.0);
    }
    
    /**
     * Тестирует простую операцию сложения.
     * Проверяет корректность вычисления выражения "2 + 3".
     */
    @Test
    void testSimpleAddition() throws ExpressionException {
        double result = evaluateExpression("2 + 3", null);
        assertEquals(5.0, result, 0.001);
    }
    
    /**
     * Тестирует комбинацию операций умножения и деления.
     * Проверяет корректность вычисления выражения "6 * 4 / 2".
     */
    @Test
    void testMultiplicationAndDivision() throws ExpressionException {
        double result = evaluateExpression("6 * 4 / 2", null);
        assertEquals(12.0, result, 0.001);
    }
    
    /**
     * Тестирует использование скобок для изменения приоритета операций.
     * Проверяет корректность вычисления выражения "(2 + 3) * 4".
     */
    @Test
    void testWithParentheses() throws ExpressionException {
        double result = evaluateExpression("(2 + 3) * 4", null);
        assertEquals(20.0, result, 0.001);
    }
    
    /**
     * Тестирует операцию возведения в степень.
     * Проверяет корректность вычисления выражения "2 ^ 3".
     */
    @Test
    void testPower() throws ExpressionException {
        double result = evaluateExpression("2 ^ 3", null);
        assertEquals(8.0, result, 0.001);
    }
    
    /**
     * Тестирует использование переменных в выражениях.
     * Проверяет корректность вычисления выражения "x * y + 1" с переменными x=2.0, y=3.0.
     */
    @Test
    void testWithVariables() throws ExpressionException {
        double result = evaluateExpression("x * y + 1", variables);
        assertEquals(7.0, result, 0.001);
    }
    
    /**
     * Тестирует вызов математической функции sin.
     * Проверяет корректность вычисления выражения "sin(0)".
     */
    @Test
    void testFunctionSin() throws ExpressionException {
        double result = evaluateExpression("sin(0)", null);
        assertEquals(0.0, result, 0.001);
    }
    
    /**
     * Тестирует обработку ошибки деления на ноль.
     * Проверяет, что выражение "1 / 0" вызывает исключение ExpressionException.
     */
    @Test
    void testDivisionByZero() {
        assertThrows(ExpressionException.class, () -> evaluateExpression("1 / 0", null));
    }
    
    /**
     * Тестирует обработку ошибки использования неизвестной переменной.
     * Проверяет, что выражение "unknown * 2" вызывает исключение ExpressionException.
     */
    @Test
    void testUnknownVariable() {
        assertThrows(ExpressionException.class, () -> evaluateExpression("unknown * 2", variables));
    }
    
    /**
     * Вспомогательный метод для выполнения вычисления выражения.
     * Объединяет парсинг выражения и его вычисление в одном вызове.
     *
     * @param expression математическое выражение для вычисления
     * @param vars карта значений переменных или null если переменных нет
     * @return результат вычисления выражения
     * @throws ExpressionException если выражение содержит ошибки
     */
    private double evaluateExpression(String expression, Map<String, Double> vars) 
            throws ExpressionException {
        ExpressionParser parser = new ExpressionParser(expression, functionRegistry);
        List<Token> tokens = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(tokens, functionRegistry, vars);
        return evaluator.evaluate();
    }
}