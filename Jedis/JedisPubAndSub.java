package redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * jedis模拟实现发布订阅机制
 * 订阅者
 * @author wangtd
 *
 */

//这里不能用“单例”，发布者和订阅者属于不同的对象
class SingleJedis{
	private static Jedis jedis = new Jedis("127.0.0.1",6379,123456);
	private SingleJedis(){}
	public static Jedis getJedisInstance(){
		return jedis;
	}
}




class Subscriber{
	public Jedis subscriber;
		
	public Subscriber(Jedis jedis){
		this.subscriber = jedis;
	}
}


class PubAndSub extends JedisPubSub{

	@Override
	public void onMessage(String channel, String message) {
		// TODO Auto-generated method stub
		System.out.println("onMessage: channel:" + channel + "--  message:" + message);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("onMessage: pattern:" + pattern + "subscribedChannels:" + subscribedChannels);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("onMessage: channel:" + channel + "subscribedChannels:" + subscribedChannels);
	}
	
}

public class JedisPubAndSub {
	
	public static String channel = "Mari";
	
	public static void main(String[] args) {
		
		//使用redis pool获取jedis对象
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(10);
		JedisPool pool = new JedisPool(jedisPoolConfig,"localhost",6379,123456);
		
		Jedis subscriber = pool.getResource();
		
		//订阅者订阅信息
		PubAndSub pubAndSub = new PubAndSub();
		pubAndSub.proceed(subscriber.getClient(), channel);
		System.out.println("客户：" + subscriber.getClient().toString() + "订阅了频道：" + channel);
	}
	
}
