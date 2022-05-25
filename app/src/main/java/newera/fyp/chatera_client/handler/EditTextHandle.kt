package newera.fyp.chatera_client.handler

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import newera.fyp.chatera_client.model.EditTextModel

object PasswordHandle {
    var config = EditTextModel(
        6,9
    )

    fun range(count : Int): EditTextModel {
        config.error = "Password range min ${config.min} to max ${config.max}"
        config.enable = count in config.min..config.max
        return config
    }

    fun compare(v1: String, v2: String): EditTextModel {
        if(v1.isNullOrEmpty() && v1.isNotBlank()){
            config.error = "Password cannot be empty"
            config.enable = false
            return config
        }
        else if(v2.isNullOrEmpty() && v2.isNotBlank()){
            config.error = "Require field"
            config.enable = false
            return config
        }
        else if(v1 != v2) {
            config.error = "Compare Password is not correct"
            config.enable = false
            return config
        }
        config.enable = true
        return config
    }
}