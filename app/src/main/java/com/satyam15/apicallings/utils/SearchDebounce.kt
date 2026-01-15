package com.satyam15.apicallings.utils

import android.os.Handler
import android.os.Looper

class SearchDebounce(
    private val delayMillis:Long = 300L,
    private val onSearch:(String) -> Unit
) {
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    fun submitQuery(query: String){
        runnable?.let{ handler.removeCallbacks(it)}
        /**
         If user typed again:
            Cancel previous pending search
            This is the core debounce logic
         */
        runnable = Runnable{
            onSearch(query)
        }
        handler.postDelayed(runnable ?: return,delayMillis)
    }
    fun clear(){runnable?.let{handler.removeCallbacks(it)}}
}


