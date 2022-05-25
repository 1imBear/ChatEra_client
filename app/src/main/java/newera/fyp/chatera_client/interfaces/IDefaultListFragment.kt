package newera.fyp.chatera_client.interfaces

import android.view.View

interface IDefaultListFragment {
    fun setList(view: View)
    fun getList()
    fun delItem(position: Int)
    fun onNewItem()
    fun onDelItem(position: Int)
    fun clearList()
}