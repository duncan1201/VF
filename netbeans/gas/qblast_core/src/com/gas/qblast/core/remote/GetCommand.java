package com.gas.qblast.core.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class GetCommand {


	
	// default values
	static FORMAT_TYPE DEFAULT_FORMAT_TYPE = FORMAT_TYPE.XML;
	static int DEFAULT_ALIGNMENTS = 500;
	static ALIGNMENT_VIEW DEFAULT_ALIGNMENT_VIEW = ALIGNMENT_VIEW.PAIRWISE;
	static int DEFAULT_DESCRIPTIONS = 500;

	// new data types for parameters
	enum FORMAT_TYPE {
		XML, HTML, TEXT
	};

	enum ALIGNMENT_VIEW {
		PAIRWISE, QueryAnchored, TABULAR
	};

	// parameters
	private int alignments;
	private FORMAT_TYPE formatType;
	private ALIGNMENT_VIEW alignmentView;
	private int descriptions;

	public GetCommand() {
		formatType = DEFAULT_FORMAT_TYPE;
		alignments = DEFAULT_ALIGNMENTS;
		alignmentView = DEFAULT_ALIGNMENT_VIEW;
		descriptions = DEFAULT_DESCRIPTIONS;
	}

	public int getAlignments() {
		return alignments;
	}

	public void setAlignments(int alignments) {
		this.alignments = alignments;
	}

	public FORMAT_TYPE getFormatType() {
		return formatType;
	}

	public void setFormatType(FORMAT_TYPE formatType) {
		this.formatType = formatType;
	}

	public ALIGNMENT_VIEW getAlignmentView() {
		return alignmentView;
	}

	public void setAlignmentView(ALIGNMENT_VIEW alignmentView) {
		this.alignmentView = alignmentView;
	}

	public String execute(PutResult putResult) {
		// ZW0NCRPB016
		String result = null;
		long waitedTime = 0;
		long pause = 0;
		final long MIN_PAUSE = 1000;
		float discount = 0.6f;
		String RID = putResult.getRID();


		DefaultHttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		HttpPost httpPost = new HttpPost(PutCommand.HOST);

		nvps.add(new BasicNameValuePair("ALIGNMENTS", Integer
				.toString(alignments)));
		nvps.add(new BasicNameValuePair("ALIGNMENT_VIEW", alignmentView
				.toString()));
		nvps.add(new BasicNameValuePair("CMD", "Get"));
		nvps.add(new BasicNameValuePair("DESCRIPTIONS", Integer
				.toString(descriptions)));
		nvps.add(new BasicNameValuePair("FORMAT_TYPE", formatType.toString()));
		nvps.add(new BasicNameValuePair("RID", RID));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			boolean waiting = true;
			while(waiting){
				pause = (long)((putResult.getRTOE() * 1000 - waitedTime) * discount);
				pause = Math.max(MIN_PAUSE, pause);
				//System.out.println("Waiting for "+pause);
				Thread.currentThread().sleep(pause);
				waitedTime += pause;
				HttpResponse response = httpclient.execute(httpPost);
				result = EntityUtils.toString(response.getEntity());
				if (result.indexOf("Status=WAITING") < 0) {
					waiting = false;
				}else{
				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void main(String[] args) {
		GetCommand get = new GetCommand();
		PutResult putResult = new PutResult();
		putResult.setRID("ZVWXS4ZM013"); // ZW0NCRPB016
		// ZVWXS4ZM013
		String ret = get.execute(putResult);
		System.out.println(ret);
	}
}
