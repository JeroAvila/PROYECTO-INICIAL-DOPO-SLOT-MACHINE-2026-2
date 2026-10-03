/**
 * Simbolo efimero: un circulo del color del simbolo que se encoge cada vez que
 * la rueda lo selecciona, hasta quedar como un punto.
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class EphemeralSymbol extends Symbol
{
    private static final int FULL_SIZE = 30;
    private static final int STEP = 5;
    private static final int DOT_SIZE = 2;
    private Circle shape;
    private int size;
    private int slotX; // esquina del espacio de 30x30 donde esta el simbolo
    private int slotY;
    private int shapeX; // esquina del circulo
    private int shapeY;

    /**
     * Crea un simbolo efimero del color dado, con su tamano completo.
     * @param color color del simbolo
     */
    public EphemeralSymbol(String color)
    {
        super(color);
        shape = new Circle();
        shape.changeColor(color);
        size = FULL_SIZE;
        slotX = 20; // posicion por defecto de Circle
        slotY = 15;
        shapeX = 20;
        shapeY = 15;
    }

    /**
     * @return un simbolo efimero nuevo con el mismo color y el tamano completo
     */
    public Symbol copy()
    {
        return new EphemeralSymbol(getColor());
    }

    /**
     * @return el diametro actual del circulo
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Encoge el circulo un paso. Nunca baja del tamano de un punto.
     */
    public void select()
    {
        size = size - STEP;
        if(size < DOT_SIZE){
            size = DOT_SIZE;
        }
        shape.changeSize(size);
        place();
    }

    public void makeVisible()
    {
        shape.makeVisible();
    }

    public void makeInvisible()
    {
        shape.makeInvisible();
    }

    /**
     * Ubica el circulo en el centro del espacio de 30x30 dado.
     * @param x esquina izquierda del espacio
     * @param y esquina superior del espacio
     */
    public void setPosition(int x, int y)
    {
        slotX = x;
        slotY = y;
        place();
    }

    public void bringToFront()
    {
        shape.moveHorizontal(0);
    }

    /**
     * Mueve el circulo para que quede centrado en su espacio con el tamano actual.
     */
    private void place()
    {
        int offset = (FULL_SIZE - size) / 2;
        shape.moveHorizontal(slotX + offset - shapeX);
        shape.moveVertical(slotY + offset - shapeY);
        shapeX = slotX + offset;
        shapeY = slotY + offset;
    }
}