package TodoItems

import android.content.Context
import helpers.DataDbHelper

class TodoDb {
    abstract class TodoTable {
      companion object {
          val TABLE_NAME = "todos"
          val ID = "id"
          val TITLE = "title"
          val MESSAGE = "message"
          val DATE = "date"
          val IMAGE_LINK = "imageLink"
      }
    }
    class Controller(context: Context) {
        private val database:DataDbHelper
        init {
            database = DataDbHelper(context)
        }

        fun insert(item:TodoItemData) {
            database.values.put(TodoTable.TITLE, item.title)
            database.values.put(TodoTable.MESSAGE, item.message)
            database.values.put(TodoTable.DATE, item.date)
            database.values.put(TodoTable.IMAGE_LINK, item.imageUri)
            database.db.insert(
                TodoTable.TABLE_NAME,
                null,
                database.values
            )
        }
        fun update(item:TodoItemData) {
            database.values.put(TodoTable.TITLE, item.title)
            database.values.put(TodoTable.MESSAGE, item.message)
            database.values.put(TodoTable.DATE, item.date)
            database.values.put(TodoTable.IMAGE_LINK, item.imageUri)
            database.db.update(TodoTable.TABLE_NAME,database.values,"id=${item.id}",null)
        }

        fun delete(item:TodoItemData){
            database.values.put(TodoTable.TITLE, item.title)
            database.values.put(TodoTable.MESSAGE, item.message)
            database.values.put(TodoTable.DATE, item.date)
            database.values.put(TodoTable.IMAGE_LINK, item.imageUri)
            database.db.delete(TodoTable.TABLE_NAME,"id=${item.id}",null)
            getTodos()
        }


        fun getTodos():MutableList<TodoItemData>{
            var todoItems:MutableList<TodoItemData> = ArrayList()
            val cols = arrayOf(
                TodoTable.ID,
                TodoTable.TITLE,
                TodoTable.MESSAGE,
                TodoTable.DATE,
                TodoTable.IMAGE_LINK
            )
            val query = database.db.query(TodoTable.TABLE_NAME, cols, null, null, null, null, null)
            if(query.moveToFirst()){
                do {
                    todoItems.add(
                        TodoItemData(
                            query.getInt(0),
                            query.getString(1),
                            query.getString(2),
                            query.getString(3),
                            query.getString(4),
                        )
                    )
                }while(query.moveToNext())
            }
            return todoItems
        }
    }
}