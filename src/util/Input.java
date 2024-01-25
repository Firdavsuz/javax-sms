package util;

import java.util.Scanner;

public interface Input {

    Scanner SCANNER_STR = new Scanner(System.in);
    Scanner SCANNER_INT = new Scanner(System.in);

    static String inputStr(String msg) {
        System.out.print(msg);
        return SCANNER_STR.nextLine();
    }

    static Integer inputInt(String msg) {
        System.out.print(msg);
        return SCANNER_INT.nextInt();
    }
}
