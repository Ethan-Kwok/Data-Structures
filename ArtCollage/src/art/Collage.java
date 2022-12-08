package art;

import java.awt.Color;

/*
 * This class contains methods to create and perform operations on a collage of images.
 * 
 * @author Ana Paula Centeno
 */ 

public class Collage {

    private Picture originalPicture;

    private Picture collagePicture;

    private int collageDimension;

    private int tileDimension;
    
    public Collage (String filename) {
        this.collageDimension = 4;
        this.tileDimension = 150;
        this.originalPicture = new Picture(filename);
        this.collagePicture = new Picture(tileDimension*collageDimension, tileDimension*collageDimension);
        scale(originalPicture, collagePicture);
    }
  
    public Collage (String filename, int td, int cd) {
        this.collageDimension = cd;
        this.tileDimension = td;
        this.originalPicture = new Picture(filename);
        this.collagePicture = new Picture(tileDimension*collageDimension, tileDimension*collageDimension);
        scale(originalPicture, collagePicture);
    }

    public static void scale (Picture originalPic, Picture collagePic) {
        int width = collagePic.width();
        int height = collagePic.height();
        for (int collageCol = 0; collageCol < width; collageCol++) {
            for (int collageRow = 0; collageRow < height; collageRow++) {
                int originalCol = collageCol * originalPic.width()  / width;
                int originalRow = collageRow * originalPic.height() / height;
                Color color = originalPic.get(originalCol, originalRow);
                collagePic.set(collageCol, collageRow, color);
            }
        }
    }

    public int getCollageDimension() {
        return collageDimension;
    }

    public int getTileDimension() {
        return tileDimension;
    }

    public Picture getOriginalPicture() {
        return originalPicture;
    }

    public Picture getCollagePicture() {
        return collagePicture;
    }
  
    public void showOriginalPicture() {
        originalPicture.show();
    }
 
    public void showCollagePicture() {
	    collagePicture.show();
    }
  
    public void makeCollage () {
        int width = tileDimension;
        int height = tileDimension;
        for (int i = 0; i < collageDimension; i++) {
            for (int j = 0; j < collageDimension; j++) {
                for (int collageCol = 0; collageCol < width; collageCol++) {
                    for (int collageRow = 0; collageRow < height; collageRow++) {
                        int originalCol = collageCol * originalPicture.width()  / width;
                        int originalRow = collageRow * originalPicture.height() / height;
                        Color color = originalPicture.get(originalCol, originalRow);
                        collagePicture.set(collageCol + i*tileDimension, collageRow + j*tileDimension, color);
                    }
                }
            }
        }

    }

    public void colorizeTile (String component, int collageCol, int collageRow) {
        /*if (component.equals("green")) {
            for (int i = 0; i < tileDimension; i++) {
                for (int j = 0; j < tileDimension; j++) {
                    Color color = collagePicture.get(j+collageCol*tileDimension, i+collageRow*tileDimension);
                    int g = color.getGreen();
                    collagePicture.set(j+collageCol*tileDimension, i+collageRow*tileDimension, new Color(0, g, 0));
                }
            }
        }*/
        if (component.equals("green")) {
            for (int i = 0+collageRow*tileDimension; i < tileDimension+collageRow*tileDimension; i++) {
                for (int j = 0+collageCol*tileDimension; j < tileDimension+collageCol*tileDimension; j++) {
                    Color color = collagePicture.get(collageCol, collageRow);
                    int g = color.getGreen();
                    collagePicture.set(j, i, new Color(0, g, 0));
                }
            }
        }
        else if (component.equals("blue")) {
            for (int i = 0; i < tileDimension; i++) {
                for (int j = 0; j < tileDimension; j++) {
                    Color color = collagePicture.get(j+collageCol*tileDimension, i+collageRow*tileDimension);
                    int b = color.getBlue();
                    collagePicture.set(j+collageCol*tileDimension, i+collageRow*tileDimension, new Color(0, 0, b));
                }
            }
        }
        else {
            for (int i = 0; i < tileDimension; i++) {
                for (int j = 0; j < tileDimension; j++) {
                    Color color = collagePicture.get(j+collageCol*tileDimension, i+collageRow*tileDimension);
                    int r = color.getRed();
                    collagePicture.set(j+collageCol*tileDimension, i+collageRow*tileDimension, new Color(r, 0, 0));
                }
            }
        }
    }

    public void replaceTile (String filename,  int collageCol, int collageRow) {
        
        this.originalPicture = new Picture(filename);

        int width = tileDimension;
        int height = tileDimension;

        for (int targetCol = collageCol*tileDimension; targetCol < width; targetCol++) {
            for (int targetRow = +collageRow*tileDimension; targetRow < height; targetRow++) {
                int sourceCol = targetCol * originalPicture.width()  / width;
                int sourceRow = targetRow * originalPicture.height() / height;
                Color color = originalPicture.get(sourceCol, sourceRow);
                collagePicture.set(targetCol, targetRow, color);
            }
        }

    }

    public void grayscaleTile (int collageCol, int collageRow) {
        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {
                Color color = collagePicture.get(j+collageCol*tileDimension, i+collageRow*tileDimension);
                //Color gray = toGray(color);
                collagePicture.set(j+collageCol*tileDimension, i+collageRow*tileDimension, toGray(color));
            }
        }
    }

    private static double intensity(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if (r == g && r == b) return r;   // to avoid floating-point issues
        return 0.299*r + 0.587*g + 0.114*b;
    }

    private static Color toGray(Color color) {
        int y = (int) (Math.round(intensity(color)));   // round to nearest int
        Color gray = new Color(y, y, y);
        return gray;
    }

    public void closeWindow () {
        if ( originalPicture != null ) {
            originalPicture.closeWindow();
        }
        if ( collagePicture != null ) {
            collagePicture.closeWindow();
        }
    }
}
