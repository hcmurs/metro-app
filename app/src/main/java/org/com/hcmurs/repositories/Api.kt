/**
 * Copyright (c) 2025 hcmurs. All rights reserved.
 * This software is the confidential and proprietary information of hcmurs.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with hcmurs.
 */
package org.com.hcmurs.repositories

import org.com.hcmurs.model.NoteItem

interface Api {
    suspend fun login(
        username: String,
        password: String,
    ): Boolean

    suspend fun loadNotes(): List<NoteItem>

    suspend fun addNote(
        title: String,
        content: String,
    )

    suspend fun editNote(
        id: Long,
        title: String,
        content: String,
    )

    suspend fun deleteNote(dt: Long)
}
