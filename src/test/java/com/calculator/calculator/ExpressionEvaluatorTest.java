package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для вычислителя выражений
 */
class ExpressionEvaluatorTest {
    private FunctionRegistry functionRegistry;
    private Map<String, Double> variables;
    
    @BeforeEach
    void setUp() {
        functionRegistry = new FunctionRegistry();
        variables = Map.of("x", 2.0, "y", 3.0);
    }
    
    @Test
    void testSimpleAddition() throws ExpressionException {
        double result = evaluateExpression("2 + 3", null);
        assertEquals(5.0, result, 0.001);
    }
    
    @Test
    void testMultiplicationAndDivision() throws ExpressionException {
        double result = evaluateExpression("6 * 4 / 2", null);
        assertEquals(12.0, result, 0.001);
    }
    
    @Test
    void testWithParentheses() throws ExpressionException {
        double result = evaluateExpression("(2 + 3) * 4", null);
        assertEquals(20.0, result, 0.001);
    }
    
    @Test
    void testPower() throws ExpressionException {
        double result = evaluateExpression("2 ^ 3", null);
        assertEquals(8.0, result, 0.001);
    }
    
    @Test
    void testWithVariables() throws ExpressionException {
        double result = evaluateExpression("x * y + 1", variables);
        assertEquals(7.0, result, 0.001);
    }
    
    @Test
    void testFunctionSin() throws ExpressionException {
        double result = evaluateExpression("sin(0)", null);
        assertEquals(0.0, result, 0.001);
    }
    
    @Test
    void testDivisionByZero() {
        assertThrows(ExpressionException.class, () -> evaluateExpression("1 / 0", null));
    }
    
    @Test
    void testUnknownVariable() {
        assertThrows(ExpressionException.class, () -> evaluateExpression("unknown * 2", variables));
    }
    
    private double evaluateExpression(String expression, Map<String, Double> vars) 
            throws ExpressionException {
        ExpressionParser parser = new ExpressionParser(expression, functionRegistry);
        List<Token> tokens = parser.parse();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(tokens, functionRegistry, vars);
        return evaluator.evaluate();
    }
}
