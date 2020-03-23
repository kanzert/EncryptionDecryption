package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    interface EncryptionDecryption {
        String encryption(String messageToEncrypt, int key);
        String decryption(String messageToDecrypt, int key);
    }

    static class EncryptionDecryptionByUnicode implements EncryptionDecryption {
        public String encryption(String messageToEncrypt, int key) {
            int sizeOfStr = messageToEncrypt.length();
            char[] cipher = new char[sizeOfStr];
            int positionCurrent;
            int positionToShift;

            for (int i = 0; i < sizeOfStr; i++) {
                cipher[i] = messageToEncrypt.charAt(i);
                if (cipher[i] >= 'a' && cipher[i] <= 'z') {
                    positionCurrent = messageToEncrypt.charAt(i) % 97;
                    positionToShift = positionCurrent + key;
                    cipher[i] = (char) (97 + positionToShift);
                } else if (cipher[i] >= 'A' && cipher[i] <= 'Z'){
                    positionCurrent = messageToEncrypt.charAt(i) % 65;
                    positionToShift = positionCurrent + key;
                    cipher[i] = (char) (65 + positionToShift);
                } else {
                    cipher[i] += key;
                }
            }

            return String.copyValueOf(cipher);
        }

        public String decryption(String messageToDecrypt, int key) {
            int sizeOfStr = messageToDecrypt.length();
            char[] cipher = new char[sizeOfStr];
            int positionCurrent;
            int positionToShift;

            for (int i = 0; i < sizeOfStr; i++) {
                cipher[i] = messageToDecrypt.charAt(i);
                if (cipher[i] >= 'a' && cipher[i] <= 'z') {
                    positionCurrent = messageToDecrypt.charAt(i) % 97;
                    positionToShift = positionCurrent - key;
                    cipher[i] = (char) (97 + positionToShift);
                } else if (cipher[i] >= 'A' && cipher[i] <= 'Z'){
                    positionCurrent = messageToDecrypt.charAt(i) % 65;
                    positionToShift = positionCurrent - key;
                    cipher[i] = (char) (65 + positionToShift);
                } else {
                    cipher[i] -= key;
                }
            }

            return String.copyValueOf(cipher);
        }
    }

    static class EncryptionDecryptionByShift implements EncryptionDecryption {
        public String encryption(String messageToEncrypt, int key) {
            int sizeOfStr = messageToEncrypt.length();
            char[] cipher = new char[sizeOfStr];
            int positionCurrent;
            int positionToShift;

            for (int i = 0; i < sizeOfStr; i++) {
                cipher[i] = messageToEncrypt.charAt(i);
                if (cipher[i] >= 'a' && cipher[i] <= 'z') {
                    positionCurrent = messageToEncrypt.charAt(i) % 97;
                    positionToShift = (positionCurrent + key) % 26;
                    cipher[i] = (char) (97 + positionToShift);
                } else if (cipher[i] >= 'A' && cipher[i] <= 'Z'){
                    positionCurrent = messageToEncrypt.charAt(i) % 65;
                    positionToShift = (positionCurrent + key) % 26;
                    cipher[i] = (char) (65 + positionToShift);
                }
            }

            return String.copyValueOf(cipher);
        }

        public String decryption(String messageToDecrypt, int key) {
            int sizeOfStr = messageToDecrypt.length();
            char[] cipher = new char[sizeOfStr];
            int positionCurrent;
            int positionToShift;

            for (int i = 0; i < sizeOfStr; i++) {
                cipher[i] = messageToDecrypt.charAt(i);
                if (cipher[i] >= 'a' && cipher[i] <= 'z') {
                    positionCurrent = messageToDecrypt.charAt(i) % 97;
                    positionToShift = (positionCurrent - key) % 26;
                    if (positionToShift >= 0) {
                        cipher[i] = (char) (97 + positionToShift);
                    } else {
                        cipher[i] = (char) (123 + positionToShift);
                    }

                } else if (cipher[i] >= 'A' && cipher[i] <= 'Z'){
                    positionCurrent = messageToDecrypt.charAt(i) % 65;
                    positionToShift = (positionCurrent - key) % 26;
                    if (positionToShift >= 0) {
                        cipher[i] = (char) (65 + positionToShift);
                    } else {
                        cipher[i] = (char) (91 + positionToShift);
                    }
                }
            }

            return String.copyValueOf(cipher);
        }
    }


    static class EncryptionDecryptionFactory {

        public void setMethod (String[] args) {
            String actionToDo = "enc";
            String data = " ";
            boolean isInEnable = false;
            boolean isOutEnable = false;
            String nameOfFileIn = "";
            String nameOfFileOut = "";
            int key = 0;
            String algorithm = "shift";

            try {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-mode")) {
                        actionToDo = args[i + 1];
                    }

                    if (args[i].equals("-key")) {
                        key = Integer.parseInt(args[i + 1]);
                    }

                    if (args[i].equals("-data") && !isInEnable) {
                        data = args[i + 1];
                    }

                    if (args[i].equals("-in")) {
                        isInEnable = true;
                        nameOfFileIn = args[i + 1];
                        //nameOfFileIn = "C:\\FromZeroToHero\\JavaProjects\\Encryption-Decryption\\Encryption-Decryption\\task\\src\\encryptdecrypt\\road_to_treasure.txt";
                    }

                    if (args[i].equals("-out")) {
                        isOutEnable = true;
                        nameOfFileOut = args[i + 1];
                        //nameOfFileOut = "C:\\FromZeroToHero\\JavaProjects\\Encryption-Decryption\\Encryption-Decryption\\task\\src\\encryptdecrypt\\protected.txt";
                    }

                    if (args[i].equals("-alg")) {
                        algorithm = args[i + 1];
                    }
                }

                if (isInEnable) {
                    File fileIn = new File(nameOfFileIn);

                    try (Scanner scanner = new Scanner(fileIn)) {
                        while (scanner.hasNext()) {
                            data = scanner.nextLine();
                        }
                    }
                }
                EncryptionDecryption encryptionDecryption = null;
                if (algorithm.equals("unicode")) {
                    encryptionDecryption = new EncryptionDecryptionByUnicode();

                } else if (algorithm.equals("shift")) {
                    encryptionDecryption = new EncryptionDecryptionByShift();
                }

                if (actionToDo.equals("enc") && !isOutEnable) {
                    System.out.println(encryptionDecryption.encryption(data, key));
                } else if (actionToDo.equals("dec") && !isOutEnable){
                    System.out.println(encryptionDecryption.decryption(data, key));
                }

                if (isOutEnable) {
                    String result = "";

                    if (actionToDo.equals("enc")) {
                        result = encryptionDecryption.encryption(data, key);
                    } else if (actionToDo.equals("dec")) {
                        result = encryptionDecryption.decryption(data, key);
                    }

                    File fileOut = new File(nameOfFileOut);
                    FileWriter writer = new FileWriter(fileOut, false); // appends text to the file
                    writer.write(result);
                    writer.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error");
            }
        }

        public void getResult(EncryptionDecryption method) {

        }
    }


    public static void main(String[] args) {
        EncryptionDecryptionFactory encryptionDecryptionFactory = new EncryptionDecryptionFactory();
        encryptionDecryptionFactory.setMethod(args);
    }
}
