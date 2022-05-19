package com.example.pdm.controlador.controladorPrincipal;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.Bundle;

public class VideoActivity extends JitsiMeetActivity{

    EditText code;
    Button join;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_videchat);
        code = findViewById(R.id.addCode);
        join = findViewById(R.id.llamada);

        URL serverURL;

        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setWelcomePageEnabled(false)
                    .build();

            JitsiMeet.setDefaultConferenceOptions(defaultOptions);


        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions roomOptions  = new JitsiMeetConferenceOptions.Builder().setRoom(code.getText().toString()).setWelcomePageEnabled(false).build();

                JitsiMeetActivity.launch(VideoActivity.this, roomOptions);
            }
        });

    }



}
