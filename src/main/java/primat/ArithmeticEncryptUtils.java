package primat;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.log;

public class ArithmeticEncryptUtils {

    public static void printHeader(PrintStream printStream) {
        printStream.println("Программа для арифметического кодирования и декодирования английского текста.");
        printStream.println("Допустимые символы: символы таблицы ASCII.");
        printStream.println("Для начала работы");
    }

    public static Object getActionFromInputWithOutput(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите 0 чтобы ввести путь к файлу для его кодирования," +
                        "\n 1 чтобы ввести путь к файлу и код для декодирования," +
                        "\n q или 2 для выхода из программы: ");
                String token = in.next();
                switch (token) {
                    case "0":
                        return new ArithmeticEncrypter(new FileInputStream(new File(readFilename(outputStream, inputStream))));
                    case "1":
                        return new ArithmeticDecrypter(new FileOutputStream(new File(readFilename(outputStream, inputStream))), readCode(outputStream, inputStream));
                    case "2":
                    case "q":
                        System.exit(0);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (FileNotFoundException e) {
                outputStream.println("Файл не найден, повторите ввод.\n");
            }  catch (Exception e) {
                outputStream.println("Произошла ошибка ввода, повторите ввод.\n");
                in.nextLine();
            }
        }
    }

    private static String readFilename(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите имя файла: ");
                String filename = in.nextLine();
                outputStream.println("Имя файла введено.");
                return filename;
            } catch (Exception e) {
                outputStream.println("Ошибка ввода, повторите ввод.");
                in.nextLine();
            }
        }
    }

    private static double readCode(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите код: ");
                String code = in.nextLine();
                outputStream.println("Код введен.");
                return Double.parseDouble(code.replace(',','.'));
            } catch (Exception e) {
                outputStream.println("Ошибка ввода, повторите ввод.");
                in.nextLine();
            }
        }
    }

    public static void main(String... args) {
        printHeader(System.out);
        while (true) {
            Object action = getActionFromInputWithOutput(System.out, System.in);
            try {
                if (action instanceof ArithmeticEncrypter) {
                    ArithmeticEncrypter arithmeticEncrypter = (ArithmeticEncrypter) action;
                    arithmeticEncrypter.readTextAndFillCounts();
                    arithmeticEncrypter.fillProbabilities();
                    System.out.println("Код: " + arithmeticEncrypter.getCode());
                } else {

                }
                System.out.println();
                throw new IOException();//!!!
            } catch (IOException e) {
                System.out.println("Введено неверное имя файла, пожалуйста, следуйте инструкциям.");
            }
        }
    }

}
