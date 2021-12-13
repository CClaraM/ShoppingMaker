package com.myapplication.shopingmarket.dataModel

class CarItem internal constructor(
    val itemId: String,
    var itemAmount: Float,
    var itemImage: String,
    var itemPrice: Float,
    var itemDiscount: Float,
    var itemMax : Int,
    var itemStock: Int,
    var itemCheck: Boolean

)