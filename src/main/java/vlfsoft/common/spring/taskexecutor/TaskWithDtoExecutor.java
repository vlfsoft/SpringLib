package vlfsoft.common.spring.taskexecutor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.task.TaskExecutor;
import vlfsoft.patterns.ConcurrencyPattern;
import vlfsoft.patterns.GOF;

import java.util.function.Consumer;

@ConcurrencyPattern.ProducerConsumer.Queue.Container.Implementation
@GOF.Singleton
public class TaskWithDtoExecutor<DTO> implements TaskWithDtoExecutorA<DTO> {

    private final @NotNull TaskExecutor mTaskExecutor;
    private @NotNull Consumer<DTO> mTaskConsumer;

    private TaskWithDtoExecutor(final @NotNull TaskExecutor aTaskExecutor) {
        mTaskExecutor = aTaskExecutor;
    }

    @GOF.Factory.StaticFactoryMethod
    public static <DTO> TaskWithDtoExecutorA<DTO> newInstance(final @NotNull TaskExecutor aTaskExecutor) {
        return new TaskWithDtoExecutor<>(aTaskExecutor);
    }

    @Override
    public void submit(@NotNull DTO aTaskDTO) {
        if (mTaskConsumer == null) {
            throw new IllegalArgumentException("mTaskConsumer == null");
        }
        mTaskExecutor.execute(() -> mTaskConsumer.accept(aTaskDTO));
    }

    @Override
    public void setTaskConsumer(final @NotNull Consumer<DTO> aTaskConsumer) {
        mTaskConsumer = aTaskConsumer;
    }
}
