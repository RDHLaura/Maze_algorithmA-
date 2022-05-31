import java.io.File
import java.io.FileNotFoundException
import java.lang.Math.abs

fun main (args: Array<String>){

    val entrada=findElement(mapa, "A")
    mapa.forEach { println(it) }
    encontrarCamino(mapa,entrada)
    imprimirCamino(mapa)
}

//variables estáticas
var nodos_por_visitar= mutableListOf<nodo>()
var nodos_visitados= mutableListOf<nodo>()
val mapa=leerArchivo2("maze.csv")


fun encontrarCamino2(mapa:MutableList<List<String>>,actual:Pair<Int, Int> ){
    val nextNode=calcularF(mapa, actual)
    if(nextNode!=null && nextNode!=findElement(mapa, "B") ){
        camino.add(nextNode)
        encontrarCamino(mapa, nextNode)
    }else if(nextNode==findElement(mapa, "B")){
        camino.add(nextNode)
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
fun findElement2(matriz:MutableList<List<String>>, element:String):Pair<Int, Int>{
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

fun leerArchivo2(archivo:String): MutableList<List<String>> {
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

    var posicion =posicion_
    var padre:nodo?=padre_
    var distancia=f()


    fun f():nodo{
        //crea un mapa con las puntuaciones de las adyacentes
        val mapAdyacentes= mutableMapOf<Int, MutableList<nodo>>()
        val salida=findElement2(mapa, "B")
        val (x,y)=salida
        val (x1,y1)=posicion
        val listadyacentes= adyacentes2(mapa, this)
        for (adyacente in listadyacentes){
            val (x2,y2)=adyacente.posicion
            val g= abs(x2-x1)+ abs(y2-y1)//calcula la distancia de la adyacente con la posición actual o previa
            val h= abs(x2-x)+ abs(y2-y)//calcula la distancia de la adyacente con la posición final u objetivo
            val f= g+h//suma ambas distancias

            //almacena la adyacente con su distancia calculada
            if(f in mapAdyacentes.keys){
                mapAdyacentes[f]!!.add(adyacente)
//            val(a, b)= mapAdyacentes[f]!!
//            //si ya habia alguna adyacente en el map con esa puntuación da prioridad a la adyacente que está abajo o a la dcha
//            if(x2>a || y2>b){
//                mapAdyacentes[f]=adyacente
//            }
            }else{ mapAdyacentes[f]= mutableListOf(adyacente)}
        }
        //selecciona del mapa el valor más alto y devuelve la adyacente o null si no hay más adyacentes
        val elegida= mapAdyacentes[mapAdyacentes.keys.toList().minOrNull()]!!.filter { it !in nodos_visitados }.randomOrNull()

        if(elegida ==null){

            return padre!!
        }else{
            return elegida
        }
    }
}

