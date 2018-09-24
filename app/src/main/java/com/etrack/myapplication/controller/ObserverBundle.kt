package com.etrack.myapplication.controller

import android.os.Bundle
import java.util.*

/**
 * Created by Hussain on 20-03-2018.
 */
class ObservableBundle : Observable {
    private var mData: Bundle? = null

    var data: Bundle?
        get() = mData
        set(data) {
            this.mData = data
            setChanged()
            notifyObservers()
        }

    constructor(data: Bundle) {
        this.mData = data
    }

    constructor() {

    }

    companion object {
        val instance = ObservableBundle()
    }
}