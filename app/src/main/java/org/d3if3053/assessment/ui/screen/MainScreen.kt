package org.d3if3053.assessment.ui.screen

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.d3if3053.assessment.R
import org.d3if3053.assessment.ui.theme.AssessmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var checked by rememberSaveable {
        mutableStateOf(true)
    }

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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.profil),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_karya_seni)
                )
            }
        }
    ) { padding ->
//        ScreenContent(showList, Modifier.padding(padding), navController)
    }
}

//@Composable
//fun ScreenContent(showList: Boolean, modifier: Modifier, navController: NavHostController) {
//
//    if (data.isEmpty()) {
//        Column (
//            modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.baseline_wallet_24),
//                contentDescription = stringResource(R.string.dompet_kosong),
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(80.dp)
//            )
//            Text(text = stringResource(R.string.catatan_keuangan_kosong))
//        }
//    } else {
//        if (showList) {
//            LazyColumn (
//                modifier = modifier
//                    .fillMaxSize(),
//                contentPadding = PaddingValues(bottom = 84.dp)
//            ) {
//                items(data) {
//                    ListItem(keuangan = it) {
//                        navController.navigate(Screen.FormUbah.withId(it.id))
//                    }
//                    Divider()
//                }
//            }
//        } else {
//            LazyVerticalStaggeredGrid(
//                modifier = modifier.fillMaxSize(),
//                columns = StaggeredGridCells.Fixed(2),
//                verticalItemSpacing = 8.dp,
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
//            ) {
//                items(data) {
//                    GridItem(keuangan = it) {
//                        navController.navigate(Screen.FormUbah.withId(it.id))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ListItem(keuangan: Keuangan, onClick: () -> Unit) {
//    Column (
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .padding(18.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text(
//            text = keuangan.pilihan,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            fontWeight = FontWeight.Bold
//        )
//        Text(
//            text = keuangan.deskripsi,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//        Text(
//            text = keuangan.jumlahUang,
//            maxLines = 2,
//            overflow = TextOverflow.Ellipsis
//        )
//        Text(text = keuangan.tanggal)
//    }
//}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    AssessmentTheme {
        MainScreen()
    }
}