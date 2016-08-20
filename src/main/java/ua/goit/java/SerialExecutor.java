package ua.goit.java;

import java.util.ArrayList;
import java.util.List;

public class SerialExecutor<T> implements Executor<T>{

    private static final Validator<Object> DEFAULT_VALIDATOR = result -> true;

    private List<TaskAndValidator<T>> tasks = new ArrayList<>();

    private List<T> validResults = new ArrayList<>();
    private List<T> invalidResults = new ArrayList<>();

    private boolean executed = false;

    @Override
    public void addTask(Task<? extends T> task) {
        checkNotExecuted();
        tasks.add(new TaskAndValidator<>(task, DEFAULT_VALIDATOR));
    }

    private void checkNotExecuted() {
        if (executed){
            throw new IllegalStateException("ua.goit.java.Executor  executed");
        }
    }

    private void checkExecuted() {
        if (!executed){
            throw new IllegalStateException("ua.goit.java.Executor already executed");
        }
    }

    @Override
    public void addTask(Task<? extends T> task, Validator<? super T> validator) {
        checkNotExecuted();
        tasks.add(new TaskAndValidator<>(task, validator));
    }

    @Override
    public void execute() {
        checkNotExecuted();
        tasks.forEach((task -> {
            task.task.execute();
            if (task.validator.isValid(task.task.getResult())) {
                validResults.add(task.task.getResult());
            }else {
                invalidResults.add(task.task.getResult());
            }
        }));
        executed = true;
    }

    @Override
    public List getValidResults() {
        checkExecuted();
        return validResults;
    }

    @Override
    public List getInvalidResults() {
        checkExecuted();
        return invalidResults;
    }

    private static class TaskAndValidator<T>{
        private Task<? extends T> task;
        private Validator<? super T> validator;

        public TaskAndValidator(Task<? extends T> task, Validator<? super T> validator) {
            this.task = task;
            this.validator = validator;
        }
    }
}
