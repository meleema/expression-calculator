package com.calculator.calculator;

/**
 * Класс, представляющий токен в выражении
 */
public class Token {
    private final TokenType type;
    private final String value;
    private final int position;
    
    /**
     * Конструктор токена
     * 
     * @param type тип токена
     * @param value строковое значение токена
     * @param position позиция в исходной строке
     */
    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
    
    // Геттеры
    public TokenType getType() { return type; }
    public String getValue() { return value; }
    public int getPosition() { return position; }
    
    @Override
    public String toString() {
        return String.format("Token{type=%s, value='%s', position=%d}", type, value, position);
    }
}
