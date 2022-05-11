import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    protected Map<String, List<PageEntry>> wordPlace;

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        wordPlace = new HashMap<>();
        for (File file : Objects.requireNonNull(pdfsDir.listFiles())) {
            var doc = new PdfDocument(new PdfReader(file));
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (String word : words) {
                    if (!word.isBlank()) {
                        freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
                    }
                }
                Set<Map.Entry<String, Integer>> currentPageWord = freqs.entrySet();
                for (Map.Entry<String, Integer> freqsEntry : currentPageWord) {
                    if (wordPlace.containsKey(freqsEntry.getKey())) {
                        wordPlace.get(freqsEntry.getKey()).add(new PageEntry(file.getName(), i, freqsEntry.getValue()));
                    } else {
                        List<PageEntry> currEntryList = new ArrayList<>();
                        currEntryList.add(new PageEntry(file.getName(), i, freqsEntry.getValue()));
                        wordPlace.put(freqsEntry.getKey(), currEntryList);
                    }
                }
            }
        }
        Set<String> keySet = wordPlace.keySet();
        for (String e : keySet) {
            wordPlace.get(e).sort(Comparator.reverseOrder());
        }
    }

    @Override
    public List<PageEntry> search(String word) throws IOException {
        if (wordPlace.containsKey(word)) {
            return wordPlace.get(word);
        } else {
            return Collections.emptyList();
        }
    }
}