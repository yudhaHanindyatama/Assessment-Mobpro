package org.d3if3053.assessment.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3053.assessment.R
import org.d3if3053.assessment.database.KeuanganDb
import org.d3if3053.assessment.model.Keuangan
import org.d3if3053.assessment.navigation.Screen
import org.d3if3053.assessment.ui.theme.AssessmentTheme
import org.d3if3053.assessment.util.SettingsDataStore
import org.d3if3053.assessment.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_catatan_keuangan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        ScreenContent(showList, Modifier.padding(padding), navController)
    }
}

@Composable
fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController) {
    val context = LocalContext.current
    val db = KeuanganDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.catatan_keuangan_kosong))
        }
    } else {
        if (showList) {
            LazyColumn (
                modifier = modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(keuangan = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                    Divider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(data) {
                    GridItem(keuangan = it) {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(keuangan: Keuangan, onClick: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = keuangan.pilihan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = keuangan.deskripsi,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = keuangan.jumlahUang,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = keuangan.tanggal)
    }
}

@Composable
fun GridItem(keuangan: Keuangan, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Text(
            text = keuangan.pilihan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = keuangan.deskripsi,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = keuangan.jumlahUang,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = keuangan.tanggal)
    }
}

@Composable
fun IconPicker(isError: Boolean) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(id = R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    AssessmentTheme {
        MainScreen(rememberNavController())
    }
}