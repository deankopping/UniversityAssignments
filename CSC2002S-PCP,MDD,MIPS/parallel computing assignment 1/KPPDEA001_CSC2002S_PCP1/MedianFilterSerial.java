import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Programme to apply median filter to image
 *
 * @author Dean Kopping
 * @version 1.0
 */

public class MedianFilterSerial {
    static int WinWidth = 0;
    static long StartTime = 0;
    static BufferedImage NewImage = null;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        MedianFilterSerial filt = new MedianFilterSerial();

        // Read in image
        
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
               
                int[] Pixels = new int[WinWidth*WinWidth];
                
                int current = 0;
                for (int x = xCoord - edgeWidth; x < xCoord + edgeWidth; x++) {
                    for (int y = yCoord - edgeWidth; y < yCoord + edgeWidth; y++) {
                        if (x >= 0 && y >= 0 && y < height && x < width) {
                            
                            Pixels[current] = img.getRGB(x, y);

                            current++;
                        }

                    }
                }
                Arrays.sort(Pixels);
                int median = Pixels[current/2];
                NewImage.setRGB(xCoord, yCoord, median);
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
