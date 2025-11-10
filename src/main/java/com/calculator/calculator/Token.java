package com.calculator.calculator;

/**
 * Класс, представляющий токен в математическом выражении.
 * Токен - это минимальная значимая единица выражения, такая как число, оператор, функция и т.д.
 * Каждый токен содержит информацию о своем типе, строковом значении и позиции в исходном выражении.
 * 
 * @author Yarovaya Maria
 * @version 1.0
 */
public class Token {
    private final TokenType type;
    private final String value;
    private final int position;
    
    /**
     * Конструктор токена.
     * Создает новый токен с указанным типом, значением и позицией.
     *
     * @param type тип токена из перечисления TokenType
     * @param value строковое представление значения токена
     * @param position позиция токена в исходной строке выражения (индекс символа)
     */
    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
    
    /**
     * Возвращает тип токена.
     *
     * @return тип токена из перечисления TokenType
     */
    public TokenType getType() { return type; }
    
    /**
     * Возвращает строковое значение токена.
     * Для чисел - строковое представление числа, для операторов - символ оператора,
     * для функций и переменных - их имя.
     *
     * @return строковое значение токена
     */
    public String getValue() { return value; }
    
    /**
     * Возвращает позицию токена в исходном выражении.
     * Позиция используется для точного указания местоположения ошибок.
     *
     * @return индекс символа в исходной строке выражения
     */
    public int getPosition() { return position; }
    
    /**
     * Возвращает строковое представление токена.
     * Формат: "Token{type=TYPE, value='VALUE', position=POSITION}"
     *
     * @return строковое представление токена для отладки и логирования
     */
    @Override
    public String toString() {
        return String.format("Token{type=%s, value='%s', position=%d}", type, value, position);
    }
}