package com.tts.demo;

import java.util.Locale;

import com.tts.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class TTSActivity extends Activity implements OnInitListener, OnClickListener {
    /** Called when the activity is first created. */
	
	private TextToSpeech tts;
	private static final int MY_DATA_CHECK_CODE = 1234;
	EditText speakstr;
	Button speakbtn;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        speakstr = (EditText)findViewById(R.id.speakstr);
        speakbtn = (Button)findViewById(R.id.speakbtn);
        speakbtn.setOnClickListener(this);
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }
    
    public void onInit(int i)
    {
        tts.speak("Welcome to Speak It!",
                TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
                null);
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.speakbtn)
		{
			String str = speakstr.getText().toString();
			if(!str.equals(""))
				tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
			else
				Toast.makeText(this,R.string.toastnullmsg,Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	public void onRadioButtonClicked(View v)
	{
		RadioButton rb = (RadioButton)v;
		if(rb.getText().equals("French"))
			tts.setLanguage(Locale.FRENCH);
		else
			tts.setLanguage(Locale.US);
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                // success, create the TTS instance
                tts = new TextToSpeech(this,this);
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
    
    public void onDestroy()
    {
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}