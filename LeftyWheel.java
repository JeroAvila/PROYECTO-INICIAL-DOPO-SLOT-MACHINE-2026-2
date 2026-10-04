/**
 * Rueda Lefty, rueda zurda. 
 * si hay una rueda a la izquierda, gira su estado mostrando el mismo que el
 * de la izquierda, gira como una rueda normal
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 */
public class LeftyWheel extends Wheel{
    /**
     * CONSTRUCTOR
     * crea una rueda lefty en la posicon dada
     * @param position posicion logica de la rueda
     */
    public LeftyWheel(int position){
        super(position, "blue");
    }
    /**
     * retorna "lefty"
     */
    public String getType(){
        return "lefty";
    }
    /**
     * gira la rueda
     * si no tiene vecina a la izq gira al azar como la normal
     * si tiene vecina, copia el simvolo que muestra, si esta
     * bloqueada o sin algun simbolo no hace nada
     * 
     */
    public void spin(){
        Wheel neighbor = getLeft();
        if(neighbor == null){
            super.spin();
        } else if(!isLocked() && hasSymbols()){
            copyStateOf(neighbor);
        }
    }
}
