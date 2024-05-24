package Last;

public class Item {
		/** title要素 */
		private String title;
		/** link要素 */
		private String link;
		/** comments要素 */
		private String comments;
		/**
		 *  コンストラクタ
		 *  @param title タイトル
		 *  @param link リンク
		 *  @param description 説明の文章
		 */
		public Item(String title, String link, String comments) {
			this.title = title;
			this.link = link;
			this.comments = comments;
		}
		/**
		 *  title要素の内容である item のタイトルを返す
		 *  @return タイトル
		 */
		public String getTitle() {
			return title;
		}
		/**
		 *  link要素の内容である URL を文字列で返す
		 *  @return リンク先のURL
		 */
		public String getLink() {
			return link;
		}
		
		public String getComments() {
			return comments;
		}
		/**
		 *  この item 要素の文字列表現を返す
		 *  @return この item 要素の文字列表現
		 */
		@Override
		public String toString() {
			return "title: " + title + "\nlink: " + link +"\ncomments:"+comments;
		}
	}

