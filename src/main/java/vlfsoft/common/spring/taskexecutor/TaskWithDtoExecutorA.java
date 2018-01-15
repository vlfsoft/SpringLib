package vlfsoft.common.spring.taskexecutor;

import org.jetbrains.annotations.NotNull;
import vlfsoft.patterns.ConcurrencyPattern;
import vlfsoft.patterns.GOF;

import java.util.function.Consumer;

/**
 * This is a {@link GOF.Singleton} {@link ConcurrencyPattern.ProducerConsumer.Queue.Container} for the application {@link org.springframework.core.task.TaskExecutor}(s).
 * It decouples {@link ConcurrencyPattern.ProducerConsumer.Producer}(s)
 * from {@link ConcurrencyPattern.ProducerConsumer.Consumer}(s)
 */
@ConcurrencyPattern.ProducerConsumer.Queue.Container.Interface
public interface TaskWithDtoExecutorA<DTO> {
    void submit(final @NotNull DTO aTaskDTO);
    void setTaskConsumer(final @NotNull Consumer<DTO> aTaskConsumer);
}
