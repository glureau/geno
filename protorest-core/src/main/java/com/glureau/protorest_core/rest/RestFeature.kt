package com.glureau.protorest_core.rest

class RestFeature<T>(val name: String,
                     val action: (params: Array<out RestParameter>) -> RestResult<T>,
        //                     val generateViews: (Activity, RestFeature<T>, ViewGroup, ViewGroup) -> Completable,
                     val params: Array<out RestParameter>) {
//    fun generateViews(activity: Activity, paramContainer: ViewGroup, resultContainer: ViewGroup) = generateViews.invoke(activity, this, paramContainer, resultContainer)

    fun execute() = action.invoke(params)
}

class RestFeatureGroup(val name: String, val features: List<RestFeature<out Any>>)