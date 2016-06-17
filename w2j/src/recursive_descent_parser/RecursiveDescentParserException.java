package recursive_descent_parser;

import java.util.List;

/**
 * Exception thrown by the parser.
 */
public class RecursiveDescentParserException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<Integer> analysisBeforeFailure = null;

	/**
	 * Constructor.
	 */
	public RecursiveDescentParserException() {
	}

	/**
	 * Constructor with exception text.
	 * 
	 * @param s
	 *            Text explaining the exception.
	 */
	public RecursiveDescentParserException(String s) {
		super(s);
	}

	/**
	 * Constructor.
	 * 
	 * @param s
	 *            Text explaining the exception.
	 * @param analysis
	 *            Recognized rules up to the exception.
	 */
	public RecursiveDescentParserException(String s, List<Integer> analysis) {
		this(s);
		this.analysisBeforeFailure = analysis;
	}

	/**
	 * Gets the recognized rules up to the exception.
	 * 
	 * @return List of rules recognized before the failure.
	 */
	public List<Integer> getAnalysisBeforeFailure() {
		return analysisBeforeFailure;
	}
}
