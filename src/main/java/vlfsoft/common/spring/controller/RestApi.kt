package vlfsoft.common.spring.controller

import org.slf4j.Logger
import vlfsoft.patterns.BehavioralPattern
import vlfsoft.patterns.CreationalPattern
import vlfsoft.patterns.ObjectKindPattern
import vlfsoft.patterns.StructuralPattern

@CreationalPattern.UtilityClass
class RestApi private constructor() {

    @StructuralPattern.Adapter.TargetInterface
    interface LogData {
        fun log(log: Logger)
    }

    @ObjectKindPattern.VO
    @BehavioralPattern.Delegation
    @ObjectKindPattern.Immutable
    @ObjectKindPattern.DTO
    @StructuralPattern.Association.Composition
    data class RequestBody<out T : LogData>(val data: T, private val restApiName: String) : LogData by data {

        fun logProcessingStarted(log: Logger) {
            log.info("{} processing started", restApiName)
        }

        fun logRequestBodyDataAreValid(log: Logger) {
            log.info("{} RequestBody data are valid", restApiName)
        }

        fun logProcessingFinished(log: Logger) {
            log.info("{} processing finished", restApiName)
        }

    }

}