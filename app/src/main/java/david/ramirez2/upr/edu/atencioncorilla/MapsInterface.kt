package david.ramirez2.upr.edu.atencioncorilla

import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.InputType.TYPE_CLASS_TEXT
import android.view.View
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsInterface : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
   // private lateinit var builder: AlertDialog.Builder


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_interface)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //var input = EditText(this)

      //  builder.setTitle("Enter Report Information")

      //  input.inputType = InputType.TYPE_CLASS_TEXT
      //  builder.setView(input)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        //builder = dialog
        //builder.setTitle("Hello")

        /*

        mMap.setOnMapClickListener{point: LatLng ->

            mMap.addMarker(MarkerOptions().position(point).title("Testing"))
            //   mMap.addMarker(MarkerOptions().)

        }*/

        //  setPermission()

        /*  if (ActivityCompat.checkSelfPermission(this,
                          android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
              ActivityCompat.requestPermissions(this,
                      arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
              return
          }*/

        // Asking for permissions to access User's locations

        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this){ location ->
            if (location != null){

                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))

            }
        }

        //  mMap.addMarker(MarkerOptions().position(18.46333, -66.105721))


        /*  // Add a marker in Sydney and move the camera
          val sydney = LatLng(18.46333, -66.105721)
          mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
          val sydney2 = LatLng(19.46333, -65.105721)
          mMap.addMarker(MarkerOptions().position(sydney2).title("Marker in Sydney"))*/

        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))
    }

    fun reportPin(view: View) {

        //val input = EditText(this)
      //  builder.setView(input)

       // val input = EditText(this)
       // builder.setView(input)

        // If the button clicked is the "Report" button

        if(view.id == R.id.report) {

            // If button clicked, turn on the Map click listener to allow user to
            // set a report pin on the map

            mMap.setOnMapClickListener {point: LatLng ->

                val dialog = AlertDialog.Builder(this)
                val input = EditText(this)
                input.inputType = TYPE_CLASS_TEXT
                dialog.setView(input)

                dialog.setTitle("Confirm Report")
                        .setMessage("Confirm!")
                        .setPositiveButton("OK", {dialog, i ->
                            val description = input.text.toString()
                            mMap.addMarker(MarkerOptions().position(point).title(description))
                        })
                        .setNegativeButton("Cancel",{dialog, which ->
                        })

                dialog.show()

               /* builder.setPositiveButton("OK"){dialog: DialogInterface, which: Int ->

                    val description = input.text.toString()
                    mMap.addMarker(MarkerOptions().position(point).title(description))
                }

                builder.setNegativeButton("Cancel"){dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                }

                builder.show()*/
                //   mMap.addMarker(MarkerOptions().)
                //if(view.id == R.id.report)

                // After report is added, turn the Map click listener off

                mMap.setOnMapClickListener(null)
            }
        }
    }
}
