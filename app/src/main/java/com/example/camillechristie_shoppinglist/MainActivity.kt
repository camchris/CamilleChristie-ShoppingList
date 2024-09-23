package com.example.camillechristie_shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.camillechristie_shoppinglist.ui.theme.CamilleChristieShoppingListTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CamilleChristieShoppingListTheme {
                shoppingList()
            }
        }
    }
}

@Composable
fun shoppingList(){
    var currentText by remember{ mutableStateOf("") }
    var currentQuantity by remember{ mutableStateOf("") }
    val items = remember { mutableStateListOf<String>() }
    val itemsChecked = remember{mutableStateListOf<Boolean>()}
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Column() {
        Spacer(modifier = Modifier.padding(15.dp))

        TextField(
            value = currentText,
            onValueChange = { currentText = it },
            label = { Text("Enter item") })

        TextField(
            value = currentQuantity,
            onValueChange = { currentQuantity = it },
            label = { Text("Enter quantity/size") })

        Spacer(modifier = Modifier.padding(10.dp))

        Button(onClick = { items.add(currentQuantity + " " + currentText)
            currentText = ""
            currentQuantity = ""
            itemsChecked.add(false)}){
            Text("Add Item to List")
        }
        Button(onClick = {
            var i = 0
            while(i < items.size){
                if(itemsChecked[i]){
                    items.removeAt(i)
                    itemsChecked.removeAt(i)
                }
                else{
                    i++
                }
            }
            scope.launch {
                val result = snackBarHostState
                    .showSnackbar(
                        message = "Cleared purchased items",
                        actionLabel = "Continue shopping",
                        duration = SnackbarDuration.Indefinite
                    )

            }
        }){
            Text("Clear Checked")
        }
        LazyColumn() {
            items(items.size) { index ->
                Row(verticalAlignment = Alignment.CenterVertically,) {
                    Checkbox(checked = itemsChecked[index], onCheckedChange = {itemsChecked[index] = it})
                    if(itemsChecked[index]){
                        Text(text = items[index], style = TextStyle(textDecoration = TextDecoration.LineThrough))
                    }
                    else{
                        Text(text = items[index])
                    }
                }
            }
        }
        SnackbarHost(hostState = snackBarHostState)
    }
}