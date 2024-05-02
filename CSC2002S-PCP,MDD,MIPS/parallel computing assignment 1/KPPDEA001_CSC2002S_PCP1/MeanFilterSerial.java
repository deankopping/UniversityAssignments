import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Programme to apply mean filter to image
 *
 * @author Dean Kopping
 * @version 1.0
 */

public class MeanFilterSerial {

    static int WinWidth = 0;
    static long StartTime = 0;
    static BufferedImage NewImage = null;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        MeanFilterSerial filt = new MeanFilterSerial();

        // Read in image
        BufferedImage image = null;
        File f = null;

        File NewFile = null;
        BufferedImage img = null;
        String imgName = args[0];

        try {
            f = new File(imgName);
            img = ImageIO.read(f);
            NewImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        } catch (IOException e) {
            System.out.println(e);
        }

        
        WinWidth = Integer.valueOf(args[2]);
        if (WinWidth < 3 || WinWidth % 2 == 0) {

                System.out.println("Invalid window width");
                System.exit(0);
        }

        
        filt.tick();
        filt.NewVal(img);
        tock();

        try {

            String newI = args[1];
            NewFile = new File(newI);
            ImageIO.write(NewImage, "jpg", NewFile);

        } catch (IOException e) {

            System.out.println(e);
        }

    }

    public void NewVal(BufferedImage img) {

        // get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        int xCoord = 0;
        int yCoord = 0;
        // Loops through whole image
        int edgeWidth = Math.floorDiv(WinWidth, 2);
        while (xCoord != width) {
            

            while (yCoord != height) {
               
                int sumOfPixelsA = 0;
                int sumOfPixelsR = 0;
                int sumOfPixelsG = 0;
                int sumOfPixelsB = 0;

                int validPos = 0;
                for (int x = xCoord - edgeWidth; x < xCoord + edgeWidth; x++) {
                    for (int y = yCoord - edgeWidth; y < yCoord + edgeWidth; y++) {
                        if (x >= 0 && y >= 0 && y < height && x < width) {

                            validPos++;

                            int p = img.getRGB(x, y);
                            sumOfPixelsR = sumOfPixelsR + ((p>>16) & 0xff);
                            sumOfPixelsA = sumOfPixelsA + ((p>>24) & 0xff);
                            sumOfPixelsG = sumOfPixelsG + ((p>>8) & 0xff);
                            sumOfPixelsB = sumOfPixelsB + (p & 0xff);
                        }

                    }
                }

                int meanA = sumOfPixelsA / validPos;
                int meanR = sumOfPixelsR / validPos;
                int meanG = sumOfPixelsG / validPos;
                int meanB = sumOfPixelsB / validPos;

                int meanPix =0;

                meanPix = (meanA<<24) | (meanR<<16) | (meanG<<8) | meanB;

                NewImage.setRGB(xCoord, yCoord, meanPix);

                yCoord++;
            }
            yCoord=0;
            xCoord++;
        }
       
    }
    public void tick(){
        StartTime = System.currentTimeMillis();
    }

    public static float tock(){
        return (System.currentTimeMillis() - StartTime);
    }
}
