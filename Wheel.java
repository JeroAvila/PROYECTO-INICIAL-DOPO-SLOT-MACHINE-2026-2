
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
    private List<String> symbols; // guarda los simbolos en una lista
    private int currentIndex;  //indica el cual simbolo se esta mostrando
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
        /**
     * MC3
     * recibe la lista de simbolos de la maquina y la guarda como una copia.
     * el currentIndex vuelve a cero cada vez que se actualiza los simbolos
     */
    public void setSymbols(List<String> symbols) {
        this.symbols = new ArrayList<>(symbols);
        currentIndex = 0;
    }
    /**
     * devuelve el color del simbolo que se esta mostrando actualmente.
     * si la rueda no tiene simbolos da null
     */
    public String getCurrentSymbol() {
        if(symbols.isEmpty()){
            return null;
        }
        return symbols.get(currentIndex);
    }
    /**
     * Avanza al siguiente simbolo. 
     * devuelve el residuo de la division para que cuando llegue al final vuelva a cero
     */
    public void spin() {
        if(!symbols.isEmpty()){
            currentIndex = (currentIndex + 1) % symbols.size();
        }
    }
    /**
     * busca el simbolo en la lista, si no encuentra da -1 y el metodo da false
     * si lo encuentra mueve el currentIndex a esa posicio para que sea visible el simbolo
     */
    public boolean placeSymbol(String symbol){
        int index = symbols.indexOf(symbol);
        if (index == -1){
            return false;
        }
        currentIndex = index;
        return true;
    }
    /**
     * metodo de shapes que hace que sea visible
     */
    public void makeVisible(){
        frame.makeVisible();
    }
    /**
     * metodo de shape que hace que sea invisible
     */
    public void makeInvisible(){
        frame.makeInvisible();
    }
}
