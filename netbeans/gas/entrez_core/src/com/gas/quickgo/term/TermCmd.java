package com.gas.quickgo.term;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openide.util.Lookup;


/**
 * Sample java client to demonstrate downloading information about a GO Term
 *
 */

public class TermCmd {
	
	private String host = "http://www.ebi.ac.uk/QuickGO/GTerm";
	private String id;
	private String format ;

	private final String ALLOWED_FORMATS = "mini,obo,oboxml";
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String execute(){
		if(getFormat() == null){
			throw new IllegalArgumentException("Format cannot be null");
		}
		if(ALLOWED_FORMATS.indexOf(getFormat().toLowerCase()) < 0){
			throw new IllegalArgumentException("Only the following formats are allowed: " + ALLOWED_FORMATS);
		}
		if(getId() == null){
			throw new IllegalArgumentException("Id cannot be null");
		}		
                IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
		CloseableHttpClient httpClient = internetSvc.createHttpClient();

		HttpPost httpPost = internetSvc.createHttpRequest(host);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("id", getId()));
		nvps.add(new BasicNameValuePair("format", getFormat()));
		
		HttpResponse response = null;
		String entityToString = null;

		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
					Consts.UTF_8);
			httpPost.setEntity(formEntity);
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			entityToString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.net.UnknownHostException e) {
                        internetSvc.displayConnProblem(true);
		} catch (IOException e) {
		} finally{
                    HttpUtil.closeQuitely(httpClient);
                }

		return entityToString;
	}

}
