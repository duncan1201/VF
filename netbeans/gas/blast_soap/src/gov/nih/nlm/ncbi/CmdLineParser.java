/*  $Id: CmdLineParser.java,v 1.1 2007/10/09 21:02:45 camacho Exp $
 * ===========================================================================
 *
 *                            PUBLIC DOMAIN NOTICE
 *               National Center for Biotechnology Information
 *
 *  This software/database is a "United States Government Work" under the
 *  terms of the United States Copyright Act.  It was written as part of
 *  the author's official duties as a United States Government employee and
 *  thus cannot be copyrighted.  This software/database is freely available
 *  to the public for use. The National Library of Medicine and the U.S.
 *  Government have not placed any restriction on its use or reproduction.
 *
 *  Although all reasonable efforts have been taken to ensure the accuracy
 *  and reliability of the software and data, the NLM and the U.S.
 *  Government do not and cannot warrant the performance or results that
 *  may be obtained by using this software or data. The NLM and the U.S.
 *  Government disclaim all warranties, express or implied, including
 *  warranties of performance, merchantability or fitness for any particular
 *  purpose.
 *
 *  Please cite the author in any work or product based on this material.
 *
 * ===========================================================================
 */

package gov.nih.nlm.ncbi;


/** Auxiliary class to parse the command line for this demo program 
 * 
 * @author Christiam Camacho
 */
public class CmdLineParser {

    /** Was the submit search lite command line option given? */
    private boolean m_SubmitSearchLite = false;
    /** Was the submit search command line option given? */
    private boolean m_SubmitSearch = false;
    
    /** The RID for check status, get search strategy and get results tests */
    private String m_RID = "";
    /** Was the check status command line option given? */
    private boolean m_CheckStatus = false;
    /** Was the get search strategy command line option given? */
    private boolean m_GetSearchStrategy = false;
    /** Was the get results command line option given? */
    private boolean m_GetResults = false;
    
    /** Was the get matrices command line option given? */
    private boolean m_GetMatrices = false;
    /** Was the get databases command line option given? */
    private boolean m_GetDatabases = false;
    /** Was the get programs command line option given? */
    private boolean m_GetPrograms = false;
    /** Was the get tasks command line option given? */
    private boolean m_GetTasks = false;
    /** Was the get options command line option given? */
    private boolean m_GetOptions = false;
    /** Was the get sequences command line option given? */
    private boolean m_GetSequences = false;
    
    /** File name to store test results */
    private String m_FileName = "";

    /** Constructor
     * @param args command line arguments as received by main function
     */
    public CmdLineParser(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].matches("-test_submission_lite")) {
                m_SubmitSearchLite = true;
            }
            if (args[i].matches("-test_submission")) {
            	m_SubmitSearch = true;
            }
            if (args[i].matches("-check_status")) {
            	m_CheckStatus = true;
            	if ( i+1 < args.length && args[i+1].length() != 0 ) {
            		m_RID = args[i+1];
            	} else {
            		PrintErrorAndExit("Missing RID");
            	}
            }
            if (args[i].matches("-get_results")) {
            	m_GetResults = true;
            	if ( i+1 < args.length && args[i+1].length() != 0 ) {
            		m_RID = args[i+1];
            	} else {
            		PrintErrorAndExit("Missing RID");
            	}
            }
            if (args[i].matches("-get_search_strategy")) {
            	m_GetSearchStrategy = true;
            	if ( i+1 < args.length && args[i+1].length() != 0 ) {
            		m_RID = args[i+1];
            	} else {
            		PrintErrorAndExit("Missing RID");
            	}
            }
            if (args[i].matches("-get_databases")) {
                m_GetDatabases = true;
                if ( i+1 < args.length && args[i+1].length() != 0 ) {
                	m_FileName = args[i+1];
                } else {
                	PrintErrorAndExit("Missing file name");
                }
            }
            if (args[i].matches("-get_options")) {
            	m_GetOptions = true;
            }
            if (args[i].matches("-get_programs")) {
                m_GetPrograms = true;
            }
            if (args[i].matches("-get_matrices")) {
                m_GetMatrices = true;
            }
            if (args[i].matches("-get_tasks")) {
                m_GetTasks = true;
            }
            if (args[i].matches("-get_sequences")) {
            	m_GetSequences = true;
            }
        }
    }
    
    /** Prints error message on stderr and exits application
     * 
     * @param msg Message to display
     */
    public void PrintErrorAndExit(String msg) {
    	System.err.println(msg);
    	System.exit(1);
    }

    /** Test lite submission? */ 
    public boolean TestSubmitLite() { return m_SubmitSearchLite; }
    /** Test retrieve programs? */
    public boolean TestGetPrograms() { return m_GetPrograms; }
    /** Test retrieve databases? */
    public boolean TestGetDatabases() { return m_GetDatabases; }
    /** Test retrieve matrices? */
    public boolean TestGetMatrices() { return m_GetMatrices; }
    /** Test retrieve tasks? */
    public boolean TestGetTasks() { return m_GetTasks; }
    /** Test search submission? */
	public boolean TestSubmitSearch() {	return m_SubmitSearch;	}
	/** Test search status? */
	public boolean TestCheckStatus() { return m_CheckStatus; }
	/** Test retrieval of search strategy? */
	public boolean TestGetSearchStrategy() { return m_GetSearchStrategy; }
	/** Test retrieval of results? */
	public boolean TestGetResults() { return m_GetResults; }
	/** Test retrieve options? */
	public boolean TestGetOptions() { return m_GetOptions; }
	/** Test fetching sequences? */
	public boolean TestGetSequences() { return m_GetSequences; }
	/** Get RID to use for tests */
	public String GetRID() { return m_RID; }
	/** Get file name to store test results */
	public String GetFileName() { return m_FileName; }
}

