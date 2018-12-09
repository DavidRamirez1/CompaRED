package david.ramirez2.upr.edu.atencioncorilla

import android.bluetooth.le.AdvertiseCallback
import android.content.Context
import android.os.Bundle
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

import david.ramirez2.upr.edu.atencioncorilla.dummy.DummyContent.DummyItem
data class Contact(val contact: String) {
    override fun toString(): String = contact
}
/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class ItemFragment : Fragment() {

    private lateinit var RemoveContact: Button
    private lateinit var MessageButton: Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.d("Test", "From Fragment:$contactlist")

        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        view.findViewById<RecyclerView>(R.id.listrecycleview).adapter = MainAdapter()

        // Added in Sprint 4
//
//        RemoveContact = view.findViewById(R.id.deleteContact)
//        MessageButton = view.findViewById(R.id.sendMessage)


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
        fun onListFragmentInteraction(item: Contact)
    }
//


}
