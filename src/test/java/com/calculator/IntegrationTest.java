package com.calculator;

import com.calculator.calculator.Calculator;
import com.calculator.exception.ExpressionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты для калькулятора математических выражений.
 * Проверяют совместную работу всех компонентов системы: парсера, вычислителя и реестра функций.
 * Тестируют сложные выражения, комбинации функций и обработку ошибок на системном уровне.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
class IntegrationTest {
    
    /**
     * Тестирует вычисление сложного выражения с комбинацией операций и скобок.
     * Проверяет корректность работы приоритета операторов и вложенных выражений.
     * Выражение: "2 + 3 * (4 - 1) ^ 2" должно вычисляться как 2 + 3 * (3 ^ 2) = 2 + 3 * 9 = 29
     */
    @Test
    void testComplexExpression() throws ExpressionException {
        Calculator calculator = new Calculator();
        double result = calculator.calculate("2 + 3 * (4 - 1) ^ 2");
        assertEquals(29.0, result, 0.001);
    }
    
    /**
     * Тестирует вычисление выражения с математическими функциями.
     * Проверяет корректную работу функций из реестра и их комбинацию с другими операциями.
     * Выражение: "sqrt(16) + abs(-5)" должно вычисляться как 4 + 5 = 9
     */
    @Test
    void testExpressionWithFunctions() throws ExpressionException {
        Calculator calculator = new Calculator();
        double result = calculator.calculate("sqrt(16) + abs(-5)");
        assertEquals(9.0, result, 0.001);
    }
    
    /**
     * Тестирует обработку синтаксически некорректного выражения.
     * Проверяет, что система корректно обнаруживает и сообщает об ошибках синтаксиса.
     * Выражение: "2 + * 3" содержит синтаксическую ошибку (оператор без правого операнда).
     */
    @Test
    void testInvalidExpression() {
        Calculator calculator = new Calculator();
        assertThrows(ExpressionException.class, () -> calculator.calculate("2 + * 3"));
    }
}