import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Invokes that callback after text change in EditText.
 */
inline fun EditText.onTextChanged(crossinline onTextChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
        }

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            onTextChange(charSequence?.toString() ?: "")
        }
    })
}