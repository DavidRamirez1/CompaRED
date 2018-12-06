package david.ramirez2.upr.edu.atencioncorilla

import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
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

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 *  Replace the implementation with code for your data type.
 */
private val contactlist: MutableList<String> = mutableListOf()
data class Compas(
        var value: Boolean? = null,
        val user: String = ""){ constructor() : this(null, "")  }

private fun fetchCurrentUser() {
    val CurrentUser = FirebaseAuth.getInstance().currentUser
    val ref = FirebaseDatabase.getInstance().getReference("/contacts/$CurrentUser")
    ref.addValueEventListener(object: ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val contact =dataSnapshot.children
            contact.forEach{
                val contactData = it.getValue(Compas::class.java)!!

                val CompaUser = contactData.user
                val Bool = contactData.value

                if(Bool!!) {
                    contactlist.add(CompaUser)
                }

            }
        }
    })
}

class MainAdapter: RecyclerView.Adapter<CustomViewHolder>() {
    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        val compa = contactlist.get(p1)
        p0?.view?.ContactName?.text = compa
    }

    // numberOfItems
    override fun getItemCount(): Int {
        fetchCurrentUser()
        return contactlist.size
    }

   override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomViewHolder {
        // how do we even create a view
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_item_list, p0, false)
        return CustomViewHolder(cellForRow)
    }



}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
