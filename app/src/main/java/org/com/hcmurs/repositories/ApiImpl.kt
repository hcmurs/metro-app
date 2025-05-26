package org.com.hcmurs.repositories

import org.com.hcmurs.model.NoteItem
import kotlinx.coroutines.delay
import org.com.hcmurs.common.exception.AppException
import javax.inject.Inject

class ApiImpl @Inject constructor() : Api {
    var notes = ArrayList<NoteItem>()

    init {
        notes.add(NoteItem(1, "1", "1"))
        notes.add(NoteItem(2, "2", "2"))
        notes.add(NoteItem(3, "3", "3"))
    }

    override suspend fun login(username: String, password: String): Boolean {
        delay(1000)
        if (username != "1" || password != "1") {
            throw AppException.InvalidCredentialsException("Wrong credentials")
        }
        return true
    }

    override suspend fun loadNotes(): List<NoteItem> {
        delay(1000)
        return notes
    }

    override suspend fun addNote(title: String, content: String) {
        delay(1000)
        notes.add(NoteItem(System.currentTimeMillis(), title, content))
    }

    override suspend fun editNote(dt: Long, title: String, content: String) {
        delay(1000)
        for (i in notes.indices) {
            if (notes[i].dateTime == dt) {
                notes[i] = NoteItem(dt, title, content)
                break
            }
        }
    }

    override suspend fun deleteNote(dt: Long) {
        delay(1000)
        for (i in notes.indices) {
            if (notes[i].dateTime == dt) {
                notes.removeAt(i)
                break
            }
        }
    }
}