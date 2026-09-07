import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Collections;

/**
 * Simulador de una maquina tragamonedas la 
 * maquina tiene una lista de simbolos, identificados por su color,
 * que es compartida por todas sus ruedas,cada rueda muestra uno de
 * esos simbolos a la vez.
 *
 * @author Oscar Carvajal
 * @version 23/08/2026
 */
public class SlotMachine
{
    public static final int WHEEL_GAP = 10;  //separacion entre rueda y rueda
    public static final int WHEEL_WIDTH = 60; //ancho del marco 
    public static final int MARGIN = 20; //separacion del canvas con ls primera rueda
    public static final int Y_BASE = 80; //coordenada en y donde se dibujan las ruedas
 
    private ArrayList<Symbol> symbolList;
    private ArrayList<Wheel> wheels;
    private boolean visible;
    private boolean ok;
 
    /**
     * Crea una maquina tragamonedas vacia, sin ruedas ni simbolos.
     * la maquina nace invisible.
     */
    public SlotMachine() {
        
        symbolList = new ArrayList<Symbol>();
        wheels = new ArrayList<Wheel>();
        visible = false;
        ok = true;
    }
 
    /**
     * Adiciona una rueda en la posicion que recibe como parametro y 
     * si la posicion es menor a 1 se usa la 1 pero si es mayor al maximo
     * se usa el maximo.
     * @param pos posicion en la que se inserta la rueda
     */
    public void addWheel(int pos) {
        ok = false;
        int p = adjust(pos,wheels.size() + 1);
        wheels.add(p-1,new Wheel());
        relocate();
        
        if(visible==true){
            wheels.get(p-1).makeVisible();
        }
        
        ok = true;
    }
 
    /**
     * Elimina la rueda que esta en la posicion indicada y 
     * si la posicion es menor a 1 se usa la 1 pero si es mayor al maximo
     * se usa el maximo.
     * @param pos posicion de la rueda 1ue se eliminara
     */
    public void delWheel(int pos) {
        
        ok = false;
        if (wheels.isEmpty()) {
            showError("La maquina no tiene ruedas");
            return;
        }
        int p = adjust(pos, wheels.size());
        wheels.get(p - 1).makeInvisible();
        wheels.remove(p - 1);
        relocate();
        ok = true;
        
    }
 
    /**
     * Adiciona un simbolo con el color indicado en la posicion dada
     * el color debe ser un nombre CSS valido y no puede estar repetido
     * @param pos posicion en la que se inserta el simbolo
     * @param color nombre CSS del color del nuevo simbolo
     */
    public void addSymbol(int pos, String color) {
        
        ok = false;
        
        if (Canvas.isValidColor(color)==false) {
            showError("El color no es un nombre CSS valido");
            return;
        }
        
        if (indexOfSymbol(color) != -1) {
            showError("Ese color ya existe en la maquina");
            return;
        }
        
        int p = adjust(pos, symbolList.size() + 1);
        for (Wheel w : wheels) {
            int c = w.getCurrent();
            if (c >= p - 1) {
                w.setCurrent(c + 1);
            }
        }
        symbolList.add(p - 1, new Symbol(color));
        ok = true;
    }
 
    /**
     * Elimina de la maquina el simbolo que tenga el color indicado.
     * @param symbol nombre CSS del color del simbolo que se eliminara
     */
    public void delSymbol(String symbol) {
        ok = false;
        int index = indexOfSymbol(symbol);
        
        if (index == -1) {
            showError("Ese simbolo no existe en la maquina");
            return;
        }for (Wheel w : wheels) {
            int c = w.getCurrent();
            if (c == index) {
                w.setCurrent(-1);
                w.refresh("white");
            }else if (c > index) {
                w.setCurrent(c - 1);
            }
        }
        symbolList.remove(index);
        ok = true;
        
        
    }
 
