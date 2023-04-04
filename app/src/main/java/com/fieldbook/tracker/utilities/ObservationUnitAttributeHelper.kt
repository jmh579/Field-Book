package com.fieldbook.tracker.utilities

import android.util.Log
import com.google.gson.JsonObject
import org.brapi.v2.model.pheno.BrAPIObservationUnit

/**
 *
 *
 * additionalInfo: {
 *
 *   "observationUnitAttributes": {
 *
 *      "attributeA": "A",
 *      "attributeB": "B",
 *      ...
 *      "attributeN": "N"
 *    },
 * }
 */
class ObservationUnitAttributeHelper {

    companion object {

        const val UNIT_ATTRIBUTE_KEY = "observationUnitAttributes"

        /**
         *
         *
         * @param unit - BrAPIObservationUnit object to parse
         * @param attributes - MutableMap<String, String> to store the parsed attributes
         */
        @JvmStatic
        fun parseAdditionalInfo(unit: BrAPIObservationUnit, attributes: MutableMap<String, String>) {

            unit.additionalInfo.entrySet().forEach { entry ->

                if (entry.key == UNIT_ATTRIBUTE_KEY) {

                    val attributeMap = entry.value.asJsonObject

                    attributeMap.entrySet().forEach { attribute ->

                        attributes[attribute.key] = attribute.value.asString
                    }

                    return@forEach //break out of the loop once we find UNIT_ATTRIBUTE_KEY
                }
            }
        }

        /**
         * Test function
         */
        fun test() {

            val unit = BrAPIObservationUnit()

            unit.additionalInfo = JsonObject().apply {

                    add(UNIT_ATTRIBUTE_KEY, JsonObject().apply {

                        addProperty("attributeA", "A")
                        addProperty("attributeB", "B")
                        addProperty("attributeC", "C")
                    })
            }

            val attributes = mutableMapOf<String, String>()

            parseAdditionalInfo(unit, attributes)

            Log.d("TEST", attributes.toString())
        }
    }
}