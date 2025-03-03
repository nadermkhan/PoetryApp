package nader.app.literature.poetry;
import java.util.List;

public class Poetry {
	private String text;
	private List<Word> words;
	
	public Poetry(String text, List<Word> words) {
		this.text = text;
		this.words = words;
	}
	
	public String getText() { return text; }
	public List<Word> getWords() { return words; }
}