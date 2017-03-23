import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static final int LETTER_HEIGHT = 6;
    public static final int LETTER_WIDTH = 4;

    public static final HashMap<Character, Integer> LETTER_MAP = new HashMap<>();

    public static void main(String[] args) {
        setupLetterMap();

        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(new File("input.txt")));

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

            ImageIO.write(image, "png", new File("output.png"));

        } catch (IOException e) {

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
