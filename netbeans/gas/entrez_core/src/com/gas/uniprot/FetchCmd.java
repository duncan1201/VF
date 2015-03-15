package com.gas.uniprot;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.openide.util.Lookup;

public class FetchCmd {
	
	private String host = "http://www.uniprot.org/uniprot";
	
	private String format ;
	private String id;
	
	private static final String ALLOWED_FORMATS = "txt,xml,rdf,fasta,gff";

	
	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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

		HttpGet httpGet = internetSvc.createHttpRequest(host + "/" + getId() + "." + getFormat(), HttpGet.class);

		HttpResponse response = null;
		String entityToString = null;

		try {
	
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();

			entityToString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.net.UnknownHostException e){
                    internetSvc.displayConnProblem(true);
                } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
                    HttpUtil.closeQuitely(httpClient);
                }

		return entityToString;
	}
}
