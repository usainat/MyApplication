package com.etrack.myapplication.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.etrack.myapplication.R
import com.etrack.myapplication.controller.MainActivity
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private var btn_login: Button? = null
    private var btn_Register: Button? = null
    private var mHom: MainActivity? = null;
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mHom = activity as MainActivity?
        var view = inflater!!.inflate(R.layout.fragment_home, container, false)
        btn_Register = view.btRegister
        btn_login = view.btLogin
        btn_login!!.setOnClickListener {
            mHom?.showLoginScreen()
        }
        btn_Register!!.setOnClickListener {
            mHom?.showRegister()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        mHom?.setDrawerState(false)
        //      mHom.setTitle(getString(R.string.pin_screen))
            mHom!!.setTitle(getString(R.string.login_register))

    }
}// Required empty public constructor
