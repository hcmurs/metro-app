package org.com.hcmurs.ui.screens.metro.buyticket
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import org.com.hcmurs.Screen
import org.com.hcmurs.repositories.apis.payment.OrderStatus

@Composable
fun PaymentRedirectScreen(
    navController: NavHostController,
    status: String?,
    orderCode: Int,
    viewModel: OrderInfoViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(status) {
        Log.d("PaymentRedirect", "status = '$status'")

        if (status?.trim()?.equals("PAID", ignoreCase = true) == true)
        { // ✅ Gọi API update trạng thái đơn hàng
            viewModel.updateOrderStatus(orderCode, OrderStatus.SUCCESSFUL)
            delay(3000)
            Toast.makeText(context, "Thanh toán thành công!", Toast.LENGTH_LONG).show()
            navController.navigate(Screen.MyTicket.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        } else { // cancel
            // ✅ Gọi API update trạng thái đơn hàng
            viewModel.updateOrderStatus(orderCode, OrderStatus.FAILED)
            delay(3000)
            Toast.makeText(context, "Giao dịch đã bị hủy.", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}