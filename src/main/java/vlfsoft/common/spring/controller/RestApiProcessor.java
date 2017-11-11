package vlfsoft.common.spring.controller;

import org.slf4j.Logger;
import vlfsoft.patterns.StructuralPattern;
import vlfsoft.common.exceptions.AppException;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * RB - ResponseBody
 */
final public class RestApiProcessor {

    private RestApiProcessor() {
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB process(final @NotNull Logger log,
                                                                                final @NotNull RB aResponseBody,
                                                                                final @NotNull Consumer<RB> aProcessorFunction,
                                                                                final @NotNull AppException.ErrorCodeAdapterA aErrorCode
    ) {
        return process(log, aResponseBody, aProcessorFunction, aErrorCode, RestApiProcessor::defaultCatchFunction);
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB process(final @NotNull Logger log,
                                                                                final @NotNull RB aResponseBody,
                                                                                final @NotNull Consumer<RB> aProcessorFunction,
                                                                                final @NotNull AppException.ErrorCodeAdapterA aErrorCode,
                                                                                final @NotNull CatchFuncInterface<RB> aCatchFunction
    ) {
        try {
            try {
                // return aProcessor.process(log);
                aProcessorFunction.accept(aResponseBody);
            } catch (Throwable e) {
                throw AppException.getExceptionToPropagate(aErrorCode, e);
                // AppException.propagate(aErrorCode, e);
                // dummy code - never runs, since AppException.propagate will always route code flow to outer catch.
            }
        } catch (AppException e) {
            aCatchFunction.accept(log, aResponseBody, e);
//            log.error(e.getMessage());
//            aResponseBodyIfError.setErrorCode(e.getErrorCode());
        }
        return aResponseBody;
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB defaultCatchFunction(final @NotNull Logger log,
                                                                                             final @NotNull RB aResponseBody,
                                                                                             AppException e) {
        log.error(e.getMessage());
        // PRB: Unchecked
        // Solution: Make AppException<AppErrorCode> generic and replace in AppException<AppErrorCode> private ErrorCodeAdapterA mErrorCode; with private AppErrorCode mErrorCode;
        // it will require to modify code in very many places :(.
        aResponseBody.setErrorCode(e.getErrorCode());
        return aResponseBody;
    }

    @FunctionalInterface
    public interface CatchFuncInterface<RB extends ResponseBodyAdapterA> {
        void accept(final @NotNull Logger log,
                    final @NotNull RB aResponseBody,
                    AppException e) throws RuntimeException;
    }

/*
    @FunctionalInterface
    public interface ProcessFuncInterface<RB extends ResponseBodyAdapterA> {
        RB process(@NotNull Logger log) throws RuntimeException;
    }
*/

    @StructuralPattern.Adapter
    public interface RequestAppCodeAdapterA {

        @NotNull String getAppCode();
        void setAppCode(final @NotNull String aAppCode);

        default public void logAppCode(final @NotNull Logger log) {
            log.info("aRequestBody.appCode : {}", getAppCode());
        }

    }

    public static abstract class GenericRequestAppCodeAdapter implements RequestAppCodeAdapterA {
        private @NotNull String mAppCode;

        public GenericRequestAppCodeAdapter(final @NotNull String aAppCode) {
            this.mAppCode = aAppCode;
        }

        @NotNull
        @Override
        public String getAppCode() {
            return mAppCode;
        }

        @Override
        public void setAppCode(final @NotNull String aAppCode) {
            this.mAppCode = aAppCode;
        }
    }

    @StructuralPattern.Adapter
    public interface ResponseBodyAdapterA<AppErrorCode extends AppException.ErrorCodeAdapterA> {

        @NotNull
        AppErrorCode getErrorCode();

        void setErrorCode(final @NotNull AppErrorCode aErrorCode);

    }

    public static abstract class GenericResponseBodyAdapter<AppErrorCode extends AppException.ErrorCodeAdapterA> implements RestApiProcessor.ResponseBodyAdapterA<AppErrorCode> {

        // private @NotNull AppException.ErrorCodeAdapterA mErrorCode;
        // PRB: JSON parse error: Can not construct instance of vlfsoft.common.exceptions.AppException$ErrorCodeAdapterA:
        // WO:
        private @NotNull AppErrorCode mErrorCode;

        @NotNull
        @Override
        public AppErrorCode getErrorCode() {
             return mErrorCode;
        }


        @Override
        public void setErrorCode(final @NotNull AppErrorCode aErrorCode) {
            mErrorCode = aErrorCode;
        }

        public GenericResponseBodyAdapter(final @NotNull AppErrorCode mErrorCode) {
            this.mErrorCode = mErrorCode;
        }
    }


    public static void logStarted(final @NotNull Logger log, final @NotNull String aRestApiName) {
        log.info("{} started", aRestApiName);
    }

    public static void logProcessingStarted(final @NotNull Logger log, final @NotNull String aRestApiName) {
        log.info("{} processing started", aRestApiName);
    }

    public static void logProcessingFinished(final @NotNull Logger log, final @NotNull String aRestApiName) {
        log.info("{} processing finished", aRestApiName);
    }

}

