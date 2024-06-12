package org.d3if3053.assessment.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.d3if3053.assessment.R
import org.d3if3053.assessment.ui.theme.AssessmentTheme

@Composable
fun ConfirmDialog(
    openDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            text = { Text(text = stringResource(R.string.pesan_hapus_karya)) },
            confirmButton = {
                Button(onClick = { onConfirmation() }) {
                    Text(text = stringResource(R.string.hapus))
                }
            },
            dismissButton = {
                Button(onClick = { onDismissRequest() }) {
                    Text(text = stringResource(R.string.tombol_batal))
                }
            },
            onDismissRequest = { onDismissRequest() },
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DeleteDialogPreview() {
    AssessmentTheme {
        ConfirmDialog(
            openDialog = true,
            onDismissRequest = { },
            onConfirmation = { },
        )
    }
}
