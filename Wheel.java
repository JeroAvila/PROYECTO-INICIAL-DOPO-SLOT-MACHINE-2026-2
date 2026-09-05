import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private Circle symbolShape;
    private List<String> symbols; 
    private int currentIndex;  //indica el cual simbolo se esta mostrando (-1 = ninguno)
    private boolean winning; // indica si esta rueda hace parte de un jackpot
    private boolean visible;
    private boolean locked;
    private int frameX, frameY; // posicion actual del frame en pantalla
    private int symX, symY;     // posicion actual del circulo del simbolo
    private Random random;
    /**
     * CONSTRUCTOR
     * Crea una rueda vacia en la posicion dada. for objects of class Wheel
     */
    public Wheel(int position)
    {
        this.position = position;
        frame = new Rectangle();
        frame.changeColor("gray");
        symbolShape = new Circle();
        symbols = new ArrayList<>();
        currentIndex = -1; 
        winning = false;
        visible = false;
        frameX = 70; frameY = 15;   
        symX = 20; symY = 15;       
        random = new Random();
        updatePosition();
    }
    
    /**
     * Cambia la posicion logica de la rueda, usado cuando se agregan o
     * eliminan ruedas en la maquina y hay que renumerar las demas.
     * Tambien reubica el frame y el circulo en pantalla para que no
     * queden varias ruedas dibujadas una encima de otra.
     */
    public void setPosition(int position){
          this.position =  position;
          updatePosition();
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
     * Si el simbolo que ya estaba visible sigue existiendo en la lista
     * nueva, se mantiene; si no, la rueda queda sin simbolo asignado.
     */
    public void setSymbols(List<String> symbols) {
        String previousColor = getCurrentSymbol();
        this.symbols = new ArrayList<>(symbols);
        if(previousColor != null && this.symbols.contains(previousColor)){
            currentIndex = this.symbols.indexOf(previousColor);
        } else {
            currentIndex = -1;
        }
        updateColor();
    }
    
    /**
     * devuelve el color del simbolo que se esta mostrando actualmente.
     * si la rueda no tiene simbolo asignado da null
     */
    public String getCurrentSymbol() {
        if(symbols.isEmpty() || currentIndex == -1){
            return null;
        }
        return symbols.get(currentIndex);
    }
    
    /**
     * Elige un simbolo al azar del catalogo de esta rueda.
     */
    public void spin() {
        if(!locked && !symbols.isEmpty()){
            currentIndex = random.nextInt(symbols.size());
            updateColor();
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
        updateColor();
        return true;
    }
    
    /**
     * marca si esta rueda hace parte de la combinacion ganadora actual.
     * si winning es true, el fondo (frame) se pone amarillo; si es
     * false, vuelve a su gris normal.
     * @param winning true si la maquina esta en estado ganador
     */
    public void setWinning(boolean winning){
        this.winning = winning;
        frame.changeColor(winning ? "yellow" : "gray");
        if(visible && getCurrentSymbol() != null){
            symbolShape.makeVisible(); 
        }
    }
    
    /**
     * metodo de shapes que hace que sea visible
     */
    public void makeVisible(){
        visible = true;
        frame.makeVisible();
        if(getCurrentSymbol() != null){
            symbolShape.makeVisible();
        }
    }
    
    /**
     * metodo de shape que hace que sea invisible
     */
    public void makeInvisible(){
        visible = false;
        frame.makeInvisible();
        symbolShape.makeInvisible();
    }
    
    /**
     * Vuelve a dibujar el frame y el circulo sin moverlos, para que
     * queden al frente en el Canvas (por ejemplo, despues de que el
     * fondo negro de la maquina cambie de tamano y quede encima).
     */
    public void bringToFront(){
        frame.moveHorizontal(0);
        symbolShape.moveHorizontal(0);
    }
    
    /**
     * actualiza el circulo del simbolo para que coincida con el color
     * actualmente visible. Si no hay simbolo asignado (getCurrentSymbol
     * da null), el circulo se oculta.
     */
    private void updateColor(){
        String color = getCurrentSymbol();
        if(color != null){
            symbolShape.changeColor(color);
            if(visible){
                symbolShape.makeVisible();
            }
        } else {
            symbolShape.makeInvisible();
        }
    }
    
    /**
     * Mueve el frame y el circulo a la posicion en pantalla que le
     * corresponde segun la posicion logica de la rueda, para que cada
     * rueda quede separada de las demas en el Canvas.
     */
    private void updatePosition(){
        int targetFrameX = 20 + (position - 1) * 70;
        int targetFrameY = 40;
        frame.moveHorizontal(targetFrameX - frameX);
        frame.moveVertical(targetFrameY - frameY);
        frameX = targetFrameX;
        frameY = targetFrameY;
        
        int targetSymX = targetFrameX + 5; // centrado dentro del frame (40x30 vs circulo 30)
        int targetSymY = targetFrameY;
        symbolShape.moveHorizontal(targetSymX - symX);
        symbolShape.moveVertical(targetSymY - symY);
        symX = targetSymX;
        symY = targetSymY;
    }
    
    public void lock(){
        locked = true;
    }
    
    public void unlock(){
        locked = false;
    }
    
    public boolean isLocked(){
        return locked;
    }
}
