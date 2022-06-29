package com.camunda.training;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Service
@Slf4j
public class TweetService {

    public long sendTweet(String content) throws Exception {
        if (content.equals("Network error")) {
            throw new RuntimeException("simulated network error");
        }
        log.info("Publishing tweet: " + content);
        final var accessToken = new AccessToken("220324559-CO8TfUmrcoCrvFHP4TacgToN5hLC8cMw4n2EwmHo",
                "WvVureFv5TBWTGhESgGe3fqZM7XbGMuyIhxB84zgcoUER");
        final var twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer("lRhS80iIXXQtm6LM03awjvrvk", "gabtxwW8lnSL9yQUNdzAfgBOgIMSRqh7MegQs79GlKVWF36qLS");
        twitter.setOAuthAccessToken(accessToken);
        return twitter.updateStatus(content).getId();
    }
}

