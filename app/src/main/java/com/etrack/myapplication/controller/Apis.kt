package com.etrack.myapplication.controller

/**
 * Created by Hussain on 19-02-2018.
 */
object Apis {
    //  private static String PROD_API = "http://192.5.2.158:2001/api/";
    private val DEV_API = "http://megadealshop-001-site1.gtempurl.com/api/"
    private val PROD_API = "http://megadealshop-001-site1.gtempurl.com/api/"
    val pryrUrl = "http://api.aladhan.com/"
    val baseUrl: String
        get() = if (false)
            PROD_API
        else
            DEV_API
}
