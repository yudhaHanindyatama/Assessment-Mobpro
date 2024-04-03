package org.d3if3053.assessment.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3053.assessment.R
import org.d3if3053.assessment.model.Bendera
import org.d3if3053.assessment.ui.theme.AssessmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    val data = getData()


    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.tentang_aplikasi))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.deskripsi),
                modifier = Modifier.padding(16.dp)
            )
            data.forEach { bendera ->
                BenderaItem(bendera = bendera)
            }
            Text(
                text = stringResource(id = R.string.copyright),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun BenderaItem(bendera: Bendera) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = bendera.mataUang,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = bendera.imageResId),
            contentDescription = bendera.mataUang,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
fun getData(): List<Bendera>{
    return listOf(
        Bendera("Dollar", R.drawable.amerika),
        Bendera("Euro", R.drawable.jerman),
        Bendera("Pound sterling", R.drawable.inggris),
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AboutScreenPreview() {
    AssessmentTheme {
        AboutScreen(rememberNavController())
    }
}