package Model

sealed class Routes(val routes: String) {
    object LoginScreen : Routes("Login")
    object CreateScreen : Routes("Create")
}