package david.ramirez2.upr.edu.atencioncorilla

import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.fragment_item.view.*
import kotlinx.android.synthetic.main.fragment_solicitudes.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 *  Replace the implementation with code for your data type.
 */
var requestlist: MutableList<String> = mutableListOf()
fun fetchRequests(){
    requestlist.clear()
    Log.d("Test", "List: $requestlist ")

    val CurrentUser = FirebaseAuth.getInstance().currentUser

    val ref = FirebaseDatabase.getInstance().getReference("/solicitudes/${CurrentUser!!.getDisplayName()}")
    Log.d("Test", "Contacts:/contacts/${CurrentUser!!.getDisplayName()} : $requestlist ")


    ref.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Log.d("Test","database error")

            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val contact = dataSnapshot.children
            contact.forEach {
                val CompaUser = it.key
//
                val Bool = it.getValue()
//                Log.d("Firebase", Bool!!)
//


                if (Bool == true) {
                    Log.d("Test", "Will be Adding:$CompaUser")
                    requestlist.add(CompaUser.toString())
                }

            }
            Log.d("Test", "After Adding:$requestlist")
        }
    })
}

class MainAdapter2: RecyclerView.Adapter<CustomViewHolder>() {

    private lateinit var deleterequest : FloatingActionButton
    private lateinit var addrequest : FloatingActionButton

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        Log.d("Test", "compa:${requestlist.get(p1)}")

        val compa = requestlist.get(p1)
        p0.view.solicitante.text = compa
    }

    // numberOfItems
    override fun getItemCount(): Int {
        Log.d("Test", "Size:${requestlist.size}")

        return requestlist.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_solicitudes, p0, false)

        deleterequest = cellForRow.findViewById(R.id.deleteRequest)
        deleterequest.setOnClickListener{
            val CurrentUser = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase.getInstance().getReference("/solicitudes/${CurrentUser!!.getDisplayName()}")
            val contact = cellForRow.solicitante.text
            ref.child("$contact").removeValue()
            fetchRequests()
        }

        addrequest = cellForRow.findViewById(R.id.AddContact)
        addrequest.setOnClickListener{
            val CurrentUser = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase.getInstance().getReference("/solicitudes/${CurrentUser!!.getDisplayName()}")
            val contact = cellForRow.solicitante.text
            ref.child("$contact").removeValue()
            fetchRequests()

            val reference = FirebaseDatabase.getInstance().getReference("/contacts/${CurrentUser!!.getDisplayName()}")
            reference.child("$contact").setValue(true)
            fetchCurrentUser()
            val reference2 = FirebaseDatabase.getInstance().getReference("/contacts/$contact")
            reference2.child("${CurrentUser!!.getDisplayName()}").setValue(true)
        }

        return CustomViewHolder(cellForRow)
    }

}


