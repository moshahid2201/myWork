
/*
 * 
 * @author : Mohammad Omar Shahid
 * 
 */

public class ResultMatrixNode {

	private String fileName;
	private float probabilty;

	ResultMatrixNode(String fn, float prob) {
		this.fileName = fn;
		this.probabilty = prob;

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public float getProbabilty() {
		return probabilty;
	}

	public void addProbablity(double probabilty) {
		this.probabilty += probabilty;

	}

	public void setProbabilty(float probabilty) {
		this.probabilty = probabilty;
	}

}
