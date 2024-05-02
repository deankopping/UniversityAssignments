import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MeanFilterParallel extends RecursiveTask<BufferedImage> {

    static int WinWidth = 0;
    static long StartTime = 0;
    static BufferedImage NewImage = null;

    static final int SEQUENTIAL_CUTOFF = 80;

    static BufferedImage img;
    int hi;
    int lo;

    public MeanFilterParallel(BufferedImage img, int lo, int hi) {
        this.img = img;
        this.lo = lo;
        this.hi = hi;

    }

    public static void readIn() {

        Scanner in = new Scanner(System.in);

        // Read in image

        File f = null;

        try {
            f = new File(imgName);
            img = ImageIO.read(f);
            NewImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        } catch (IOException e) {
            System.out.println(e);
        }


        if (WinWidth < 3 || WinWidth % 2 == 0) {


            System.out.println("Invalid window width");
            System.exit(0);
            
        }

    }

    static String imgName;

    public static void main(String[] args) {
        imgName = args[0];
        String newI = args[1];
        WinWidth = Integer.valueOf(args[2]);

        readIn();
        MeanFilterParallel mfp = new MeanFilterParallel((img), 0, img.getWidth());

        mfp.tick();
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mfp);
        tock();

        File NewFile = null;

        try {
            
            NewFile = new File(newI);
            ImageIO.write(NewImage, "jpg", NewFile);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    protected BufferedImage compute() {

        if (hi - lo <= SEQUENTIAL_CUTOFF) {

            NewVal(img, lo, hi);
            return img;
        }

        else {

            MeanFilterParallel left = new MeanFilterParallel(img, lo, (hi + lo) / 2);
            MeanFilterParallel right = new MeanFilterParallel(img, (hi + lo) / 2, hi);
            left.fork();
            right.compute();
            left.join();
            return img;

        }

    }

    public static void NewVal(BufferedImage img, int lo, int hi) {

        // get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        int xCoord = lo;
        int yCoord = 0;
        // Loops through whole image
        int edgeWidth = Math.floorDiv(WinWidth, 2);
        while (xCoord <= hi - 1) {

            while (yCoord != height) {

                int[] Pixels = new int[WinWidth * WinWidth];

                
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
            yCoord = 0;
            xCoord++;

        }

    }

    public void tick() {
        StartTime = System.currentTimeMillis();
    }

    public static float tock() {
        return (System.currentTimeMillis() - StartTime);
    }

}
