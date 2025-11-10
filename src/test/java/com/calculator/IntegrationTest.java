package com.calculator;

import com.calculator.calculator.Calculator;
import com.calculator.exception.ExpressionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционные тесты
 */
class IntegrationTest {
    
    @Test
    void testComplexExpression() throws ExpressionException {
        Calculator calculator = new Calculator();
        double result = calculator.calculate("2 + 3 * (4 - 1) ^ 2");
        assertEquals(29.0, result, 0.001);
    }
    
    @Test
    void testExpressionWithFunctions() throws ExpressionException {
        Calculator calculator = new Calculator();
        double result = calculator.calculate("sqrt(16) + abs(-5)");
        assertEquals(9.0, result, 0.001);
    }
    
    @Test
    void testInvalidExpression() {
        Calculator calculator = new Calculator();
        assertThrows(ExpressionException.class, () -> calculator.calculate("2 + * 3"));
    }
}