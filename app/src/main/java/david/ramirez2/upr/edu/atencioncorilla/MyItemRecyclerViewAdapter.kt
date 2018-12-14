package david.ramirez2.upr.edu.atencioncorilla

import android.provider.ContactsContract
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
var contactlist: MutableList<String> = mutableListOf()
//data class Compas(
//        var value: Boolean? = null,
//        val user: String = ""){ constructor() : this(null, "")  }


fun fetchCurrentUser(){
    contactlist.clear()
    Log.d("Test", "List: $contactlist ")

    val CurrentUser = FirebaseAuth.getInstance().currentUser

    val ref = FirebaseDatabase.getInstance().getReference("/contacts/${CurrentUser!!.getDisplayName()}")
    Log.d("Test", "Contacts:/contacts/${CurrentUser!!.getDisplayName()} : $contactlist ")


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
                    contactlist.add(CompaUser.toString())
                }

            }
            Log.d("Test", "After Adding:$contactlist")
        }
    })
}

class MainAdapter: RecyclerView.Adapter<CustomViewHolder>() {
    private lateinit var deletecontact : FloatingActionButton
    private lateinit var sendmessage : FloatingActionButton

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        Log.d("Test", "compa:${contactlist.get(p1)}")

        val compa = contactlist.get(p1)
        p0.view.ContactName.text = compa
    }

    // numberOfItems
    override fun getItemCount(): Int {
        Log.d("Test", "Size:${contactlist.size}")

        return contactlist.size
    }

   override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_item, p0, false)


        deletecontact = cellForRow.findViewById(R.id.deleteContact)
        deletecontact.setOnClickListener{
            val CurrentUser = FirebaseAuth.getInstance().currentUser
            val ref = FirebaseDatabase.getInstance().getReference("/contacts/${CurrentUser!!.getDisplayName()}")
            val contact = cellForRow.ContactName.text
            ref.child("$contact").removeValue()
            fetchCurrentUser()
        }

       sendmessage = cellForRow.findViewById(R.id.sendMessage)

       sendmessage.setOnClickListener{
           val CurrentUser = FirebaseAuth.getInstance().currentUser
           val sender = CurrentUser!!.getDisplayName()
           val contact = cellForRow.ContactName.text
           var ref = FirebaseDatabase.getInstance().getReference("/mensajes/${sender}:$contact")
//           val key = ref.push().key
           val key = "key"
           val message = "maria quiere que cambie el mensaje"
           ref.child(key!!).child("sender").setValue("$sender")
           ref.child(key!!).child("message").setValue(message)
           ref = FirebaseDatabase.getInstance().getReference("/mensajes/${contact}:$sender")
           ref.push()
           ref.child(key!!).child("sender").setValue("$contact")
           ref.child(key!!).child("message").setValue(message)


       }

        return CustomViewHolder(cellForRow)
    }

}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
