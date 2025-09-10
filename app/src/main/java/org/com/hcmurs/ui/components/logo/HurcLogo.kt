/*
 * Copyright (c) 2025 hcmurs.
 * All rights reserved.
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
