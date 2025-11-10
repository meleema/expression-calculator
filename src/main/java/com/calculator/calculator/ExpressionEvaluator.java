package com.calculator.calculator;

import com.calculator.exception.ExpressionException;
import com.calculator.calculator.functions.FunctionRegistry;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Вычислитель математических выражений
 */
public class ExpressionEvaluator {
    private final List<Token> tokens;
    private final FunctionRegistry functionRegistry;
    private final Map<String, Double> variables;
    private int currentTokenIndex;
    
    /**
     * Конструктор вычислителя
     * 
     * @param tokens список токенов
     * @param functionRegistry реестр функций
     * @param variables значения переменных
     */
    public ExpressionEvaluator(List<Token> tokens, FunctionRegistry functionRegistry, 
                              Map<String, Double> variables) {
        this.tokens = tokens;
        this.functionRegistry = functionRegistry;
        this.variables = variables != null ? variables : new HashMap<>();
        this.currentTokenIndex = 0;
    }
    
    /**
     * Вычисляет значение выражения
     * 
     * @return результат вычисления
     * @throws ExpressionException если выражение содержит ошибки
     */
    public double evaluate() throws ExpressionException {
        double result = parseExpression();
        if (currentToken().getType() != TokenType.EOF) {
            throw new ExpressionException("Ожидался конец выражения", currentToken().getPosition());
        }
        return result;
    }
    
    private double parseExpression() {
        double result = parseTerm();
        
        while (true) {
            Token token = currentToken();
            if (token.getType() == TokenType.OPERATOR && 
                (token.getValue().equals("+") || token.getValue().equals("-"))) {
                
                consumeToken();
                double right = parseTerm();
                
                if (token.getValue().equals("+")) {
                    result += right;
                } else {
                    result -= right;
                }
            } else {
                break;
            }
        }
        
        return result;
    }
    
    private double parseTerm() {
        double result = parseFactor();
        
        while (true) {
            Token token = currentToken();
            if (token.getType() == TokenType.OPERATOR && 
                (token.getValue().equals("*") || token.getValue().equals("/"))) {
                
                consumeToken();
                double right = parseFactor();
                
                if (token.getValue().equals("*")) {
                    result *= right;
                } else {
                    if (right == 0) {
                        throw new ExpressionException("Деление на ноль", token.getPosition());
                    }
                    result /= right;
                }
            } else {
                break;
            }
        }
        
        return result;
    }
    
    private double parseFactor() {
        double result = parsePower();
        
        while (currentToken().getType() == TokenType.OPERATOR && 
               currentToken().getValue().equals("^")) {
            
            consumeToken();
            double right = parsePower();
            result = Math.pow(result, right);
        }
        
        return result;
    }
    
    private double parsePower() {
        return parsePrimary();
    }
    
    private double parsePrimary() {
        Token token = currentToken();
        
        if (token.getType() == TokenType.NUMBER) {
            consumeToken();
            try {
                return Double.parseDouble(token.getValue());
            } catch (NumberFormatException e) {
                throw new ExpressionException("Неверный формат числа: " + token.getValue(), 
                                            token.getPosition());
            }
        } else if (token.getType() == TokenType.VARIABLE) {
            consumeToken();
            String varName = token.getValue();
            if (!variables.containsKey(varName)) {
                throw new ExpressionException("Неизвестная переменная: " + varName, 
                                            token.getPosition());
            }
            return variables.get(varName);
        } else if (token.getType() == TokenType.FUNCTION) {
            return parseFunction();
        } else if (token.getType() == TokenType.LEFT_PAREN) {
            consumeToken(); // Потребляем '('
            double result = parseExpression();
            
            if (currentToken().getType() != TokenType.RIGHT_PAREN) {
                throw new ExpressionException("Ожидалась закрывающая скобка", 
                                            currentToken().getPosition());
            }
            consumeToken(); // Потребляем ')'
            return result;
        } else if (token.getType() == TokenType.OPERATOR && token.getValue().equals("-")) {
            consumeToken();
            return -parsePrimary();
        } else if (token.getType() == TokenType.OPERATOR && token.getValue().equals("+")) {
            consumeToken();
            return parsePrimary();
        } else {
            throw new ExpressionException("Неожиданный токен: " + token.getType(), 
                                        token.getPosition());
        }
    }
    
    private double parseFunction() {
        Token functionToken = currentToken();
        String functionName = functionToken.getValue();
        consumeToken(); // Потребляем имя функции
        
        if (currentToken().getType() != TokenType.LEFT_PAREN) {
            throw new ExpressionException("Ожидалась открывающая скобка после функции", 
                                        currentToken().getPosition());
        }
        consumeToken(); // Потребляем '('
        
        List<Double> arguments = new ArrayList<>();
        if (currentToken().getType() != TokenType.RIGHT_PAREN) {
            arguments.add(parseExpression());
            
            while (currentToken().getType() == TokenType.COMMA) {
                consumeToken(); // Потребляем ','
                arguments.add(parseExpression());
            }
        }
        
        if (currentToken().getType() != TokenType.RIGHT_PAREN) {
            throw new ExpressionException("Ожидалась закрывающая скобка после аргументов функции", 
                                        currentToken().getPosition());
        }
        consumeToken(); // Потребляем ')'
        
        try {
            double[] argsArray = arguments.stream().mapToDouble(Double::doubleValue).toArray();
            return functionRegistry.executeFunction(functionName, argsArray);
        } catch (IllegalArgumentException e) {
            throw new ExpressionException("Ошибка в функции " + functionName + ": " + e.getMessage(), 
                                        functionToken.getPosition());
        }
    }
    
    private Token currentToken() {
        if (currentTokenIndex >= tokens.size()) {
            return new Token(TokenType.EOF, "", -1);
        }
        return tokens.get(currentTokenIndex);
    }
    
    private void consumeToken() {
        if (currentTokenIndex < tokens.size()) {
            currentTokenIndex++;
        }
    }
}
