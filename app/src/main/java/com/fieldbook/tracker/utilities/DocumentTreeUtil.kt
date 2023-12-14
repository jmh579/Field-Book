package com.fieldbook.tracker.utilities

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.fieldbook.tracker.R
import com.fieldbook.tracker.preferences.GeneralKeys
import org.phenoapps.utils.BaseDocumentTreeUtil

@RequiresApi(Build.VERSION_CODES.KITKAT)
class DocumentTreeUtil: BaseDocumentTreeUtil() {

    /**
     * Static functions to be used to handle exports.
     * These functions will attempt to create these directories if they do not exist.
     */
    companion object {

        const val TAG = "DocumentTreeUtil"

        const val FIELD_AUDIO_MEDIA = "field_audio"
        const val FIELD_GNSS_LOG = "field_gnss_log"
        const val FIELD_GNSS_LOG_FILE_NAME = "field_gnss.csv"

        /**
         * Gets a specific directory for the currently chosen plot.
         * @param traitName: trait name of the folder, also photos and audio
         */
        fun getFieldMediaDirectory(context: Context?, traitName: String): DocumentFile? {

            if (context != null) {

                val prefs = context.getSharedPreferences(GeneralKeys.SHARED_PREF_FILE_NAME, 0)
                val field = prefs.getString(GeneralKeys.FIELD_FILE, "") ?: ""

                if (field.isNotBlank()) {
                    val plotDataDirName = context.getString(R.string.dir_plot_data)
                    val fieldDir = createDir(context, plotDataDirName, field)
                    if (fieldDir != null) {
                        val traitDir = fieldDir.findFile(traitName)
                        if (traitDir == null || !traitDir.exists()) {
                            fieldDir.createDirectory(traitName)
                        }
                        return fieldDir.findFile(traitName)
                    }
                } else return null
            }

            return null
        }

        /**
         * Gets a specific directory for the currently chosen field.
         * @param attributeName: attribute name of the folder
         * currently used for field_audio, can be used in future for other types od field data
         */
        fun getFieldDataDirectory(context: Context?, attributeName: String): DocumentFile? {

            if (context != null) {

                val prefs = context.getSharedPreferences(GeneralKeys.SHARED_PREF_FILE_NAME, 0)
                val field = prefs.getString(GeneralKeys.FIELD_FILE, "") ?: ""

                if (field.isNotBlank()) {
                    val fieldDataDirName = context.getString(R.string.dir_field_data)
                    val fieldDir = createDir(context, fieldDataDirName, field)
                    if (fieldDir != null) {
                        val attributeDir = fieldDir.findFile(attributeName)
                        if (attributeDir == null || !attributeDir.exists()) {
                            fieldDir.createDirectory(attributeName)
                        }
                        return fieldDir.findFile(attributeName)
                    }
                } else return null
            }

            return null
        }

        /**
         * Gets plot media for a given plot based on its extension
         */
        fun getPlotMedia(mediaDir: DocumentFile?, plot: String, ext: String): List<DocumentFile> {

            return getPlotMedia(mediaDir, plot).filter { it.name?.endsWith(ext) == true }
        }

        /**
         * Returns an array of files that have been collected for a plot.
         * Field Book media file names always contain the plot name, which are unique to the study.
         */
        private fun getPlotMedia(mediaDir: DocumentFile?, plot: String): ArrayList<DocumentFile> {

            val mediaList = arrayListOf<DocumentFile>()

            try {

                mediaDir?.listFiles()?.forEach { file ->
                    if (file.exists()) {
                        file?.name?.let { fileName ->
                            if (plot in fileName) {
                                mediaList.add(file)
                            }
                        }
                    }
                }

            } catch (e: UnsupportedOperationException) {

                e.printStackTrace()

            }


            return mediaList
        }
    }
}