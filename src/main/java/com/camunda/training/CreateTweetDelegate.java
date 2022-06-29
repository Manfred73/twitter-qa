package com.camunda.training;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("tweetDelegate")
public class CreateTweetDelegate implements JavaDelegate {

    private final TweetService tweetService;

    @Autowired
    public CreateTweetDelegate(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        final var content = (String) execution.getVariable("content");
        final var tweetId = tweetService.sendTweet(content);
        execution.setVariable("tweetId", tweetId);
    }
}
