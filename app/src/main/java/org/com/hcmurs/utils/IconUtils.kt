/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.utils

import org.com.hcmurs.R
import org.com.hcmurs.core.constant.ScreenTitle

val screenTitleIconMap =
    mapOf<ScreenTitle, Int>(
        ScreenTitle.BUY_TICKET to R.drawable.ic_buy_ticket,
        ScreenTitle.ROUTE to R.drawable.ic_route,
        ScreenTitle.MY_TICKET to R.drawable.ic_my_ticket,
        ScreenTitle.MAPS to R.drawable.ic_map,
        ScreenTitle.REDEEM_CODE_FOR_TICKET to R.drawable.ic_redeem_code_for_ticket,
        ScreenTitle.VIRTUAL_TOUR to R.drawable.ic_virtual,
        ScreenTitle.CHATBOT to R.drawable.ic_chatbot,
        ScreenTitle.TICKET_INFORMATION to R.drawable.ic_ticket_info,
        ScreenTitle.ACCOUNT to R.drawable.ic_account,
        ScreenTitle.FEEDBACK to R.drawable.ic_feedback,
        ScreenTitle.EVENT to R.drawable.ic_event,
        ScreenTitle.CONSTRUCTION_IMAGE to R.drawable.ic_construction,
        ScreenTitle.SETTING to R.drawable.ic_setting,
        ScreenTitle.COOPERATION_LINK to R.drawable.ic_connect,
        ScreenTitle.INTRODUCTION to R.drawable.ic_info,
        ScreenTitle.SCAN_QR_CODE to R.drawable.ic_qr,
    )
