package david.ramirez2.upr.edu.atencioncorilla

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


import david.ramirez2.upr.edu.atencioncorilla.ItemFragment.OnListFragmentInteractionListener
import david.ramirez2.upr.edu.atencioncorilla.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */

data class Compas(
        var value: Boolean? = null,
        val uid: String = ""){    constructor() : this(null, "")  }

private fun fetchCurrentUser() {
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("/contacts/$uid")

    c


}


class MyAdapter : RecyclerView.Adapter() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
    }
    f    override fun getItemCount(): Int {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}