package david.ramirez2.upr.edu.atencioncorilla


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build.VERSION_CODES.N
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import java.time.Month
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS



// Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap // Holds the instanced google map
    private lateinit var fusedLocationClient: FusedLocationProviderClient // Handles the user's location
    private lateinit var lastLocation: Location // Stores the user's most recent updated location
   // private lateinit var nav: NavigationActivity
    private lateinit var mFirebaseDatabase: FirebaseDatabase // Handles the instance of the firebase to retrieve information
    private lateinit var mFirebaseReference: DatabaseReference // Handles the instanced inforamtion retrived
    private lateinit var reportButton: Button // Button used for user reporting
    private lateinit var filterButton: Button // Button used to filter the map
    private lateinit var heatmapButton: Button // Button to toggle heat map on and off
    private lateinit var mOverlay: TileOverlay // Handles the heatmap overlay

    // Used to save the date and time picked

    private var PinYear: Int = 0
    private var PinMonth: Int = 0
    private var PinDay: Int = 0
    private var PinHour: String = ""
    private var PinMinute: String = ""

    // Used to save the data of a pin's category and position

    private var PinCategory: String = ""
    private lateinit var PinPosition: LatLng
    private var PinLatitude: Double = 0.0
    private var PinLongitude: Double = 0.0

    // Lists used to handle pin information for map and heatmap

    private val pinList: MutableList<Pins> = mutableListOf()
    private val heatList: MutableList<LatLng> = mutableListOf()

    // Holds the filter type on the map

    private var FilterType: String = "Todos"

    // Handles whether the heatmap is on or off

    private var isHeatMapOn: Boolean = false

    // Used for custom buttoms on Date and Time pickers

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Initialize the buttons with their corresponding ids

        // Added in Sprint 4

        reportButton = view.findViewById(R.id.report)
        filterButton = view.findViewById(R.id.filter)
        heatmapButton = view.findViewById(R.id.heatmap)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the mapFragment, and fusedLocationClient

        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        // Initializing the map

        val v = view
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Getting an instance of the database to prepare for data reading

        mFirebaseDatabase = FirebaseDatabase.getInstance()

        // Add the data pins from the database

        mFirebaseReference = mFirebaseDatabase.getReference()

        // Define a Value Event Listener object for the Firebase Reference

        // This will trigger at the start for each data in the DB, aswell as when a new pin is introduced onto the DB

        val pinListener = object : ValueEventListener{ // Added in Sprint 4

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Loads every Pin

               // pinList.clear()

               // val pin = dataSnapshot.getValue(Pins::class.java)
             //   Log.d("Msg222", "HeyoHeyo " + pin!!.Category)
             //   pinList.add(pin)

                //dataSnapshot.children.mapNotNullTo(pinList){
                 //   Log.d("Size2", "We in here?")

                    //dataSnapshot.value<Pins>(Pins::class.java)
                  //  Log.d("Hmm", "Category " + pinList[0].Category )
                //}
                Log.d("Count", "The count is " + dataSnapshot.childrenCount)
                Log.d("Value", "Test: ${dataSnapshot.value}")

                val pin = dataSnapshot.children

                pin.forEach{

                    Log.d("PinsInfo", "Pin Info Is: " + it.value)
                    val pinData = it.getValue(Pins::class.java)!!

                    PinCategory = pinData.Category
                    PinLatitude = pinData.Latitude
                    PinLongitude = pinData.Longitude
                    PinMonth = pinData.Month
                    PinDay = pinData.Day
                    PinYear = pinData.Year
                    PinHour = pinData.Hour
                    PinMinute = pinData.Minute
                    PinPosition = LatLng(PinLatitude, PinLongitude)

                    // If there's no filter in place or the category is the same as the filter type, post the pin

                   // if(FilterType == "Todos" || FilterType == PinCategory ) {
                    localPostPin()
                   // }

                    pinList.add(pinData)

                    Log.d("PinList","List info empty?!?! Category: " + pinList[0].Category)
                    Log.d("Size ", "Size of Pin List is " + pinList.size)

                }



               /* for(postSnapshot: DataSnapshot in dataSnapshot.children){

                   val pin = postSnapshot.getValue(Pins::class.java)
                    Log.d("Pin", "Pin info is " + pin)
                    pinList.add(pin!!)

                }*/

            }

            override fun onCancelled(databaseError: DatabaseError) {

                // If there was an error accessing data from Firebase

                Toast.makeText(activity, "Ocurrio un error al tratar de cargar informacion de la base de datos...", Toast.LENGTH_SHORT).show()
                Log.d("msg", "Error accessing info from database")
            }
        }

        // After defining the listener object, add it to the FirebaseReference Listener
        // This will handle putting all the pins from the database into the map.

        mFirebaseReference.child("Pins").addListenerForSingleValueEvent(pinListener)

        // Define the reportButton click listener

        // Set-up the report button listener so that after clicking, the user can report somewhere onto the map

        reportButton.setOnClickListener {

            Toast.makeText(activity, "Escoga el area donde quiera hacer su reporte", Toast.LENGTH_SHORT).show()

            mMap.setOnMapClickListener { point: LatLng ->

                val vr = layoutInflater.inflate(R.layout.pin_category_spinner, null)
                val mSpinner = vr!!.findViewById<Spinner>(R.id.spinner)
                val dialogPin = AlertDialog.Builder(v!!.context)

                val adapter = ArrayAdapter<String>(v.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.pincategoryList))

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                mSpinner.adapter = adapter

                // We do this to set the default values of the date and time as the current date and time

                val cal = Calendar.getInstance()
                val year = cal.get(Calendar.YEAR)
                val month = cal.get(Calendar.MONTH)
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                // Listener for the Date Dialog

                val mDateListener = DatePickerDialog.OnDateSetListener { view, year1, month1, day1 ->

                    // When called, save the date

                    val i = month1 + 1 // Since month is handled from 0 to 11, we add 1 for it to be from 1 - 12
                    PinYear = year1
                    PinMonth = i
                    PinDay = day1
                    Log.d("msg", "Data changed")
                }

                // Listener for the Time Dialog

                val mTimeListener = TimePickerDialog.OnTimeSetListener { view, h, m ->

                    // When called, save the time

                    PinHour = h.toString()
                    PinMinute = m.toString()

                    // Since we got the position, date and time, we can now post the pin onto the map and database

                    postPin()

                }

                // Initialize DatePicker and TimePicker

                val dialogDate = DatePickerDialog(context!!, mDateListener, year, month, day)
                val dialogTime = TimePickerDialog(context, mTimeListener, hour, minute, android.text.format.DateFormat.is24HourFormat(context))

                // Callback for when user clicks "OK" in Date dialog

                dialogDate.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { dialog2, which ->

                    if(which == DialogInterface.BUTTON_POSITIVE){

                        // When user clicks ok, we make a call to the mDateListener to save the values

                        val dp = dialogDate.datePicker
                        mDateListener.onDateSet(dp, dp.year, dp.month, dp.dayOfMonth)

                        // Call the time dialog to get the time

                        dialogTime.show()
                        Toast.makeText(activity, "Someta la hora aproximada en el que ocurrio el incidente", Toast.LENGTH_SHORT).show()
                    }

                }

                // Callback for when the user clicks "Cancel"

                dialogDate.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { dialog3, which ->

                    // If user cancels, dismiss the dialog and let the user know

                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        dialogDate.dismiss()
                        Toast.makeText(activity, "Reporte cancelado", Toast.LENGTH_SHORT).show()
                    }
                }
