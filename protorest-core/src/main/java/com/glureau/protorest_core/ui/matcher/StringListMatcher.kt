package com.glureau.protorest_core.ui.matcher

import java.util.List
import kotlin.reflect.KClass

object StringListMatcher : TypeMatcher {
    override fun match(kClass: KClass<*>, annotations: Array<out Annotation>?) = kClass.java == List::class.java
    // TODO : Have a better collection implementation, this is pretty crap...
}