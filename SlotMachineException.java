
/**
 * excepciones de SlotMachineException
 * se lanza cuando se pide algo que no puede hacer la maquina
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 */
public class SlotMachineException extends Exception{
    public static final String NULL_WHEEL_TYPE = "el tipo de la rueda no puede ser nulo.";
    public static final String UNKNOW_WHEEL_TYPE = "no existe este tipo de rueda.";
    public static final String NULL_SYMBOL_TYPE = " el tipo del simbolo no puede ser nulo.";
    public static final String UNKNOW_SYMBOL_TYPE = "no exite este tipo de simbolo.";
    public static final String REBEL_NOT_LOCKABLE = "la rueda rebelde no se puede bloquear.";
    public static final String REBEL_NOT_SWAPPABLE = "la rueda rebelde no se puede intercambiar";
    public static final String REBEL_NOT_DELETABLE = "la rueda rebelde no se puede eliminar";
    /**
     * crea la exception
     * @param message explicacion de la excepcion
     */
    public SlotMachineException(String message){
        super(message);
    }
}