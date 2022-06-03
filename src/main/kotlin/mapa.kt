
data class mapa(
    var mapa: MutableList<List<String>>,
    var nodos_visitados: MutableList<nodo>,
    var entrada:nodo?,
    var salida: nodo?)
//crea una data class para almacenar los datos del mapa
fun creacionMapa():mapa{
    val mapaP=mapa(
        leerArchivo("maze.csv"), //devuelve el mapa que es una lista de listas
        mutableListOf<nodo>(), //nodos visitados, que es una lista mutable de nodos
        null,null
    )
    val salida=findElement(mapaP.mapa, "B")//devuelve la posición de la salida
    val entrada=findElement(mapaP.mapa, "A")//devuelve la posición de la entrada

    mapaP.entrada= nodo(null,entrada, mapaP) //nodo con la posición de entrada
    mapaP.salida=nodo(null,salida, mapaP) //nodo con la posición de salida
    return mapaP
}