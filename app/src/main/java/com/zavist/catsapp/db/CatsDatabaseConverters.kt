package com.zavist.catsapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.zavist.catsapp.db.entity.Image
import com.zavist.catsapp.db.entity.Weight
import com.zavist.catsapp.util.CLog


object CatsDatabaseConverters {

    private const val TAG: String = "[CATS][DB]CatsDatabaseConverters"
    private val gson: Gson = GsonBuilder()
        .create()

    @TypeConverter
    fun weightToString(weight: Weight?): String? {
        return weight?.let {
            gson.runCatching {
                toJson(weight, object : TypeToken<Weight>() {}.type)
            }.getOrElse {
                CLog.e(TAG, "weightToString", it.message)
                null
            }
        }
    }

    @TypeConverter
    fun stringToWeight(weightString: String?): Weight? {
        return weightString?.let {
            gson.runCatching {
                fromJson<Weight>(weightString, object : TypeToken<Weight>() {}.type)
            }.getOrElse {
                CLog.e(TAG, "weightToString", it.message)
                null
            }
        }
    }

    @TypeConverter
    fun imageToString(image: Image?): String? {
        return image?.let {
            gson.runCatching {
                toJson(image, object : TypeToken<Image>() {}.type)
            }.getOrElse {
                CLog.e(TAG, "imageToString", it.message)
                null
            }
        }
    }

    @TypeConverter
    fun stringToImage(imageString: String?): Image? {
        return imageString?.let {
            gson.runCatching {
                fromJson<Image>(imageString, object : TypeToken<Image>() {}.type)
            }.getOrElse {
                CLog.e(TAG, "stringToImage", it.message)
                null
            }
        }
    }

}