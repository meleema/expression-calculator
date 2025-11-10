package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса {@link ExpressionParser}.
 * Проверяют корректность лексического анализа математических выражений.
 * Тестируют распознавание различных типов токенов: чисел, операторов, функций, переменных, скобок.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
class ExpressionParserTest {
    private FunctionRegistry functionRegistry;
    
    /**
     * Подготовка тестового окружения перед каждым тестом.
     * Инициализирует реестр функций для корректного распознавания имен функций.
     */
    @BeforeEach
    void setUp() {
        functionRegistry = new FunctionRegistry();
    }
    
    /**
     * Тестирует разбор простого арифметического выражения.
     * Проверяет корректное распознавание чисел и оператора сложения.
     * Ожидаемая последовательность токенов: NUMBER, OPERATOR, NUMBER, EOF
     */
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
    
    /**
     * Тестирует разбор выражения со скобками.
     * Проверяет корректное распознавание скобок и сохранение структуры выражения.
     * Ожидаемая последовательность токенов: LEFT_PAREN, NUMBER, OPERATOR, NUMBER, 
     * RIGHT_PAREN, OPERATOR, NUMBER, EOF
     */
    @Test
    void testParseWithParentheses() {
        ExpressionParser parser = new ExpressionParser("(2 + 3) * 4", functionRegistry);
        List<Token> tokens = parser.parse();

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
    
    /**
     * Тестирует разбор выражения с математическими функциями.
     * Проверяет корректное распознавание имен функций и их аргументов.
     * Ожидаемая последовательность токенов: FUNCTION, LEFT_PAREN, NUMBER, RIGHT_PAREN,
     * OPERATOR, FUNCTION, LEFT_PAREN, NUMBER, RIGHT_PAREN, EOF
     */
    @Test
    void testParseWithFunction() {
        ExpressionParser parser = new ExpressionParser("sin(90) + cos(0)", functionRegistry);
        List<Token> tokens = parser.parse();
        
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
    
    /**
     * Тестирует разбор выражения с переменными.
     * Проверяет корректное распознавание идентификаторов переменных.
     * Ожидаемая последовательность токенов: VARIABLE, OPERATOR, VARIABLE, OPERATOR, VARIABLE, EOF
     */
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
    
    /**
     * Тестирует обработку некорректных символов в выражении.
     * Проверяет, что парсер выбрасывает исключение при встрече неизвестного символа '#'.
     */
    @Test
    void testInvalidCharacter() {
        ExpressionParser parser = new ExpressionParser("2 # 3", functionRegistry);
        assertThrows(ExpressionException.class, parser::parse);
    }
}