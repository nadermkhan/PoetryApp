package nader.app.literature.poetry;
public class Word {
	private String word;
	private String meaning;
	private String bangla;  // Add this field if needed
	
	public Word(String word, String meaning, String bangla) {
		this.word = word;
		this.meaning = meaning;
		this.bangla = bangla;  // Initialize
	}
	
	public String getWord() { return word; }
	public String getMeaning() { return meaning; }
	public String getBangla() { return bangla; }  // FIX: Add this method
}