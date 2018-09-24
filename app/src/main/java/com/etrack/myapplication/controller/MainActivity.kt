package com.etrack.myapplication.controller

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.etrack.myapplication.R
import com.etrack.myapplication.commonUtils.CommonUtils
import com.etrack.myapplication.commonUtils.ConnectivityReceiver
import com.etrack.myapplication.commonUtils.FrgmentName
import com.etrack.myapplication.commonUtils.SharedPreferenceHelper
import com.etrack.myapplication.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private var mSnackbar: Snackbar? = null
    private var mContext: Context? = null;
    private lateinit var userName: TextView
    private var mCurrentFragment = "It will contain Fragment name"
    private var fManager: FragmentManager? = null
    private var connectivityReceiver: ConnectivityReceiver? = null
    private var navigationView: NavigationView? = null
    val toggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    }

    //  private var coordinatorLayout: CoordinatorLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fManager = supportFragmentManager
        mContext = applicationContext
        mSnackbar = Snackbar.make(coordinatorLayout, getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.ok)) { mSnackbar!!.dismiss() }
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.syncState()
        drawer_layout.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)
        val header = nav_view.getHeaderView(0)
        userName = header!!.userName
        //  showPurchaseItem()
        // setDashboard()
        setHomePage()
        //  showHome()
        //showAddItem()
    }

    fun setDashboard() {
        var userRole = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_USERROLE, "empty")
        if (userRole.equals("1") || userRole.equals("2")) {
            nav_view.getMenu().clear()
            setDrawerState(true)
            nav_view.inflateMenu(R.menu.activity_admin_drawer)
        } else if (userRole.equals("3")) {
            setDrawerState(true)
            nav_view.getMenu().clear()
            nav_view.inflateMenu(R.menu.activity_user_drawer)
        } else if (userRole.equals("empty")) {
            setDrawerState(false)
        }

    }

    fun setDrawerState(isEnabled: Boolean) {
        if (isEnabled) {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            toggle.setDrawerIndicatorEnabled(true)

        } else {
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            toggle.setDrawerIndicatorEnabled(false)
        }

    }

    fun setHomePage() {
        setDashboard()
        var userId = SharedPreferenceHelper.getSharedPreferenceInt(mContext, SharedPreferenceHelper.PREF_APP_USERID, 0)
        var userRole = SharedPreferenceHelper.getSharedPreferenceString(mContext, SharedPreferenceHelper.PREF_APP_USERROLE, "empty")
        if (userId == 0 && userRole.equals("empty")) {

            showHome()
        } else if (userId != 0 && !userRole.equals("empty")) {
            userName.setText(CommonUtils.getUserName(mContext))
            showPurchaseItem()
        }
    }

    override fun onBackPressed() {
/*        val fr =.javaClass
        if (fr != null) {
            Log.e("fragment=",  )
        }*/
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            mCurrentFragment
            if (fManager!!.getBackStackEntryCount() == 1 || fManager!!.getBackStackEntryCount() == 0) {
                CommonUtils.popupDialog(this@MainActivity, "Exit App", getString(R.string.exit_message), getString(R.string.yes), getString(R.string.no))
            } else {
                super.onBackPressed()
            }
        }
    }
    /* override fun onBackPressed() {
         if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
             drawer_layout.closeDrawer(GravityCompat.START)
         } else {
             super.onBackPressed()
         }
     }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkUnavailableMsg(isConnected)
    }

    fun showNetworkUnavailableMsg(isConnected: Boolean) {
        val color: Int
        if (isConnected) {
            color = Color.WHITE
            DismissSnackBar()
        } else {
            color = Color.RED
            mSnackbar!!.show()
        }
    }

    fun DismissSnackBar() {
        if (mSnackbar!!.isShown()) {
            mSnackbar!!.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
        MyApplication.getInstance().setConnectivityListener(this)
    }

    override fun onPause() {
        super.onPause()
        if (connectivityReceiver != null) {
            unregisterReceiver(connectivityReceiver)
        }
    }

    fun registerBroadcastReceiver() {
        connectivityReceiver = ConnectivityReceiver()
        val intentFilter = IntentFilter()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //For Nougat
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }   //  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_CHANGE);
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_purchase -> {
                showPurchaseItem()
            }
            R.id.nav_sales_item -> {
                showSalesFragment()
            }
            R.id.nav_stock -> {
                showStockAdjustment()
            }
            R.id.nav_add_item -> {
                showAddItem()
            }
            R.id.nav_add_group -> {
                showAddCatogory()
            }
            R.id.nav_add_branch -> {
                showCreateBranch()
            }
            R.id.nav_del_branch -> {
                showDeleteBranch()
            }
            R.id.nav_del_group -> {
                showDeleteCatog()
            }
            R.id.nav_del_item -> {
                showDeleteItem()
            }
            R.id.nav_del_user -> {
                // showDeleteBranch()
            }
            R.id.nav_report_gen -> {
                showReportGendratebyBill()
            }
            R.id.nav_report_gen_item -> {
                showReportGendratebyItem()
            }
            R.id.nav_report_item_hist -> {
                showReportGendratebyItemHistory()
            }
            R.id.nav_report_item_list -> {
                showReportGendratebyItemList()
            }
            R.id.nav_add_acco -> {
                showCreateAccount()
            }
            R.id.nav_create_user -> {
                showCreateUser()
            }
            R.id.nav_delete_acc -> {
                showDeleteAccount()
            }

            R.id.nav_logout -> {
                showLogout()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showReportGendratebyBill() {
        val fragment = GenderateReportBillFragment()
        mCurrentFragment = FrgmentName.FRAG_REPORT_GENDRATE_BY_BILL
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }
    private fun showReportGendratebyItemList() {
        val fragment = GenReportItemListFragment()
        mCurrentFragment = FrgmentName.FRAG_REPORT_GENDRATE_BY_ITEM_LIST
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }
    private fun showReportGendratebyItemHistory() {
        val fragment = GenReportItemHistoryFragment()
        mCurrentFragment = FrgmentName.FRAG_REPORT_GENDRATE_BY_ITEM_HISTORY
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }
    private fun showReportGendratebyItem() {
        val fragment = GenderateReportItemFragment()
        mCurrentFragment = FrgmentName.FRAG_REPORT_GENDRATE_BY_ITEM
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }


    fun fragmentTrans(idContainer: Int, frag: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(idContainer, frag)
                .commit()
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun showLoginScreen() {
        val fragment = LoginFragment()
        mCurrentFragment = FrgmentName.FRAG_LOGIN_SCREEN
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showSalesFragment() {
        popFullBackStack()
        val fragment = SalesFragment()
        mCurrentFragment = FrgmentName.FRAG_SALES
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showCreateBranch() {
        val fragment = CreateBranchFragment()
        mCurrentFragment = FrgmentName.FRAG_ADD_BRANCH
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showCreateUser() {
        val fragment = CreateNewUserFragment()
        mCurrentFragment = FrgmentName.FRAG_COMPANY_CREATE
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showDeleteAccount() {
        val fragment = DeleteAccountFragment()
        mCurrentFragment = FrgmentName.FRAG_DELETE_ACCOUNT
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showDeleteItem() {
        val fragment = DeleteItemFragment()
        mCurrentFragment = FrgmentName.FRAG_DELETE_ITEM
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showCreateAccount() {
        val fragment = AddAccountFragment()
        mCurrentFragment = FrgmentName.FRAG_ADD_ACCOUNT
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showAddItem() {
        val fragment = AddItemFragment()
        mCurrentFragment = FrgmentName.FRAG_ADD_ITEM
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }


    fun showStockAdjustment() {
        popFullBackStack()
        val fragment = StockFragment()
        mCurrentFragment = FrgmentName.FRAG_STOCK
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showDeleteUser() {
        val fragment = DeleteBranchFragment()
        mCurrentFragment = FrgmentName.FRAG_DELETE_USER
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showDeleteBranch() {
        val fragment = DeleteBranchFragment()
        mCurrentFragment = FrgmentName.FRAG_DELETE_BRANCH
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showDeleteCatog() {
        val fragment = DeleteCatogFragment()
        mCurrentFragment = FrgmentName.FRAG_DELETE_CATAGOREY
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showRegister() {
        val fragment = RegistrationCompFragment()
        mCurrentFragment = FrgmentName.FRAG_REGISTER_SCREEN
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showHome() {
        val fragment = HomeFragment()
        mCurrentFragment = FrgmentName.FRAG_REGISTER_SCREEN
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showAddCatogory() {
        val fragment = CreateGroupFragment()
        mCurrentFragment = FrgmentName.FRAG_ADD_CATAGOREY
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    /*  fun showCompanyCreate() {
          val fragment = CompanyFragment()
          mCurrentFragment = FrgmentName.FRAG_LOGIN_SCREEN
          val fTrans = fManager!!.beginTransaction()
          fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
          fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
          fTrans.addToBackStack(mCurrentFragment)
          fTrans.commit()
      }*/
    fun showPurchaseItem() {
        popFullBackStack()
        val fragment = PurchaseFragment()
        mCurrentFragment = FrgmentName.FRAG_PURCHASE
        val fTrans = fManager!!.beginTransaction()
        fTrans.setCustomAnimations(R.anim.right_enter, R.anim.left_exit, R.anim.left_in, R.anim.right_out)
        fTrans.replace(R.id.content_hom, fragment, mCurrentFragment)
        fTrans.addToBackStack(mCurrentFragment)
        fTrans.commit()
    }

    fun showLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.Are_you_sure_logout)
                .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
                    SharedPreferenceHelper.clearSharedPreferencence(mContext)
                    popFullBackStack()
                    setHomePage()
                })
                .setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = builder.create()
        alert.show()
    }

    fun popFullBackStack() {
        fManager!!.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

