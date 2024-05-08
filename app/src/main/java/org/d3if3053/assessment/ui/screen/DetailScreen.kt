package org.d3if3053.assessment.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3053.assessment.R
import org.d3if3053.assessment.database.KeuanganDb
import org.d3if3053.assessment.ui.theme.AssessmentTheme
import org.d3if3053.assessment.util.ViewModelFactory

const val KEY_ID_CATATAN = "idCatatan"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = KeuanganDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var pilihan by rememberSaveable {
        mutableStateOf("")
    }
    var keterangan by rememberSaveable {
        mutableStateOf("")
    }
    var jumlahUang by rememberSaveable {
        mutableStateOf("")
    }
    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getKeuangan(id) ?: return@LaunchedEffect
        pilihan = data.pilihan
        keterangan = data.deskripsi
        jumlahUang = data.jumlahUang
    }

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { if (id == null) {
                    Text(text = stringResource(id = R.string.tambah_catatan_keuangan))
                    } else {
                        Text(text = stringResource(R.string.edit_catatan_keuangan))
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        val regex = Regex("[0-9]*")
                        if (pilihan == "" || keterangan == "" || !regex.matches(jumlahUang)) {
                            Toast.makeText(context,
                                context.getString(R.string.invalid), Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(pilihan, keterangan, jumlahUang)
                        } else {
                            viewModel.update(id, pilihan, keterangan, jumlahUang)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }
                        ) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) {padding ->
        FormCatatanKeuangan(
            selectedText = pilihan,
            onChooseChange = { pilihan = it },
            description = keterangan,
            ondescriptionChange = { keterangan = it },
            amountOfMoney = jumlahUang,
            onamountOfMoneyChange = { jumlahUang = it },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FormCatatanKeuangan(
    selectedText: String, onChooseChange: (String) -> Unit,
    description: String, ondescriptionChange: (String) -> Unit,
    amountOfMoney: String, onamountOfMoneyChange: (String) -> Unit,
    modifier: Modifier
) {
    val pilihan = listOf(
        stringResource(R.string.pemasukan),
        stringResource(R.string.pengeluaran)
    )

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(text = stringResource(id = R.string.pilihan_uang))
            pilihan.forEach {
                    text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable { onChooseChange(text) }
                ) {
                    ClassOption(
                        label = text,
                        isSelected = selectedText == text,
                        onSelect = { onChooseChange(text) }
                    )
                }
            }
        }
        OutlinedTextField(
            value = description,
            onValueChange = { ondescriptionChange(it) },
            label = { Text(text = stringResource(R.string.keterangan)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amountOfMoney,
            onValueChange = { onamountOfMoneyChange(it) },
            label = { Text(text = stringResource(R.string.jumlah_uang)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                capitalization = KeyboardCapitalization.Words
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ClassOption(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    RadioButton(selected = isSelected, onClick = onSelect)
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
    )
}


@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.hapus_catatan_keuangan))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    AssessmentTheme {
        DetailScreen(rememberNavController())
    }
}