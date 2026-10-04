/**
 * Rueda loca. cuando se agrega una rueda a la maquina 
 * mete 3 ruedas mas y todos los cambios que se hagan a esta rueda tambien afecta
 * a las otras 3 creadas
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * 
 */
public class CrazyWheel extends Wheel{
    private static final int COMPANIONS = 3;
    
    /**
     * crea una rueda loca en la posicion
     * @param position lo que hace es la posicion logica de la rueda
     */
    public CrazyWheel(int position){
        super(position, "magenta");
    }
    /**
     * retorna "crazy"
     */
    public String getType(){
        return "crazy";
    }
    /**
     * Retorna 3 que son las ruedas que llegan junto con esta n+3
     * 
     */
    public int getCompanions(){
        return COMPANIONS;
    }
}