    /**
     * Hace que la rueda indicada muestre el simbolo del color dado
     * el color debe existir en la maquina
     * @param wheel posicion de la rueda, contada desde 1
     * @param symbol nombre CSS del color que debe mostrar la rueda
     */
    public void placeSymbol(int wheel, String symbol) {
        
        ok = false;
        if (wheels.isEmpty()) {
            showError("La maquina no tiene ruedas");
            return;
        }
        
        int index = indexOfSymbol(symbol);
        
        if (index == -1) {
            showError("Ese simbolo no existe en la maquina");
            return;
        }
        int w = adjust(wheel, wheels.size());
        wheels.get(w - 1).setCurrent(index);
        wheels.get(w - 1).refresh(symbol);
        ok = true;
    }
 
    /**
     * Gira la rueda indicada que muestra el siguiente simbolo de la lista de simbolos.
     * @param wheel posicion de la rueda
     */
    public void spin(int wheel) {
        
        ok = false;
        if (wheels.isEmpty()) {
            showError("La maquina no tiene ruedas");
            return;
        }
        
        if (symbolList.isEmpty()) {
            showError("La maquina no tiene simbolos");
            return;
        }
        int w = adjust(wheel, wheels.size());
        Wheel r = wheels.get(w - 1);
        if(r.isLocked()){                         //ciclo 2 
            showError("La rueda esta fija");
            return;
        
        }
        r.next(symbolList.size());
        r.refresh(symbolList.get(r.getCurrent()).getColor());
        ok = true;
        
    }
 
    /**
     * Gira todas las ruedas de la maquina y muestra el siguiente simbolo de la lista de 
     * simbolos
     */
    public void spin() {
        
        for (int i = 1; i <= wheels.size(); i++) {

            spin(i);
        }
    }
 
    /**
     * Consulta los colores de los simbolos de la maquina en el orden
     * en que estan
     * @return arreglo con los nombres CSS de los colores
     */
    public String[] symbols() {
        
        String[] result = new String[symbolList.size()];
        for (int i = 0; i < symbolList.size(); i++) {
            result[i] = symbolList.get(i).getColor();
        }
        return result;
    }
 
    /**
     * Cuenta cuantos colores diferentes estan visibles en las ruedas
     * @return cantidad de colores distintos en la configuracion actual
     */
    public int distinctSymbols() {
        
        ArrayList<Integer> distintos = new ArrayList<Integer>();
        for (Wheel w : wheels) {
            int c = w.getCurrent();
            if (c != -1 && !distintos.contains(c)) {
                distintos.add(c);
            }
        }
        
        return distintos.size();
    }
 
    /**
     * Consulta los colores de los simbolos visibles en todas las ruedas,
     * ordenados de izquierda a derecha.
     * @return arreglo con un color por cada rueda
     */
    public String[] configuration() {
        
       String[] result = new String[wheels.size()];
       
        for (int i = 0; i < wheels.size(); i++) {
            int c = wheels.get(i).getCurrent();
            if (c == -1) {
                result[i] = "none";
            } else {
                result[i] = symbolList.get(c).getColor();
            }
        }
        return result;
    }
 
    /**
     * Indica es ganador es decir si
     * todas las ruedas muestran el mismo simbolo.
     * @return true si la maquina esta en un estado ganador
     */
    public boolean isJackpot() {
        if(wheels.size()>1){
            if(distinctSymbols() == 1){
                return true;
            
            }else{
                return false;
            
            }
        
        }
        return false;
    }
 
    /**
     * Hace visible el simulador y todos los elementos.
     */
    public void makeVisible() {
        
        visible = true;
        for (Wheel w : wheels) {
            w.makeVisible();      
        }
        ok = true;
    }
 
    /**
     * Hace invisible el simulador y todos los elementos.
     */
    public void makeInvisible() {
        
        visible = false;
        for (Wheel w : wheels) {
            w.makeInvisible();
        }
        ok = true;
    }
 
    /**
     * Termina el simulador.
     */
    public void exit() {
        System.exit(0);
    }
 
    /**
     * Indica si se logro realizar la ultima operacion.
     * @return true si la ultima operacion fue exitosa
     */
    public boolean ok() {
        return ok;
    }
 
    /**
     * Ajusta una posicion al rango valido: menor a 1 se vuelve 1,
     * mayor al maximo se vuelve el maximo.
     * @param pos posicion solicitada
     * @param max posicion maxima permitida
     * @return posicion corregida
     */
    private int adjust(int pos, int max) {
        if(pos<1){
            return 1;
        }
        
        if(pos>max){
            return max;
        }
        
        return pos;
    }
 
