import java.io.File
import java.io.FileNotFoundException
import java.lang.Math.abs

fun main (args: Array<String>){

    val entrada=findElement(mapa, "A")
    val nodoEntrada=nodo(null,entrada)
    mapa.forEach { println(it) }

    encontrarCamino(mapa,nodoEntrada)
    println("*****************************")
    imprimirCamino(mapa)
}
fun imprimirCamino(mapa: MutableList<List<String>>){
    val nuevoMapa=mutableListOf<MutableList<String>>()
        mapa.forEach{nuevoMapa.add(it.toMutableList())}
    camino.forEach {
        val(a,b)=it.posicion
        nuevoMapa[a][b]="X"
    }
    nuevoMapa.forEach { println(it) }

}

//variables estáticas
var camino= mutableListOf<nodo>()
var nodos_visitados= mutableListOf<nodo>()
val mapa=leerArchivo("miniMaze2.csv")
val salida=findElement(mapa, "B")


fun encontrarCamino(mapa:MutableList<List<String>>,actual:nodo ){
    val nextNode=siguienteNodo(actual)
    if(nextNode.posicion!=findElement(mapa, "B") ){
        nodos_visitados.add(nextNode)
        camino.add(nextNode)
        encontrarCamino(mapa, nextNode)
    }else if(nextNode.posicion==findElement(mapa, "B")){
        nodos_visitados.add(nextNode)
    }
}
fun siguienteNodo(actual: nodo):nodo{
    //crea un mapa con las puntuaciones de las adyacentes
    //val listAdyacentes= mutableListOf<nodo>()
    val (x,y)=salida
    val (x1,y1)=actual.posicion
    val listadyacentes= adyacentes2(mapa, actual)
    for (adyacente in listadyacentes){
        val (x2,y2)=adyacente.posicion
        val g= abs(x2-x1)+ abs(y2-y1)//calcula la distancia de la adyacente con la posición actual o previa
        val h= abs(x2-x)+ abs(y2-y)//calcula la distancia de la adyacente con la posición final u objetivo
        val f= g+h//suma ambas distancias
        adyacente.distancia=f

    }

    val noVisitados=listadyacentes.filter { it !in nodos_visitados }.sortedBy { it.distancia }
    //selecciona del mapa el valor más alto y devuelve la adyacente o null si no hay más adyacentes

    if(noVisitados.isEmpty()){
        return actual.padre!!
    }else{
        val elegido=noVisitados.first()
        return elegido
    }
}

fun adyacentes2(mapa:MutableList<List<String>>, actual:nodo):List<nodo>{
    //lista donde se almacena los nodos adyacentes al actual
    val listAdyacentes= mutableListOf<Pair<Int, Int>>()
    val (x,y)=actual.posicion
    if(x!=0){listAdyacentes.add( Pair(x-1, y))} //adyacente superior
    if(y>0) {listAdyacentes.add(Pair(x, y-1))}//adyacente izquierda
    if(y<mapa[0].size) {listAdyacentes.add(Pair(x, y+1))}//adyacente derecha
    if(x<mapa.size){listAdyacentes.add(Pair(x+1, y))}//adyacente inferior
    //luego se hace un filtro de esos nodos eligiendo solo los caminos, eliminando las paredes
    val posibles= mutableListOf<nodo>()
    for (adyacente in listAdyacentes){
        val (a,b)=adyacente
        if(mapa[a][b]=="0" || mapa[a][b]=="B"){
            //se crea una clase nodo que almacena el padre y la posición del nodo
            posibles.add(nodo(actual, adyacente))
        }
    }
    return posibles
}


//encuentra la posición de la entrada y la salida
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


class nodo(padre_:nodo?,posicion_:Pair<Int, Int> ){
    var distancia=0
    var posicion =posicion_
    var padre:nodo?=padre_
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as nodo
        if (posicion != other.posicion) return false
        return true
    }
    override fun hashCode(): Int {
        return posicion.hashCode()
    }

}

