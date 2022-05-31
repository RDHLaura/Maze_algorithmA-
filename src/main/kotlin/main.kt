import java.io.File
import java.io.FileNotFoundException
import java.lang.Math.abs
import kotlin.math.max

fun main(args: Array<String>) {

    val mapa=leerArchivo("maze.csv")

    val entrada=findElement(mapa, "A")
    mapa.forEach { println(it) }
    encontrarCamino(mapa,entrada)
    imprimirCamino(mapa)
}
fun imprimirCamino(mapa: MutableList<List<String>>){
    val nuevoMapa=mutableListOf<MutableList<String>>()
        mapa.forEach{nuevoMapa.add(it.toMutableList())}
    camino.forEach {
        val(a,b)=it
        nuevoMapa[a][b]="X"
    }
    nuevoMapa.forEach { println(it) }

}
 var camino=  mutableListOf<Pair<Int, Int>>()
//MutableMap<Pair<Int, Int>,
fun calcularF(mapa:MutableList<List<String>>,actual:Pair<Int, Int>):Pair<Int, Int>?{
    //crea un mapa con las puntuaciones de las adyacentes
    val mapAdyacentes= mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
    val salida=findElement(mapa, "B")
    val (x,y)=salida
    val (x1,y1)=actual
    val listadyacentes= adyacentes(mapa, actual)
    for (adyacente in listadyacentes){
        val (x2,y2)=adyacente
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
    var elegida= mapAdyacentes[mapAdyacentes.keys.toList().minOrNull()]!!.filter { it !in camino }.randomOrNull()

    if(elegida ==null){
        elegida= mapAdyacentes[mapAdyacentes.keys.toList().minOrNull()]!!.randomOrNull()
        return elegida
    }else {
        return elegida
    }

}

fun encontrarCamino(mapa:MutableList<List<String>>,actual:Pair<Int, Int> ){
    val nextNode=calcularF(mapa, actual)
    if(nextNode!=null && nextNode!=findElement(mapa, "B") ){
        camino.add(nextNode)
        encontrarCamino(mapa, nextNode)
    }else if(nextNode==findElement(mapa, "B")){
        camino.add(nextNode)
    }
}
fun adyacentes(mapa:MutableList<List<String>>, actual:Pair<Int, Int>):List<Pair<Int, Int>>{
    val listAdyacentes= mutableListOf<Pair<Int, Int>?>()
    listAdyacentes.add(adyacentesArriba(mapa, actual))
    listAdyacentes.add(adyacentesIzq(mapa, actual))
    listAdyacentes.add(adyacentesDcha(mapa, actual))
    listAdyacentes.add(adyacentesAbajo(mapa, actual))

    val posibles= mutableListOf<Pair<Int, Int>>()
    for (adyacente in listAdyacentes.filter { it!=null }){
        val (a,b)=adyacente!!
        if(mapa[a][b]=="0" || mapa[a][b]=="B"){posibles.add(adyacente)}
    }

    return posibles

}
fun adyacentesArriba(mapa:MutableList<List<String>>, actual:Pair<Int, Int>):Pair<Int, Int>?{
    val (x,y)=actual
    return if(x!=0){  Pair(x-1, y) }
    else null
}
fun adyacentesAbajo(mapa:MutableList<List<String>>, actual:Pair<Int, Int>):Pair<Int, Int>?{
    val (x,y)=actual
    return if(x<mapa.size){Pair(x+1, y)}
    else null

}
fun adyacentesIzq(mapa:MutableList<List<String>>, actual:Pair<Int, Int>):Pair<Int, Int>?{
    val (x,y)=actual
    return if(y>0) {Pair(x, y-1)}
    else null
}
fun adyacentesDcha(mapa:MutableList<List<String>>, actual:Pair<Int, Int>):Pair<Int, Int>?{
    val (x,y)=actual
    return if(y<mapa[0].size) {Pair(x, y+1)}
    else null

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