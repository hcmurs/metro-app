/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.screens.metro.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.com.hcmurs.R
import org.com.hcmurs.repositories.apis.chat.ChatMessage
import org.com.hcmurs.ui.theme.PrimaryGreen
import org.com.hcmurs.ui.theme.TextPrimaryColor
import org.com.hcmurs.ui.theme.TextSecondaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotTopBar(
    onBackClick: () -> Unit,
    onClearChat: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "Chatbot",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.chatbot),
                    color = PrimaryGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Trở về",
                    tint = PrimaryGreen,
                )
            }
        },
        actions = {
            IconButton(onClick = onClearChat) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Xóa cuộc trò chuyện",
                    tint = PrimaryGreen,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
        ),
    )
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start,
    ) {
        if (message.isFromUser) {
            Spacer(modifier = Modifier.weight(0.2f))
        }

        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromUser) 16.dp else 4.dp,
                bottomEnd = if (message.isFromUser) 4.dp else 16.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) PrimaryGreen else Color(0xFFF5F5F5),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = message.message,
                    color = if (message.isFromUser) Color.White else TextPrimaryColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                )

                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp)),
                    color = if (message.isFromUser) Color.White.copy(alpha = 0.7f) else TextSecondaryColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    textAlign = if (message.isFromUser) TextAlign.End else TextAlign.Start,
                )
            }
        }

        if (!message.isFromUser) {
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

@Composable
fun ChatInputField(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(bottom = 15.dp)
        ,
        verticalAlignment = Alignment.Bottom,
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Nhập câu hỏi của bạn...",
                    color = TextSecondaryColor,
                )
            },
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.trim().isNotEmpty() && !isLoading) {
                        onSendClick()
                        keyboardController?.hide()
                    }
                },
            ),
            maxLines = 4,
            shape = RoundedCornerShape(24.dp),
        )

        Spacer(modifier = Modifier.size(8.dp))

        IconButton(
            onClick = {
                if (message.trim().isNotEmpty() && !isLoading) {
                    onSendClick()
                    keyboardController?.hide()
                }
            },
            enabled = message.trim().isNotEmpty() && !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = PrimaryGreen,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Gửi",
                    tint = if (message.trim().isNotEmpty()) PrimaryGreen else TextSecondaryColor,
                )
            }
        }
    }
}

// Preview Functions
@Preview(name = "Empty Chat Screen", showBackground = true)
@Composable
private fun PreviewChatbotScreenEmpty() {
    ChatbotScreenContent(
        messages = emptyList(),
        isLoading = false,
        currentMessage = "",
        onMessageChange = {},
        onSendClick = {},
        onBackClick = {},
        onClearChat = {},
        listState = rememberLazyListState(),
    )
}

@Preview(name = "Chat with Messages", showBackground = true)
@Composable
private fun PreviewChatbotScreenWithMessages() {
    val sampleMessages = listOf(
        ChatMessage(
            message = "Xin chào! Tôi cần hỏi về tuyến metro số 1.",
            isFromUser = true,
            timestamp = System.currentTimeMillis() - 180000,
        ),
        ChatMessage(
            message = "Xin chào! Tôi có thể giúp gì cho bạn về tuyến metro số 1?",
            isFromUser = false,
            timestamp = System.currentTimeMillis() - 120000,
        ),
        ChatMessage(
            message = "Giá vé bao nhiêu và giờ hoạt động thế nào?",
            isFromUser = true,
            timestamp = System.currentTimeMillis() - 60000,
        ),
        ChatMessage(
            message = "Giá vé tuyến metro số 1 từ 7.000đ - 20.000đ tùy theo khoảng cách. Giờ hoạt động từ 5:00 sáng đến 22:00 tối hàng ngày.",
            isFromUser = false,
            timestamp = System.currentTimeMillis(),
        ),
    )

    ChatbotScreenContent(
        messages = sampleMessages,
        isLoading = false,
        currentMessage = "",
        onMessageChange = {},
        onSendClick = {},
        onBackClick = {},
        onClearChat = {},
        listState = rememberLazyListState(),
    )
}

@Preview(name = "Loading State", showBackground = true)
@Composable
private fun PreviewChatbotScreenLoading() {
    val sampleMessages = listOf(
        ChatMessage(
            message = "Cho tôi biết về các ga dừng?",
            isFromUser = true,
            timestamp = System.currentTimeMillis(),
        ),
    )

    ChatbotScreenContent(
        messages = sampleMessages,
        isLoading = true,
        currentMessage = "",
        onMessageChange = {},
        onSendClick = {},
        onBackClick = {},
        onClearChat = {},
        listState = rememberLazyListState(),
    )
}

