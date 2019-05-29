package com.devediapp.tarefakotlin.views

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.PrioridadeBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.repository.PrioridadeCacheConstantes
import com.devediapp.tarefakotlin.util.SecurityPreferences

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mSecurityPreferences : SecurityPreferences
    private lateinit var mPrioridadeBusiness : PrioridadeBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        /*val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        mSecurityPreferences = SecurityPreferences(this)
        mPrioridadeBusiness = PrioridadeBusiness(this)

        armazenarCachePrioridade()
        startDeafultFragment()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Retirado pois não precisaremos do menu superior direito
    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment : Fragment
        when (item.itemId) {
            R.id.nav_done -> {
                // Handle the camera action
                fragment = ListaTarefaFragment.newInstance(TarefasConstants.FILTRO_TAREFAS.COMPLETA)
                inicializaFragment(fragment)
            }
            R.id.nav_todo -> {
                fragment = ListaTarefaFragment.newInstance(TarefasConstants.FILTRO_TAREFAS.INCOMPLETA)
                inicializaFragment(fragment)
            }
            R.id.nav_logout -> {
                fazerLogout()
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun inicializaFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameContent, fragment).commit()
    }

    private fun startDeafultFragment(){
        val fragment = ListaTarefaFragment.newInstance(TarefasConstants.FILTRO_TAREFAS.TODAS)
        inicializaFragment(fragment)
    }

    private fun fazerLogout(){

        //Apagar dados da memoria
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_NOME)
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_EMAIL)
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_ID)

        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun armazenarCachePrioridade(){
        PrioridadeCacheConstantes.setCahe(mPrioridadeBusiness.getList())
    }
}
