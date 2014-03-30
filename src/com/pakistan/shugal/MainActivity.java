package com.pakistan.shugal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity {

	List<String> str_lst = new ArrayList<String>();
	Button b1;
	private AdView adView;
	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "ca-app-pub-5489803849031898/1155173164";

	private void loadJokes() throws IOException {
        final Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(R.raw.jokes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            String joke = "";
            while ((line = reader.readLine()) != null) {
            	if (line.equals(".")) {
            		str_lst.add(joke);
            		joke = "";
            		line = "";
            	}
            	joke = joke + line + '\n';
            }
        } finally {
            reader.close();
        }
    }

    View.OnClickListener myhandler1 = new View.OnClickListener() {
    	public void onClick(View v) {
    		updateTextView();
    	}
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		try {
			loadJokes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b1 = (Button) findViewById(R.id.next_btn);
		b1.setOnClickListener(myhandler1);
		
		// Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.
	    LinearLayout layout = (LinearLayout) findViewById(R.id.container);
	    layout.addView(adView);

	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder()
	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
	        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
    protected void onResume() {
        super.onResume();
        updateTextView();
        if (adView != null) {
            adView.resume();
        }
    }
	
	private void updateTextView() {
        TextView textView = (TextView)findViewById(R.id.joke_text); 
        Random random = new Random();

        int maxIndex = str_lst.size();
        int generatedIndex = random.nextInt(maxIndex);

        textView.setText(str_lst.get(generatedIndex));
        textView.setTextColor(Color.parseColor("#FFFFFF"));   
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	  public void onPause() {
	    if (adView != null) {
	      adView.pause();
	    }
	    super.onPause();
	  }

	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    // Destroy the AdView.
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }
}
