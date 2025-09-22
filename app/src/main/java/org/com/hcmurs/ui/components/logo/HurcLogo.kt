/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.ui.components.logo

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun HurcLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = org.com.hcmurs.R.drawable.hurc),
        contentDescription = "HURC Logo",
        modifier = modifier,
    )
}
