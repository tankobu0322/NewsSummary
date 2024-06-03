# 概要 
Javaのシェルから受け付けた入力値をタイトルに含むニュースを  
Yahoo!ニュースフィードから探し  
該当するニュースの本文記事を抽出し、Text Summarization APIに要約させて  
シェルに表示するものです

# 使い方
![image](https://github.com/tankobu0322/NewsSummary/assets/170719141/45783c55-c410-4655-a701-6c3f137eaa08)  
調べたい事柄のキーワードを入力してエンターを押すと  
![image](https://github.com/tankobu0322/NewsSummary/assets/170719141/2cb85626-f7fa-44f6-b002-56b8dc414bb8)  
ジャンルごとに選定した8つのフィードにキーワードを投げて  
マッチすると要約が表示されます  
(時間がなかったので、マッチしなかったときにもう一度入力させる機構や, while文での繰り返しは実装してません)

