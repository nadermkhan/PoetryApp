package nader.tool.lolpanel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultContracts;
import androidx.activity.result.ActivityResultLauncher;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	
	private CircularProgressIndicator progressBar;
	private TextView logTextView;
	private Button selectFileButton;
	private OkHttpClient okHttpClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize OkHttpClient
		okHttpClient = new OkHttpClient();
		
		progressBar = findViewById(R.id.progressBar);
		logTextView = findViewById(R.id.logTextView);
		selectFileButton = findViewById(R.id.selectFileButton);
		
		// File picker setup (to select .txt file)
		ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
		new ActivityResultContracts.GetContent(),
		new ActivityResultCallback<Uri>() {
			@Override
			public void onActivityResult(Uri result) {
				if (result != null) {
					File selectedFile = new File(getRealPathFromURI(result));
					processFile(selectedFile);
				}
			}
		});
		
		selectFileButton.setOnClickListener(v -> filePickerLauncher.launch("text/plain"));
	}
	
	private String getRealPathFromURI(Uri uri) {
		// Convert URI to file path (implement this as needed)
		return uri.getPath();
	}
	
	private void processFile(File file) {
		progressBar.setVisibility(View.VISIBLE);
		new Thread(() -> {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line;
				int totalLines = 0;
				ArrayList<String> lines = new ArrayList<>();
				
				// Count total lines first
				while ((line = reader.readLine()) != null) {
					if (line.contains("|")) {
						lines.add(line);
					}
					totalLines++;
				}
				
				int lineCount = 0;
				
				// Reset reader to process lines
				reader.close();
				reader = new BufferedReader(new FileReader(file));
				
				// Process each line
				while ((line = reader.readLine()) != null) {
					if (line.contains("|")) {
						String[] parts = line.split("\\|");
						String url = parts[0];
						
						// Check URL HTTP status using OkHttp
						if (checkHttpStatus(url)) {
							appendLog("Accessible: " + url);
							} else {
							appendLog("Not Accessible: " + url);
						}
						
						lineCount++;
						
						// Update progress bar
						final int progress = (int) (((double) lineCount / totalLines) * 100);
						runOnUiThread(() -> progressBar.setProgress(progress));
					}
				}
				
				reader.close();
				
				// File processing complete
				runOnUiThread(() -> progressBar.setVisibility(View.GONE));
				
				} catch (IOException e) {
				runOnUiThread(() -> appendLog("Error: " + e.getMessage()));
			}
		}).start();
	}
	
	private boolean checkHttpStatus(String urlString) {
		try {
			Request request = new Request.Builder()
			.url(urlString)
			.build();
			
			try (Response response = okHttpClient.newCall(request).execute()) {
				return response.isSuccessful(); // returns true if status code is 2xx
			}
			
			} catch (Exception e) {
			return false;
		}
	}
	
	private void appendLog(String message) {
		runOnUiThread(() -> logTextView.append(message + "\n"));
	}
}