package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Лексический анализатор математических выражений.
 * Преобразует строковое математическое выражение в последовательность токенов.
 * Поддерживает числа, операторы, скобки, функции и переменные.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
public class ExpressionParser {
    private final String expression;
    private final FunctionRegistry functionRegistry;
    private int position;
    private char currentChar;
    
    /**
     * Конструктор парсера выражений.
     * Инициализирует парсер с очищенным от пробелов выражением.
     *
     * @param expression математическое выражение для разбора
     * @param functionRegistry реестр функций для идентификации имен функций
     */
    public ExpressionParser(String expression, FunctionRegistry functionRegistry) {
        this.expression = expression.replaceAll("\\s+", ""); // Удаляем пробелы
        this.functionRegistry = functionRegistry;
        this.position = -1;
        advance();
    }
    
    /**
     * Разбирает математическое выражение на последовательность токенов.
     * Токенизация включает распознавание чисел, операторов, скобок, функций и переменных.
     *
     * @return список токенов, представляющих структуру выражения
     * @throws ExpressionException если выражение содержит неизвестные символы
     *                            или некорректный формат чисел
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
    
    /**
     * Разбирает числовой литерал из входной строки.
     * Поддерживает целые числа и числа с плавающей точкой.
     * Проверяет корректность формата числа (не более одной десятичной точки).
     *
     * @return токен типа NUMBER с числовым значением
     * @throws ExpressionException если обнаружено более одной десятичной точки
     */
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
    
    /**
     * Разбирает идентификатор (имя функции или переменной) из входной строки.
     * Идентификаторы могут содержать буквы и цифры, но должны начинаться с буквы.
     * Проверяет реестр функций для определения типа идентификатора.
     *
     * @return токен типа FUNCTION или VARIABLE в зависимости от наличия в реестре функций
     */
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
    
    /**
     * Перемещает указатель на следующий символ во входной строке.
     * Обновляет currentChar текущим символом или '\0' при достижении конца строки.
     * Используется для последовательного чтения символов выражения.
     */
    private void advance() {
        position++;
        if (position < expression.length()) {
            currentChar = expression.charAt(position);
        } else {
            currentChar = '\0';
        }
    }
}