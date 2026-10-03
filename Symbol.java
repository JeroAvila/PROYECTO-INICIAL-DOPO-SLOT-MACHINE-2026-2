/**
 * Simbolo de una rueda. Define el color (su identidad) y la forma con que se dibuja.
 * Cada tipo de simbolo es una subclase.
 * @author Laura Juliana Parra Velandia
 * @author Thomas Jeronimo Avila Castillo
 * @version 1.0
 */
public abstract class Symbol
{
    private String color;

    /**
     * Crea un simbolo del color dado.
     * @param color color del simbolo
     */
    public Symbol(String color)
    {
        this.color = color;
    }

    /**
     * @return el color del simbolo
     */
    public String getColor()
    {
        return color;
    }

    /**
     * Crea un simbolo nuevo del mismo tipo y color, sin el estado del actual.
     * Cada rueda trabaja con sus propias copias.
     * @return la copia
     */
    public abstract Symbol copy();

    /**
     * Dibuja el simbolo.
     */
    public abstract void makeVisible();

    /**
     * Borra el simbolo.
     */
    public abstract void makeInvisible();

    /**
     * Ubica el simbolo dentro de la rueda.
     * @param x esquina izquierda del espacio de 30x30 que ocupa el simbolo
     * @param y esquina superior de ese espacio
     */
    public abstract void setPosition(int x, int y);

    /**
     * Lo vuelve a dibujar sin moverlo, para dejarlo al frente en el Canvas.
     */
    public abstract void bringToFront();

    /**
     * Se llama cada vez que la rueda selecciona este simbolo (al girar o al ubicarlo).
     * El simbolo normal no hace nada; los otros tipos lo sobreescriben.
     */
    public void select()
    {
    }
}