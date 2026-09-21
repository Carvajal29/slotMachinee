import java.util.ArrayList;

/**
 * Resuelve el problema de la maraton Slot Machine
 * usando SlotMachine y usando solo spin(wheel, steps) y distinctSymbols().
 * @author Carvajal
 */
public class SlotMachineContest{
    /**
     * Resuelve una maquina de n ruedas y n simbolos inicializada aleatoriamente
     * @param n numero de ruedas y simbolos
     * @return secuencia de acciones {i, j}: girar la rueda i, j pasos
     */
    public static int[][] solve(int n) {
        SlotMachine machine = new SlotMachine(n);
        return solve(machine, n);
    }

    /**
     * Resuelve la maquina dada y registra cada accion realizada.
     */
    static int[][] solve(SlotMachine machine, int n) {
        ArrayList<int[]> actions = new ArrayList<int[]>();
        isolateFirstWheel(machine, n, actions);
        int[] offsets = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            offsets[i] = offsetToFirst(machine, i, n, actions);
        }
        for (int i = 2; i <= n; i++) {
            step(machine, i, offsets[i], n, actions);
        }
        return actions.toArray(new int[0][]);
    }

    /**
     * Mueve la rueda 1 a una posicion donde no haya ninguna otdra rueda.
     */
    private static void isolateFirstWheel(SlotMachine machine, int n,ArrayList<int[]> actions) {
        int[] k = sweep(machine, 1, n, actions);
        int best = n;
        for (int t = 1; t <= n; t++) {
            if (k[t - 1] > k[best - 1]) {
                best = t;
            }
        }
        step(machine, 1, best, n, actions);
    }

    /**
     * Descubre cuantos pasos le faltan a una rueda para alinearse con
     * la rueda 1 "barriendola" dos veces con la rueda 1 en su lugar y
     * con la rueda 1 corrida hacia un  paso.
     */
    private static int offsetToFirst(SlotMachine machine, int wheel, int n,ArrayList<int[]> actions) {
        int[] before = sweep(machine, wheel, n, actions);
        step(machine, 1, 1, n, actions);
        int[] after = sweep(machine, wheel, n, actions);
        step(machine, 1, n - 1, n, actions);
        int minBefore = min(before);
        int minAfter = min(after);
        for (int t = 1; t <= n; t++) {
            if (before[t - 1] == minBefore && after[t - 1] != minAfter) {
                return t;
            }
        }
        return n;
    }

    /**
     * Gira una rueda n veces de a un paso y anota cuantos simbolos
     * distintos hay despues de cada paso y al terminar la rueda vuelve
     * a su posicion original.
     */
    private static int[] sweep(SlotMachine machine, int wheel, int n,ArrayList<int[]> actions) {
        int[] k = new int[n];
        for (int t = 1; t <= n; t++) {
            step(machine, wheel, 1, n, actions);
            k[t - 1] = machine.distinctSymbols();
        }
        return k;
    }

    /**
     * Gira una rueda y registra la accion y si el giro es una vuelta
     * completa no hace nada porque la rueda quedaria igual.
     */
    private static void step(SlotMachine machine, int wheel, int steps, int n,ArrayList<int[]> actions) {
        if (steps % n == 0) {
            return;
        }
        machine.spin(wheel, steps);
        actions.add(new int[] {wheel, steps});
    }

    /**
     * Retorna el menor valor de un arreglo.
     */
    private static int min(int[] values) {
        int m = values[0];
        for (int v : values) {
            if (v < m) {
                m = v;
            }
        }
        return m;
    }
    
    /**
     * Simula visualmente la solucion de una maquina de n ruedas y n
     * simbolos inicializada aleatoriamente.
     * @param n numero de ruedas y simbolos
     */
    public static void simulate(int n) {
        SlotMachine machine = new SlotMachine(n);
        machine.makeVisible();
        solve(machine, n);
    }
}