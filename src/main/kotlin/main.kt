import java.io.File
import java.io.FileNotFoundException
import java.lang.Math.abs

fun main(args:Array<String>) {
    val mapaP=creacionMapa()
    mapaP.mapa.forEach { println(it) }
    encontrarCamino(mapaP, mapaP.entrada!!)
    println("*****************************")
    imprimirCamino(mapaP)
}


fun encontrarCamino(mapaP_: mapa, actual:nodo ){
    //lama a la función siguiente nodo, que nos dará el nodo que se acerca más a la salida
    val nextNode=siguienteNodo(actual, mapaP_)
    //si el nodo siguiente no es la salida, añade ese nodo a los visitados y vuelve a llamar a la función
    if(nextNode.posicion!=findElement(mapaP_.mapa, "B") ){
        mapaP_.nodos_visitados.add(nextNode)
        encontrarCamino(mapaP_, nextNode)
    //si es la salida solo lo almacena en los visitados
    }else if(nextNode.posicion==findElement(mapaP_.mapa, "B")){
        mapaP_.nodos_visitados.add(nextNode)
    }
}

//devuelve el nodo siguiente que debe recorrer para salir del laberinto
fun siguienteNodo(actual: nodo, mapaP_: mapa):nodo{

    //genera los adyacentes del nodo actual y los almacena en el propio objeto
    generarAdyacentes(actual)

    //filtra de los adyacentes del nodo actual eligiendo los no visitados y los ordena por distancia
    val noVisitados=actual.adyacentes.filter { it !in mapaP_.nodos_visitados }.sortedBy { it.distancia }

    //si la lista de no visitados está vacia vuelve atrás mediante el nodo padre
    if(noVisitados.isEmpty()){
        return actual.padre!!
    }else{
        //en caso contrario elige el primero que sería el de menor distancia con la salida
        val elegido=noVisitados.first()
        //si el nodo elegido está más lejos que el nodo del que procede y el actual no es la entrada del laberinto,
        //vuelve al padre, o nodo del que procede, y comprueba que en los adyacentes de este haya alguno sin explorar
        //si es así se mueve hacia ese nodo
        if(elegido.distancia>=actual.distancia && actual!= mapaP_.entrada){
            val adyacentePadre=elegido.padre!!.padre!!.adyacentes.filter { it !in mapaP_.nodos_visitados && it!=elegido }.firstOrNull()

            if(adyacentePadre==null){
                //en caso de que en el padre no haya adyacentes sin explorar se mueve hacia el nodo elegido a pesar de que aumente la distancia respecto a la entrada
                return elegido
            }else{
                return adyacentePadre
            }
        }
        return elegido
    }
}

/*devuelve la posición de un elemento del mapa
se usa para devolver la posición de entrada y salida del laberinto*/
fun findElement(matriz:MutableList<List<String>>, element:String):Pair<Int, Int>{
    var x:Int=0
    var y:Int=0
    matriz.forEach{
        if(it.contains(element)){
            x=matriz.indexOf(it)
            y=it.indexOf(element)
        }
    }
    return Pair(x,y)
}

fun generarAdyacentes(nodo:nodo){
    val adyacentes= mutableListOf<Pair<Int, Int>>()
    val (x,y)=nodo.posicion
    if(x!=0){adyacentes.add( Pair(x-1, y))} //adyacente superior
    if(y>0) {adyacentes.add(Pair(x, y-1))}//adyacente izquierda
    if(y<nodo.mapa.mapa[0].size) {adyacentes.add(Pair(x, y+1))}//adyacente derecha
    if(x<nodo.mapa.mapa.size){adyacentes.add(Pair(x+1, y))}//adyacente inferior
    //luego se hace un filtro de esos nodos eligiendo solo los caminos, eliminando las paredes
    val posibles= mutableListOf<nodo>()
    for (adyacente in adyacentes){
        val (a,b)=adyacente
        if(nodo.mapa.mapa[a][b]=="0" || nodo.mapa.mapa[a][b]=="B"){
            //se crea una clase nodo que almacena el padre y la posición del nodo
            posibles.add(nodo(nodo, adyacente, nodo.mapa))
        }
    }
    nodo.adyacentes.addAll(posibles)
}

fun leerArchivo(archivo:String): MutableList<List<String>> {
    val lines:List<String>
    val matriz= mutableListOf<List<String>>()
    try {
        lines = File("src/main/resources/${archivo}").readLines()
        lines.forEach{matriz.add(it.split(","))}
    }catch (e: FileNotFoundException){
        println("No existe el archivo")
    }
    return matriz
}

//Imprime el mapa sustituyendo el cammino por X
fun imprimirCamino(mapa:mapa){
    val mapaCamino=mutableListOf<MutableList<String>>()
    //se tranforman las filas en listas mutables para poder cambiar
    // los nodos del camino por X
    mapa.mapa.forEach{mapaCamino.add(it.toMutableList())}
    mapa.nodos_visitados.forEach {
        val(a,b)=it.posicion
        mapaCamino[a][b]="X"
    }
    mapaCamino.forEach { println(it) }
}