    /**
     * Busca en la lista el simbolo que tenga el color indicado.
     * @param color nombre CSS del color buscado
     * @return indice del simbolo o -1 si no existe
     */
    private int indexOfSymbol(String color) {
        
        for (int i = 0; i < symbolList.size(); i= i+1) {
            if (symbolList.get(i).getColor().equals(color)) {
                return i;          
            }
        }
        return -1; 
    }
 
    /**
     * Muestra un mensaje de error al usuario solo si el simulador
     * esta visible.
     * @param message texto del mensaje
     */
    private void showError(String message) {   
        if (visible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
 
    /**
     * Recalcula la coordenada horizontal de todas las ruedas segun
     * la posicion que ocupan.
     */
    private void relocate() {
        
        for (int i = 0; i < wheels.size(); i++) {
            wheels.get(i).moveTo(MARGIN + i * (WHEEL_WIDTH + WHEEL_GAP));
        }
    }
 
    /**
     * Actualiza el color que muestra cada rueda a partir del indice
     * de simbolo que tiene guardado.
     */
    private void refreshWheels() {
        
        for (Wheel w : wheels) {
            int i = w.getCurrent();
            if (i >= 0 && i < symbolList.size()) {
                w.refresh(symbolList.get(i).getColor());
            }
        }
        
    }
    
    ///////////////////////////CICLO 2///////////////////////////////////
    
    /**
     * 
     */
    public void swap(int wheel1, int wheel2) {
    ok = false;
    if (wheels.size() < 2) {
        showError("Se necesitan al menos dos ruedas");
        return;
    }
    int w1 = adjust(wheel1, wheels.size());
    int w2 = adjust(wheel2, wheels.size());
    Collections.swap(wheels, w1 - 1, w2 - 1);
    relocate();
    ok = true;
    }
    
    /**
     * Fija la rueda indicada para que no pueda girar.
     */
    public void lock(int wheel) {
        ok = false;
        if (wheels.isEmpty()) {
            showError("La maquina no tiene ruedas");
            return;
        }
        int w = adjust(wheel, wheels.size());
        wheels.get(w - 1).lock();
        ok = true;
    }
    
    /**
     * Desbloquea la rueda indicada para que pueda girar de nuevo.
     */
    public void unlock(int wheel) {
    ok = false;
    if (wheels.isEmpty()) {
        showError("La maquina no tiene ruedas");
        return;
    }
    int w = adjust(wheel, wheels.size());
    wheels.get(w - 1).unlock();
    ok = true;
    }
    
    /**
     * Rota la rueda indicada un numero de pasos 
     * si el simulador esta visible entonces el movimiento se ve
     * paso a paso
     */
    
    public void spin(int wheel, int steps) {
        
        ok = false;
        if (wheels.isEmpty()) {
            showError("La maquina no tiene ruedas");
            return;
        }
        if (symbolList.isEmpty()) {
            showError("La maquina no tiene simbolos");
            return;
        }
        int w = adjust(wheel, wheels.size());
        Wheel r = wheels.get(w - 1);
        if (r.isLocked()) {
            showError("La rueda esta fija");
            return;
        }
        for (int i = 0; i < steps; i++) {
            r.next(symbolList.size());
            r.refresh(symbolList.get(r.getCurrent()).getColor());
            if (visible) {
                return;
            }
        }
        ok = true;
    }
    
    /**
     * Hace colocar todas las ruedas con los colores que tiene 
     */
    public void spin(String[] setSymbols){
        
        ok = false;
        
        if (setSymbols.length != wheels.size()) {
            showError("setSymbols no coincide con el numero de ruedas");
            return;
        }
        
        for (int i = 0; i < setSymbols.length; i++) {
            
            if (indexOfSymbol(setSymbols[i]) == -1) {
                showError("El simbolo " + setSymbols[i] + "no existe");
                return;
            }
            
            if (wheels.get(i).isLocked()) {
                showError("La rueda " + (i + 1) + "esta fija");
                return;
            }
        }
        
        for (int i = 0; i < setSymbols.length; i++) {
            placeSymbol(i + 1, setSymbols[i]);
        }
        
        ok = true;
    }
}