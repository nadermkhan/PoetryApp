package nader.tool.lolpanel;

import android.content.Context;
import java.io.*;
import java.util.*;

public class FileUtils {
	public static void writeToFile(Context context, String fileName, List<String> data) {
		File file = new File(context.getExternalFilesDir(null), fileName);
		try (FileWriter writer = new FileWriter(file)) {
			for (String line : data) {
				writer.write(line + "\n");
			}
			} catch (IOException e) {
			e.printStackTrace();
		}
	}
}