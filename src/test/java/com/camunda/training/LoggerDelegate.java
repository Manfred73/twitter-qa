package com.camunda.training;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class LoggerDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        final var content = (String) execution.getVariable("content");
        log.info("Your tweet with content \"" + content + "\" has been rejected");
    }
}
