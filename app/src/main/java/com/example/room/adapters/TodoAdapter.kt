import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.room.R
import com.example.room.database.entities.Todo
import com.example.room.database.repositories.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(items:List<Todo>, repository: TodoRepository,
                  viewModel:MainActivityData): RecyclerView.Adapter<TodoAdapter.ToDoViewHolder>() {
    var context: Context? = null
    val items = items
    val repository = repository
    val viewModel = viewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item,parent,false)
        context = parent.context
        return ToDoViewHolder(view)
    }
    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.cbTodo.text = items.get(position).item
        holder.ivDelete.setOnClickListener {
            val isChecked = holder.cbTodo.isChecked
            if(isChecked){
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(items.get(position))
                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main){
                        viewModel.setData(data)
                    }
                }
                Toast.makeText(context,"Item Deleted",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context,"Select the item to delete",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ToDoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val cbTodo: CheckBox = view.findViewById(R.id.cbTodo)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    }
    class MainActivityData: ViewModel() {
        private val _data = MutableLiveData<List<Todo>>()
        val data: LiveData<List<Todo>> = _data
        fun setData(data:List<Todo>){
            _data.value = data
        }
    }
    fun displayDialog(repository: TodoRepository){
        val builder = AlertDialog.Builder(this)
// Set the alert dialog title and message
        builder.setTitle("Enter New Todo item:")
        builder.setMessage("Enter the todo item below:")
// Create an EditText input field
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
// Set the positive button action
        builder.setPositiveButton("OK") { dialog, which ->
// Get the input text and display a Toast message
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Todo(item))
                val data = repository.getAllTodoItems()
                runOnUiThread {
                    viewModel.setData(data)
                }
            }
        }
// Set the negative button action
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
// Create and show the alert dialog
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
