package com.tecknobit.mantis.helpers

import com.tecknobit.mantis.Mantis
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.util.*

open class MantisManager {

    companion object {

        val mantisManager = MantisManager()

    }

    private var currentResources = JSONObject()

    fun createNewResource(mantisResource: MantisResource) {
        currentResources = JSONObject(Scanner(mantisResource.resourcesFile!!).useDelimiter("\\Z").next())
        val autoTranslate = mantisResource.autoTranslate
        var resource = mantisResource.resource
        currentResources.keys().forEach { language ->
            val languageSet = currentResources.getJSONObject(language)
            if(autoTranslate && language != Locale.getDefault().toLanguageTag())
                resource = Mantis(Locale.getDefault()).translate(resource)
            languageSet.put(mantisResource.key, resource)
            currentResources.put(language, languageSet)
        }
        val fileWriter = FileWriter(mantisResource.resourcesFile!!, false)
        fileWriter.write(currentResources.toString(4))
        fileWriter.flush()
    }

    fun keyExists(resourceKey: String): Boolean {
        return currentResources.toString().contains("\"$resourceKey\":")
    }

    data class MantisResource(
        var resourcesFile: File? = null,
        var resource: String = "",
        var key: String = "",
        var autoTranslate: Boolean = true
    )

}