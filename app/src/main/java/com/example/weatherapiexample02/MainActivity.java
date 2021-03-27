// 2021년 03월 26일
// 공공데이터 동네날씨 OpenAPI
// 언어: 자바
// 참고:https://cpcp127.tistory.com/16
// 1. total count 값에 따라 numofRows를 값을 바꾸면 더 많은 값들이 나온다.


/*
   * POP	강수확률	 %
   * PTY	강수형태	코드값
   * R06	6시간 강수량	범주 (1 mm)
   * REH	습도	 %
   * S06	6시간 신적설	범주(1 cm)
   * SKY	하늘상태	코드값 ##
   * T3H	3시간 기온	 ℃
   * TMN	아침 최저기온	 ℃  ##
   * TMX	낮 최고기온	 ℃  ##
   * UUU	풍속(동서성분)	 m/s  ##
   * VVV	풍속(남북성분)	 m/s  ##
   */



package com.example.weatherapiexample02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText edit;
    TextView text;

    // 요청 변수
    String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";
    String key = "iCKdg%2FFSCKfmWG%2BGZGuSq%2B2uRATW8O4ZGzIripft7t1PROAEUzQ%2BXXnJ0X6fzrajhHUqqrtVNFswK6auuT%2Flsw%3D%3D"; //라이선스 키
    String mpageNo = "1";
    String mnumbverOfRows = "255";
    String mdataType = "XML";
    String mbase_data = "20210326";
    String mbase_time = "0500";
    String mNX = "93";
    String mNY = "91";

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = (EditText)findViewById(R.id.edit);
        text = (TextView)findViewById(R.id.text);
    }

    //Button 클릭시 자동으로 호출되는 callback method
    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run(){
                        data = getXmlData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(data);
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    // 이제부터 json 파일 분석 갑니다.
    String getXmlData() {
        StringBuffer buffer = new StringBuffer(); // 출력될 값들을 한번에 보여주기 위해 사용.
        //StringBuilder urlBuilder = new StringBuilder(WEATHER_URL); /*URL*/
        //String queryUrl =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?serviceKey=" + key + "&pageNo=1&numOfRows=10&dataType=XML&base_date=20210325&base_time=0500&nx=93&ny=91";
        String queryUrl =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?serviceKey=" + key + "&pageNo=" + mpageNo + "&numOfRows=" + mnumbverOfRows + "&dataType=" + mdataType + "&base_date=" + mbase_data +"&base_time=" + mbase_time + "&nx=" + mNX + "&ny=" + mNY;

        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.

            InputStream is= url.openStream(); //url위치로 입력스트림 연결  오


            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();
            Log.d("파싱===>", String.valueOf(eventType));

            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
//                        case XmlPullParser.START_DOCUMENT:
//                           buffer.append("파싱시작...\n\n");
//                           //Log.d("파싱===>", "파싱시작 ~~~");
//                           break;

                        case  XmlPullParser.START_TAG:
                            tag= xpp.getName();
                            //Log.d("파싱===tag>", String.valueOf(tag));

                            if (tag.equals("item"));
                            else if(tag.equals("baseDate")){
                                buffer.append("금일 날짜 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("baseTime")){
                                buffer.append("기본 시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("category")){
                                buffer.append("날씨 형태 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("fcstDate")){
                                buffer.append("측정 날짜 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("fcstTime")){
                                buffer.append("측정 시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("fcstValue")){
                                buffer.append("측정 값 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("nx")){
                                buffer.append("격자X : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            else if(tag.equals("ny")){
                                buffer.append("격자Y : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }
                            break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName();

                        if(tag.equals("item")) buffer.append("\n");
                        break;
                }
                eventType =  xpp.next();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.append("파싱종료");
        Log.d("파싱===>", "파싱종료 ~~~");
        return  buffer.toString();

    }// getXmlData()

}// 처음괄호