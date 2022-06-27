package com.camunda.training;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Slf4j
public class CreateTweetDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        final var content = (String) execution.getVariable("content");
        log.info("Publishing tweet: " + content);
        final var accessToken = new AccessToken("220324559-CO8TfUmrcoCrvFHP4TacgToN5hLC8cMw4n2EwmHo",
                "WvVureFv5TBWTGhESgGe3fqZM7XbGMuyIhxB84zgcoUER");
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("lRhS80iIXXQtm6LM03awjvrvk", "gabtxwW8lnSL9yQUNdzAfgBOgIMSRqh7MegQs79GlKVWF36qLS");
        twitter.setOAuthAccessToken(accessToken);
        twitter.updateStatus(content);
    }
}
