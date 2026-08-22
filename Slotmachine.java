import java.util.ArrayList;
import java.util.List;

/**
 * Simula una maquina tragamonedas 
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public class Slotmachine
{
    private List<Wheel> wheels;
    private List<String> symbols;
    private boolean ok;

    /**
     * Crear una maquina tragamonedas sin ruedas
     */
    public Slotmachine()
    {
        wheels = new ArrayList <>();
        symbols = new ArrayList <>();
        ok = true;
    }

    /**
     * Agregar una rueda nueva en la posicion dada, desplazando las
     * demas ruedas. Si la posicion es menor a 1 se usa 1, y si es mayor
     * al numero de ruedas mas uno se usa ese maximo. 
     *  @param pos posicion donde se va a insertar la rueda
     */
    public void addWheel(int pos){
        if(pos < 1){
            pos = 1;
        }
        if(pos > wheels.size() + 1){
            pos = wheels.size() + 1;
        }
        wheels.add(pos -1, new Wheel(pos));
        for(int i = 0; i <wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
        ok = true;
    }
    
    /**
     * Eliminar la rueda que esta en la posicion dada, desplazando las
     * demas ruedas. Si la maquina no tiene ruedas la operacion falla.
     * @param pos posicion de la rueda a eliminar
     */
    public void delWheel(int pos){
        if(wheels.size()==0){
            ok = false;
            return;
        }
        if (pos <1){
            pos = 1;
        }
        if(pos > wheels.size()){
            pos = wheels.size();
        }
        wheels.remove(pos-1);
        for (int i = 0; i < wheels.size(); i++){
            wheels.get(i).setPosition(i+1);
        }
        ok = true;
    }
    
    /**
     * Indica si la ultima operacion realizada sobre la maquina se completo con exito.
     * @return true si la ultima operacion fue exitosa, false en caso
     * contrario
     */
    public boolean ok(){
        return ok;
    }
    
    /**
     * Agregar un símbolo del color indicado en la posición dada
     * El color debe ser válido y no repetirse. Si la posición es menor que 1, se usa 1,
     * si supera el número de símbolos más uno, se usa ese máximo.
     * @param pos posición donde se insertará el símbolo 
     * @param color color del símbolo
     */
    public void addSymbol(int pos, String color){
        if(!(color.equals("red") || color.equals("black") || color.equals("blue")
            || color.equals("yellow") || color.equals("green") || color.equals("magenta")
            || color.equals("white"))){
            ok = false;
            return;
        }
        if(symbols.contains(color)){
            ok = false;
            return;
        }
        if(pos < 1){
            pos = 1;
        }
        if(pos > symbols.size() + 1){
            pos = symbols.size() + 1;
        }
        symbols.add(pos - 1, color);
        ok = true;
    }
    
     /**
     * Eliminar el simbolo con el color dado. Falla si ese color no
     * esta registrado en la maquina.
     * @param symbol color del simbolo a eliminar
     */
    public void delSymbol(String symbol){
        if(!symbols.contains(symbol)){
            ok = false;
            return;
        }
        symbols.remove(symbol);
        ok = true;
    }
}