package nader.tool.lolpanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1; // Request code for file picker

    private ProgressBar progressBar;
    private TextView logTextView;
    private Button selectFileButton;
    private ExecutorService executorService; // Executor for background tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the UI elements
        progressBar = findViewById(R.id.progressBar);
        logTextView = findViewById(R.id.logTextView);
        selectFileButton = findViewById(R.id.selectFileButton);

        // Initialize ExecutorService for handling background tasks
        executorService = Executors.newSingleThreadExecutor();

        selectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    /**
     * Opens a file picker to allow the user to select a file.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                String filePath = getFilePath(fileUri);
                if (filePath != null) {
                    processFile(filePath);
                } else {
                    showErrorMessage("Failed to retrieve the file path");
                }
            } else {
                showErrorMessage("File URI is null");
            }
        } else {
            showErrorMessage("File selection failed or was canceled");
        }
    }

    /**
     * Retrieves the file path from a Uri.
     *
     * @param uri Uri of the selected file
     * @return The file path or null if not retrievable
     */
    private String getFilePath(Uri uri) {
        // You can use ContentResolver or FileProvider to get the path from the Uri
        // For simplicity, we use uri.getPath() here. Adjust accordingly based on URI scheme.
        return uri.getPath();
    }

    /**
     * Processes the selected file: reads it and updates progress.
     *
     * @param filePath Path to the selected file
     */
    private void processFile(String filePath) {
        // Show progress bar and start background task
        progressBar.setVisibility(View.VISIBLE);
        logTextView.append("\nProcessing file: " + filePath);

        File file = new File(filePath);
        int totalLines = countTotalLines(file);
        if (totalLines == -1) {
            showErrorMessage("Unable to read the file");
            return;
        }

        // Start file processing in the background
        executorService.submit(new FileProcessor(file, totalLines));
    }

    /**
     * Counts the total lines in the file.
     *
     * @param file File to count the lines in
     * @return The total number of lines or -1 if there is an error
     */
    private int countTotalLines(File file) {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            showErrorMessage("Error counting lines: " + e.getMessage());
            return -1;
        }
        return lineCount;
    }

    /**
     * Handles reading the file and updating progress.
     */
    private class FileProcessor implements Runnable {
        private File file;
        private int totalLines;

        FileProcessor(File file, int totalLines) {
            this.file = file;
            this.totalLines = totalLines;
        }

        @Override
        public void run() {
            int linesRead = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    linesRead++;
                    updateProgress(linesRead, totalLines, line);
                }
            } catch (IOException e) {
                showErrorMessage("Error reading file: " + e.getMessage());
            } finally {
                // Hide progress bar after reading is complete
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    /**
     * Updates the progress bar and logs the reading progress.
     *
     * @param linesRead Current number of lines read
     * @param totalLines Total lines in the file
     * @param currentLine The current line being processed
     */
    private void updateProgress(final int linesRead, final int totalLines, final String currentLine) {
        final int progress = (int) ((linesRead / (float) totalLines) * 100);
        final String logMessage = "Reading line " + linesRead + ": " + currentLine;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
                logTextView.append("\n" + logMessage);
            }
        });
    }

    /**
     * Shows an error message in the log and as a toast.
     *
     * @param message The error message to show
     */
    private void showErrorMessage(String message) {
        logTextView.append("\n" + message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up executor service
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
