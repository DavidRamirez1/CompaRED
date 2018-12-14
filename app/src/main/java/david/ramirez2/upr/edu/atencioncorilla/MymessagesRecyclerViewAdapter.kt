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
import kotlinx.android.synthetic.main.fragment_messages.view.*
import kotlinx.android.synthetic.main.fragment_solicitudes.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 *  Replace the implementation with code for your data type.
 */
var messagelist: MutableList<String> = mutableListOf()

fun fetchMessages(){
    messagelist.clear()
    Log.d("Test", "List: $messagelist ")

    val CurrentUser = FirebaseAuth.getInstance().currentUser

    val ref = FirebaseDatabase.getInstance().getReference("/mensajes/${CurrentUser!!.getDisplayName()}")
    Log.d("Test", "Contacts:/contacts/${CurrentUser!!.getDisplayName()} : $messagelist ")


    ref.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Log.d("Test","database error")

            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val contact = dataSnapshot.children
            contact.forEach {
                val message = it.key
//
                val msg = it.getValue()
//                Log.d("Firebase", Bool!!)
//
                    Log.d("Test", "Will be Adding:$message")
                    messagelist.add(msg.toString())


            }
            Log.d("Test", "After Adding:$contactlist")
        }
    })
}

class MainAdapter3: RecyclerView.Adapter<CustomViewHolder>() {

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        Log.d("Test", "compa:${messagelist.get(p1)}")

        val msg = messagelist.get(p1)
        p0.view.mensaje.text = msg
    }

    // numberOfItems
    override fun getItemCount(): Int {
        Log.d("Test", "Size:${messagelist.size}")

        return messagelist.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_messages, p0, false)

        return CustomViewHolder(cellForRow)
    }

}

