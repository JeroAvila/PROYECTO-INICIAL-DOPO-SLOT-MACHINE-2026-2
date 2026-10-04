/**
 * rueda normal, gira, se bloquea, se intercambia y se puede eliminar
 * @author laura Juliana Parra Velandia
 * @autor Thomas Jeronimo Avila
 */
public class NormalWheel extends Wheel{
    /**
     * CONSTRUCTOR
     * crea una rueda normal
     * @param position posicion logica de la rueda
     */
    public NormalWheel(int position){
        super(position, "white");
    }
    /**
     * retorna 
     * "normal"
     */
    public String getType(){
        return "normal";
    }
}