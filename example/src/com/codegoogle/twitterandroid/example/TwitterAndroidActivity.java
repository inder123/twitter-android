/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codegoogle.twitterandroid.example;

import com.codegoogle.twitterandroid.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TwitterAndroidActivity extends Activity {
  //
  // This code is based on
  // http://abhinavasblog.blogspot.com/2011/06/for-all-my-code-thirsty-friends-twitter.html
  //
  private static final String CONSUMER_KEY = "Your twitter consumer key here";
  private static final String CONSUMER_SECRET = "Your twitter consumer secret here";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    final TwitterApp twitter = new TwitterApp(this, CONSUMER_KEY, CONSUMER_SECRET);
    twitter.setListener(new TwitterAuthListener() {
      @Override
      public void onError(String value) {
        Toast.makeText(TwitterAndroidActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
        Log.e("TWITTER", value);
        twitter.resetAccessToken();
      }
      @Override
      public void onComplete(String value) {
        tweetWithValidAuth(twitter);
      }
    });
    Button tweetButton = (Button) findViewById(R.id.tweet_button);
    tweetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        twitter.resetAccessToken();
        if (twitter.hasAccessToken()) {
          tweetWithValidAuth(twitter);
        } else {
          twitter.authorize();
        }
      }
    });
  }

  private void tweetWithValidAuth(TwitterApp twitter) {
    try {
      EditText tweetTextView = (EditText) findViewById(R.id.tweet_text);
      String tweet = tweetTextView.getText().toString();
      twitter.updateStatus(tweet);
      Toast.makeText(this, "Posted Successfully", Toast.LENGTH_LONG).show();
    } catch (Exception e) {
      if (e.getMessage().toString().contains("duplicate")) {
        Toast.makeText(this, "Post failed because of duplicates...", Toast.LENGTH_LONG).show();
      }
      e.printStackTrace();
    }
    twitter.resetAccessToken();
  }
}
