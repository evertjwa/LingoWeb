package tk.evertwagenaar.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.FSDirectory;

/**
 * The beating heart of the Lingo Helper.
 * 
 * @author Evert Wagenaar
 * @see http://evertwagenaar.tk/
 * @since 26-10-2016
 * 
 */
public class LingoSearcher {

	/**
	 * The LingoSerch bean is called by the LingoServlet it retrieves all
	 * matching terms as HTML. 
	 */
	

	public LingoSearcher() {
	}

	private static  QueryParserBase parser;
	private  static IndexReader reader;
	

	static String getQueryString(String startchars, int wordLength) {
		System.err.println("startchars: " + startchars);
		System.err.println("wordLength: " + wordLength);
		System.err.println("chars length: " + startchars.length());
		int charstoAdd = 0;
		String res = "";
		res = res + startchars;
		charstoAdd = (wordLength - startchars.length()-1);
		for (int i = 0; i <= charstoAdd; i++) {
			res = res + "?";
		}
		return res;
	}

	/**
	 * Performs the WildCardQuery
	 * 
	 * @return
	 */
	public String doSearch(String startChars, int wordlength) throws Exception {
		String queryString = getQueryString(startChars, wordlength);
		startChars = startChars.toLowerCase();
		String index = "/usr/local/index";
		String field = "contents";

		String res = "";

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		QueryParser parser = new QueryParser(field, analyzer);
		parser.setMultiTermRewriteMethod(MultiTermQuery.CONSTANT_SCORE_BOOLEAN_REWRITE);

		Query query = parser.parse(queryString);
		System.err.println("Searching for: " + query.toString(field));
		Date start = new Date();

		TopDocs t = searcher.search(query, 1);

		Date end = new Date();
		System.err.println("Time: " + (end.getTime() - start.getTime()) + "ms");
		if (t.totalHits > 0) {
			System.err.println(" \nPattern found in wordlist!!!!");
			Query q = parser.parse(queryString);
			q = q.rewrite(reader);
			Set<Term> terms = new HashSet<>();
			Weight weight = q.createWeight(searcher, false);
			weight.extractTerms(terms);
			System.err.println("startchars: " + startChars);
			res = "";
			for (Term term : terms) {
				String trm = term.toString().replaceAll("contents:", "");
				if (trm.startsWith(startChars)) {
					res += (trm + "<br/>");
				}
			}
		}
		return res;
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 * @throws ParseException
	 * 
	 */
	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, int hitsPerPage,
			boolean raw, boolean interactive) throws IOException, ParseException {

		
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		@SuppressWarnings("unused")
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");
		parser.setMultiTermRewriteMethod(MultiTermQuery.CONSTANT_SCORE_BOOLEAN_REWRITE);
		Query q = parser.parse(getQueryString(null, 0));
		q = q.rewrite(reader);
		Set<Term> terms = new HashSet<>();
		Weight weight = q.createWeight(searcher, false);
		weight.extractTerms(terms);
		System.out.println("Matching" + terms);

	}
}

