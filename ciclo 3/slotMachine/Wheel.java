
/**
 * Una rueda de la maquina tragamonedas.
 * La rueda no sabe de la lista de simbolos de la maquina: solo recuerda
 * el indice del simbolo que esta mostrando y se dibuja a si misma.
 *
 * @author Oscar Carvajal
 * @version 23/08/2026
 */
public class Wheel{
    
    public static final int WHEEL_WIDTH = 60; //ancho del marco
    public static final int WHEEL_HEIGHT = 120; //alto del marco 
    public static final int PADDING = 8; // la distancia que hay del marco y el simbolo o display 
    public static final int Y_BASE = 80; // la distancia con respecto al canvas
    
    private int current;
    private int xPosition;
    private Rectangle frame;
    private Rectangle display;
    private boolean visible;
    private boolean locked; //ciclo 2
 
    /**
     * Crea una rueda sin simbolo visible ubicada en la posicion indicada.
     * la rueda nace invisible.
     * @param x coordenada horizontal donde se dibuja la rueda
     * @param y coordenada vertical donde se dibuja la rueda
     */
    public Wheel() {
        
        //marco 
        frame = new Rectangle();
        frame.changeSize(WHEEL_HEIGHT, WHEEL_WIDTH);
        frame.changeColor("gray");
        
        //simbolo
        display = new Rectangle();
        display.changeSize(WHEEL_HEIGHT - 2 * PADDING, WHEEL_WIDTH - 2 * PADDING);
        display.changeColor("white");
    
        current = -1;
        xPosition = 0;
        visible = false;
        locked = false;//ciclo 2 
        
    }
 
    /**
     * Consulta el indice del simbolo que la rueda esta mostrando.
     * @return indice del simbolo visible o -1 si no muestra ninguno simbolo
     */
    public int getCurrent() {
        return current;
    }
 
    /**
     * Dice directamente el indice del simbolo que debe mostrar la rueda
     * @param index indice del simbolo dentro de la lista de la maquina
     */
    public void setCurrent(int index) {
        current = index;
    }
 
    /**
     * Avanza la rueda al siguiente simbolo y vuelve al primero
     * cuando se llega al final.
     * @param total cantidad de simbolos que tiene la maquina
     */
    public void next(int total) {
        current = (current + 1) % total;
    }
 
    /**
     * Pinta la rueda con el color del simbolo que esta mostrando.
     * @param color nombre CSS del color a mostrar
     */
    public void refresh(String color) {
        display.changeColor(color);
    }
 
    /**
     * Reubica la rueda y sus figuras en la coordenada horizontal indicada.
     * @param x nueva coordenada horizontal
     */
    public void moveTo(int x) {
        
        frame.setPosition(x, Y_BASE);
        display.setPosition(x + PADDING, Y_BASE + PADDING);
        
    }
 
    /**
     * Hace visible la rueda.
     */
    public void makeVisible() {
        
        visible = true;
        frame.makeVisible();
        display.makeVisible();
        
    }
 
    /**
     * Hace invisible la rueda.
     */
    public void makeInvisible() {
        
        visible = false;
        frame.makeInvisible();
        display.makeInvisible();
    }
    
    ////////////////ciclo 2//////////////
    /**
     * fija la rueda para que no gire
     */
    public void lock(){
        locked = true;
    
    }
    
    /**
     * Consulta el valor del atributo locked
     */
    public boolean isLocked(){
        return locked;
    }
    
    /**
     * Desbloquea la rueda para que pueda girar de nuevo
     */
    public void unlock() {
        locked = false;
    }
}
