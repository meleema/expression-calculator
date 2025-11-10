package com.calculator.exception;

/**
 * Исключение для ошибок в выражении
 */
public class ExpressionException extends RuntimeException {
    private final int position;
    
    /**
     * Конструктор исключения
     * 
     * @param message сообщение об ошибке
     * @param position позиция ошибки в выражении
     */
    public ExpressionException(String message, int position) {
        super(message + " (позиция: " + position + ")");
        this.position = position;
    }
    
    public int getPosition() {
        return position;
    }
}
