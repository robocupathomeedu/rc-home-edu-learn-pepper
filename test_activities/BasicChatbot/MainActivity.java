package com.example.basicchatbot;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;

public class MainActivity  extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG ="MainActivty";

    // Textview element in the GUI
    private TextView textView;
    private ImageView imageView;

    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    // Store the Chat action.
    private Chat chatAction;

    // Animate actions
    private Animate goodAnimAction, badAnimAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView1);
        imageView = (ImageView)findViewById(R.id.imageView1);
        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this);
    }

    @Override
    protected void onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this);
        super.onDestroy();
    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        Log.i(TAG, "App started.");

        // Store the provided QiContext.
        this.qiContext = qiContext;
        initActions();

        // Run the Chat action asynchronously.
        Future<Void> chatFuture = chatAction.async().run();

        // Add a lambda to the action execution.
        chatFuture.thenConsume(future -> {
            if (future.hasError()) {
                Log.e(TAG, "Discussion finished with error.", future.getError());
            }
        });

    }

    @Override
    public void onRobotFocusLost() {
        // Remove on started listeners from the Chat action.
        if (chatAction != null) {
            chatAction.removeAllOnStartedListeners();
        }
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    private void textViewSetValue(String val) {
        runOnUiThread(() -> {
            textView.setText(val);
        });
    }

    private void imageViewSetValue(String val) {
        if (val.equals("coke"))
            runOnUiThread(() -> { imageView.setImageResource(R.drawable.coke); });
        else if (val.equals("wine"))
            runOnUiThread(() -> { imageView.setImageResource(R.drawable.wine); });

    }

    public void initActions() {
        // Create a chat topic
        Topic topic = TopicBuilder.with(qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.greetings) // Set the topic resource.
                .build(); // Build the topic.
        // Create a new QiChatbot.
        QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build();
        // Create a new Chat action.
        chatAction = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build();

        // Add an on started listener to the Chat action.
        chatAction.addOnStartedListener(() -> Log.i(TAG, "Discussion started."));


        // Set up a listener for a chat variable
        QiChatVariable nameVariable = qiChatbot.variable("Name");

        nameVariable.addOnValueChangedListener(
                currentValue -> {
                    Log.i(TAG, "Chat var Name: " + currentValue);
                    textViewSetValue("Hello " + currentValue);
                }
        );

        // Set up a listener for a chat variable
        QiChatVariable drinkVariable = qiChatbot.variable("Drink");

        drinkVariable.addOnValueChangedListener(
                currentValue -> {
                    Log.i(TAG, "Chat var Drink: " + currentValue);
                    imageViewSetValue(currentValue);
                }
        );


        // Create animations
        Animation animation_good = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.nicereaction_a001) // Set the animation resource.
                .build(); // Build the animation.
        Animation animation_bad = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.sad_a001) // Set the animation resource.
                .build(); // Build the animation.

        // Create animate actions
        goodAnimAction = AnimateBuilder.with(qiContext) // Create the builder with the context.
                .withAnimation(animation_good) // Set the animation.
                .build(); // Build the animate action.

        badAnimAction = AnimateBuilder.with(qiContext) // Create the builder with the context.
            .withAnimation(animation_bad) // Set the animation.
            .build(); // Build the animate action.

        qiChatbot.addOnBookmarkReachedListener((bookmark) -> {
            Log.i(TAG, "Bookmark event: " + bookmark.getName());
            if (bookmark.getName().equals("feelgood")) {
                goodAnimAction.async().run();
            }
            else if (bookmark.getName().equals("feelbad")) {
                badAnimAction.async().run();
            }
        });

    }

}
