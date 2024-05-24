package Last;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class NewsArticleDisplayBody {
	private record Score(int vol, double score, Element elm) {
		double rate() {
			if(vol == 0) return 0;
			return score * score/vol;
		}
	}
	private static final double RATE = 0.7;
	private static final Map<String,Double> customRate = Map.of("p",1.,"li",0.3);
	private static final int MAX_LINE_LENGTH = 200;
	private Score longest;
	public static String splitText(String text) {
		StringBuilder result = new StringBuilder();
		int length = text.length();
		for(int i=0; i < length; i +=MAX_LINE_LENGTH) {
			int endIndex = Math.min(i+MAX_LINE_LENGTH, length);
			result.append(text,i,endIndex).append("\n");
		}
		return result.toString();
	}
	NewsArticleDisplayBody() {
		
	}
	private Element findLongest(Element elm) {
		longest = null;
		calc(elm);
		return longest == null ? null: longest.elm();
	}
	private Score calc(Element elm) {
		 int textlen = elm.ownText().length();
	        double score = textlen;
	        for (Element child : elm.children()) {
	            var chScore = calc(child);
	            textlen += chScore.vol();
	            score += chScore.score() * 
	                customRate.getOrDefault(child.tagName().toLowerCase(), RATE);
	        }
	        var result = new Score(textlen, score, elm);
	        if (longest == null || longest.rate() < result.rate()) {
	            longest = result;
	        }
	        return result;
	}
    public static String findArticle(String url) {
        try {
            var doc = Jsoup.connect(url).get();
            var body = doc.body();
            var elm = findArticleElement(body);
            return elm == null ? "" : elm.text();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    public static Element findArticleElement(Element elm) {
        var fa = new NewsArticleDisplayBody() ;
        return fa.findLongest(elm);
    }
}
