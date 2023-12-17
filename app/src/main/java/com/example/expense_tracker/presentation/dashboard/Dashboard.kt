package com.example.expense_tracker.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expense_tracker.R
import com.example.expense_tracker.presentation.common.components.TransactionCard
import com.example.expense_tracker.presentation.util.Screen

@Composable
fun Dashboard(
    navHostController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val overviewCardState by viewModel.overviewCardState
    val recentList by viewModel.recentTransactions

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
                navHostController.navigate(Screen.About.route)
            }) {

            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(16.dp, 0.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0xFF673AB7)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            text = stringResource(R.string.total_balance),
                            color = Color.White.copy(0.7f),
                            fontSize = 16.sp,

                            )
                        Text(
                            text = if (overviewCardState.totalBalance!! >= 0) {
                                "$${overviewCardState.totalBalance}"
                            } else {
                                "-$${-overviewCardState.totalBalance!!}"
                            },
                            fontSize = 40.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            SummaryMiniCard(
                                color = Color(0xFF00CB51),
                                imageVector = Icons.Outlined.ArrowDownward,
                                heading = "Income",
                                content = "+$${overviewCardState.income}"
                            )
                            SummaryMiniCard(
                                color = Color(0xFFCB0000),
                                imageVector = Icons.Outlined.ArrowUpward,
                                heading = "Expense",
                                content = "-$${overviewCardState.expense}"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))


                }

            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 16.dp),
        ) {
            Text(
                text = "Recent Transactions..",
                color = Color(0xFFFFFFFF),)
        }

        if (recentList.list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent transactions..",
                    color = Color(0xFFFFFFFF),
                )
            }

        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    8.dp, 0.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentList.list) {
                    TransactionCard(transaction = it) {

                        navHostController.navigate(Screen.TransactionDetails.withArgs(it.id.toString()))

                    }
                }

            }
        }

    }
}

@Composable
fun SummaryMiniCard(
    color: Color,
    imageVector: ImageVector,
    heading: String,
    content: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFFAD9E6))
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = heading,
                modifier = Modifier.size(40.dp),
                tint = color
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column() {
            Text(
                text = heading,
                color = Color.White.copy(0.7f),
                fontSize = 16.sp,
            )
            Text(
                text = content,
                color = Color.White
            )

        }
    }
}