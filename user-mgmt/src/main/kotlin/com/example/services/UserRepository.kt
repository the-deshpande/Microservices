package com.example.services

import com.example.models.UserRequest
import com.example.models.Users
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*

class UserRepository(
    private val client: HttpClient,
    private val users: MutableList<Users> = mutableListOf()
){
    init {
        loadUsers()
    }

    private fun loadUsers() = runBlocking {
        val allDocs = "http://127.0.0.1:5984/users/_all_docs/"

        var request = async { client.get(allDocs) }.await().bodyAsText()
        val revArray = Json.parseToJsonElement(request).jsonObject["rows"]?.jsonArray ?: return@runBlocking

        for (i in revArray) addUser(i.jsonObject)
    }

    fun allUsers(): MutableList<Users> {
        users.clear()
        loadUsers()
        return users
    }

    fun getByID(id: String) = users.firstOrNull { it.id == id }
    fun getByUsername(username: String) = users.firstOrNull { it.username == username }

    private fun addUser(obj: JsonObject) = runBlocking {
        val path = "http://127.0.0.1:5984/users/"

        val id = obj["id"]?.jsonPrimitive?.content ?: return@runBlocking false
        val request = async { client.get(path + id) }.await().bodyAsText()

        val response = Json.parseToJsonElement(request).jsonObject

        val user = Users(
            id = response["_id"]?.jsonPrimitive?.content ?: "NOT_FOUND",
            rev = response["_rev"]?.jsonPrimitive?.content ?: "NOT_FOUND",
            username = response["username"]?.jsonPrimitive?.content ?: "NOT_FOUND",
            password = response["password"]?.jsonPrimitive?.content ?: "NOT_FOUND"
        )
        if (user.id == "NOT_FOUND" || user.rev == "NOT_FOUND" ||
            user.username == "NOT_FOUND" || user.password == "NOT_FOUND") return@runBlocking false

        return@runBlocking users.add(user)
    }

    fun createUser(obj: UserRequest) = runBlocking {
        val path = "http://127.0.0.1:5984/users"

        val request = async { client.post(path) {
            contentType(ContentType.Application.Json)
            setBody(obj)
        } }.await().bodyAsText()
        val response = Json.parseToJsonElement(request).jsonObject

        return@runBlocking addUser(response)
    }

    fun deleteUser(user: Users) : Boolean = runBlocking {
        val path = "http://127.0.0.1:5984/users/"

        val request = async { client.delete(path+user.id){
            parameter("rev",user.rev)
        } }.await()

        return@runBlocking request.status == HttpStatusCode.OK
    }
}