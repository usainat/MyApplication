package com.etrack.myapplication.`interface`

import java.util.*

/**
 * Created by Hussain on 14-03-2018.
 */

interface ProductAdapterInterface {
    fun updateTotalCost(totalCost: Int)
    fun getProductAndCount(productIdCount: HashMap<Int, Int>)
    fun selectedItem(selectedItemsList: List<Int>)

}
