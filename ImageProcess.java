package com.company;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class ImageProcess {
    public static void main(String[] args) {
        int[][] imageData = imgToTwoD("./kitten.jpg");
        //viewImageData(imageData);

        int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed, "./trimmed_kitten.jpg");

        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "./negative_kitten.jpg");

        int[][] stretched = stretchHorizontally(imageData);
        twoDToImage(stretched, "./stretched_kitten.jpg");

        int[][] shrunken = shrinkVertically(imageData);
        twoDToImage(shrunken, "./shrunk_kitten.jpg");

        int[][] invert = invertImage(imageData);
        twoDToImage(invert, "./invert_kitten.jpg");

        int[][] newCol = colorFilter(imageData,100,30,180);
        twoDToImage(newCol, "./newCol_kitten.jpg");

        int[][] blankCanvas = new int[500][500];
        int[][] randomImg = paintRandomImage(blankCanvas);
        twoDToImage(randomImg, "./rand_img.jpg");

        int[] rgba = {125,75,3,255};
        int[][] rectangleImg = paintRectangle(imageData,100,200,100,100,getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImg, "./rectangle_kitten.jpg");

        int[][] rectangleGen = generateRectangles(imageData, 3);
        twoDToImage(rectangleImg, "./recGen_kitten.jpg");
        // int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));
        // Painting with pixels


    }
    // Image Processing Methods
    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        // Example Method
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }
    public static int[][] negativeColor(int[][] imageTwoD) {

        int[][] negImage = new int[imageTwoD.length][imageTwoD[0].length];

        for(int i =0; i<imageTwoD.length; i++){
            for (int j = 0; j< imageTwoD[i].length; j++){
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                negImage[i][j] = getColorIntValFromRGBA(rgba);
            }
        }

        return negImage;
    }
    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] stretchedImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
        int it =0;
        for(int i =0; i<imageTwoD.length; i++){
            for(int j =0; j<imageTwoD[i].length; j++){
                it = j*2;
                stretchedImg[i][it] = imageTwoD[i][j];
                stretchedImg[i][it+1] = imageTwoD[i][j];
            }
        }
        return stretchedImg;
    }
    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] shrinkVert = new int[imageTwoD.length/2][imageTwoD[0].length];
        for(int i = 0; i<imageTwoD[0].length; i++){
            for (int j = 0; j < imageTwoD.length-1; j+=2){
                shrinkVert[j/2][i] = imageTwoD[j][i];
            }
        }

        return shrinkVert;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] invertImg = new int[imageTwoD.length][imageTwoD[0].length];
        for(int i =0; i<imageTwoD.length;i++){
            for (int j=0; j<imageTwoD[0].length;j++){
                invertImg[i][j] = imageTwoD[(imageTwoD.length-1)-i][(imageTwoD[i].length-1)-j];
            }
        }

        return invertImg;
    }
    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] newColor = new int[imageTwoD.length][imageTwoD[0].length];

        for(int i =0; i<newColor.length; i++){
            for (int j = 0; j < newColor[0].length;j++){
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                int red = rgba[0] + redChangeValue;
                int green = rgba[1] + greenChangeValue;
                int blue = rgba[2] + blueChangeValue;
                int a = rgba[3];

                if(red<0){
                    red = 0;
                } else if (red>255){
                    red = 255;
                }

                if(green<0){
                    green = 0;
                } else if (green>255){
                    green = 255;
                }

                if(blue<0){
                    blue = 0;
                } else if (blue>255){
                    blue = 255;
                }

                if(a<0){
                    a = 0;
                } else if (a>255){
                    a = 255;
                }

                rgba[0] = red;
                rgba[1] = green;
                rgba[2] = blue;
                rgba[3] = a;
                newColor[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return newColor;
    }

    // Painting Methods
    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        for(int i = 0; i< canvas.length; i++){
            for(int j =0; j< canvas[0].length; j++){
                int ranRed = rand.nextInt(256);
                int ranGreen = rand.nextInt(256);
                int ranBlue = rand.nextInt(256);
                int[] rgba = {ranRed, ranGreen, ranBlue, 255};
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }
    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {

        for(int i =0; i<canvas.length; i++){
            for(int j = 0; j<canvas[0].length; j++){
                if(i>=rowPosition && i<= rowPosition + width){
                    if(j>= colPosition && j<= colPosition + height){
                        canvas[i][j] = color;
                    }
                }
            }
        }
        return canvas;
    }
    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for(int i = 1; i<numRectangles; i++){
            int width = rand.nextInt(canvas[0].length);
            int height = rand.nextInt(canvas.length);
            int rowPos = rand.nextInt(canvas.length-height);
            int colPos = rand.nextInt(canvas[0].length-width);

            int rgba[] = {rand.nextInt(256),rand.nextInt(256),rand.nextInt(256),255};
            int randomColor = getColorIntValFromRGBA(rgba);
            canvas = paintRectangle(canvas, width, height, rowPos, colPos, randomColor);
        }
        return canvas;
    }
    // Utility Methods
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }
            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }
            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }
    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }
            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }
    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }
    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }
    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from top the left corner.");
            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
