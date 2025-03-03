package nader.app.literature.poetry;

import java.util.List;

public class PoetryLine {
    private String text;
    private List<WordMeaning> words;

    public PoetryLine(String text, List<WordMeaning> words) {
        this.text = text;
        this.words = words;
    }

    public String getText() {
        return text;
    }

    public List<WordMeaning> getWords() {
        return words;
    }
}
