package com.camunda.training;

import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("rejectionNotificationDelegate")
@AllArgsConstructor
public class SendRejectionNotificationDelegate implements JavaDelegate {

    private EmailService emailService;

    @Override
    public void execute(DelegateExecution execution) {
        final var mailId = emailService.sendEmail();
        execution.setVariable("mailId", mailId);
    }
}
