import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * This is a modification of the general purpose Canvas, specially made for
 * the BlueJ "shapes" example. 
 *
 * @author: Bruce Quig
 * @author: Michael Kolling (mik)
 *
 * @version: 1.6 (shapes)
 */
public class Canvas{
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas(){
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Shapes Demo", 600, 600, 
                                         Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List <Object> objects;
    private HashMap <Object,ShapeDescription> shapes;
    
    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgClour  the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color bgColour){
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList <Object>();
        shapes = new HashMap <Object,ShapeDescription>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible){
        if(graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * @param  referenceObject  an object to define identity for this shape
     * @param  color            the color of the shape
     * @param  shape            the shape object to be drawn on the canvas
     */
     // Note: this is a slightly backwards way of maintaining the shape
     // objects. It is carefully designed to keep the visible shape interfaces
     // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape){
        objects.remove(referenceObject);   // just in case it was already there
        objects.add(referenceObject);      // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased 
     */
    public void erase(Object referenceObject){
        objects.remove(referenceObject);   // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * @param  newColour   the new colour for the foreground of the Canvas 
     */
    public void setForegroundColor(String colorString){
        if(colorString.equals("red"))
            graphic.setColor(Color.red);
        else if(colorString.equals("black"))
            graphic.setColor(Color.black);
        else if(colorString.equals("blue"))
            graphic.setColor(Color.blue);
        else if(colorString.equals("yellow"))
            graphic.setColor(Color.yellow);
        else if(colorString.equals("green"))
            graphic.setColor(Color.green);
        else if(colorString.equals("magenta"))
            graphic.setColor(Color.magenta);
        else if(colorString.equals("white"))
            graphic.setColor(Color.white);
        else if(colorString.equals("orange"))
            graphic.setColor(Color.decode("#FFA500"));
        else if(colorString.equals("purple"))
            graphic.setColor(Color.decode("#800080"));
        else if(colorString.equals("pink"))
            graphic.setColor(Color.decode("#FFC0CB"));
        else if(colorString.equals("brown"))
            graphic.setColor(Color.decode("#A52A2A"));
        else if(colorString.equals("cyan"))
            graphic.setColor(Color.decode("#00FFFF"));
        else if(colorString.equals("gray"))
            graphic.setColor(Color.decode("#808080"));
        else if(colorString.equals("lime"))
            graphic.setColor(Color.decode("#00FF00"));
        else if(colorString.equals("navy"))
            graphic.setColor(Color.decode("#000080"));
        else if(colorString.equals("teal"))
            graphic.setColor(Color.decode("#008080"));
        else if(colorString.equals("gold"))
            graphic.setColor(Color.decode("#FFD700"));
        else if(colorString.equals("maroon"))
            graphic.setColor(Color.decode("#800000"));
        else if(colorString.equals("olive"))
            graphic.setColor(Color.decode("#808000"));
        else if(colorString.equals("indigo"))
            graphic.setColor(Color.decode("#4B0082"));
        else if(colorString.equals("violet"))
            graphic.setColor(Color.decode("#EE82EE"));
        else if(colorString.equals("turquoise"))
            graphic.setColor(Color.decode("#40E0D0"));
        else if(colorString.equals("coral"))
            graphic.setColor(Color.decode("#FF7F50"));
        else if(colorString.equals("salmon"))
            graphic.setColor(Color.decode("#FA8072"));
        else if(colorString.equals("silver"))
            graphic.setColor(Color.decode("#C0C0C0"));
        else if(colorString.equals("crimson"))
            graphic.setColor(Color.decode("#DC143C"));
        else if(colorString.equals("chocolate"))
            graphic.setColor(Color.decode("#D2691E"));
        else if(colorString.equals("darkblue"))
            graphic.setColor(Color.decode("#00008B"));
        else if(colorString.equals("darkgreen"))
            graphic.setColor(Color.decode("#006400"));
        else if(colorString.equals("darkorange"))
            graphic.setColor(Color.decode("#FF8C00"));
        else if(colorString.equals("darkred"))
            graphic.setColor(Color.decode("#8B0000"));
        else if(colorString.equals("darkviolet"))
            graphic.setColor(Color.decode("#9400D3"));
        else if(colorString.equals("deeppink"))
            graphic.setColor(Color.decode("#FF1493"));
        else if(colorString.equals("deepskyblue"))
            graphic.setColor(Color.decode("#00BFFF"));
        else if(colorString.equals("dodgerblue"))
            graphic.setColor(Color.decode("#1E90FF"));
        else if(colorString.equals("firebrick"))
            graphic.setColor(Color.decode("#B22222"));
        else if(colorString.equals("forestgreen"))
            graphic.setColor(Color.decode("#228B22"));
        else if(colorString.equals("hotpink"))
            graphic.setColor(Color.decode("#FF69B4"));
        else if(colorString.equals("indianred"))
            graphic.setColor(Color.decode("#CD5C5C"));
        else if(colorString.equals("khaki"))
            graphic.setColor(Color.decode("#F0E68C"));
        else if(colorString.equals("lavender"))
            graphic.setColor(Color.decode("#E6E6FA"));
        else if(colorString.equals("lightblue"))
            graphic.setColor(Color.decode("#ADD8E6"));
        else if(colorString.equals("lightgreen"))
            graphic.setColor(Color.decode("#90EE90"));
        else if(colorString.equals("lightpink"))
            graphic.setColor(Color.decode("#FFB6C1"));
        else if(colorString.equals("limegreen"))
            graphic.setColor(Color.decode("#32CD32"));
        else if(colorString.equals("mediumblue"))
            graphic.setColor(Color.decode("#0000CD"));
        else if(colorString.equals("mediumpurple"))
            graphic.setColor(Color.decode("#9370DB"));
        else if(colorString.equals("mediumseagreen"))
            graphic.setColor(Color.decode("#3CB371"));
        else if(colorString.equals("midnightblue"))
            graphic.setColor(Color.decode("#191970"));
        else if(colorString.equals("orchid"))
            graphic.setColor(Color.decode("#DA70D6"));
        else if(colorString.equals("peru"))
            graphic.setColor(Color.decode("#CD853F"));
        else if(colorString.equals("plum"))
            graphic.setColor(Color.decode("#DDA0DD"));
        else if(colorString.equals("royalblue"))
            graphic.setColor(Color.decode("#4169E1"));
        else if(colorString.equals("saddlebrown"))
            graphic.setColor(Color.decode("#8B4513"));
        else if(colorString.equals("seagreen"))
            graphic.setColor(Color.decode("#2E8B57"));
        else if(colorString.equals("sienna"))
            graphic.setColor(Color.decode("#A0522D"));
        else if(colorString.equals("skyblue"))
            graphic.setColor(Color.decode("#87CEEB"));
        else if(colorString.equals("slateblue"))
            graphic.setColor(Color.decode("#6A5ACD"));
        else if(colorString.equals("springgreen"))
            graphic.setColor(Color.decode("#00FF7F"));
        else if(colorString.equals("tan"))
            graphic.setColor(Color.decode("#D2B48C"));
        else if(colorString.equals("tomato"))
            graphic.setColor(Color.decode("#FF6347"));
        else if(colorString.equals("wheat"))
            graphic.setColor(Color.decode("#F5DEB3"));
        else
            graphic.setColor(Color.black);
    }


    /**
     * Wait for a specified number of milliseconds before finishing.
     * This provides an easy way to specify a small delay which can be
     * used when producing animations.
     * @param  milliseconds  the number 
     */
    public void wait(int milliseconds){
        try{
            Thread.sleep(milliseconds);
        } catch (Exception e){
            // ignoring exception at the moment
        }
    }

    /**
     * Redraw ell shapes currently on the Canvas.
     */
    private void redraw(){
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
                       shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
       
    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase(){
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }


    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel{
        public void paint(Graphics g){
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class ShapeDescription{
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color){
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic){
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }

}
