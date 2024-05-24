package Last;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
// URL, URLConnection クラスの属するパッケージ
import java.net.URL;
import java.net.URLConnection;
public class NewsReader {
	private URL url;
	private String content;
	public NewsReader(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public void read() {
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while((line = reader.readLine())!=null) {
				stringBuilder.append(line).append("\n");
			}
			
			content = stringBuilder.toString();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public String getContent() {
		return content;
	}
	public void disPlayContent() {
		System.out.println(content);
	}
}
