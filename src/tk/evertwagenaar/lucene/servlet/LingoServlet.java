package tk.evertwagenaar.lucene.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.evertwagenaar.lucene.LingoSearcher;

/**
 * Servlet implementation class DebugServlet
 */
@WebServlet(description = "Simple servlet to check request params", urlPatterns = { "/LingoServlet" })
public class LingoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LingoServlet() {
		super();
	}

	String HTML_HEAD = "" + "<!DOCTYPE HTML PUBLIC \"" + "-//W3C//DTD HTML " + "4.01 Transitional//EN\" "
			+ "\"http://www.w3.org/TR/html4/loose.dtd\">" + "<html>" + "<head><link rel=\"stylesheet\" "
			+ "type=\"text/css\" href=\"default.css\">" + "<meta http-equiv=\"cache-control\" content=\"max-age=0\" />"
			+ "<meta http-equiv=\"cache-control\" content=\"no-cache\" />"
			+ "<meta http-equiv=\"expires\" content=\"0\" />"
			+ "<meta http-equiv=\"expires\" content=\"Tue, 01 Jan 1980 1:00:00 GMT\" />"
			+ "<meta http-equiv=\"pragma\" content=\"no-cache\" />" + "<title>Lingo Helper</title>"
			+ "<meta http-equiv=\"Content-Type\" " + "content=\"text/html; charset=UTF-8\">" + "</head>" + "<body>";
	String BODY = "<h1>Results:</h1>";
	String FOOTER = "</html>";

	// Prevent caching by destroying it first.
	LingoSearcher ls = null;

	HttpServletRequest req = null;
	HttpServletResponse res = null;
	PrintWriter pw = null;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest req, HttpServletResponse res)
	 */

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// PrintWriter pw = null;
		LingoSearcher ls = null;
		PrintWriter pw = res.getWriter();

		String wl = req.getParameter("wl").trim();
		String wc = req.getParameter("wc").trim().toLowerCase();

		int wlength = Integer.parseInt(wl);

		LingoSearcher searcher = new LingoSearcher();

		try {
			BODY = BODY + "<div id=\"scroll\">" + "<pre>" + ls.doSearch(wc, wlength) + "</pre></div>";

		} catch (Exception e) {
			String msg = "An error occured. Please check your input" + " parameters and try again.<br/>"
					+ "If the prblem persists please report the error" + "as 122";
			pw.println(msg);
		}
		pw.print(HTML_HEAD);
		pw.print(BODY);
		pw.print(FOOTER);
		pw.flush();
		pw.close();
		res = null;
		req = null;
	}



}
