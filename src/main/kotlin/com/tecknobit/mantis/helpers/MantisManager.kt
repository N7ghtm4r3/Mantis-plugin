package com.tecknobit.mantis.helpers

import com.tecknobit.mantis.Mantis
import java.util.*

class MantisManager {

    data class MantisResource(
        var resource: String = "",
        var key: String = "",
        var autoTranslate: Boolean = true
    )

    companion object {

        var mantis: Mantis = Mantis(Locale.getDefault())

        fun createNewResource(mantisResource: MantisResource) {

        }

    }

}