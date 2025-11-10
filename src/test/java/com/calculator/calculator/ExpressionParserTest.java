package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserTest {
    private FunctionRegistry functionRegistry;
    
    @BeforeEach
    void setUp() {
        functionRegistry = new FunctionRegistry();
    }
    
    @Test
    void testParseSimpleExpression() {
        ExpressionParser parser = new ExpressionParser("2 + 3", functionRegistry);
        List<Token> tokens = parser.parse();
        
        assertEquals(4, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        assertEquals(TokenType.OPERATOR, tokens.get(1).getType());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals(TokenType.EOF, tokens.get(3).getType());
    }
    
    @Test
    void testParseWithParentheses() {
        ExpressionParser parser = new ExpressionParser("(2 + 3) * 4", functionRegistry);
        List<Token> tokens = parser.parse();
        
        // ИСПРАВЛЕНО: 8 токенов вместо 7
        // Токены: ( 2 + 3 ) * 4 EOF = 8 токенов
        assertEquals(8, tokens.size());
        assertEquals(TokenType.LEFT_PAREN, tokens.get(0).getType());
        assertEquals(TokenType.NUMBER, tokens.get(1).getType());
        assertEquals(TokenType.OPERATOR, tokens.get(2).getType());
        assertEquals(TokenType.NUMBER, tokens.get(3).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(4).getType());
        assertEquals(TokenType.OPERATOR, tokens.get(5).getType());
        assertEquals(TokenType.NUMBER, tokens.get(6).getType());
        assertEquals(TokenType.EOF, tokens.get(7).getType());
    }
    
    @Test
    void testParseWithFunction() {
        ExpressionParser parser = new ExpressionParser("sin(90) + cos(0)", functionRegistry);
        List<Token> tokens = parser.parse();
        
        // ИСПРАВЛЕНО: 10 токенов вместо 8
        // Токены: sin ( 90 ) + cos ( 0 ) EOF = 10 токенов
        assertEquals(10, tokens.size());
        assertEquals(TokenType.FUNCTION, tokens.get(0).getType());
        assertEquals("sin", tokens.get(0).getValue());
        assertEquals(TokenType.LEFT_PAREN, tokens.get(1).getType());
        assertEquals(TokenType.NUMBER, tokens.get(2).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(3).getType());
        assertEquals(TokenType.OPERATOR, tokens.get(4).getType());
        assertEquals(TokenType.FUNCTION, tokens.get(5).getType());
        assertEquals("cos", tokens.get(5).getValue());
        assertEquals(TokenType.LEFT_PAREN, tokens.get(6).getType());
        assertEquals(TokenType.NUMBER, tokens.get(7).getType());
        assertEquals(TokenType.RIGHT_PAREN, tokens.get(8).getType());
        assertEquals(TokenType.EOF, tokens.get(9).getType());
    }
    
    @Test
    void testParseWithVariables() {
        ExpressionParser parser = new ExpressionParser("x + y * z", functionRegistry);
        List<Token> tokens = parser.parse();
        
        assertEquals(6, tokens.size());
        assertEquals(TokenType.VARIABLE, tokens.get(0).getType());
        assertEquals("x", tokens.get(0).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(1).getType());
        assertEquals(TokenType.VARIABLE, tokens.get(2).getType());
        assertEquals("y", tokens.get(2).getValue());
        assertEquals(TokenType.OPERATOR, tokens.get(3).getType());
        assertEquals(TokenType.VARIABLE, tokens.get(4).getType());
        assertEquals("z", tokens.get(4).getValue());
        assertEquals(TokenType.EOF, tokens.get(5).getType());
    }
    
    @Test
    void testInvalidCharacter() {
        ExpressionParser parser = new ExpressionParser("2 # 3", functionRegistry);
        assertThrows(ExpressionException.class, parser::parse);
    }
}