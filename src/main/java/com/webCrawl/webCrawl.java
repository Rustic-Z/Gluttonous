package com.webCrawl;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class webCrawl {
	
	public static void main(String args[]) {
		Scanner cin = new Scanner(System.in) ; 
		System.out.println("商家ID:") ; 
		String shopid = cin.next() ; 
		System.out.println("商品ID:") ; 
		String itemid = cin.next() ; 
		System.out.println("設定每頁評價數量:") ; 
		String pagesize = cin.next() ; 
		
		//实例化一个http请求客户端
		@SuppressWarnings("deprecation")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			//实例化一个httpget对象
			HttpGet httpget = new HttpGet("http://rate.1688.com/remark/offerDetail/rates.json?"
					+ "_input_charset=GBK&offerId="+itemid+"&page=1&pageSize=+"+pagesize+"&starLevel=&orderBy=date&"
							+ "semanticId=&showStat=1&content=1&t=1422953024588&memberId="+shopid+"&"
									+ "callback=jQuery17206406422895379364_1422953023603");
			
			//执行请求url并得到结果
			HttpResponse response = httpClient.execute(httpget);
			//取出返回结果码
			int resStatu = response.getStatusLine().getStatusCode();
			
			//返回结果状态码为200是成功
			if (resStatu == HttpStatus.SC_OK) {
				//得到请求返回结果对象
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String html = EntityUtils.toString(entity, "UTF-8") ;
					html = html.substring(html.indexOf("{"), html.length()) ;
					JSONObject json = JSONObject.parseObject(html);
					JSONArray comments = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("rates").toString());
					for (int i = 0 ; i < comments.size(); i++) {
						JSONObject obj = comments.getJSONObject(i);
						// 會員編號
						String member = obj.getString("member");
						// 累計採購
						String countQuantity = obj.getString("countQuantity");
						// 採購數量
						String quantity = obj.getString("quantity");
						
						JSONArray items = obj.getJSONArray("rateItem");
						JSONObject item = items.getJSONObject(0) ;
						// 星級
						String starLevel = item.getString("starLevel") ;
						// 時間
						String remarkTime = item.getString("remarkTime") ;
						// 評價內容
						String remarkContent = item.getString("remarkContent") ; 
						
						 System.out.println("會員編號:\t"+member); 
					     System.out.println("累計採購:\t"+countQuantity); 
					     System.out.println("採購數量:\t"+quantity); 
					     System.out.println("星級:\t"+starLevel); 
					     System.out.println("時間:\t"+remarkTime); 
					     System.out.println("評價內容:\t"+remarkContent); 
					     System.out.println("=========================="); 
					}
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
