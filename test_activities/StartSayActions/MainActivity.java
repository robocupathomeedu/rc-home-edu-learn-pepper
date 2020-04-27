package com.example.hellopepper;

import android.os.Bundle;
import android.widget.Button;

import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class MainActivity  extends RobotActivity implements RobotLifecycleCallbacks {

    // GUI button
    private Button button;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    private Say say_action1, say_action2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button1);
        // Set the button onClick listener.
        button.setOnClickListener(v -> {
            say_action2.async().run();
        });

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
        // Store the provided QiContext.
        this.qiContext = qiContext;
        initActions();
        say_action1.async().run();
    }

    @Override
    public void onRobotFocusLost() {
        // Remove the QiContext.
        this.qiContext = null;
    }

    @Override
    public void onRobotFocusRefused(String reason) {
        // The robot focus is refused.
    }

    public void initActions() {
        // Create new say actions.
        say_action1 = SayBuilder.with(this.qiContext) // Create the builder with the context.
                .withText("Hello") // Set the text to say.
                .build(); // Build the say action.

        say_action2 = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("OK. I am ready to start.") // Set the text to say.
                .build(); // Build the say action.
    }

}
