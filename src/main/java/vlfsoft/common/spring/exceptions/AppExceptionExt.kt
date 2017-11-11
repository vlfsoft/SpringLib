package vlfsoft.common.spring.exceptions

import org.slf4j.Logger
import vlfsoft.common.exceptions.AppException

fun <R> AppException.ErrorCodeAdapterA.runAndCatchAppExceptionWithLog(tryBlock: AppException.ErrorCodeAdapterA.() -> R, log: Logger, catchBlock: AppException.ErrorCodeAdapterA.(e: AppException) -> R) =
        try {
            tryBlock()
        } catch (e: AppException) {
            log.error(e.message)
            catchBlock(e)
        }
