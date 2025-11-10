package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Парсер математических выражений
 */
public class ExpressionParser {
    private final String expression;
    private final FunctionRegistry functionRegistry;
    private int position;
    private char currentChar;
    
    /**
     * Конструктор парсера
     * 
     * @param expression математическое выражение
     * @param functionRegistry реестр функций
     */
    public ExpressionParser(String expression, FunctionRegistry functionRegistry) {
        this.expression = expression.replaceAll("\\s+", ""); // Удаляем пробелы
        this.functionRegistry = functionRegistry;
        this.position = -1;
        advance();
    }
    
    /**
     * Разбирает выражение на токены
     * 
     * @return список токенов
     * @throws ExpressionException если выражение содержит ошибки
     */
    public List<Token> parse() throws ExpressionException {
        List<Token> tokens = new ArrayList<>();
        
        while (currentChar != '\0') {
            if (Character.isDigit(currentChar) || currentChar == '.') {
                tokens.add(parseNumber());
            } else if (Character.isLetter(currentChar)) {
                tokens.add(parseIdentifier());
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.OPERATOR, "+", position));
                advance();
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.OPERATOR, "-", position));
                advance();
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.OPERATOR, "*", position));
                advance();
            } else if (currentChar == '/') {
                tokens.add(new Token(TokenType.OPERATOR, "/", position));
                advance();
            } else if (currentChar == '^') {
                tokens.add(new Token(TokenType.OPERATOR, "^", position));
                advance();
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", position));
                advance();
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", position));
                advance();
            } else if (currentChar == ',') {
                tokens.add(new Token(TokenType.COMMA, ",", position));
                advance();
            } else {
                throw new ExpressionException("Неизвестный символ: " + currentChar, position);
            }
        }
        
        tokens.add(new Token(TokenType.EOF, "", position));
        return tokens;
    }
    
    private Token parseNumber() {
        int startPos = position;
        StringBuilder sb = new StringBuilder();
        boolean hasDecimal = false;
        
        while (currentChar != '\0' && 
               (Character.isDigit(currentChar) || currentChar == '.')) {
            if (currentChar == '.') {
                if (hasDecimal) {
                    throw new ExpressionException("Неверный формат числа", position);
                }
                hasDecimal = true;
            }
            sb.append(currentChar);
            advance();
        }
        
        return new Token(TokenType.NUMBER, sb.toString(), startPos);
    }
    
    private Token parseIdentifier() {
        int startPos = position;
        StringBuilder sb = new StringBuilder();
        
        while (currentChar != '\0' && 
               (Character.isLetter(currentChar) || Character.isDigit(currentChar))) {
            sb.append(currentChar);
            advance();
        }
        
        String identifier = sb.toString();
        
        // Проверяем, является ли идентификатор функцией
        if (functionRegistry.hasFunction(identifier)) {
            return new Token(TokenType.FUNCTION, identifier, startPos);
        } else {
            return new Token(TokenType.VARIABLE, identifier, startPos);
        }
    }
    
    private void advance() {
        position++;
        if (position < expression.length()) {
            currentChar = expression.charAt(position);
        } else {
            currentChar = '\0';
        }
    }
}
