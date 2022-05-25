package newera.fyp.chatera_client.model

class EditTextModel(
    var min: Short = 6,
    var max: Short = 200,
    var enable: Boolean = false
) {
    var error: String = "[error message]"

}