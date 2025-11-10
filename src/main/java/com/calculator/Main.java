package com.calculator;

import com.calculator.calculator.Calculator;
import com.calculator.exception.ExpressionException;

import java.util.Scanner;
import java.util.Set;
import java.util.Map;

/**
 * Главный класс приложения
 */
public class Main {
    
    /**
     * Точка входа в приложение
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Калькулятор математических выражений ===");
        System.out.println("Поддерживаемые операции: +, -, *, /, ^");
        System.out.println("Поддерживаемые функции: sin, cos, tan, log, log10, sqrt, abs, pow, max, min");
        System.out.println("Для выхода введите 'exit'");
        System.out.println();
        
        while (true) {
            try {
                System.out.print("Введите выражение: ");
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                
                if (input.isEmpty()) {
                    continue;
                }
                
                // Извлекаем переменные из выражения
                Set<String> variables = calculator.extractVariables(input);
                
                // Получаем значения переменных
                Map<String, Double> variableValues = null;
                if (!variables.isEmpty()) {
                    System.out.println("Обнаружены переменные: " + variables);
                    variableValues = calculator.getVariableValues(variables, scanner);
                }
                
                // Вычисляем выражение
                double result = calculator.calculate(input, variableValues);
                System.out.println("Результат: " + result);
                
            } catch (ExpressionException e) {
                System.out.println("Ошибка в выражении: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Неожиданная ошибка: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println();
        }
        
        scanner.close();
        System.out.println("Работа завершена.");
    }
}