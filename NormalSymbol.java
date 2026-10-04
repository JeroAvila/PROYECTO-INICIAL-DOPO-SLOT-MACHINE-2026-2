/**
 * Simbolo normal: un triangulo del color del simbolo.
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class NormalSymbol extends Symbol{
    private Triangle shape;
    private int shapeX; // vertice superior del triangulo
    private int shapeY;

    /**
     * Crea un simbolo normal del color dado.
     * @param color color del simbolo
     */
    public NormalSymbol(String color)
    {
        super(color);
        shape = new Triangle();
        shape.changeSize(30, 30);
        shape.changeColor(color);
        shapeX = 140; 
        shapeY = 15;
    }

    /**
     * @return un simbolo normal nuevo con el mismo color
     */
    public Symbol copy()
    {
        return new NormalSymbol(getColor());
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
     * Ubica el triangulo dentro del espacio de 30x30 dado.
     * @param x esquina izquierda del espacio
     * @param y esquina superior del espacio
     */
    public void setPosition(int x, int y)
    {
        int targetX = x + 15;
        shape.moveHorizontal(targetX - shapeX);
        shape.moveVertical(y - shapeY);
        shapeX = targetX;
        shapeY = y;
    }

    public void bringToFront()
    {
        shape.moveHorizontal(0);
    }
}