object UserSession {
    var name: String? = null
    var email: String? = null
    var id: String? = null

    fun initializeSession(userName: String, userEmail: String, userId: String) {
        name = userName
        email = userEmail
        id = userId
    }
}
