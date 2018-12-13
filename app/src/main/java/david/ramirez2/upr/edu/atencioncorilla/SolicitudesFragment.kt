package david.ramirez2.upr.edu.atencioncorilla

import android.bluetooth.le.AdvertiseCallback
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import david.ramirez2.upr.edu.atencioncorilla.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.android.synthetic.main.fragment_solicitudes.view.*
import kotlinx.android.synthetic.main.fragment_solicitudes_list.view.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class SolicitudesFragment : Fragment() {
    private lateinit var sendrequest : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.d("Test", "From Solicitudes:$contactlist")

        val view = inflater.inflate(R.layout.fragment_solicitudes_list, container, false)
        view.findViewById<RecyclerView>(R.id.solicitudrecycleview).adapter = MainAdapter2()

        // Added in Sprint 4
        sendrequest  = view.findViewById(R.id.addUserButton)
        sendrequest.setOnClickListener{
            val CurrentUser = FirebaseAuth.getInstance().currentUser
            val username = CurrentUser!!.getDisplayName()
            val contact = view.addUser.text.toString()
            var existence = false

            Log.d("Test", "From Solicitudes:$username $contact")

            if( username == contact){
                Toast.makeText(activity,"No puedes añadirte a ti mismo.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (contact == "") {
                    Log.d("Test", "From Solicitudes:Empty String entered")
                } else {

                    val reference = FirebaseDatabase.getInstance().getReference("/users")

                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            existence = p0.child("$contact").exists()
                            Log.d("Test", "From Solicitudes: $contact $existence")

                            if (existence) {
                                val ref = FirebaseDatabase.getInstance().getReference("/solicitudes/$contact")
                                ref.child("$username").setValue(true)
                            }
                        }
                    })
                    Toast.makeText(activity, "Solicitud enviada.", Toast.LENGTH_SHORT).show()
                }

            }
        }
        return view

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
    }

}
