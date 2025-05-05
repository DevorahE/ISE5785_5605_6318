package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;

class ImageWriterTest {

    /**
     * Test method for the writeToImage functionality of the ImageWriter class.
     * This test creates an image of 800x500 pixels with a grid pattern.
     * The grid lines are colored grey, and the rest of the pixels are turquoise.
     * The resulting image is saved with the name "firstImage".
     */
    @Test
    void testWriteToImage() {
        ImageWriter imageWriter = new ImageWriter(800, 500);

        Color turquoise = new Color(26, 241,225);
        Color grey = new Color(71, 90,89);
        for(int i=0; i < imageWriter.nX(); i++)
        {
            for(int j=0; j < imageWriter.nY(); j++)
            {
                imageWriter.writePixel(i, j, i % 50 == 0 || j % 50 == 0 ? grey : turquoise);
            }
        }
        imageWriter.writeToImage("firstImage");
    }

}