import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import redis.clients.jedis.Jedis;


public class Extr {

	/**
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException  
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("E:/团队资料/关系抽取/vote_relation_weight_result_fnlp_150w-2000w.txt")), "utf-8"));
		
		String line = null;
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		HashMap<String, HashMap<String, Info>> map = new HashMap<String, HashMap<String,Info>>();
		
		int index = 0;
		
		while((line = bufferedReader.readLine())!= null){
			
			String [] array = line.split("INNER");
			
			index++;
			
			System.err.println("index:"+index+"\t"+line);
			
			if(array.length != 3){
				continue;
			}
			
		    double score = Double.parseDouble(array[1].substring(0, 4));
			
			String [] key_rels = array[0].split("DIV");
			
			String [] relArray = key_rels[1].split(" ");
			
			if(map.containsKey(key_rels[0])){
				HashMap<String, Info> relMap = map.get(key_rels[0]);
				
				for(String rel : relArray){
					
					if(relMap.containsKey(rel)){
						Info info = relMap.get(rel);
						info.setScore(info.getScore()+score);
						info.getIndex().add(array[2]);
					}else{
						Info info = new Info();
						HashSet<String> set = new HashSet<String>();
						set.add(array[2]);
						info.setRelname(rel);
						info.setIndex(set);
						info.setScore(score);
						relMap.put(rel,info);
					}
					
				}
				
				
			}else{
				for(String rel : relArray){
					HashMap<String, Info> relMap = new HashMap<String, Info>();
					Info info = new Info();
					HashSet<String> set = new HashSet<String>();
					set.add(array[2]);
					info.setRelname(rel);
					info.setIndex(set);
					info.setScore(score);
					relMap.put(rel, info);
					map.put(key_rels[0], relMap);
				}
				
			}
		}
		
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("E:/团队资料/关系抽取/output_1.txt"), true), "utf-8");
		
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator. next();
			HashMap<String, Info> relMap = map.get(key);
			Iterator<String> iterator2 = relMap.keySet().iterator();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(key+"#DIV#");
			while(iterator2.hasNext()){
				String key2 = iterator2.next();
				Info info = relMap.get(key2);
				stringBuffer.append(info.toString()+"|DIV|");
			}
			osw.append(stringBuffer+"\n");
		}
		osw.flush();
		osw.close();
		
	}

}
