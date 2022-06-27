package com.camunda.training;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("rejectionNotificationDelegate")
public class SendRejectionNotificationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // Send the rejection notification to the employee...
    }
}
