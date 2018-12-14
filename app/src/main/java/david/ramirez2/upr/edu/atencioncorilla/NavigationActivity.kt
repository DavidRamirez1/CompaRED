package david.ramirez2.upr.edu.atencioncorilla

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.maps.model.LatLng

import com.google.firebase.database.FirebaseDatabase

var message: String = ""
fun sendGroupMSG() {

    val CurrentUser = FirebaseAuth.getInstance().currentUser
    var username = CurrentUser!!.getDisplayName()
    for(x in contactlist){
        val ref = FirebaseDatabase.getInstance().getReference("/mensajes/$x")
        ref.child("${ref.push().key}").setValue("$username:$message")

    }
    message = ""

}
class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

      /*  if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }*/

        val mapFragment = MapFragment()
        val manager = supportFragmentManager
        manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit()
        fetchCurrentUser()
        fetchRequests()
        fetchMessages()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //val mapsInterface: MapsInterface

        /*
        fab.setOnClickListener {view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_map -> {
                // Handle the camera action
                fetchRequests()
                fetchCurrentUser()
                fetchMessages()
                val mapFragment = MapFragment()
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.mainLayout, mapFragment).commit()

            }
            R.id.nav_requests-> {
                val solicitudes = SolicitudesFragment()
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.mainLayout, solicitudes).commit()

            }
            R.id.nav_messages-> {
                val msgs = MessagesFragment()
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.mainLayout, msgs).commit()


            }
            R.id.nav_escribir-> {
                val dialog = AlertDialog.Builder(this)
                val input = EditText(this)
                input.setSingleLine(false)
                input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                input.setLines(3)
                input.setMaxLines(6)
                input.setGravity(Gravity.START)
                dialog.setView(input)

                dialog.setTitle("Escriba su mensaje")
                        .setMessage("Enviar!")
                        .setPositiveButton("OK") { dialog, i ->
                            message = input.text.toString()
                            sendGroupMSG()
                        }
                        .setNegativeButton("Cancelar") { dialog, which ->
                        }
                dialog.show()


            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            R.id.nav_contacts -> {
                val compas = ItemFragment()
                val manager = supportFragmentManager
                manager.beginTransaction().replace(R.id.mainLayout, compas).commit()



            }
            R.id.nav_panic-> {

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    val CurrentUser = FirebaseAuth.getInstance().currentUser
                    var username = CurrentUser!!.getDisplayName()
                    val message = "$username: PÁNICO! @ $currentLatLng"
                    for(x in contactlist){
                        val ref = FirebaseDatabase.getInstance().getReference("/mensajes/$x")
                        ref.child("${ref.push().key}").setValue(message)

                    }

                }
            }


            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
