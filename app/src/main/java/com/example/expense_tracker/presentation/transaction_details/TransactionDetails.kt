

package com.example.expense_tracker.presentation.transaction_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expense_tracker.presentation.util.Screen

@Composable
fun TransactionDetails(
    navHostController: NavHostController,
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    transactionId: Int

) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp),
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { navHostController.navigateUp() },
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
            }

            Row {
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Transactions", fontSize = 20.sp)
            }
            Row {
                IconButton(onClick = {
                    viewModel.onEvent(
                        TransactionsDetailEvent.Delete(
                            navHostController,
                            id = transactionId
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                }
                IconButton(onClick = { viewModel.onEvent(TransactionsDetailEvent.Share(context)) }) {
                    Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFFF9800))
                        .padding(24.dp, 32.dp),
                    verticalArrangement = Arrangement.Center,
                ) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Column {
                        Text(
                            text = "Title",
                            color = Color.White.copy(0.7f),

                            )
                        viewModel.currTransaction.value.transaction?.let {
                            Text(
                                text = it.title,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Amount",
                            color = Color.White.copy(0.7f),

                            )
                        Text(
                            text = "$${viewModel.currTransaction.value.transaction?.amount}",
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Transaction Type",
                            color = Color.White.copy(0.7f),

                            )
                        viewModel.currTransaction.value.transaction?.let {
                            Text(
                                text = it.transactionType,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "When",
                            color = Color.White.copy(0.7f),

                            )
                        viewModel.currTransaction.value.transaction?.let {
                            Text(
                                text = it.date,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Text(
                            text = "Note",
                            color = Color.White.copy(0.7f),

                            )
                        viewModel.currTransaction.value.transaction?.let {
                            Text(
                                text = it.note,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        navHostController.navigate(Screen.AddEditTransaction.route + "/${transactionId}" + "/${Screen.TransactionDetails.route}")
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
