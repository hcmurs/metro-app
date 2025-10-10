/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.core.constant

import androidx.annotation.StringRes
import org.com.hcmurs.R

enum class ScreenTitle(
    @StringRes val titleRes: Int,
) {
    BUY_TICKET(R.string.buy_ticket),
    ROUTE(R.string.route),
    MY_TICKET(R.string.my_ticket),
    MAPS(R.string.maps),
    REDEEM_CODE_FOR_TICKET(R.string.redeem_code_for_ticket),
    VIRTUAL_TOUR(R.string.virtual_tour),
    CHATBOT(R.string.chatbot),
    TICKET_INFORMATION(R.string.ticket_information),
    ACCOUNT(R.string.account),
    FEEDBACK(R.string.feedback),
    EVENT(R.string.event),
    CONSTRUCTION_IMAGE(R.string.construction_image),
    SETTING(R.string.setting),
    COOPERATION_LINK(R.string.cooperation_link),
    INTRODUCTION(R.string.introduction),
    SCAN_QR_CODE(R.string.scan_qr_code),
}
