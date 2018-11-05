package david.ramirez2.upr.edu.atencioncorilla


import android.content.pm.PackageManager
import android.location.Location
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationServices




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var mMap: GoogleMap
private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var lastLocation: Location
private lateinit var nav: NavigationActivity

/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback{

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val v = view
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        mMap.setOnMapClickListener { point: LatLng ->

            val dialog = AlertDialog.Builder(v!!.context)
            val input = EditText(v.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            dialog.setView(input)

            dialog.setTitle("Confirm Report")
                    .setMessage("Confirm!")
                    .setPositiveButton("OK", { dialog, i ->
                        val description = input.text.toString()
                        mMap.addMarker(MarkerOptions().position(point).title(description))
                    })
                    .setNegativeButton("Cancel", { dialog, which ->
                    })

            dialog.show()
        }


            //builder = dialog
        //builder.setTitle("Hello")


        /*
        mMap.setOnMapClickListener{point: LatLng ->

            mMap.addMarker(MarkerOptions().position(point).title("Testing"))
            //   mMap.addMarker(MarkerOptions().)

        }*/

        //  setPermission()
              /*
          if (ContextCompat.checkSelfPermission(NavigationActivity(),
                          android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             ContextCompat.requestPermissions(this,
                      arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

          }*/

        Log.d("HMM", "HELLO")

        if (ActivityCompat.checkSelfPermission(v!!.context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MapFragment.LOCATION_PERMISSION_REQUEST_CODE)
        }

        /*
        if (ContextCompat.checkSelfPermission(NavigationActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Show rationale and request permission.
        }*/

        // Asking for permissions to access User's locations

        mMap.isMyLocationEnabled = true

      //  val currentLatLng = LatLng(
      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))
      /*  fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))
           */

     //   fusedLocationClient.lastLocation.

                /*.addOnSuccessListener{ location ->

            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))

            }
        }*/

          //mMap.addMarker(MarkerOptions().position(18.46333, -66.105721))

          // Add a marker in Sydney and move the camera
          val sydney = LatLng(18.46333, -66.105721)
          mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
          val sydney2 = LatLng(19.46333, -65.105721)
          mMap.addMarker(MarkerOptions().position(sydney2).title("Marker in Sydney"))

         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f))
    }

    fun reportPin(view: View) {

        //val input = EditText(this)
        //  builder.setView(input)

        // val input = EditText(this)
        // builder.setView(input)

        // If the button clicked is the "Report" button

        if (view.id == R.id.report) {

            // If button clicked, turn on the Map click listener to allow user to
            // set a report pin on the map

            mMap.setOnMapClickListener { point: LatLng ->

                val dialog = AlertDialog.Builder(view.context)
                val input = EditText(view.context)
                input.inputType = InputType.TYPE_CLASS_TEXT
                dialog.setView(input)

                dialog.setTitle("Confirm Report")
                        .setMessage("Confirm!")
                        .setPositiveButton("OK", {dialog, i ->
                            val description = input.text.toString()
                            mMap.addMarker(MarkerOptions().position(point).title(description))
                        })
                        .setNegativeButton("Cancel", { dialog, which ->
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