/*
            dialogTime.setButton(DialogInterface.BUTTON_POSITIVE, "OK"){ dialog4, which ->

                if(which == DialogInterface.BUTTON_POSITIVE){
                    OkayPressed = true


                    dialogTime.updateTime()
                    // Post the Pin on the map with the user input information
                    //Toast.makeText(activity, "HE", Toast.LENGTH_SHORT).show()
                  //  postPin()

                }

            }

            dialogTime.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel"){ dialog5, which ->

                if(which == DialogInterface.BUTTON_NEGATIVE) {
                    dialogTime.dismiss()
                }
            }*/


                //dialogDate.setTitle("Establecer Hora")
                dialogTime.setTitle("Establecer Hora")

                dialogPin.setTitle("Reporte")
                        .setMessage("Categoria de Reporte")
                        .setPositiveButton("OK") { dialog, i ->

                            // If the user selected ok, if it wasn't the default value

                            if (!mSpinner.selectedItem.toString().equals("Escoja el tipo de reporte...")) {

                                // Save the selected category and position, used later to post the pin

                                PinCategory = mSpinner.selectedItem.toString()
                                PinPosition = point
                                PinLatitude = point.latitude
                                PinLongitude = point.longitude

                                // Show the next dialog, the date dialog so that the user can input the date.

                                dialogDate.show()
                                Toast.makeText(activity, "Someta la fecha en el que ocurrio el incidente", Toast.LENGTH_SHORT).show()

                            } else {

                                // If the user didn't choose a valid option, display error message

                                Toast.makeText(activity, "Por favor escoja una categoria para reportar!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Cancel") { dialog, which ->

                            Toast.makeText(activity, "Reporte cancelado", Toast.LENGTH_SHORT).show()
                            // Dismisses the dialog window
                        }

                // After setting up the dialog window, we set up the view on the Dialog and launch it

                dialogPin.setView(vr)
                dialogPin.show()

                // Turn Map Click Listener off after adding the pin

                mMap.setOnMapClickListener(null)

            }
        }

        // Define the filterButton listener

        filterButton.setOnClickListener {

            // After clicking the button, make a AlertDialog w/ a spinner pop up with the filter options

            Toast.makeText(activity, "Escoga como quiere filtrar el mapa", Toast.LENGTH_SHORT).show()

            // Setting up the spinner for the dialog

            val spinnerView = layoutInflater.inflate(R.layout.pin_category_spinner, null)
            val filterSpinner = spinnerView!!.findViewById<Spinner>(R.id.spinner)
            val dialogFilter = AlertDialog.Builder(v!!.context)

            // Loads up the options for the spinner

            val adapter = ArrayAdapter<String>(v.context, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.filterList))

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            filterSpinner.adapter = adapter

            dialogFilter.setTitle("Filtro")
                    .setMessage("Escoja el tipo de reporte que quiere ver")
                    .setPositiveButton("OK") { dialog, i ->

                        // Ocurrs when user clicks "OK"

                        // Takes the chosen filter value

                        FilterType = filterSpinner.selectedItem.toString()

                        // Executes filter() to filter the map according to the chosen filter value

                        filter()

                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        // Dismisses the dialog window
                    }

            // Show the dialog on top of the map

            dialogFilter.setView(spinnerView)
            dialogFilter.show()
        }

        // Define the heatmapButton click listener

        heatmapButton.setOnClickListener {

            // Calls addHeatMap to add the heatmap overlay on top of the map

            addHeatMap()
            if(!isHeatMapOn){
            Toast.makeText(activity, "Filtro de Calor de Mapa", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity, "Filtro de Pines", Toast.LENGTH_SHORT).show()
            }
        }

        // Asks permission to access user location

        if (ActivityCompat.checkSelfPermission(v!!.context,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MapFragment.LOCATION_PERMISSION_REQUEST_CODE)
        }

        // After asking for permission, enable the user's location on the map

        mMap.isMyLocationEnabled = true

        // Handles updating the position of the user on the map

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))
            }
        }
    }


    private fun postPin(){ // Complete function added in Sprint 4. Used by Users to report pins into the database

        // Get an instance and reference from the database to add a pin into it

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseReference = mFirebaseDatabase.getReference()

        // Depending of the selected category with put a pin in the specified location with a particular color

        val key = mFirebaseReference.child("Pins").push().key

        // Add the pin values into the database

        val pin = Pins(PinCategory, PinLatitude, PinLongitude, PinMonth, PinDay, PinYear, PinHour, PinMinute)
        mFirebaseReference.child("Pins").child(key!!).setValue(pin)

        // Add the pin value to pinList for local use

        pinList.add(pin)

        // Add the pin onto the map locally

        localPostPin()
        Toast.makeText(activity, "El reporte se ha agregado al mapa.", Toast.LENGTH_SHORT).show()
    }

    fun localPostPin(){  // Function added in Sprint 4. Function used to mark pins in the map locally (Used by Firebase Data Listener and postPin())

        // Depending of the selected category with put a pin in the specified location with a particular color

        if(PinCategory == "Acoso") {

            // Local addition of marker

            mMap.addMarker(MarkerOptions()
                    .position(PinPosition)
                    .title(PinCategory)
                    .snippet("" + PinMonth + "/" + PinDay + "/" + PinYear
                            + " | " + PinHour + ":" + PinMinute))

        }

        else if(PinCategory == "Asalto") {

            // Local addition of marker

            mMap.addMarker(MarkerOptions()
                    .position(PinPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(PinCategory)
                    .snippet("" + PinMonth + "/" + PinDay + "/" + PinYear
                            + " | " + PinHour + ":" + PinMinute))
        }

        else if(PinCategory == "Violacion"){

            // Local addition of marker

            mMap.addMarker(MarkerOptions()
                    .position(PinPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title(PinCategory)
                    .snippet("" + PinMonth + "/" + PinDay + "/" + PinYear
                            + " | " + PinHour + ":" + PinMinute))
        }

        else if(PinCategory == "Otro"){

            // Local addition of marker

            mMap.addMarker(MarkerOptions()
                    .position(PinPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(PinCategory)
                    .snippet("" + PinMonth + "/" + PinDay + "/" + PinYear
                            + " | " + PinHour + ":" + PinMinute))
        }

       // Toast.makeText(activity, "El reporte se ha agregado al mapa.", Toast.LENGTH_SHORT).show()

    }

    fun filter(){ // Added in Sprint 4

        // Filters the pins in the map by the chosen category by the user

        // Clear the current markers from the map

        mMap.clear()

        // Put the new markers on the map according to the filter

        for(i in 0 until pinList.size){

            if(FilterType == pinList[i].Category || FilterType == "Todos") {

                // If the category of the pin is equal to the chose FilterType, or the FilterType
                // is set to "all", obtain the information of that pin

                PinCategory = pinList[i].Category
                PinLatitude = pinList[i].Latitude
                PinLongitude = pinList[i].Longitude
                PinPosition = LatLng(PinLatitude, PinLongitude)
                PinMonth = pinList[i].Month
                PinDay = pinList[i].Day
                PinYear = pinList[i].Year
                PinHour = pinList[i].Hour
                PinMinute = pinList[i].Minute

                // Used the obtained values and post the pin onto the map locally

                localPostPin()

            }
        }

    }

    fun addHeatMap(){ // Added in Sprint 4

        // This function handles creating and adding the overlay of the heat map onto the map

        // Clear the map from pins

        mMap.clear()

        if(isHeatMapOn){ // If heatmap is already on when button is pushed, turn it back to normal pin map

            // Repopulate the map with all the pins

            FilterType = "Todos"
            filter()
            isHeatMapOn = false

        }

        else {

            // Add the LatLng coordinates of each pin into a different list

            for (i in 0 until pinList.size) {

                val position = LatLng(pinList[i].Latitude, pinList[i].Longitude)
                heatList.add(position)

            }

            // Add the coordinate list into a HeatmapTileProvider

            val mProvider = HeatmapTileProvider.Builder()
                    .data(heatList)
                    .build()

            // Overlay the Heatmap Tile onto the map

            mOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))

            // Remember the heat map has been turned on

            isHeatMapOn = true
        }
    }
}

// Class Pins classifies and organizes the information that will be added to the database

class Pins(// Class added in Sprint 4

    var Category: String = "",
    var Latitude: Double = 0.0,
    var Longitude: Double = 0.0,
    //var Position: LatLng = LatLng(0.0, 0.0),
    var Month: Int = 0,
    var Day: Int = 0,
    var Year: Int = 0,
    var Hour: String = "",
    var Minute: String = ""
    // var uuid: String = ""

)

/*
class Pins{// Class added in Sprint 4

    var Category: String = ""
    var Latitude: Double = 0.0
    var Longitude: Double = 0.0
    //var Position: LatLng = LatLng(0.0, 0.0),
    var Month: Int = 0
    var Day: Int = 0
    var Year: Int = 0
    var Hour: String = ""
    var Minute: String = ""
   // var uuid: String = ""

    constructor() {

        // Needed by Firebase

    }

    constructor(category: String, latitude: Double, longitude: Double, month: Int, day: Int,
                year: Int, hour: String, minute: String){
        this.Category = category
        this.Latitude = latitude
        this.Longitude = longitude
        this.Month = month
        this.Day = day
        this.Year = year
        this.Hour = hour
        this.Minute = minute

    }

}*/
