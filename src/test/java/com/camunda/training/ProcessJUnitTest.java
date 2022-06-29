package com.camunda.training;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.process_test_coverage.junit5.ProcessEngineCoverageExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.execute;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.findId;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.jobQuery;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.taskService;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, ProcessEngineCoverageExtension.class})
public class ProcessJUnitTest {

    @Mock
    private TweetService tweetService;

    @Mock
    private EmailService emailService;

    @Test
    @Deployment(resources = "TwitterQAProcess.bpmn")
    public void testHappyPath() throws Exception {
        // Register mocks
        Mocks.register("tweetDelegate", new CreateTweetDelegate(tweetService));

        final var expectedTweetId = 909L;

        // Create a HashMap to put in variables for the process instance
        final var content = "Gompie did it again at " + LocalDateTime.now();
        final var variables = new HashMap<String, Object>();
        variables.put("content", content);

        given(tweetService.sendTweet(content)).willReturn(expectedTweetId);

        // Start process with Java API and variables
        final var processInstance = runtimeService().startProcessInstanceByKey("TwitterQAProcess", variables);

        // Make assertions on the process instance
        assertThat(processInstance).isWaitingAt(findId("Review tweet"));
        assertThat(task()).hasCandidateGroup("management").isNotAssigned();

        // Get all tasks for group management
        final var taskList = taskService()
                .createTaskQuery()
                .taskCandidateGroup("management")
                .processInstanceId(processInstance.getId())
                .list();

        // We expect only one task
        assertThat(taskList).hasSize(1);

        // Simulate task completion by getting the first task and approve it
        final var task = taskList.get(0);
        final var approvedMap = new HashMap<String, Object>();
        approvedMap.put("approved", true);
        taskService().complete(task.getId(), approvedMap);

        // Complete async save points and move process forward
        final var jobList = jobQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertThat(jobList).hasSize(1);
        final var job = jobList.get(0);
        execute(job);

        // We expect process to have ended and that it contains variable tweetId
        assertThat(processInstance)
                .isEnded()
                .variables()
                .containsEntry("tweetId", expectedTweetId);
    }

    @Test
    @Deployment(resources = "TwitterQAProcess.bpmn")
    public void testRejectionPath() {
        // Register mocks
        //Mocks.register("rejectionNotificationDelegate", new LoggerDelegate());
        Mocks.register("rejectionNotificationDelegate", new SendRejectionNotificationDelegate(emailService));

        final var expectedMailId = UUID.randomUUID().toString();

        // Create a HashMap to put in variables for the process instance
        final var variables = new HashMap<String, Object>();
        variables.put("approved", false);
        variables.put("content", "Gompie did it again at " + LocalDateTime.now());

        given(emailService.sendEmail()).willReturn(expectedMailId);

        // Start process with Java API and variables
        final var processInstance = runtimeService()
                .createProcessInstanceByKey("TwitterQAProcess")
                .setVariables(variables)
                .startAfterActivity(findId("Review tweet"))
                .execute();

        // Make assertions on the process instance
        assertThat(processInstance)
                .isEnded()
                .hasPassed(findId("Tweet rejected"))
                .variables()
                .containsEntry("mailId", expectedMailId);
    }
}
