

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * The test class SlotMachineC2Test.
 *
 * @author  Oscar Carvajal
 */
public class SlotMachineC2Test {

    private SlotMachine machine; 

    @BeforeEach                                 
    public void setUp() {
        machine = new SlotMachine();   
        machine.makeInvisible();  
    }
    
    ///////////////////////////////// constructor//////////////////////////

    @Test                                      
    public void shouldCreateEmptyMachine() {
        assertEquals(0, machine.symbols().length); // debe nacer sin simbolos 
        assertEquals(0, machine.configuration().length);// debe nacer sin ruedas
        assertTrue(machine.ok());                         // no ha fallado nada
    }

    @Test
    public void shouldNotBeJackpotWhenEmpty() {
        assertFalse(machine.isJackpot()); // sin ningunarueda no puede haber jackpot
    }
    
    
    ///////////////////////////// addWheel //////////////////////////
    /**
     * Verifica que se puedan agregar las ruedas en las posiciones 
     * indicadas
     */
    @Test
    public void shouldAddWheelAtGivenPosition() {
        machine.addWheel(1);                                
        machine.addWheel(2);                          
        assertEquals(2, machine.configuration().length);// deberian quedar dos ruedas
        assertTrue(machine.ok());    // todo salio bien
    }
    
    /**
     * Verifica que una posicion mayor al maximo se ajuste al maximo
     * de la lista de ruedas y no falle
     *
     */
    @Test
    public void shouldAdjustPositionWhenTooBig() {
        machine.addWheel(1);
        machine.addWheel(50);      
        assertEquals(2, machine.configuration().length);//deberia ajustarse al maximo
        assertTrue(machine.ok());  // ajustar no es un error
    }
    
    /**
     * Verifica que una posicion menor a 1 se ajuste a la posicion 1
     * en lugar de fallar.
     */
    @Test
    public void shouldAdjustPositionWhenNegative() {
        machine.addWheel(-7);                               
        assertEquals(1, machine.configuration().length);//deberia ajustarse a la posicion 1
        assertTrue(machine.ok());// ajustar no es un error
    }
    
