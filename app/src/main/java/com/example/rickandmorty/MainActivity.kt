
package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val Green = Color(0xFF35856A)
private val DarkBackground = Color(0x33333333)
private val CardBackground = Color(0xFFFFFFFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Green,
                    background = DarkBackground
                )
            ) {
                RickAndMortyScreen()
            }
        }
    }
}

@Composable
fun RickAndMortyScreen() {
    var characterId by remember { mutableStateOf("1") }
    var characters by remember {
        mutableStateOf<List<Character>>(emptyList())
    }
    var selectedCharacter by remember {
        mutableStateOf<Character?>(null)
    }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .safeDrawingPadding()
            .padding(20.dp)
    ) {
        Text(
            text = "Rick & Morty",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )

        Text(
            text = "Przeglądarka postaci",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    loading = true
                    message = ""
                    selectedCharacter = null

                    try {
                        characters = RetrofitClient.api
                            .getCharacters().results
                    } catch (e: Exception) {
                        message = "Błąd: ${e.message}"
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Pobierz listę postaci")
        }

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = characterId,
                onValueChange = { characterId = it },
                label = { Text("ID postaci") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    val id = characterId.toIntOrNull()

                    if (id == null || id <= 0) {
                        message = "Podaj poprawne ID"
                    } else {
                        scope.launch {
                            loading = true
                            message = ""
                            characters = emptyList()
                            selectedCharacter = null

                            try {
                                selectedCharacter = RetrofitClient.api
                                    .getCharacterById(id)
                            } catch (e: Exception) {
                                message = "Błąd: ${e.message}"
                            } finally {
                                loading = false
                            }
                        }
                    }
                },
                enabled = !loading,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text("Szukaj")
            }
        }

        Spacer(Modifier.height(20.dp))

        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = Color.Red
            )
        }

        selectedCharacter?.let {
            CharacterCard(it)
        }

        if (characters.isNotEmpty()) {
            Text(
                text = "Postacie",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(10.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(characters) { character ->
                CharacterCard(character)
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${character.id}. ${character.name}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Status: ${character.status}",
                fontSize = 14.sp,
                color = Green
            )

            Text(
                text = "Gatunek: ${character.species}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}
