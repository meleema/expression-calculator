package com.calculator.calculator;

/**
 * Перечисление типов токенов для разбора выражения
 */
public enum TokenType {
    NUMBER,           // Число
    VARIABLE,         // Переменная
    OPERATOR,         // Оператор (+, -, *, /, ^)
    FUNCTION,         // Функция (sin, cos, etc.)
    LEFT_PAREN,       // Левая скобка
    RIGHT_PAREN,      // Правая скобка
    COMMA,            // Запятая (для аргументов функций)
    EOF               // Конец выражения
}
