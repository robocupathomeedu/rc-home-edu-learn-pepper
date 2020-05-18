package com.example.actions;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.AnimateBuilder;
import com.aldebaran.qi.sdk.builder.AnimationBuilder;
import com.aldebaran.qi.sdk.builder.SayBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.actuation.Animate;
import com.aldebaran.qi.sdk.object.actuation.Animation;
import com.aldebaran.qi.sdk.object.conversation.Say;

public class MainActivity  extends RobotActivity implements RobotLifecycleCallbacks {

    private static final String TAG ="MainActivty";

    // GUI button
    private Button startButton, stopButton;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    // Action definition
    private Say say1Action, say2Action;
    private Animate gorillaAction, trajAction;

    // Future for action execution
    private Future<Void> execFuture = null;

    // UI thread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button)findViewById(R.id.button1);
        // Set the button onClick listener.
        startButton.setOnClickListener(v -> {
             executeActions(); // start actions and set execFuture
        });

        stopButton = (Button)findViewById(R.id.button2);
        // Set the button onClick listener.
        stopButton.setOnClickListener(v -> {
            if (execFuture!=null)
                execFuture.requestCancellation();  // cancel action execution
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
        say1Action = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Hello. Welcome to the show.") // Set the text to say.
                .build(); // Build the say action.

        say2Action = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("I hope you enjoed.") // Set the text to say.
                .build(); // Build the say action.

        // Create an animation.
        Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.gorilla_b001) // Set the animation resource.
                .build(); // Build the animation.

        // Create gorilla action.
        gorillaAction = AnimateBuilder.with(qiContext) // Create the builder with the context.
                .withAnimation(animation) // Set the animation.
                .build(); // Build the animate action.

        // Create a motion animation.
        Animation trajectory = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.trajectory_00) // Set the animation resource.
                .build(); // Build the animation.

        // Create trajectory action
        trajAction = AnimateBuilder.with(qiContext) // Create the builder with the context.
                .withAnimation(trajectory) // Set the animation.
                .build(); // Build the animate action.

    }

    // UI thread (cannot run sync actions here!)
    public void executeActions() {

        // Compose several actions in a sequence
        // and return a future for monitoring the execution
        execFuture = say1Action.async().run()
                .andThenCompose( v -> gorillaAction.async().run() )
                .andThenCompose( v -> trajAction.async().run() )
                .andThenCompose( v -> gorillaAction.async().run() )
                .andThenCompose( v -> say2Action.async().run() )
                .andThenConsume( v -> Log.i(TAG, "Execution Success") );

    }

}
