package nader.app.literature.poetry;
package nader.app.literature.poetry;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private final Context context;
    private final List<Word> words;

    public WordAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.word_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = words.get(position);
        String wordText = word.getText();
        String meaningEn = word.getMeaningEn();
        String meaningBn = word.getMeaningBn();

        // Format Bangla meaning with Bold & Underline
        SpannableString banglaSpannable = new SpannableString(meaningBn);
        banglaSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, meaningBn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        banglaSpannable.setSpan(new UnderlineSpan(), 0, meaningBn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set text with formatted meaning
        holder.wordText.setText(wordText);
        holder.meaningText.setText(meaningEn + " - ");
        holder.meaningText.append(banglaSpannable);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordText, meaningText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordText = itemView.findViewById(R.id.word_text);
            meaningText = itemView.findViewById(R.id.meaning_text);
        }
    }
}
