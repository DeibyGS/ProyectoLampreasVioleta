package utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtils {

    public static String readStrings(Scanner sc, String prompt) {
        String input;
        while (true) {
        System.out.print(prompt);
        input = sc.nextLine().trim();
        if(!input.isEmpty()) return input;
        System.out.println("Error: el valor no puede estar vacio");

    }
    }

    public static Integer readIntegers(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if(!input.isEmpty()){
                try {
                    return Integer.parseInt(input);
                }catch (NumberFormatException e){
                    System.out.println("Error: Ingrese un numero");
                }
            }
            System.out.println("Error: el valor no puede estar vacio");
        }
    }

    public static Double readDoubles(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if(!input.isEmpty()){
                try {
                    return Double.parseDouble(input);
                }catch (NumberFormatException e){
                    System.out.println("Error: Ingrese un numero");
                }
            }
            System.out.println("Error: el valor no puede estar vacio");
        }
    }

    public static LocalDate readLocalDate(Scanner sc, String prompt) {
        LocalDate date;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                date = LocalDate.parse(input);
                return date;
            }catch(DateTimeParseException e) {
                System.out.println("Error: formato de fecha no valido. Usa YYYY-MM-DD");
            }
        }
    }

    public static String readEmail(Scanner sc, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                if (isValidEmail(input)) {
                    return input;
                } else {
                    System.out.println("Error: Ingrese un email válido");
                }
            } else {
                System.out.println("Error: el valor no puede estar vacío");
            }
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }


}
