package nader.app.literature.poetry;package nader.app.literature.poetry;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private LinearLayout poetryLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		poetryLayout = findViewById(R.id.poetryLayout);
		
		// Load poetry data from JSON
		List<Poetry> poetryList = JsonHelper.loadPoetry(this);
		
		// Display poetry
		displayPoetry(poetryList);
	}
	
	private void displayPoetry(List<Poetry> poetryList) {
		for (Poetry poetry : poetryList) {
			// Display the full poetry line
			TextView lineTextView = new TextView(this);
			lineTextView.setText(poetry.getText());
			lineTextView.setTextSize(20);
			poetryLayout.addView(lineTextView);
			
			// Create a container for word-by-word meaning
			LinearLayout wordLayout = new LinearLayout(this);
			wordLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			// Display each word with its meaning
			for (Word word : poetry.getWords()) {
				LinearLayout wordBlock = new LinearLayout(this);
				wordBlock.setOrientation(LinearLayout.VERTICAL);
				
				// Word in English
				TextView wordText = new TextView(this);
				wordText.setText(word.getWord());
				wordText.setTextSize(18);
				wordBlock.addView(wordText);
				
				// Meaning in English
				TextView meaningText = new TextView(this);
				meaningText.setText(word.getMeaning());
				meaningText.setTextSize(14);
				wordBlock.addView(meaningText);
				
				// Meaning in Bangla
				TextView banglaText = new TextView(this);
				banglaText.setText(word.getBangla());
				banglaText.setTextSize(14);
				wordBlock.addView(banglaText);
				
				wordLayout.addView(wordBlock);
			}
			
			poetryLayout.addView(wordLayout);
		}
	}
}