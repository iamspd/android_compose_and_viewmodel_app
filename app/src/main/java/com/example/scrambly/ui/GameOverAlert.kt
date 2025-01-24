package com.example.scrambly.ui


import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrambly.R

@Composable
fun GameOverAlert(
    modifier: Modifier = Modifier,
    score: Int,
    newGame: () -> Unit,
) {

    val activity = LocalActivity.current as Activity

    AlertDialog(modifier = modifier, onDismissRequest = {}, icon = {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = stringResource(R.string.game_over)
        )
    }, title = {
        Text(
            text = stringResource(R.string.game_over),
            style = MaterialTheme.typography.bodyLarge
        )
    }, text = {
        Text(
            text = stringResource(R.string.your_score, score),
            style = MaterialTheme.typography.bodyMedium
        )
    }, confirmButton = {
        TextButton(onClick = { newGame() }) {
            Text(
                text = stringResource(R.string.new_game)
            )
        }
    }, dismissButton = {
        TextButton(onClick = { activity.finish() }) {
            Text(
                text = stringResource(R.string.exit)
            )
        }
    }

    )
}