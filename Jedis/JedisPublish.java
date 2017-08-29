package redis.jedis;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

/**
 * jedis模拟实现发布订阅机制
 * 发布者
 * @author wangtd
 *
 */
//publisher
class Publisher{
	public Jedis publisher;
	public static String message;
	
	public Publisher(Jedis jedis){
		this.publisher = jedis;
	}
	
	//向redis插入数据
	public String write2Redis(String key,TreeMap<String,String> dataMap){
		if(dataMap != null){
			//开启pipeline
			Pipeline pipline = publisher.pipelined();
			Iterator<Map.Entry<String,String>> it = dataMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> entry = it.next();
				//待插入的数据
				String temp = entry.getKey() + "-" + entry.getValue();
				pipline.lpush(key, temp);
				System.out.println(key + temp);
				message = message + temp + " ";
			}
			pipline.sync();
		}
		return message;
	}
	
	//发布者将消息发布出去
	public void publish(String channel,String message){
		if(message != null){
			publisher.publish(channel, message);
		}
	}
	
}

public class JedisPublish {
	
	public static TreeMap<String,String> initData; 
	public static String channel = "Mari";
	public static String key = "common";
	
	static {
		initData = new TreeMap<String,String>();
		initData.put("范冰冰", "范爷");
		initData.put("云南", "过桥米线");
		initData.put("威廉", "伊丽沙白");
	}
	
	public static void main(String[] args) {
		//使用redis pool获取jedis对象
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(10);
		JedisPool pool = new JedisPool(jedisPoolConfig,"localhost",6379,123456);
		
		Publisher publisher = new Publisher(pool.getResource());
		
		//发布者发布信息
		String message = publisher.write2Redis(key, initData);
		System.out.println("信息：" + message + "已经写入到redis...");
		publisher.publish(channel,message);
		System.out.println("发布者在频道：" + channel + "发布了信息：" + message);
	}
}
