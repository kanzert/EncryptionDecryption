package encryptdecrypt;

import com.sun.tools.javac.jvm.Code;

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
        public void setMethod(String[] args) {
            Arguments arguments = Parser.parse(args);
            EncryptionDecryption encryptionDecryption = null;
            encryptionDecryption = arguments.algorithm == Algorithm.Shift ? new EncryptionDecryptionByShift() : new EncryptionDecryptionByUnicode();
            String result = "";
            switch (arguments.coder) {
                case Encode:
                    if (!arguments.isOutEnable) {
                        System.out.println(encryptionDecryption.encryption(arguments.data, arguments.key));
                    } else {
                        result = encryptionDecryption.encryption(arguments.data, arguments.key);
                    }
                    break;
                case Decode:
                    if (!arguments.isOutEnable) {
                        System.out.println(encryptionDecryption.decryption(arguments.data, arguments.key));
                    } else {
                        result = encryptionDecryption.decryption(arguments.data, arguments.key);
                    }
                    break;
            }
            if (arguments.isOutEnable) {
                try {
                    File fileOut = new File(arguments.nameOfFileOut);
                    FileWriter writer = new FileWriter(fileOut, false); // appends text to the file
                    writer.write(result);
                    writer.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Error");
                }
            }
        }
    }


    public static void main(String[] args) {
        EncryptionDecryptionFactory encryptionDecryptionFactory = new EncryptionDecryptionFactory();
        encryptionDecryptionFactory.setMethod(args);
    }

    enum Coder {
        Encode("enc"),
        Decode("dec");

        private String coder;

        Coder(String method) {
            this.coder = method;
        }

        public String getCoder() {
            return coder;
        }
    }

    enum Algorithm {
        Shift("shift"),
        Unicode("unicode");

        private String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    public static class Arguments {
        Coder coder = Coder.Encode;
        String data = " ";
        boolean isInEnable = false;
        boolean isOutEnable = false;
        String nameOfFileIn = "";
        String nameOfFileOut = "";
        int key = 0;
        Algorithm algorithm = Algorithm.Shift;
    }

    static class Parser {
        public static Arguments parse(String[] args) {
            Arguments arguments = new Arguments();
            try {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-mode")) {
                        for (Coder coder : Coder.values()) {
                            if (coder.getCoder().equals(args[i + 1])) {
                                arguments.coder = coder;
                            }
                        }
                    }
                    if (args[i].equals("-key")) {
                        arguments.key = Integer.parseInt(args[i + 1]);
                    }

                    if (args[i].equals("-data") && !arguments.isInEnable) {
                        arguments.data = args[i + 1];
                    }

                    if (args[i].equals("-in")) {
                        arguments.isInEnable = true;
                        arguments.nameOfFileIn = args[i + 1];
                        //nameOfFileIn = "C:\\FromZeroToHero\\JavaProjects\\Encryption-Decryption\\Encryption-Decryption\\task\\src\\encryptdecrypt\\road_to_treasure.txt";
                    }

                    if (args[i].equals("-out")) {
                        arguments.isOutEnable = true;
                        arguments.nameOfFileOut = args[i + 1];
                        //nameOfFileOut = "C:\\FromZeroToHero\\JavaProjects\\Encryption-Decryption\\Encryption-Decryption\\task\\src\\encryptdecrypt\\protected.txt";
                    }

                    if (args[i].equals("-alg")) {
                        for (Algorithm algorithm : Algorithm.values()) {
                            if (algorithm.getAlgorithm().equals(args[i + 1])) {
                                arguments.algorithm = algorithm;
                            }
                        }
                    }
                }
                if (arguments.isInEnable) {
                    File fileIn = new File(arguments.nameOfFileIn);
                    try (Scanner scanner = new Scanner(fileIn)) {
                        while (scanner.hasNext()) {
                            arguments.data = scanner.nextLine();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error");
            }
            return arguments;
        }
    }
}