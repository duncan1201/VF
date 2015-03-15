package com.gas.qblast.core.tmp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class TaxTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		extracProt();
		simplify();
		analyzeCounter();
	}
	
	private static void simplify() throws IOException{
		Properties p = new Properties();
		FileInputStream fis = new FileInputStream("D:\\downloads\\taxonomy\\taxdmp\\names.dmp");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		String line ;
		while((line = reader.readLine()) != null){
			String[] tmp = line.split("\\|");
			if(tmp.length == 0 || tmp[0].trim().equals("") || tmp[1].trim().equals("")){
				System.out.print("");
			}
			if(counter.containsKey(tmp[0].trim()) && counter.get(tmp[0].trim()) > 500){
				p.setProperty(tmp[0].trim(), tmp[1].trim());
			}
		}
		//System.out.println("pro Tax with names:"+p.size());
		FileOutputStream fos = new FileOutputStream("D:\\tmp\\primer3\\tax.txt");
		p.store(fos, "comments");		
	}
	static Map<String, Integer> counter;
	private static void extracProt() throws IOException{
		counter = new HashMap<String, Integer>((int)(391236 /0.75) + 300);
		FileInputStream fis = new FileInputStream("D:\\downloads\\taxonomy\\gi_taxid_prot.dmp");
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
		String line ;
		int i = 1;
		while((line = reader.readLine()) != null){
			String[] tmp = line.split("\\s");
				//System.out.println(tmp[0]);
				//System.out.println(tmp[1]);
				//if(i % 100 == 0)
				//System.out.println(i);
				i++;
				String trim = tmp[1].trim();
				if(!counter.containsKey(trim)){
					counter.put(trim, 1);
				}
				counter.put(trim, counter.get(trim)+1);
		}
		System.out.println("Pro Tax="+counter.size());
	}
	private static void analyzeCounter(){
		Map<String, Integer> analysis = new LinkedHashMap<String, Integer>();
		analysis.put("<5", 0);
		analysis.put("5-10", 0);
		analysis.put("10-20", 0);
		analysis.put("20-30", 0);
		analysis.put("30-50", 0);
		analysis.put("50-100", 0);
		analysis.put("100-500", 0);
		analysis.put(">500", 0);
		Iterator<String> keys = counter.keySet().iterator();
		while(keys.hasNext()){
			String t = keys.next();
			int count = counter.get(t);
			if(count < 5){
				analysis.put("<5", analysis.get("<5")+1);
			}else if(count < 10){
				analysis.put("5-10", analysis.get("5-10")+1);
			}else if(count < 20){
				analysis.put("10-20", analysis.get("10-20")+1);
			}else if(count < 30){
				analysis.put("20-30", analysis.get("20-30")+1);
			}else if(count < 50){
				analysis.put("30-50", analysis.get("30-50")+1);
			}else if(count < 100){
				analysis.put("50-100", analysis.get("50-100")+1);
			}else if(count < 500){
				analysis.put("100-500", analysis.get("100-500")+1);				
			}else{
				analysis.put(">500", analysis.get(">500")+1);
			}
			
			if(t.equals("9913")){
				System.out.println("9913:"+counter.get("9913"));
			}
		}
		
		keys = analysis.keySet().iterator();
		while(keys.hasNext()){
			String t = keys.next();
			System.out.println(t + ":" + analysis.get(t));
		}
	}

}
