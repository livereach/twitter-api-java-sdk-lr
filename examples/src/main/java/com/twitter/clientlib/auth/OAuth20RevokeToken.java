/*
Copyright 2020 Twitter, Inc.
SPDX-License-Identifier: Apache-2.0

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
https://openapi-generator.tech
Do not edit the class manually.
*/


package com.twitter.clientlib.auth;

import java.util.HashSet;
import java.util.Set;

import com.github.scribejava.core.revoke.TokenTypeHint;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.auth.TwitterOAuth20Service;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;
import com.twitter.clientlib.model.SingleTweetLookupResponse;

/**
* This is an example of revoking an OAuth2 access token.
* It's expected to set TwitterCredentialsOAuth2.
*
* Example steps:
* 1. Call the API successfully.
* 2. Call revoke token API.
* 3. Call the API again, this time it should end in failure.
*/

public class OAuth20RevokeToken {

  public static void main(String[] args) {
    OAuth20RevokeToken example = new OAuth20RevokeToken();
    TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(System.getenv("TWITTER_OAUTH2_CLIENT_ID"),
        System.getenv("TWITTER_OAUTH2_CLIENT_SECRET"),
        System.getenv("TWITTER_OAUTH2_ACCESS_TOKEN"),
        System.getenv("TWITTER_OAUTH2_REFRESH_TOKEN"));
    TwitterApi apiInstance = new TwitterApi();
    apiInstance.setTwitterCredentials(credentials);

    TwitterOAuth20Service service = new TwitterOAuth20Service(
        credentials.getTwitterOauth2ClientId(),
        credentials.getTwitterOAuth2ClientSecret(),
        "http://twitter.com",
        "offline.access tweet.read users.read like.write space.read list.read tweet.write like.read");

    // Assuming that the access token is valid this call should be successful
    example.callApi(apiInstance);
    // Revoke the token
    try {
      service.revokeToken(credentials.getTwitterOauth2AccessToken(), TokenTypeHint.ACCESS_TOKEN);
    } catch (Exception e) {
      System.err.println("Error while trying to revoke existing token : " + e);
      e.printStackTrace();
      return;
    }
    // This call should fail
    example.callApi(apiInstance);
  }

  public void callApi(TwitterApi apiInstance) {
    Set<String> tweetFields = new HashSet<>();
    tweetFields.add("author_id");
    tweetFields.add("id");
    tweetFields.add("created_at");

    try {
      // findTweetById
      SingleTweetLookupResponse result = apiInstance.tweets().findTweetById("20", null, tweetFields, null,
          null, null, null);
      if (result.getErrors() != null && result.getErrors().size() > 0) {
        System.out.println("Error:");
        result.getErrors().forEach(e -> {
          System.out.println(e.toString());
          if (e instanceof ResourceUnauthorizedProblem) {
            System.out.println(e.getTitle() + " " + e.getDetail());
          }
        });
      } else {
        System.out.println("findTweetById - Tweet Text: " + result.toString());
      }
    } catch (ApiException e) {
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}