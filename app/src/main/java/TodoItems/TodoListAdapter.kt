package TodoItems

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listadetareasdatospersistentes.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_todo_item.view.*

class TodoListAdapter (val items:MutableList<TodoItemData>, val context:Context, val onClickEdit:(item:TodoItemData) -> Unit, val actualizar:() -> Unit):RecyclerView.Adapter<TodoListAdapter.TodoHolder>(){
    class TodoHolder(val itemTemplate:View):RecyclerView.ViewHolder(itemTemplate) {
        var todoDbController:TodoDb.Controller? = null
        fun render(item:TodoItemData, context: Context, onClickEdit:(item:TodoItemData) -> Unit, actualizar: () -> Unit,items: MutableList<TodoItemData>) {
            /* Datos del template */
            itemTemplate.vTodoTitle.text = item.title
            itemTemplate.vTodoMessage.text = item.message
            itemTemplate.vTodoDate.text = item.date
            Picasso.get().load(item.imageUri).into(itemTemplate.vTodoImage)
            /* Button Listeners */
            itemTemplate.vTodoEdit.setOnClickListener {
                onClickEdit(item)
            }
            itemTemplate.vTodoDone.setOnClickListener {
                todoDbController = TodoDb.Controller(context)
                todoDbController!!.delete(item)
                items.remove(item)
                actualizar()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TodoHolder(layoutInflater.inflate(R.layout.activity_main_todo_item, parent, false))
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        holder.render(items[position], context, onClickEdit,actualizar,items)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}