import java.util.*;

public class GreaderTwo {
    public static String secretCode, rangeChar;
    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();
    public static StringBuilder buffer = new StringBuilder();
    public static int bulls = 0, cows = 0, counterTry = 1, lengthCode, rangeCharCode;
    public static int leftLimit = 48, rightLimit = 122;
    public static boolean enableGame = false;

    public static void main(String[] args) {

        System.out.println("Input the length of the secret code:");
        try{
            lengthCode = scanner.nextInt();
            if (lengthCode > 0) {
                buffer = new StringBuilder(lengthCode);
                enableGame = checkInput();
                game(enableGame);
            } else {
                System.out.println("Error: minimum length of the secret code is 1.");
            }
        } catch (Exception e) {
            System.out.print("Error");
        }


    }

    /** игровая логика */
    public static boolean checkInput() {
        if (lengthCode > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return false;
        } else {
            System.out.println("Input the number of possible symbols in the code:");
            //scanner.nextLine();
            rangeChar = scanner.next();
            try {
                rangeCharCode = Integer.parseInt(rangeChar);
                if (rangeCharCode > 36) {
                    System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                    return false;
                } else if (rangeCharCode > lengthCode) {
                    String asterisk = "";
                    for (int i = 0; i < rangeCharCode; i++) {
                        asterisk = asterisk + '*';
                    }
                    System.out.print("The secret is prepared: " + asterisk + " (");
                    calcRangeCharCode(rangeCharCode);

                    /** 48...57 = 0...9 / 97...122 = a...z */

                    rightLimit = leftLimit + rangeCharCode - 1;
                    if (rightLimit > 57 && rightLimit < 97) {
                        rightLimit = 97 + rangeCharCode - 9;
                    }
                    generateCode(lengthCode, leftLimit, rightLimit);
                    System.out.println("Okay, let's start a game!");
                    return true;

                } else {
                    System.out.println("Error: it's not possible to generate a code with a length of " +
                            lengthCode + " with " + rangeCharCode + " unique symbols.");
                    return false;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.print("Error: \"" + rangeChar + "\" isn't a valid number.");
                return false;
            }

        }
    }

    /** игровая логика */
    public static void game(boolean enable) {
        int count1, count2;
        scanner.nextLine();

        while(enable) {
            bulls = 0;
            cows = 0;
            System.out.println("Turn " + counterTry + ":");


            secretCode = scanner.nextLine();
            if (secretCode.length() < lengthCode || secretCode.length() > lengthCode) {
                System.out.println("Error");
                break;
            } else if (secretCode.length() == lengthCode) {
                count1 = checkRangeOfSymbols(secretCode, rangeCharCode);
                count2 = checkRepeatSymbols(secretCode);

                if (count1 == 0 || count2 == 0) {
                    System.out.println("Error");
                    break;
                }

                counterCowsAndBulls(secretCode, buffer);
                printResult(bulls, cows);

                if (bulls == buffer.length()) {
                    System.out.println("Congratulations! You guessed the secret code.");
                    break;
                }
                counterTry++;
            }
        }
    }

    /** генерация кода */
    public static void generateCode(int num, int left, int right) {
        for (int i = 0; i < num; i++) {
            int randomLimitedInt = left + (int)(random.nextFloat() * (right - left + 1));
            if (randomLimitedInt > 57 && randomLimitedInt < 77) {
                randomLimitedInt = 57;
            } else if (randomLimitedInt >= 77 && randomLimitedInt < 97) {
                randomLimitedInt = 97;
            }
            buffer.append((char) randomLimitedInt);
        }
        buffer = buffer.append("0123456789").append("abcdefghijklmnopqrstuvwxyz");

        for (int i = 0; i < buffer.length(); i++) {
            for (int j = i + 1; j < buffer.length(); j++) {
                if (buffer.charAt(i) == buffer.charAt(j)) {
                    buffer.deleteCharAt(j);
                    j -= 1;
                }
            }
        }

        if (num != 36) {
            buffer = buffer.delete(num, buffer.length());
        }
    }

    /** информирование о дапазоне возможных символов в коде */
    public static void calcRangeCharCode(int RangeCode) {
        char firstCharOfCode = 97;
        char lastCharOfCode = (char)(firstCharOfCode + RangeCode - 11);
        if (RangeCode == 1) {
            System.out.println("0).");
        } else if (RangeCode > 1 && RangeCode <= 10){
            System.out.println("0-" + (RangeCode - 1) + ").");
        } else if (RangeCode > 10 && RangeCode == 11) {
            System.out.println("0-9, " + firstCharOfCode + ").");
        } else {
            System.out.println("0-9, " + firstCharOfCode + "-" +  lastCharOfCode + ").");
        }

    }


    /** определение совпадения введенного числа с кодом */
    public static void counterCowsAndBulls(String input, StringBuilder strBu) {

        for (int i = 0; i < strBu.length(); i++) {
            for (int j = 0; j < strBu.length(); j++) {
                if (i == j) {
                    if (input.charAt(i) == strBu.charAt(j)) {
                        bulls++;
                    }
                } else if (input.charAt(i) == strBu.charAt(j)) {
                    cows++;
                }

            }
        }
    }

    public static void printResult(int counterBulls, int counterCows){
        if (counterBulls != 0 && counterCows != 0) {
            System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s). ");
        } else if (counterBulls != 0 && counterCows == 0) {
            System.out.println("Grade: " + bulls + " bull(s). ");
        } else if (counterBulls == 0 && counterCows != 0) {
            System.out.println("Grade: " + cows + " cow(s). ");
        } else {
            System.out.println("Grade: None. ");
        }

    }

    public static int checkRangeOfSymbols(String inputCode, int range) {
        int i, count = 1;
        char n;
        StringBuilder charOf = new StringBuilder();

        for (i = 0; i < range; i++) {
            if (i <= 9) {
                n = (char)(48 + i);
            } else {
                n = (char)(97 + i - 10);
            }
            charOf = charOf.append(n);

        }

        for (int j = 0; j < inputCode.length(); j++) {
            if (count != 0) {
                for (int k = 0; k < charOf.length(); k++) {
                    //System.out.println(charOf.charAt(k) + " " + inputCode.charAt(j));
                    if (inputCode.charAt(j) == charOf.charAt(k)) {
                        count++;
                        break;
                    } else {
                        count = 0;
                    }
                }
            }
        }

        return count;

    }

    public static int checkRepeatSymbols(String inputCode) {
        String temp = inputCode;
        int countRepeat = 0;

        for (int i = 0; i < inputCode.length(); i++) {
            for (int j = 0; j < temp.length(); j++) {
                if (inputCode.charAt(i) == temp.charAt(j)) {
                    countRepeat++;
                    if (countRepeat > 1) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }


}