@Preview(name = "Typing Message", showBackground = true)
@Composable
private fun PreviewChatbotScreenTyping() {
    ChatbotScreenContent(
        messages = emptyList(),
        isLoading = false,
        currentMessage = "Tôi muốn biết thông tin về...",
        onMessageChange = {},
        onSendClick = {},
        onBackClick = {},
        onClearChat = {},
        listState = rememberLazyListState(),
    )
}

@Preview(name = "User Message Item", showBackground = true)
@Composable
private fun PreviewChatMessageItemUser() {
    Box(modifier = Modifier.padding(16.dp)) {
        ChatMessageItem(
            message = ChatMessage(
                message = "Xin chào! Tôi cần hỏi về lịch trình tàu metro.",
                isFromUser = true,
                timestamp = System.currentTimeMillis(),
            ),
        )
    }
}

@Preview(name = "Bot Message Item", showBackground = true)
@Composable
private fun PreviewChatMessageItemBot() {
    Box(modifier = Modifier.padding(16.dp)) {
        ChatMessageItem(
            message = ChatMessage(
                message = "Xin chào! Tôi sẵn sàng giúp bạn với thông tin về lịch trình tàu metro. Bạn muốn biết thông tin về tuyến nào?",
                isFromUser = false,
                timestamp = System.currentTimeMillis(),
            ),
        )
    }
}

@Preview(name = "Long Message", showBackground = true)
@Composable
private fun PreviewChatMessageItemLong() {
    Column(modifier = Modifier.padding(16.dp)) {
        ChatMessageItem(
            message = ChatMessage(
                message = "Metro số 1 Bến Thành - Suối Tiên là tuyến metro đầu tiên của Thành phố Hồ Chí Minh với tổng chiều dài 19.7km, gồm 14 ga (3 ga ngầm và 11 ga trên cao). Tuyến này kết nối trung tâm thành phố với khu vực phía Đông.",
                isFromUser = false,
                timestamp = System.currentTimeMillis(),
            ),
        )
        Spacer(modifier = Modifier.size(12.dp))
        ChatMessageItem(
            message = ChatMessage(
                message = "Cảm ơn bạn! Thông tin rất chi tiết và hữu ích. Vậy tôi có thể mua vé ở đâu và bằng cách nào?",
                isFromUser = true,
                timestamp = System.currentTimeMillis(),
            ),
        )
    }
}

@Preview(name = "Input Field", showBackground = true)
@Composable
private fun PreviewChatInputField() {
    Box(modifier = Modifier.background(Color.White)) {
        ChatInputField(
            message = "Nhập tin nhắn ở đây",
            onMessageChange = {},
            onSendClick = {},
            isLoading = false,
        )
    }
}

@Preview(name = "Input Field Loading", showBackground = true)
@Composable
private fun PreviewChatInputFieldLoading() {
    Box(modifier = Modifier.background(Color.White)) {
        ChatInputField(
            message = "Đang chờ phản hồi...",
            onMessageChange = {},
            onSendClick = {},
            isLoading = true,
        )
    }
}

@Composable
fun ChatbotScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var currentMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ChatbotScreenContent(
        messages = messages,
        isLoading = isLoading,
        currentMessage = currentMessage,
        onMessageChange = { currentMessage = it },
        onSendClick = {
            viewModel.sendMessage(currentMessage)
            currentMessage = ""
        },
        onBackClick = { navController.popBackStack() },
        onClearChat = { viewModel.clearChat() },
        listState = listState,
    )
}

@Composable
internal fun ChatbotScreenContent(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    currentMessage: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit,
    onClearChat: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
) {
    Scaffold(
        topBar = {
            ChatbotTopBar(
                onBackClick = onBackClick,
                onClearChat = onClearChat,
            )
        },
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            ChatInputField(
                message = currentMessage,
                onMessageChange = onMessageChange,
                onSendClick = onSendClick,
                isLoading = isLoading,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Chatbot",
                            tint = TextSecondaryColor,
                            modifier = Modifier.size(64.dp),
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = "Chào mừng đến với Trợ lý AI Metro!",
                            color = TextSecondaryColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Hãy đặt câu hỏi để bắt đầu cuộc trò chuyện",
                            color = TextSecondaryColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(messages) { message ->
                        ChatMessageItem(message = message)
                    }

                    // Add some bottom padding for better UX
                    item {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}