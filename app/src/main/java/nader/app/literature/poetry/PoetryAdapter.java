package nader.app.literature.poetry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PoetryAdapter extends RecyclerView.Adapter<PoetryAdapter.ViewHolder> {
    private List<PoetryLine> poetryLines;

    public PoetryAdapter(List<PoetryLine> poetryLines) {
        this.poetryLines = poetryLines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poetry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PoetryLine line = poetryLines.get(position);
        holder.textViewLine.setText(line.getText());

        StringBuilder wordMeaningText = new StringBuilder();
        for (WordMeaning wordMeaning : line.getWords()) {
            wordMeaningText.append(wordMeaning.getWord()).append("\n").append(wordMeaning.getMeaning()).append("\n\n");
        }
        holder.textViewMeaning.setText(wordMeaningText.toString());
    }

    @Override
    public int getItemCount() {
        return poetryLines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLine, textViewMeaning;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewLine = itemView.findViewById(R.id.textViewLine);
            textViewMeaning = itemView.findViewById(R.id.textViewMeaning);
        }
    }
}
