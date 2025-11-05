package com.example.joystickfun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val outerRecyclerView: RecyclerView = findViewById(R.id.main)
        outerRecyclerView.layoutManager = LinearLayoutManager(this)

        val outerItems = createOuterItems()
        outerRecyclerView.adapter = OuterAdapter(outerItems)
    }
}

@Composable
fun Greeting(item: InnerItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(item.color)
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier.fillMaxSize(),
            text = item.text,
            color = Color.White,
        )
    }

}

private fun createOuterItems(): List<OuterItem> {
    val colors = listOf(
        Color.Green,
        Color.Black,
        Color.LightGray,
        Color.Red,
        Color.Blue,
        Color.DarkGray,
        Color.Yellow,
        Color.Cyan,
        Color.Gray,
        Color.Magenta,
    )
    return List(colors.size) {
        val color = colors[it]
        OuterItem(List(20) { InnerItem(text = "Inner Item $it", color = color) })
    }
}

class OuterAdapter(private val outerItems: List<OuterItem>) :
    RecyclerView.Adapter<OuterAdapter.OuterViewHolder>() {

    class OuterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val innerRecyclerView: RecyclerView = itemView.findViewById(R.id.inner_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_outer, parent, false)
        return OuterViewHolder(view)
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        val outerItem = outerItems[position]

        holder.innerRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context, LinearLayoutManager.HORIZONTAL, false
        )
        holder.innerRecyclerView.adapter = InnerAdapter(outerItem.innerItems)
    }

    override fun getItemCount(): Int = outerItems.size
}

class ComposeItemViewHolder(val composeView: ComposeView, val recyclerView: RecyclerView) :
    RecyclerView.ViewHolder(composeView) {
    init {
        composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    }

    fun bind(content: InnerItem, recyclerView: RecyclerView) {
        composeView.setContent {
            val context = LocalContext.current
            Greeting(
                modifier = Modifier
                    .size(150.dp),
                item = content
            ) {
                Toast.makeText(context, content.text, Toast.LENGTH_SHORT).show()
            }
        }
        composeView.isFocusable = false
    }
}

class InnerAdapter(private val innerItems: List<InnerItem>) :
    RecyclerView.Adapter<ComposeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComposeItemViewHolder {
        return ComposeItemViewHolder(ComposeView(parent.context), parent as RecyclerView)
    }

    override fun onBindViewHolder(holder: ComposeItemViewHolder, position: Int) {
        holder.bind(innerItems[position], holder.recyclerView)
    }

    override fun getItemCount(): Int = innerItems.size
}


data class OuterItem(val innerItems: List<InnerItem>)

data class InnerItem(val text: String, val color: Color)
