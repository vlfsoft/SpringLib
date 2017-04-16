package vlfsoft.common.spring.controller;

import org.slf4j.Logger;

import javax.annotation.Nonnull;

import vlfsoft.common.annotations.design.patterns.gof.StructuralPattern;
import vlfsoft.common.exceptions.AppException;

final public class RestApiProcessor {

    private RestApiProcessor() {
    }

    public static ResponseBodyAdapterA process(@Nonnull Logger log,
                                               @Nonnull AppException.ErrorCodeAdapterA aErrorCode,
                                               @Nonnull ResponseBodyAdapterA aResponseBody,
                                               final @Nonnull ProcessFuncInterface aProcessor) {
        try {
            try {
                return aProcessor.process(log);
            } catch (AppException e) {
                throw e;
            } catch (Throwable e) {
                AppException.propagate(aErrorCode, e);
            }
        } catch (AppException e) {
            log.error(e.getMessage());
            aResponseBody.setErrorCodeA(e.getErrorCode());
        }
        return aResponseBody;
    }

    @FunctionalInterface
    public interface ProcessFuncInterface<ResponseBody extends ResponseBodyAdapterA> {
        ResponseBody process(@Nonnull Logger log) throws RuntimeException;
    }

    @StructuralPattern.Adapter
    public interface ResponseBodyAdapterA {

        /**
         * PRB: JsonConverter tried to get info to deserialize from setErrorCode and raised error,
         * because it didn't know how to deserialize AppException.ErrorCodeAdapterA.
         * WO: Rename from setErrorCode to setErrorCodeA to this method is not interpreted as setter.
         * @param aErrorCode -
         */
        void setErrorCodeA(@Nonnull AppException.ErrorCodeAdapterA aErrorCode);

    }
}
