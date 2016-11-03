package tk.evertwagenaar.lucene;

public class LingoRun {

	/**
	 * @param args
	 * @throws Exception
	 *             Example usage: LingoSearcher ls = new LingoSearcher("ac",
	 *             12); LingoSearcher.doSearch();
	 */
	public static void main(String[] args) throws Exception {
		LingoSearcher ls = new LingoSearcher();
		String terms=ls.doSearch("ab",5);
		System.out.println(terms);
	}
}
