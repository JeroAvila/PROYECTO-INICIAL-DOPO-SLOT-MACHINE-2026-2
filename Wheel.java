import java.util.ArrayList;
import java.util.List;
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
    private Triangle jackpotMark;
    private List<String> symbols; // guarda los simbolos en una lista
    private int currentIndex;  //indica el cual simbolo se esta mostrando (-1 = ninguno)
    private boolean winning; // indica si esta rueda hace parte de un jackpot
    private boolean visible;
    private int frameX, frameY; // posicion actual del frame en pantalla
    private int symX, symY;     // posicion actual del circulo del simbolo
    private int markX, markY;   // posicion actual del triangulo dorado
    /**
     * CONSTRUCTOR
     * Crea una rueda vacia en la posicion dada. for objects of class Wheel
     */
    public Wheel(int position)
    {
        this.position = position;
        frame = new Rectangle();
        frame.changeColor("white");
        symbolShape = new Circle();
        jackpotMark = new Triangle();
        jackpotMark.changeColor("yellow"); // el mas cercano a dorado en shapes
        symbols = new ArrayList<>();
        currentIndex = -1; // -1 significa "todavia no le han asignado simbolo"
        winning = false;
        visible = false;
        frameX = 70; frameY = 15;   // posicion por defecto de Rectangle
        symX = 20; symY = 15;       // posicion por defecto de Circle
        markX = 140; markY = 15;    // posicion por defecto de Triangle
        updatePosition();
    }
    
    /**
     * Cambia la posicion logica de la rueda, usado cuando se agregan o
     * eliminan ruedas en la maquina y hay que renumerar las demas.
     * Tambien reubica el frame, el circulo y el triangulo en pantalla
     * para que no queden varias ruedas dibujadas una encima de otra.
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
     * Avanza al siguiente simbolo. 
     * devuelve el residuo de la division para que cuando llegue al final vuelva a cero
     */
    public void spin() {
        if(!symbols.isEmpty()){
            currentIndex = (currentIndex + 1) % symbols.size();
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
     * si winning es true, se muestra el triangulo dorado encima de la
     * rueda (solo si la rueda ya esta visible); si es false, se oculta.
     * @param winning true si la maquina esta en estado ganador
     */
    public void setWinning(boolean winning){
        this.winning = winning;
        if(visible && winning){
            jackpotMark.makeVisible();
        } else {
            jackpotMark.makeInvisible();
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
        if(winning){
            jackpotMark.makeVisible();
        }
    }
    
    /**
     * metodo de shape que hace que sea invisible
     */
    public void makeInvisible(){
        visible = false;
        frame.makeInvisible();
        symbolShape.makeInvisible();
        jackpotMark.makeInvisible();
    }
    
    /**
     * Vuelve a dibujar el frame, el circulo y el triangulo sin moverlos,
     * para que queden al frente en el Canvas (por ejemplo, despues de
     * que el fondo negro de la maquina cambie de tamano y quede encima).
     */
    public void bringToFront(){
        frame.moveHorizontal(0);
        symbolShape.moveHorizontal(0);
        jackpotMark.moveHorizontal(0);
    }
    
    /**
     * actualiza el circulo del simbolo para que coincida con el color
     * actualmente visible. El frame (Rectangle) siempre se queda blanco,
     * es solo el fondo de la rueda. Si no hay simbolo asignado
     * (getCurrentSymbol da null), el circulo se oculta.
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
     * Mueve el frame, el circulo y el triangulo a la posicion en
     * pantalla que le corresponde segun la posicion logica de la rueda,
     * para que cada rueda quede separada de las demas en el Canvas.
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
        
        int targetMarkX = targetFrameX;
        int targetMarkY = targetFrameY - 25;
        jackpotMark.moveHorizontal(targetMarkX - markX);
        jackpotMark.moveVertical(targetMarkY - markY);
        markX = targetMarkX;
        markY = targetMarkY;
    }
}
