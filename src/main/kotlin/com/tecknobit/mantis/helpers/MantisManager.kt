package com.tecknobit.mantis.helpers

import com.tecknobit.mantis.Mantis
import java.io.File
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
            println(Scanner(File(javaClass.classLoader.getResources("resources.mantis").nextElement().path)).next("\\Z"))
        }

    }

}