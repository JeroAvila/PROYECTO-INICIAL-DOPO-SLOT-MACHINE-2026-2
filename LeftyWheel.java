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
     * sobreescribe Wheel.spin() en lugar de eligir al azar
     * copia a la vecina de la izquierda
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
