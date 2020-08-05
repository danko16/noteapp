package com.dicoding.mynotesapp

import android.view.View

class CustomOnItemClickListener(private val position: Int, private val onItemClickCallBack: OnItemCLickCallBack): View.OnClickListener {
    override fun onClick(view: View?) {
        onItemClickCallBack.onItemClicked(view, position)
    }

    interface OnItemCLickCallBack {
        fun onItemClicked(view: View?, position: Int)
    }
}