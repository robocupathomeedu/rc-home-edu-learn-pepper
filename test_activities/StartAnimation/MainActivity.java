// Leave package statement as defined by your project
// as first line of this file and replace all the rest

import android.os.Bundle;
import android.widget.Button;

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

    // GUI button
    private Button button;
    // The QiContext provided by the QiSDK.
    private QiContext qiContext = null;

    private Say sayAction1, sayAction2;
    private Animate gorillaAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button1);
        // Set the button onClick listener.
        button.setOnClickListener(v -> {
            sayAction2.async().run();
            gorillaAction.async().run();
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
        sayAction1.async().run();
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
        sayAction1 = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("Hello") // Set the text to say.
                .build(); // Build the say action.

        sayAction2 = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText("OK. I am ready to start.") // Set the text to say.
                .build(); // Build the say action.

        // Create an animation.
        Animation animation = AnimationBuilder.with(qiContext) // Create the builder with the context.
                .withResources(R.raw.gorilla_a001) // Set the animation resource.
                .build(); // Build the animation.

        // Create an animate action.
        gorillaAction = AnimateBuilder.with(qiContext) // Create the builder with the context.
                .withAnimation(animation) // Set the animation.
                .build(); // Build the animate action.
    }

}
