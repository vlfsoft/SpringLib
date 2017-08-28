package vlfsoft.common.spring.controller

import java.lang.annotation.Documented
import java.lang.annotation.Inherited

/**
 * PRB: Could not write request: no suitable HttpMessageConverter found for request type *.*.controller.model.*Body$Request
 * WO 1: Don't use Request.
 * WO 2: Declare in Body.Request var dummy: Int = 0 or public int dummy;
 */
@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class DummyBodyRequest
