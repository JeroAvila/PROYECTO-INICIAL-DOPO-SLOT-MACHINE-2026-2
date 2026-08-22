
/**
 * Es la representacion de una rueda en la maquina tragamonedas
 *
 * @author Thomas Jeronimo Avila Casillo
 * @author Laura Juliana Parra Velandia
 * @version (a version number or a date)
 */
public class Wheel
{
    private int position;
    private Rectangle frame;
    /**
     * Crea una rueda vacia en la posicion dada. for objects of class Wheel
     */
    public Wheel(int position)
    {
        this.position = position;
        frame = new Rectangle();
    }

    /**
     * Cambia la posicion logica de la rueda, usado cuando se agregan o
     * eliminan ruedas en la maquina y hay que renumerar las demas.
     */
    public void setPosition(int position){
          this.position =  position;
    }
    
    /**
     * Consulta la posicion actual de la rueda.
     */
    public int getPosition(){
        return position;
    }
}