package nader.app.literature.poetry;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    public static List<Poetry> loadPoetry(Context context) {
		List<Poetry> poetryList = new ArrayList<>();
		
		try {
			InputStream is = context.getAssets().open("words.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			
			String json = new String(buffer, StandardCharsets.UTF_8);
			JSONArray jsonArray = new JSONArray(json); // FIX: Using JSONArray instead of JSONObject
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String text = jsonObject.getString("text");
				JSONArray wordsArray = jsonObject.getJSONArray("words");
				
				List<Word> words = new ArrayList<>();
				for (int j = 0; j < wordsArray.length(); j++) {
					JSONObject wordObject = wordsArray.getJSONObject(j);
					words.add(new Word(wordObject.getString("word"), wordObject.getString("meaning"), wordObject.getString("bangla")));
				}
				
				poetryList.add(new Poetry(text, words));
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
		
		return poetryList;
	}
}
