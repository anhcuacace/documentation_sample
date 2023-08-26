package tunanh.documentation.util.storage.exception

data class PermissionNotAllowedException(val currentFilePath: String)  : Exception()
