package de.ramelsberger.lmu.smartremoteapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class ButttonChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttton_chooser);
        Typeface font = Typeface.createFromAsset( getAssets(), "../fontawesome-webfont.ttf");

        Button button = (Button)findViewById( R.id.panelAwsomeIcon );
        button.setTypeface(font);
    }
}
