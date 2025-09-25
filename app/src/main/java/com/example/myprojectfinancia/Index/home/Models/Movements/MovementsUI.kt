package com.example.myprojectfinancia.Index.home.Models.Movements

data class MovementsUI(
    val id: String,
    val fecha: String,
    val monto: String,
    val categoria: String,
    val naturaleza: String
) {

}

//fun MovementsFirestore: MovementsUI {
//    return MovementsUI(
//        id = this.id,
//        fecha = this.fecha,
//        monto = "${String.format("%.2f", this.monto)}",
//        categoria = this.categoria,
//        naturaleza = this.naturaleza
//
//    )
//}

