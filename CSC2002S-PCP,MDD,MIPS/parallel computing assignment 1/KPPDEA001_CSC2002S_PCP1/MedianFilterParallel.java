import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MedianFilterParallel extends RecursiveTask<BufferedImage> {

    static int WinWidth = 0;
    static long StartTime = 0;
    static BufferedImage NewImage = null;

    static final int SEQUENTIAL_CUTOFF = 80;

    static BufferedImage img;
    int hi;
    int lo;

    public MedianFilterParallel(BufferedImage img, int lo, int hi) {
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
        MedianFilterParallel mfp = new MedianFilterParallel((img), 0, img.getWidth());

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

            MedianFilterParallel left = new MedianFilterParallel(img, lo, (hi + lo) / 2);
            MedianFilterParallel right = new MedianFilterParallel(img, (hi + lo) / 2, hi);
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
                int median = Pixels[current / 2];
                NewImage.setRGB(xCoord, yCoord, median);
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
