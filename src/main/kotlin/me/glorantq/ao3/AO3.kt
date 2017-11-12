package me.glorantq.ao3

import me.glorantq.ao3.utils.AO3Exception

object AO3 {
    fun getWork(workID: Int) = try {
        AO3Work(workID)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain work. Most likely a parsing error!", e)
    }

    fun getUser(username: String) = try {
        AO3User(username, username)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain user. Most likely a parsing error!", e)
    }

    fun getPseud(username: String, pseud: String) = try {
        AO3User(username, pseud)
    } catch (e: Exception) {
        throw AO3Exception("Failed to obtain user. Most likely a parsing error!", e)
    }
}