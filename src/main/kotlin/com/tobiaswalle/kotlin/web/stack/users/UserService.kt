package com.tobiaswalle.kotlin.web.stack.users

import com.tobiaswalle.kotlin.web.stack.users.models.User

class UserService {
    private val storage = mutableSetOf<User>()

    fun add(user: User) = storage.add(user)

    fun getAll(): Set<User> = storage

    fun getByName(name: String): User? = storage.find { it.name == name }
}
