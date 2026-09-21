import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de unidad de constructor SlotMachine(n) y la
 * solucion del problema de la maraton.
 *
 * @author Carvajal
 */
public class SlotMachineContestTest
{
    /**
     * Verifica que SlotMachine(n) cree n ruedas y n simbolos,
     * y que no arranque ya en jackpot.
     */
    @Test
    public void shouldCreateMachineWithNWheelsAndNSymbols() {
        SlotMachine machine = new SlotMachine(5);
        assertEquals(5, machine.configuration().length);    
        assertEquals(5, machine.symbols().length);            
        assertFalse(machine.isJackpot());  // deberia no arrancar ganada
    }

    /**
     * Verifica que para todo n entre 3 y 50 se llegue al jackpot
     */
    @Test
    public void shouldReachJackpotForEveryN() {
        for (int n = 3; n <= 50; n++) {
            SlotMachine machine = new SlotMachine(n);
            int[][] actions = SlotMachineContest.solve(machine, n);
            assertEquals(1, machine.distinctSymbols());    
            assertTrue(actions.length <= 10000);           
        }
    }

    /**
     * Verifica que cada accion retornada por solve gire una rueda
     * que existe.
     */
    @Test
    public void shouldReturnValidActions() {
        int n = 6;
        int[][] actions = SlotMachineContest.solve(n);
        assertTrue(actions.length > 0);                    
        for (int[] action : actions) {
            assertEquals(2, action.length);                   
            assertTrue(action[0] >= 1 && action[0] <= n);   
        }
    }
}