    /**
     * Verifica que al insertar una rueda en el medio las ruedas
     * siguientes se corran una posicion y tengan su mismo simbolo.
     */
    @Test
    public void shouldShiftWheelsWhenInsertingInTheMiddle() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");                      
        machine.placeSymbol(2, "blue");                       
        machine.addWheel(2);                       
        assertEquals("red", machine.configuration()[0]); // la roja no se mueve
        assertEquals("none", machine.configuration()[1]);// la nueva nace en blanco
        assertEquals("blue", machine.configuration()[2]);// la azul se corrio a la 3
    }
    
    ///////////////////////////////delWheel////////////////////////////////
    
    /**
     * Verifica que se pueda eliminar una rueda en la posicion indicada.
     */
    @Test
    public void shouldDeleteWheelAtGivenPosition() {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(1);                                 
        assertEquals(1, machine.configuration().length);      
        assertTrue(machine.ok());                             
    }
    
    /**
     * Verifica que no se pueda eliminar una rueda cuando la maquina
     * no tiene ninguna.
     */
    @Test
    public void shouldNotDeleteWheelWhenMachineIsEmpty() {
        machine.delWheel(1);                
        assertFalse(machine.ok());                            
        assertEquals(0, machine.configuration().length);    
    }
    
    /**
     * Verifica que una posicion fuera de rango se ajuste al maximo
     * en lugar de fallar.
     */
    @Test
    public void shouldAdjustPositionWhenDeletingOutOfRange() {
        machine.addWheel(1);
        machine.addWheel(2);
        machine.delWheel(50);                                 
        assertEquals(1, machine.configuration().length);      
        assertTrue(machine.ok());                       
    }
    
    /**
     * Verifica que al eliminar una rueda del medio las siguientes 
     * de esa se corran una posicion y conserven su simbolo.
     */
    @Test
    public void shouldShiftWheelsWhenDeletingFromTheMiddle() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");                        
        machine.placeSymbol(3, "blue");                
        machine.delWheel(2);                    
        assertEquals(2, machine.configuration().length);// deberian quedar  dos
        assertEquals("red", machine.configuration()[0]);// deberia seguir siendo roja 
        assertEquals("blue", machine.configuration()[1]);// deberia seguir siendo azul
    }
    
    ////////////////////////////////addSymbol/////////////////////////
    /**
     * Verifica que se pueda agregar un simbolo con un color CSS que 
     * sea valido.
     */
    @Test
    public void shouldAddSymbolWithValidColor() {
        machine.addSymbol(1, "red");
        assertEquals(1, machine.symbols().length);//deberia haber un simbolo
        assertEquals("red", machine.symbols()[0]);// y deberia ser rojo
        assertTrue(machine.ok());                         
    }
    
    /**
     * Verifica que no se pueda agregar un color que no pertenece
     * al CSS.
     */
    @Test
    public void shouldNotAddInvalidCssColor() {
        machine.addSymbol(1, "morado");                     
        assertFalse(machine.ok()); //deberia decir que fallo
        assertEquals(0, machine.symbols().length); //no deberia agregar nada
    }
    
    /**
     * Verifica que no se pueda agregar un color que ya existe,
     * porque los simbolos deben tener colores diferentes.
     */
    @Test
    public void shouldNotAddRepeatedColor() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "red");                          
        assertFalse(machine.ok());                            
        assertEquals(1, machine.symbols().length);         
    }
    
    /**
     * Verifica que los simbolos se inserten en la posicion indicada
     * y que los demas se corran.
     */
    @Test
    public void shouldInsertSymbolAtGivenPosition() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(2, "green");                      
        assertEquals("red", machine.symbols()[0]);
        assertEquals("green", machine.symbols()[1]);//deberia ahora ser verde 
        assertEquals("blue", machine.symbols()[2]); // el azul se corrio a la 3
    }
    
    /**
     * Verifica que al insertar un simbolo antes del que muestra 
     * una rueda la rueda conserve su color.
     */
    @Test
    public void shouldKeepWheelColorWhenInsertingSymbolBefore() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "blue");                    
        machine.addSymbol(1, "green");                       
        assertEquals("blue", machine.configuration()[0]);// deberia  seguir en azul
    }
    
    ///////////////////////////// delSymbol ///////////////////
    
    /**
     * Verifica que se pueda eliminar un simbolo por su color.
     */
    @Test
    public void shouldDeleteSymbolByColor() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.delSymbol("red");
        assertEquals(1, machine.symbols().length);//deberia quedar solo uno
        assertEquals("blue", machine.symbols()[0]);// y deberia ser azul
        assertTrue(machine.ok());
    }
    
    /**
     * Verifica que no se pueda eliminar un simbolo que no existe.
     */
    @Test
    public void shouldNotDeleteMissingSymbol() {
        machine.addSymbol(1, "red");
        machine.delSymbol("blue");     
        assertFalse(machine.ok());//deberia reportar el fallo
        assertEquals(1, machine.symbols().length);// deberia seguir habiendo uno           
    }
    
    /////////////////////////placeSymbol/////////////////////////////
    
    /**
     * Verifica que se pueda ponerr el simbolo que muestra una rueda.
     */
    @Test
    public void shouldPlaceSymbolOnGivenWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(2, "blue");
        assertEquals("blue", machine.configuration()[1]); //deberia mostrar muestra azul
        assertEquals("none", machine.configuration()[0]);//no deberia cambiar
        assertTrue(machine.ok());
    }
    
    /**
     * Verifica que no se pueda colocar un simbolo que no existe
     * en la maquina.
     */
    @Test
    public void shouldNotPlaceMissingSymbol() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.placeSymbol(1, "blue");               
        assertFalse(machine.ok());   //deberia reportar el fallo
        assertEquals("none", machine.configuration()[0]);
    }
    
    //////////////////////////////spin////////////////////////////
    /**
     * Verifica que al girar una rueda avance al siguiente simbolo.
     */
    @Test
    public void shouldAdvanceToNextSymbol() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.spin(1);
        assertEquals("blue", machine.configuration()[0]);//deberia pasar del rojo al azul
        assertTrue(machine.ok());
    }
    
    /**
     * Verifica que al llegar al ultimo simbolo la rueda vuelva al primero.
     */
    @Test
    public void shouldWrapAroundAtTheEnd() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "blue");                    
        machine.spin(1);
        assertEquals("red", machine.configuration()[0]);//deberia dar la  vuelta al primero que es rojo
    }
    
    /**
     * Verifica que no se pueda girar cuando la maquina no tiene simbolos.
     */
    @Test
    public void shouldNotSpinWithoutSymbols() {
        machine.addWheel(1);
        machine.spin(1);                                     
        assertFalse(machine.ok());//deberia  reportar el fallo
    }
    
    /**
     * Verifica que una rueda fija no gire.
     */
    @Test
    public void shouldNotSpinLockedWheel() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.lock(1);
        machine.spin(1);
        assertFalse(machine.ok());// debe reportar el fallo
        assertEquals("red", machine.configuration()[0]);//deberia seguir en el mismo simbolo
    }
    
    /**
     * Verifica que spin sin parametros gire todas las ruedas libres
     * y salte las fijas.
     */
    @Test
    public void shouldSpinAllExceptLockedWheels() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.lock(2);
        machine.spin();
        assertEquals("blue", machine.configuration()[0]);//la libre deberia girar
        assertEquals("red", machine.configuration()[1]);// la fija no deberia girar
    }
    
    /**
     * Verifica que spin con pasos avance esa cantidad de simbolos.
     */
    @Test
    public void shouldSpinGivenNumberOfSteps() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        machine.spin(1, 2);                                   // avanza dos pasos
        assertEquals("green", machine.configuration()[0]);    // rojo -> azul -> verde
    }
    
    ////////////////////////////////distinctSymbols//////////////////
    /**
     * Verifica que cuente bien los colores distintos
     * cuando todas las ruedas muestran cosas diferentes.
     */
    @Test
    public void shouldCountAllDistinctSymbols() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        assertEquals(2, machine.distinctSymbols());//deberian ser 2 diferentes
    }
    
    /**
     * Verifica que los colores repetidos se cuenten una sola vez.
     */
    @Test
    public void shouldCountRepeatedSymbolsOnce() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        machine.placeSymbol(3, "red");
        assertEquals(1, machine.distinctSymbols());//deberia ser uno solo
    }
    
    /**
     * Verifica que las ruedas en blanco no aporten ningun color.
     */
    @Test
    public void shouldNotCountBlankWheels() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red"); // la rueda 2 queda en blanco
        assertEquals(1, machine.distinctSymbols());//deberia contar solo la que tiene color
    }
    
    /////////////////////////////isJackpot/////////////////////////
    /**
     * Verifica que haya jackpot cuando todas las ruedas muestran
     * el mismo simbolo.
     */
    @Test
    public void shouldBeJackpotWhenAllWheelsMatch() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "red");
        assertTrue(machine.isJackpot());//deberia ser true
    }
    
    /**
     * Verifica que no haya jackpot cuando las ruedas muestran
     * simbolos diferentes.
     */
    @Test
    public void shouldNotBeJackpotWhenWheelsDiffer() {
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        assertFalse(machine.isJackpot());// no deberia daeñ jackpot
    }
    
    /**
     * Verifica que no haya jackpot con una sola rueda aunque solo
     * exista un color distinto.
     */
    @Test
    public void shouldNotBeJackpotWithOneWheel() {
        machine.addSymbol(1, "red");
        machine.addWheel(1);
        machine.placeSymbol(1, "red");
        assertFalse(machine.isJackpot()); //no deberia dar true porque osino no tendria sentido el juego
    }
    
}