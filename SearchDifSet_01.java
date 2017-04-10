package common.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;  
import java.util.LinkedList;

import utils.FileUtil;

/**
已经实现了布隆过滤器，但是由于数据量过大，即使在调整jvm内存后依然存在内存溢出现象
后面的版本决定改进读取策略，这个版本是保留版本
*/

class SimpleHash {//这玩意相当于C++中的结构体  
	  
    private int cap;  
    private int seed;  
  
    public  SimpleHash(int cap, int seed) {  
        this.cap = cap;  
        this.seed = seed;  
    }  
  
    public int hash(String value) {//字符串哈希，选取好的哈希函数很重要  
        int result = 0;  
        int len = value.length();  
        for (int i = 0; i < len; i++) {  
            result = seed * result + value.charAt(i);  
        }  
        return (cap - 1) & result;  
    }  
}  

class BloomFilter{
	
	private static final int DEFAULT_SIZE = 2 << 24;//布隆过滤器的比特长度  
    private static final int[] seeds = {3,5,7, 11, 13, 31, 37, 61};//这里要选取质数，能很好的降低错误率 8  
    private static BitSet bits = new BitSet(DEFAULT_SIZE);  
    private static SimpleHash[] func = new SimpleHash[seeds.length];
	
    public BloomFilter(){
    	init();
    }
    
    public void init(){
    	for(int i=0;i<seeds.length;i++){
    		func[i] = new SimpleHash(DEFAULT_SIZE,seeds[i]);
    	}
    }
    
    public void addValue(String value)  
    {  
        for(SimpleHash f : func){//将字符串value哈希为8个或多个整数，然后在这些整数的bit上变为1 
            bits.set(f.hash(value),true);
        }
    }  
      
    public void add(String value)  
    {  
        if(value != null) addValue(value);  
    } 
    
    public boolean contains(String value)  
    {  
        if(value == null) return false;  
        boolean ret = true;  
        for(SimpleHash f : func)//这里其实没必要全部跑完，只要一次ret==false那么就不包含这个字符串  
            ret = ret && bits.get(f.hash(value));  
        return ret;  
    }  
    
}


public class SearchDifSet {
	
	public static FileUtil fileUtil = new FileUtil();
	
	public static String inFullSetPath = "D:\\workstation\\ifengDataPlayground\\txtpool\\All-15554627590856.txt";
//	public static String inFullSetPath = "D:\\workstation\\ifengDataPlayground\\txtpool\\part.txt";
	public static String inPartSetPath = "D:\\workstation\\ifengDataPlayground\\txtpool\\All-false-329043826423715.txt";
//	public static String inPartSetPath = "D:\\workstation\\ifengDataPlayground\\txtpool\\full.txt";
	
	public static String outDifSetPath = "D:\\workstation\\ifengDataPlayground\\cache\\dif_set_20170407.txt";
	
	public static LinkedList<String> resultList = new LinkedList<String>(); 
	
	public static ArrayList<String> getCleanFullSetList(String str){
		String[] strArr = str.split("\n+");
		ArrayList<String> resultList = new ArrayList<String>();
		for(int i=0;i<strArr.length;i++){
			String[] itemSplitArr = strArr[i].trim().split("\\s+");
			if(itemSplitArr.length==2){
				resultList.add(itemSplitArr[0]);
			}
		}
		return resultList;
	}
	
	public static String[] getInputPartSetList(String str){
		String[] strArr = str.split("\n+");
		return strArr;
	}
	
	public static String createOutputContent(LinkedList<String> resultList){
		StringBuffer content = new StringBuffer();
		for(String item3:resultList){
			content.append(item3.trim()+"\n");
		}
		return content.toString();
	}
	
	public static void main(String[] args) {
		
		String inFullSetStr = fileUtil.Read(inFullSetPath);
		String inPartSetStr = fileUtil.Read(inPartSetPath);
		
		String[] cleanFullSetList = getInputPartSetList(inFullSetStr);
		
		ArrayList<String> inPartSetList = getCleanFullSetList(inPartSetStr);
		
		BloomFilter blFilter = new BloomFilter();
		
		//初始化被查询数组
		for(String item:cleanFullSetList){
			blFilter.add(item.trim());
		}
		
		//查找补集数据
		for(String item2:inPartSetList){
			if(!blFilter.contains(item2.trim())){
				resultList.add(item2);
			}
		}
		
		//保存补集数据
		String content = createOutputContent(resultList);
		fileUtil.Save(outDifSetPath, content);
		
	}
	
}
