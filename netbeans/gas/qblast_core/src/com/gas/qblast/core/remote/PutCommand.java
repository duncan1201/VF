package com.gas.qblast.core.remote;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.gas.qblast.core.parameter.BlastParams;

public class PutCommand {

	public static final String HOST = "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi";

	public PutResult execute(BlastParams params)
			throws IllegalArgumentException {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(HOST);

		List<NameValuePair> nvps = getNameValuePairs(params);

		PutResult ret = null;
		String line = null;
		try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps,
					HTTP.UTF_8);
			httpPost.setEntity(formEntity);
			HttpResponse response = httpclient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity());

			BufferedReader reader = new BufferedReader(new StringReader(result));

			line = reader.readLine();
			ret = new PutResult();
			boolean infoBegin = false;
			while (line != null) {
				line = line.trim();
				if (infoBegin && line.indexOf("RID") > -1) {
					StringTokenizer tokenizer = new StringTokenizer(line, "=");
					if (tokenizer.countTokens() != 2) {
						System.out.println(result);
						throw new Exception("No RID found from NCBI!");
					} else {
						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken();
							if (!token.equals("RID")) {
								ret.setRID(token);
							}
						}
					}
				} else if (infoBegin && line.indexOf("RTOE") > -1) {
					StringTokenizer tokenizer = new StringTokenizer(line, "= ");
					while (tokenizer.hasMoreTokens()) {
						String token = tokenizer.nextToken();
						if (!token.equals("RTOE")) {
							ret.setRTOE(new Integer(token));
							break;
						}
					}
				} else if (line.indexOf("<!--QBlastInfoBegin") > -1) {
					infoBegin = true;
				}

				line = reader.readLine();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;

	}

	private List<NameValuePair> getNameValuePairs(BlastParams params) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("AUTO_FORMAT", "Off"));
		if (params.getBlastPrograms() != null)
			nvps.add(new BasicNameValuePair("BLAST_PROGRAMS", params
					.getBlastPrograms().toString()));
		nvps.add(new BasicNameValuePair("CLIENT", "web"));
		nvps.add(new BasicNameValuePair("CMD", "Put"));

		if (params.getCompositionBasedStatistics() != null) {
			nvps.add(new BasicNameValuePair("COMPOSITION_BASED_STATISTICS",
					params.getCompositionBasedStatistics().toString()));
		}
		if (params.getDatabase() == null) {
			throw new IllegalArgumentException("Database cannot null");
		}
		nvps.add(new BasicNameValuePair("DATABASE", params.getDatabase()
				.toString()));
		nvps.add(new BasicNameValuePair("DB_GENETIC_CODE", "1"));
		if (params.getEqText() != null) {
			nvps.add(new BasicNameValuePair("EQ_TEXT", params.getEqText()));
		}
		nvps.add(new BasicNameValuePair("EXPECT", Integer.toString(params
				.getExpect())));
		if (params.getExcludeModels() != null) {
			nvps.add(new BasicNameValuePair("EXCLUDE_MODELS", params
					.getExcludeModels().toString()));
		}

		if (params.getFilterLowComplexity() != null
				|| params.getFilterMask() != null || params.getFilterRepeats() != null) {
			StringBuffer ret = new StringBuffer();
			
			ret.append(params
					.getFilterLowComplexity()==null?"":params
							.getFilterLowComplexity().toString()+";");
			
			ret.append(params
					.getFilterMask()==null?"":params
							.getFilterMask().toString()+";");
			ret.append(params
					.getFilterRepeats()==null?"":params
							.getFilterRepeats().toString()+";");

			
			BasicNameValuePair vp = new BasicNameValuePair("FILTER", ret.toString());
			nvps.add(vp);
		}
		if (params.getGapCost() != null)
			nvps.add(new BasicNameValuePair("GAPCOSTS", params.getGapCost()
					.toString()));
		if (params.getHspRangeMax() != null)
			nvps.add(new BasicNameValuePair("HSP_RANGE_MAX", params
					.getHspRangeMax().toString()));
		if (params.getLcaseMask() != null)
			nvps.add(new BasicNameValuePair("LCASE_MASK", params.getLcaseMask()
					.toString()));
		if (params.getMatrixName() != null) {
			nvps.add(new BasicNameValuePair("MATRIX_NAME", params
					.getMatrixName().toString()));
		}
		if (params.getMaxNumSeq() != null) {
			nvps.add(new BasicNameValuePair("MAX_NUM_SEQ", params
					.getMaxNumSeq().toString()));
		}
		if (params.getMatchScores() != null) {
			nvps.add(new BasicNameValuePair("MATCH_SCORES", params
					.getMatchScores().toString()));
		}
		/*
		 * nvps.add(new BasicNameValuePair("NUCL_PENALTY",
		 * Integer.toString(params .getNuclPenalty()))); nvps.add(new
		 * BasicNameValuePair("NUCL_REWARD", Integer.toString(params
		 * .getNuclReward())));
		 */
		nvps.add(new BasicNameValuePair("PROGRAM", params.getProgram()
				.toString()));
		nvps.add(new BasicNameValuePair("QUERY", params.getQuery()));
		nvps.add(new BasicNameValuePair("QUERY_FROM", Integer.toString(params
				.getQueryFrom())));
		nvps.add(new BasicNameValuePair("QUERY_TO", Integer.toString(params
				.getQueryTo())));
		if (params.getRepeats() != null) {
			nvps.add(new BasicNameValuePair("REPEATS", params.getRepeats()
					.toString()));
		}
		// nvps.add(new BasicNameValuePair("SERVICE", "plain") );
		if (params.getShortQueryAdjust() != null)
			nvps.add(new BasicNameValuePair("SHORT_QUERY_ADJUST", params
					.getShortQueryAdjust().toString()));
		nvps.add(new BasicNameValuePair("WORD_SIZE", Integer.toString(params
				.getWordSize())));
		return nvps;
	}
}
