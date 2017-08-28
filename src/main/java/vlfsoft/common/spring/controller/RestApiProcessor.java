package vlfsoft.common.spring.controller;

import org.slf4j.Logger;
import vlfsoft.common.annotations.design.patterns.StructuralPattern;
import vlfsoft.common.exceptions.AppException;

import javax.annotation.Nonnull;
import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * RB - ResponseBody
 */
final public class RestApiProcessor {

    private RestApiProcessor() {
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB process(final @Nonnull Logger log,
                                                                                final @Nonnull RB aResponseBody,
                                                                                final @Nonnull Consumer<RB> aProcessorFunction,
                                                                                final @Nonnull AppException.ErrorCodeAdapterA aErrorCode
    ) {
        return process(log, aResponseBody, aProcessorFunction, aErrorCode, RestApiProcessor::defaultCatchFunction);
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB process(final @Nonnull Logger log,
                                                                                final @Nonnull RB aResponseBody,
                                                                                final @Nonnull Consumer<RB> aProcessorFunction,
                                                                                final @Nonnull AppException.ErrorCodeAdapterA aErrorCode,
                                                                                final @Nonnull CatchFuncInterface<RB> aCatchFunction
    ) {
        try {
            try {
                // return aProcessor.process(log);
                aProcessorFunction.accept(aResponseBody);
            } catch (Throwable e) {
                AppException.propagate(aErrorCode, e);
                // dummy code - never runs, since AppException.propagate will always route code flow to outer catch.
            }
        } catch (AppException e) {
            aCatchFunction.accept(log, aResponseBody, e);
//            log.error(e.getMessage());
//            aResponseBodyIfError.setErrorCode(e.getErrorCode());
        }
        return aResponseBody;
    }

    public static <RB extends RestApiProcessor.ResponseBodyAdapterA> RB defaultCatchFunction(final @Nonnull Logger log,
                                                                                             final @Nonnull RB aResponseBody,
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
        void accept(final @Nonnull Logger log,
                    final @Nonnull RB aResponseBody,
                    AppException e) throws RuntimeException;
    }

/*
    @FunctionalInterface
    public interface ProcessFuncInterface<RB extends ResponseBodyAdapterA> {
        RB process(@Nonnull Logger log) throws RuntimeException;
    }
*/

    @StructuralPattern.Adapter
    public interface RequestAppCodeAdapterA {

        @Nonnull String getAppCode();
        void setAppCode(final @Nonnull String aAppCode);

        default public void logAppCode(final @Nonnull Logger log) {
            log.info("aRequestBody.appCode : {}", getAppCode());
        }

    }

    public static abstract class GenericRequestAppCodeAdapter implements RequestAppCodeAdapterA {
        private @Nonnull String mAppCode;

        public GenericRequestAppCodeAdapter(final @Nonnull String aAppCode) {
            this.mAppCode = aAppCode;
        }

        @Nonnull
        @Override
        public String getAppCode() {
            return mAppCode;
        }

        @Override
        public void setAppCode(final @Nonnull String aAppCode) {
            this.mAppCode = aAppCode;
        }
    }

    @StructuralPattern.Adapter
    public interface ResponseBodyAdapterA<AppErrorCode extends AppException.ErrorCodeAdapterA> {

        @Nonnull
        AppErrorCode getErrorCode();

        void setErrorCode(final @Nonnull AppErrorCode aErrorCode);

    }

    public static abstract class GenericResponseBodyAdapter<AppErrorCode extends AppException.ErrorCodeAdapterA> implements RestApiProcessor.ResponseBodyAdapterA<AppErrorCode> {

        // private @Nonnull AppException.ErrorCodeAdapterA mErrorCode;
        // PRB: JSON parse error: Can not construct instance of vlfsoft.common.exceptions.AppException$ErrorCodeAdapterA:
        // WO:
        private @Nonnull AppErrorCode mErrorCode;

        @Nonnull
        @Override
        public AppErrorCode getErrorCode() {
             return mErrorCode;
        }


        @Override
        public void setErrorCode(final @Nonnull AppErrorCode aErrorCode) {
            mErrorCode = aErrorCode;
        }

        public GenericResponseBodyAdapter(final @Nonnull AppErrorCode mErrorCode) {
            this.mErrorCode = mErrorCode;
        }
    }


    public static void logStarted(final @Nonnull Logger log, final @Nonnull String aRestApiName) {
        log.info("{} started", aRestApiName);
    }

    public static void logProcessingStarted(final @Nonnull Logger log, final @Nonnull String aRestApiName) {
        log.info("{} processing started", aRestApiName);
    }

    public static void logProcessingFinished(final @Nonnull Logger log, final @Nonnull String aRestApiName) {
        log.info("{} processing finished", aRestApiName);
    }

}

