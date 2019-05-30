package com.devediapp.tarefakotlin.views

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.devediapp.tarefakotlin.R
import com.devediapp.tarefakotlin.business.PrioridadeBusiness
import com.devediapp.tarefakotlin.contants.TarefasConstants
import com.devediapp.tarefakotlin.repository.PrioridadeCacheConstantes
import com.devediapp.tarefakotlin.util.SecurityPreferences
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mPrioridadeBusiness: PrioridadeBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Remove nome do titulo do aplicativo
        supportActionBar?.setDisplayShowTitleEnabled(false)

        /*val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view_main)
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
        formatarNome()
        formatarData()
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
        var fragment: Fragment
        when (item.itemId) {
            R.id.nav_todos -> {
                fragment = ListaTarefaFragment.newInstance(TarefasConstants.FILTRO_TAREFAS.TODAS)
                inicializaFragment(fragment)
            }
            R.id.nav_done -> {
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

    private fun inicializaFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frameContent, fragment).commit()
    }

    private fun startDeafultFragment() {
        val fragment = ListaTarefaFragment.newInstance(TarefasConstants.FILTRO_TAREFAS.TODAS)
        inicializaFragment(fragment)
    }

    private fun fazerLogout() {

        //Apagar dados da memoria
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_NOME)
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_EMAIL)
        mSecurityPreferences.removerStoredString(TarefasConstants.KEY.USER_ID)

        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun armazenarCachePrioridade() {
        PrioridadeCacheConstantes.setCahe(mPrioridadeBusiness.getList())
    }

    private fun formatarNome() {
        val str = "Olá, ${mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_NOME)}!"
        textAppBarHello.text = str

        //Obter o filho da main que seria o navigation view, para conseguir obter o Nav Header Main na MainActivity, para alterar dados no header
        val navigationView = findViewById(R.id.nav_view_main) as NavigationView
        val header = navigationView.getHeaderView(0)

        val nome = header.findViewById<TextView>(R.id.textNavHeaderMainNome)
        val email = header.findViewById<TextView>(R.id.textNavHeaderMainEmail)
        nome.text = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_NOME)
        email.text = mSecurityPreferences.getRecuperarString(TarefasConstants.KEY.USER_EMAIL)
    }

    private fun formatarData() {
        val calendar = Calendar.getInstance()
        val days =
            arrayOf("Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado")
        val months = arrayOf(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )

        val str =
            "${days[calendar.get(Calendar.DAY_OF_WEEK) - 1]}, ${calendar.get(Calendar.DAY_OF_MONTH)} de ${months[calendar.get(
                Calendar.MONTH
            )]}"

        textAppBarDescricaoData.text = str
    }
}