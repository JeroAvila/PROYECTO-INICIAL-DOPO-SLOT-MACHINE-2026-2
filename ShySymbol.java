
/**
 * shysimbol o simbolo timido que lo que hace es que es un cuadrado
 * que alterna entre visible e invisible cuando la rueda lo selecciona
 * @autor Laura Juliana Velandia
 * @autor Thomas Jeronimo Avila Castillo
 */

public class ShySymbol extends Symbol {
    private Rectangle shape;
    private boolean hidden;
    private int shapeX;     
    private int shapeY;
    
    /**
     * crea el simbolo timido de un color dado y lo hace visible
     * @param color es el color del simbolo
     */    
    public ShySymbol(String color){
        super(color);
        shape = new Rectangle();
        shape.changeSize(30, 30);
        shape.changeColor(color);
        hidden = false;
        shapeX = 70;
        shapeY = 15;
    }
    /**
     * retorna un simbolo timido mas con el mismo color y lo hace visible
     */
    public Symbol copy(){
        return new ShySymbol(getColor());
    }
    /**
     * retorna si el simbolo esta invisible
     */
    public boolean isHidden(){
        return hidden;
    }
    /**
     * alterana entre visible y invisible
     */
    public void select(){
        hidden = !hidden;
        if(hidden){
            shape.makeInvisible();
        }
    }
    /**
     * hace visible
     */
    public void makeVisible(){
        if(!hidden){
            shape.makeVisible();
        }
    }
    /**
     * se hace Invisible
     */
    public void makeInvisible(){
        if(!hidden){
            shape.makeVisible();
        }
    }
    /**
     * ubicacion del cuadrado en el espacio
     * @param x esquina izquierda del espacio
     * @param y esquina superior del espacio
     */
    public void setPosition(int x, int y){
        shape.moveHorizontal(x - shapeX);
        shape.moveVertical(y - shapeY);
        shapeX = x;
        shapeY = y;
    }
    /**
     * redibuja el cuadrado al frente
     */
    public void bringToFront(){
        shape.moveHorizontal(0);
    }
}