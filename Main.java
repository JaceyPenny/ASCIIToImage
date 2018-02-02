import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // sample change to commit
    public static final int LETTER_HEIGHT = 6;
    public static final int LETTER_WIDTH = 4;

    public static final HashMap<Character, Integer> LETTER_MAP = new HashMap<>();

    public static void main(String[] args) {
        setupLetterMap();


        Scanner scanner = new Scanner(System.in);
        System.out.print("1 - Image to ASCII\n2- ASCII to Image\n$ ");
        int input = Integer.parseInt(scanner.nextLine());

        System.out.print("Input file name: ");
        String inputFileName = scanner.nextLine();

        System.out.print("Output file name: ");
        String outputFileName = scanner.nextLine();

        if (input == 1) {
            try {
                double scaling = -1;
                while (scaling == -1) {
                    System.out.print("Scaling (default 1.0): ");
                    String scalingInput = scanner.nextLine();
                    if (scalingInput.equals("")) {
                        scaling = 1;
                        break;
                    }

                    try {
                        scaling = Double.parseDouble(scalingInput);
                    } catch (NumberFormatException e) {
                        scaling = -1;
                    }
                }

                BufferedImage image = ImageIO.read(new File(inputFileName));

                int scaledLetterHeight = (int) Math.floor(LETTER_HEIGHT / scaling);
                int scaledLetterWidth = (int) Math.floor(LETTER_WIDTH / scaling);

                int lineLength = image.getWidth() / scaledLetterWidth;
                int lineCount = image.getHeight() / scaledLetterHeight;

                System.out.println("Creating " + lineLength + " lines each with " + lineCount + " characters");

                PrintWriter fileWriter = new PrintWriter(new FileOutputStream(new File(outputFileName)));

                Raster raster = image.getData();

                for (int y = 0; y < lineCount; y++) {
                    StringBuilder thisLine = new StringBuilder();
                    for (int x = 0; x < lineLength; x++) {
                        char character = getCharForPosition(x, y, scaling, raster);
                        thisLine.append(character);
                    }
                    fileWriter.println(thisLine.toString());
                }

                fileWriter.close();
            } catch (IOException e) {

            }
        } else {
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(new File(inputFileName)));

                ArrayList<String> lines = new ArrayList<>();
                String nextLine;
                while ((nextLine = fileReader.readLine()) != null) {
                    lines.add(nextLine);
                }

                int asciiHeight = lines.size();
                int asciiWidth = lines.get(0).length();

                int imageHeight = asciiHeight * LETTER_HEIGHT;
                int imageWidth = asciiWidth * LETTER_WIDTH;

                BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

                for (int i_y = 0; i_y < asciiHeight; i_y++) {
                    for (int i_x = 0; i_x < asciiWidth; i_x++) {
                        char character = lines.get(i_y).charAt(i_x);
                        fillCharacterAtPosition(image, character, i_x, i_y);
                    }
                }

                ImageIO.write(image, "png", new File(outputFileName));

            } catch (IOException e) {

            }
        }
    }

    private static char getCharForPosition(int x, int y, double scaling, Raster image) {

        int grayscaleSum = 0;

        int scaledLetterHeight = (int) Math.ceil(LETTER_HEIGHT / scaling);
        int scaledLetterWidth = (int) Math.ceil(LETTER_WIDTH / scaling);

        System.out.println(scaledLetterHeight);
        System.out.println(scaledLetterWidth);

        for (int i_y = 0; i_y < scaledLetterHeight; i_y++) {
            for (int i_x = 0; i_x < scaledLetterWidth; i_x++) {
                int[] pixelData = new int[4];
                image.getPixel(x*scaledLetterWidth + i_x, y*scaledLetterHeight + i_y, pixelData);
                grayscaleSum += (pixelData[0] + pixelData[1] + pixelData[2]) / 3;
            }
        }

        grayscaleSum /= scaledLetterHeight * scaledLetterWidth;

        char closestCharBelow = 'M';
        int closestBelow = 0;

        char closestCharAbove = ' ';
        int closestAbove = 255;

        for (Map.Entry<Character, Integer> entry : LETTER_MAP.entrySet()) {
            char character = entry.getKey();
            int value = entry.getValue();
            if (value > grayscaleSum && value < closestAbove) {
                closestAbove = value;
                closestCharAbove = character;
            }

            if (value < grayscaleSum && value > closestBelow) {
                closestBelow = value;
                closestCharBelow = character;
            }
        }

        int aboveDifference = closestAbove - grayscaleSum;
        int belowDifference = grayscaleSum - closestBelow;

        if (aboveDifference < belowDifference) {
            return closestCharAbove;
        } else {
            return closestCharBelow;
        }
    }

    public static void fillCharacterAtPosition(BufferedImage image, char character, int c_x, int c_y) {
        int x = c_x * LETTER_WIDTH;
        int y = c_y * LETTER_HEIGHT;

        int shade;

        try {
            shade = LETTER_MAP.get(character);
        } catch (NullPointerException e) {
            System.out.println(character);
            throw new RuntimeException("Invalid character: " + character);
        }

        for (int i = 0; i < LETTER_WIDTH; i++) {
            for (int j = 0; j < LETTER_HEIGHT; j++) {
                image.setRGB(x+i, y+j, getGrayscalePixel(shade));
            }
        }
    }

    public static int getGrayscalePixel(int grayValue) {
        return 0xFF000000 + (grayValue << 16) + (grayValue << 8) + grayValue;
    }

    public static void setupLetterMap() {
        int count = 0;
        LETTER_MAP.put('M', count); count += 91;
        LETTER_MAP.put('N', count); count += 13;
        LETTER_MAP.put('D', count); count += 11;
        LETTER_MAP.put('8', count); count += 11;
        LETTER_MAP.put('O', count); count += 9;
        LETTER_MAP.put('Z', count); count += 9;
        LETTER_MAP.put('$', count); count += 9;
        LETTER_MAP.put('7', count); count += 9;
        LETTER_MAP.put('I', count); count += 9;
        LETTER_MAP.put('?', count); count += 11;
        LETTER_MAP.put('+', count); count += 9;
        LETTER_MAP.put('=', count); count += 9;
        LETTER_MAP.put('~', count); count += 11;
        LETTER_MAP.put(':', count); count += 9;
        LETTER_MAP.put(',', count); count += 6;
        LETTER_MAP.put('.', count);
        LETTER_MAP.put(' ', 255);

    }
}
