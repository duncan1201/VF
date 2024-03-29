/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.utils;

import javax.sql.DataSource;



/**
* Returns a DataSource that implements connection pooling
*
* Uses Jakarta Commons DBCP and Pool packages.
* See the description of the dbcp package at 
* http://jakarta.apache.org/commons/dbcp/api/overview-summary.html#overview_description
*
* @author Simon Foote
* @author Len Trigg
* @author Andy Yates
* @author Thomas Down
*/

public class JDBCPooledDataSource {

  public static DataSource getDataSource(final String driver, 
                                         final String url,
                                         final String user,
                                         final String pass)
    throws Exception {    

    return null;
  }


  // Adds simple equals and hashcode methods so that we can compare if
  // two connections are to the same database. This will fail if the
  // DataSource is redirected to another database etc (I doubt this is
  // ever likely to be used).
  /**
   * @depercated This is no longer used in favor of {@link BasicDataSource}
   * from DBCP
    static class MyPoolingDataSource extends PoolingDataSource {
    final String source;
    public MyPoolingDataSource(ObjectPool connectionPool, String source) {
      super(connectionPool);
      this.source = source;
    }
    public boolean equals(Object o2) {
      
      return true;
    }
    public int hashCode() {
      return source.hashCode();
    }
  }
*/

  public static void main(String[] args) {
    try {
      DataSource ds1 = getDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:/tmp/hsqldb/biosql", "sa", "");
      DataSource ds2 = getDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:/tmp/hsqldb/biosql", "sa", "");
      System.err.println(ds1);
      System.err.println(ds2);
      System.err.println(ds1.equals(ds2));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
