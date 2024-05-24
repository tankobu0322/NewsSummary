package Last;

import javax.swing.JFrame;

public class NewsArticleSummarizationDisplayGui extends JFrame{
	public static void main(String args[]) {
		JFrame w = new NewsArticleSummarizationDisplayGui("NEWS SUMMARIZE");
		w.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		w.setSize(600,600);
		w.setVisible(true);
	}
	
	public NewsArticleSummarizationDisplayGui(String title) {
		super(title);
		
	}
}
