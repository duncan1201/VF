package com.gas.uniprot;

import com.gas.common.ui.proxysetting.api.IProxyInternetService;
import com.gas.common.ui.util.HttpUtil;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.openide.util.Lookup;

public class QueryCmd {

	private String host = "http://www.uniprot.org/uniprot/";
	private String query;
	private String format;
	private Boolean include;
	private Integer limit;
	private Integer offset;
	private Boolean compress;
	private List<String> columns = new ArrayList<String>();
	
	private final String ALLOWED_FORMATS = "html | tab | xls | fasta | gff | txt | xml | rdf | list | rss";
	private final String ALLOWED_COLUMNS = "citation | clusters | comments | database | domains | domain | ec | id | entry name | existence | families | features | genes | go | go-id | interpro | interactor | keywords | keyword-id | last-modified | length | organism | organism-id | pathway | protein names | reviewed | score | sequence | 3d | subcellular locations | taxon | tools | version | virus hosts";
	
	
	
	public String getHost() {
		return host;
	}



	public void setHost(String host) {
		this.host = host;
	}



	public String getQuery() {
		return query;
	}



	public void setQuery(String query) {
		this.query = query;
	}



	public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
	}



	public Boolean getInclude() {
		return include;
	}



	public void setInclude(Boolean include) {
		this.include = include;
	}



	public Integer getLimit() {
		return limit;
	}



	public void setLimit(Integer limit) {
		this.limit = limit;
	}



	public Integer getOffset() {
		return offset;
	}



	public void setOffset(Integer offset) {
		this.offset = offset;
	}



	public Boolean getCompress() {
		return compress;
	}



	public void setCompress(Boolean compress) {
		this.compress = compress;
	}



	public List<String> getColumns() {
		return columns;
	}



	public void setColumns(List<String> columns) {
		this.columns = columns;
	}



	public String execute(){
		if(getFormat() == null){
			throw new IllegalArgumentException("Format cannot be null");
		}
		if(ALLOWED_FORMATS.indexOf(getFormat().toLowerCase()) < 0){
			throw new IllegalArgumentException("Only the following formats are allowed: " + ALLOWED_FORMATS);
		}
		if(getQuery() == null){
			throw new IllegalArgumentException("Query cannot be null");
		}		
                IProxyInternetService internetSvc = Lookup.getDefault().lookup(IProxyInternetService.class);
		CloseableHttpClient httpClient = internetSvc.createHttpClient();
		
		HttpHost httpHost = new HttpHost("http://www.uniprot.org");
		
		HttpGet httpGet = new HttpGet("/uniprot/?");

		httpGet.getParams().setParameter("query", getQuery());
		httpGet.getParams().setParameter("format", getFormat());
		if(getInclude()!=null){
			httpGet.getParams().setParameter("include", getInclude()?"yes":"no");
		}
		if(getCompress()!=null){
			httpGet.getParams().setParameter("compress", getCompress()?"yes":"no");
		}
		if(getLimit() != null){
			httpGet.getParams().setParameter("limit", getLimit());
		}
		if(getOffset() != null){
			httpGet.getParams().setParameter("offset", getOffset());
		}
		if(getColumns().size() > 0){
			StringBuffer columnStr = new StringBuffer();
			
			for(String col: columns){
				if(columnStr.length() > 0){
					columnStr.append(",");
				}
				columnStr.append(col);
			}
			if(columnStr.length() > 0){
				httpGet.getParams().setParameter("columns", columnStr.toString());
			}
		}
				
		HttpResponse response = null;
		String entityToString = null;

		try {
			response = httpClient.execute(httpHost, httpGet);
			
			HttpEntity entity = response.getEntity();

			entityToString = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
                    HttpUtil.closeQuitely(httpClient);
                }

		return entityToString;
	}

	
}
