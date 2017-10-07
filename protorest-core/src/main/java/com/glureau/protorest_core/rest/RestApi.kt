package com.glureau.protorest_core.rest

import com.glureau.protorest_core.network.RestNetworkClient
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import com.squareup.moshi.Types
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import kotlin.reflect.KClass

open class RestApi(val baseApi: String, vararg adapters: Any) {

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class Image

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    @SuppressWarnings("unused")
    annotation class Endpoint(@JvmField val returnType: KClass<*>) // TODO : pass the KClass as generic of the annotation, for more flexibility if required

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    annotation class EndpointParam(@JvmField val name: String, @JvmField val defaultValue: String="")

    val moshi: Moshi

    init {
        var builder = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .add(Types.newParameterizedType(List::class.java, String::class.java).rawType, StringArrayJsonAdapter)
        adapters.forEach { builder = builder.add(it) }
        moshi = builder.build()
    }

    fun <T> get(path: String, clazz: Class<T>): Observable<RestResult<T>> =
            RestNetworkClient.get(baseApi + path)
                    .map { response ->
                        val body = response.body()?.string()
                        if (body != null) {
                            Timber.i("Receive response from server: %s", body)
                        } else {
                            Timber.e("Error when requesting %s", response.request().url())
                            Timber.e("Body is null, status: %i %s", response.code(), response.message())
                        }
                        val jsonAdapter = moshi.adapter(clazz).lenient()
                        val result = jsonAdapter.fromJson(body)
                        Timber.i("Response parsed: %s", result)
                        if (result == null) {
                            error("Unable to parse")
                        } else {
                            RestResult(result)
                        }
                    }


}