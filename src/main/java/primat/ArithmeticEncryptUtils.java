package primat;

import java.io.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class ArithmeticEncryptUtils {

    public static void printHeader(PrintStream printStream) {
        printStream.println("Программа для арифметического кодирования и декодирования английского текста.");
        printStream.println("Допустимые символы: символы таблицы ASCII.");
        printStream.println("Для начала работы");
    }

    public static ArithmeticEncrypter getArithmeticEncrypterFromInputWithOutput(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите 0 чтобы ввести путь к файлу для его кодирования и декодирования" +
                        "\n 1 чтобы ввести путь к файлу и код для декодирования," +
                        "\n q или 2 для выхода из программы: ");
                String token = in.next();
                switch (token) {
                    case "0":
                        File output = new File(readFilename(outputStream, inputStream, "выходного"));
                        output.createNewFile();
                        return new ArithmeticEncrypter(new FileInputStream(new File(readFilename(outputStream, inputStream, "входного"))),
                                new FileOutputStream(output));
                    case "2":
                    case "q":
                        System.exit(0);
                    default:
                        throw new IllegalArgumentException();
                }
            } catch (FileNotFoundException e) {
                outputStream.println("Файл не найден, повторите ввод.\n");
            } catch (Exception e) {
                outputStream.println("Произошла ошибка ввода, повторите ввод.\n");
                in.nextLine();
            }
        }
    }

    private static String readFilename(PrintStream outputStream, InputStream inputStream, String fileType) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите имя " + fileType + " файла: ");
                String filename = in.nextLine();
                outputStream.println("Имя файла введено.");
                return filename;
            } catch (Exception e) {
                outputStream.println("Ошибка ввода, повторите ввод.");
                in.nextLine();
            }
        }
    }

    private static BigDecimal readCode(PrintStream outputStream, InputStream inputStream) {
        Scanner in = new Scanner(inputStream);
        while (true) {
            try {
                outputStream.print("Введите код: ");
                String code = in.nextLine();
                outputStream.println("Код введен.");
                return new BigDecimal(code.replace(',', '.'));
            } catch (Exception e) {
                outputStream.println("Ошибка ввода, повторите ввод.");
                in.nextLine();
            }
        }
    }

    public static void main(String... args) {
        printHeader(System.out);
        while (true) {
            ArithmeticEncrypter arithmeticEncrypter = getArithmeticEncrypterFromInputWithOutput(System.out, System.in);
            try {
                arithmeticEncrypter.readTextAndFillCounts();
                arithmeticEncrypter.fillProbabilities();
                System.out.println("Код: " + arithmeticEncrypter.getCode());
                arithmeticEncrypter.decode();
                System.out.println();
            } catch (Exception e) {
                System.out.println("Введено неверное имя файла, пожалуйста, следуйте инструкциям.");
            }
        }
    }

}
