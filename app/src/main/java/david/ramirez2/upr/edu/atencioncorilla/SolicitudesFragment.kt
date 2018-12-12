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
import kotlinx.android.synthetic.main.fragment_item.view.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ItemFragment.OnListFragmentInteractionListener] interface.
 */
class SolicitudesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.d("Test", "From Solicitudes:$contactlist")

        val view = inflater.inflate(R.layout.fragment_solicitudes_list, container, false)
        view.findViewById<RecyclerView>(R.id.solicitudrecycleview).adapter = MainAdapter2()

        // Added in Sprint 4

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
