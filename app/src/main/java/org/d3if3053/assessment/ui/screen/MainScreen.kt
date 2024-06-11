package org.d3if3053.assessment.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3053.assessment.BuildConfig
import org.d3if3053.assessment.R
import org.d3if3053.assessment.model.User
import org.d3if3053.assessment.network.UserDataStore
import org.d3if3053.assessment.ui.theme.AssessmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    var showDialog by remember {
        mutableStateOf(false)
    }
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
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        } else {
                            showDialog = true
                        }
                    }) {
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
        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }
            ) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }
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

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(result: GetCredentialResponse, dataStore: UserDataStore) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    } else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    AssessmentTheme {
        MainScreen()
    }
}