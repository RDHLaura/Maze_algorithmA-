class nodo(padre_:nodo?, posicion_:Pair<Int, Int>, mapaP_:mapa ){
    var distancia=0
    var posicion =posicion_
    var padre:nodo?=padre_
    var adyacentes= mutableListOf<nodo>()
    var mapa=mapaP_
    init {
        if(mapa.entrada!=null && mapa.salida!=null)
            calculaDistancia()
    }

    fun calculaDistancia(){
        //calcula la distancia del nodo
        val (x2,y2)=posicion //posición del nodo
        val(x1, y1)=padre!!.posicion//posición del nodo padre
        val(x,y)=mapa.salida!!.posicion//posición del nodo de salida
        val g= Math.abs(x2 - x1) + Math.abs(y2 - y1)//calcula la distancia de la adyacente con la posición actual o previa
        val h= Math.abs(x2 - x) + Math.abs(y2 - y)//calcula la distancia de la adyacente con la posición final u objetivo
        val f= g+h
        distancia=f
    }

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