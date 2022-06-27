package com.camunda.training;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit5.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;

@ExtendWith(ProcessEngineCoverageExtension.class)
public class ProcessJUnitTest {

    @Test
    @Deployment(resources = "TwitterQAProcess.bpmn")
    public void testHappyPath() {
        // Create a HashMap to put in variables for the process instance
        final var variables = new HashMap<String, Object>();
        variables.put("approved", true);
        variables.put("content", "Gompie did it again at " + LocalDateTime.now());

        // Start process with Java API and variables
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("TwitterQAProcess", variables);

        // Make assertions on the process instance
        assertThat(processInstance).isEnded();
    }

    @Test
    @Deployment(resources = "TwitterQAProcess.bpmn")
    public void testRejectionPath() {
        Mocks.register("rejectionNotificationDelegate", new LoggerDelegate());

        // Create a HashMap to put in variables for the process instance
        final var variables = new HashMap<String, Object>();
        variables.put("approved", false);
        variables.put("content", "Gompie did it again at " + LocalDateTime.now());

        // Start process with Java API and variables
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("TwitterQAProcess", variables);

        // Make assertions on the process instance
        assertThat(processInstance).isEnded();
    }
}
