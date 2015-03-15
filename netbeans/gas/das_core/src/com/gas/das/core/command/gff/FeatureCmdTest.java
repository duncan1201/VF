package com.gas.das.core.command.gff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gas.das.core.Seg;
import com.gas.das.core.command.type.DasTypes;
import com.gas.das.core.command.type.DasTypesParser;

public class FeatureCmdTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int typeCount = 0;
		List<String> typeList = new ArrayList<String>();
		DasTypesParser parser = new DasTypesParser();
		DasTypes dasTypes = parser.parse(DasTypesParser.class, "types_response_hg19.xml");
		DasTypes.Gff gff = dasTypes.getGff();
		Set<DasTypes.Segment> segments = gff.getSegments();
		for(DasTypes.Segment seg: segments){
			String version = seg.getVersion();
			//System.out.println(version);
			Set<DasTypes.Type> types = seg.getTypes();
			for(DasTypes.Type type: types){
				typeCount++;
				if(typeList.size() < 50){
					typeList.add(type.getId());
				}
				//System.out.println(type.getId()+ "\t" + type.getCategory() + "\t" + type.getMethod());
			}
		}
		System.out.println("typeCount="+typeCount);
		System.out.print("");
		
		FeatureCmd cmd = new FeatureCmd();
		cmd.setTypes(typeList);
		cmd.getSegments().add(new Seg("21", 33038597,33041570));
		//cmd.setCategorize(true);
		cmd.execute();
	}

}
