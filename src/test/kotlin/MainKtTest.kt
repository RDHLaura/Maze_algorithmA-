import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MainKtTest {
    val mapa1=creacionMapa()
    val primer_nodo=nodo(mapa1.entrada, Pair(1,1),mapa1 )

    @BeforeEach
    internal fun setUp() {
        generarAdyacentes(primer_nodo)
        generarAdyacentes(mapa1.entrada!!)
    }
    @Test
    fun findElement() {
        /*comprueba que da las coordenadas correctas del nodo de entrada*/
        assertEquals(findElement(mapa1.mapa, "A"), mapa1.entrada!!.posicion)
    }

    @Test
    fun siguienteNodo_() {
       /*comprueba que selecciona un nodo correcto*/
        assertEquals(siguienteNodo(mapa1.entrada!!, mapa1), primer_nodo)
    }

    @Test
    fun generarAdyacentes() {
       /*este test comprueba que se generan los adyacentes correctos del nodo en la
       posición (1,1) y el nodo de entrada*/
        assertEquals(primer_nodo.adyacentes.size, 2)
        assertEquals(mapa1.entrada!!.adyacentes.size, 1)

    }
}