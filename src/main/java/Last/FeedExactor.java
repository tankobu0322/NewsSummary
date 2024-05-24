package Last;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class FeedExactor {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("キーワードを入力してください。");
    	String keyword = scanner.nextLine();
    	//主要トピックス
        Feed feed = new Feed("https://news.yahoo.co.jp/rss/topics/top-picks.xml", "utf-8");
        //国内トピックス
        Feed feed2 = new Feed("https://news.yahoo.co.jp/rss/topics/domestic.xml", "utf-8");
        //国際トピックス
        Feed feed3 = new Feed("https://news.yahoo.co.jp/rss/categories/world.xml", "utf-8");
        //経済トピックス
        Feed feed4 = new Feed("https://news.yahoo.co.jp/rss/topics/business.xml", "utf-8");
        //エンターテイメントトピックス
        Feed feed5 = new Feed("https://news.yahoo.co.jp/rss/topics/entertainment.xml", "utf-8");
        //スポーツトピックス
        Feed feed6 = new Feed("https://news.yahoo.co.jp/rss/topics/sports.xml", "utf-8");
        //ITトピックス
        Feed feed7 = new Feed("https://news.yahoo.co.jp/rss/topics/it.xml", "utf-8");
        //科学トピックス
        Feed feed8 = new Feed("https://news.yahoo.co.jp/rss/topics/science.xml", "utf-8");
        ArrayList<Item> itemList1 = feed.getItemList(keyword);
        ArrayList<Item> itemList2 = feed2.getItemList(keyword);
        ArrayList<Item> itemList3 = feed3.getItemList(keyword);
        ArrayList<Item> itemList4 = feed4.getItemList(keyword);
        ArrayList<Item> itemList5 = feed5.getItemList(keyword);
        ArrayList<Item> itemList6 = feed6.getItemList(keyword);
        ArrayList<Item> itemList7 = feed7.getItemList(keyword);
        ArrayList<Item> itemList8 = feed8.getItemList(keyword);
        
        //文章抽出
        String textToSummarize1 = printItemList(itemList1);
        String textToSummarize2 = printItemList(itemList2);
        String textToSummarize3 = printItemList(itemList3);
        String textToSummarize4 = printItemList(itemList4);
        String textToSummarize5 = printItemList(itemList5);
        String textToSummarize6 = printItemList(itemList6);
        String textToSummarize7 = printItemList(itemList7);
        String textToSummarize8 = printItemList(itemList8);
        
        // 要約APIのHTTPリクエスト
        String apiKey = "apiKEYだから秘密♡";
        int linenumber = 1; // 抽出文章数
        summarizeAndPrint(apiKey, textToSummarize1, linenumber);
        summarizeAndPrint(apiKey, textToSummarize2, linenumber);
        summarizeAndPrint(apiKey, textToSummarize3, linenumber);
        summarizeAndPrint(apiKey, textToSummarize4, linenumber);
        summarizeAndPrint(apiKey, textToSummarize5, linenumber);
        summarizeAndPrint(apiKey, textToSummarize6, linenumber);
        summarizeAndPrint(apiKey, textToSummarize7, linenumber);
        summarizeAndPrint(apiKey, textToSummarize8, linenumber);
    }

    public static String printItemList(ArrayList<Item> itemList) {
        StringBuilder result = new StringBuilder();
        int sentenceCount = 0;
        for (Item item : itemList) {
            NewsReader reader = new NewsReader(item.getLink());
            reader.read();
            String content = reader.getContent();
            String articleURL = extractArticleURL(content);
            if (articleURL != null) {
                String articleContent = extractArticleContent(articleURL);
                // 文章を「。」で区切って表示
                String[] sentences = splitSentences(articleContent);
                for (String sentence : sentences) {
                    result.append(sentence).append("。");
                    sentenceCount++;
                    if (sentenceCount >= 10) {
                        // 10文章に達したらストップ
                        return result.toString();
                    }
                }
            }
        }
        return result.toString();
    }
    
    private static void summarizeAndPrint(String apiKey, String textToSummarize, int linenumber) {
        // APIエンドポイント
        String apiEndpoint = "https://api.a3rt.recruit.co.jp/text_summarization/v1";
        HttpURLConnection connection = null; // ここで connection を宣言・初期化


        // HTTPリクエストを作成
        try {
            URL url = new URL(apiEndpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // リクエストボディの作成
            String urlEncodedText = URLEncoder.encode(textToSummarize, StandardCharsets.UTF_8);
            String requestBody = "apikey=" + apiKey +
                    "&sentences=" + urlEncodedText +
                    "&linenumber=" + linenumber;
            // リクエストボディを送信
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // レスポンスの読み取り
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // レスポンスをJSONとしてパース
                JsonArray summaryArray = parseSummary(response.toString());
                if (summaryArray != null) {
                    System.out.println("要約結果:");
                    for (JsonElement summaryElement : summaryArray) {
                        System.out.println(summaryElement.getAsString());
                    }
                } else {
                    System.out.println("要約結果がありません。");
                }
            }

            // 接続を閉じる
            connection.disconnect();
        } catch (IOException e) {
        	try {
                    // エラーレスポンスの読み取り
                    if (connection != null) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                            StringBuilder response = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            System.out.println("ニュースが見つからなかったよ");
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    // 接続を閉じる
                    if (connection != null) {
                        connection.disconnect();
                   }
                }
        }
        
    }
    private static JsonArray parseSummary(String jsonResponse) {
        try {
            // JSONレスポンスをオブジェクトに変換
            JsonElement jsonElement = JsonParser.parseString(jsonResponse);

            // "summary"キーの配列を取得
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonArray summaryArray = jsonObject.getAsJsonArray("summary");

            return summaryArray;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    
    private static String[] splitSentences(String text) {
        // 文末に「。」を含んでいる場合、それを保持しながら分割
        String[] sentences = text.split("。(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return sentences;
    }

    private static String extractArticleContent(String articleURL) {
        String articleContent = NewsArticleDisplayBody.findArticle(articleURL);
        return articleContent;
    }

    private static String extractArticleURL(String content) {
        String pattern = "https://news.yahoo.co.jp/articles/[a-zA-Z0-9]+";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }
}
