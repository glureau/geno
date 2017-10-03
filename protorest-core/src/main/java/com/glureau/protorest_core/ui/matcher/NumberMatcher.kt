package com.glureau.protorest_core.ui.matcher

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object NumberMatcher : TypeMatcher {
    override fun match(kClass: KClass<*>, annotations: Array<out Annotation>?) = kClass.isSubclassOf(Number::class)
}