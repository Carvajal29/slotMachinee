
/**
 * Un simbolo de la maquina tragamonedas
 * el simbolo se identifica unicamente por su color cuyo nombre
 * debe ser de CSS.
 *
 * @author  Oscar Carvajal
 * @version 23/08/2026
 */
public class Symbol
{
    private String color;
 
    /**
     * Crea un simbolo con el color indicado.
     * @param color nombre CSS del color del simbolo
     */
    public Symbol(String color) {
        this.color = color;
    }
 
    /**
     * Consulta el color del simbolo.
     * @return nombre CSS del color
     */
    public String getColor() {
        return color;
    }
}
 