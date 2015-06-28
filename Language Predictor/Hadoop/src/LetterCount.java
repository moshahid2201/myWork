/*
 * 
 * @author : Mohammad Omar Shahid
 * 
 */

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.BasicConfigurator;




public class LetterCount {
	private static Log LOG = LogFactory.getLog(LetterCount.class);
	private static String filename;
	private static ArrayList<node> probabilityMatrix = new ArrayList<node>();
	private static float totalLetterCount = 0;

	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			FileSplit a = (FileSplit) context.getInputSplit();
			filename = a.getPath().getName();
			// data pre-processing to remove whitespace and punctuation marks,
			// and split the word
			// into letters
			char[] test = value.toString().trim()
					.replaceAll("\\s|(\\d+)|[-+.^:,!;>>" + "\"" + "]", "")
					.toCharArray();
			int counter = 0;

			while (counter < test.length) {
				word.set(Character.toString(test[counter]));
				context.write(word, one);
				counter++;
			}
		}
	}

	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
			// adding the data to the array list of nodes
			addNode(filename, result.get(), key.toString());

		}

	}

	// this function adds the output of the reduce to array list
	public static void addNode(String fn, int counter, String letter) {
		boolean found = false;
		for (int i = 0; i < probabilityMatrix.size(); i++) {
			if ((probabilityMatrix.get(i).getLetter().equals(letter))
					&& (probabilityMatrix.get(i).getFileName().equals(fn))
					&& (probabilityMatrix.get(i).getCounter() == counter)) {
				found = true;
				System.out.println("found");
				break;
			} 
			
		}
		if (!found) {

			probabilityMatrix.add(new node(fn, counter, letter));
			setTotalLetterCount(getTotalLetterCount() + counter);
		}
	}

	public static float getTotalLetterCount() {
		return totalLetterCount;
	}

	public static void setTotalLetterCount(float f) {
		LetterCount.totalLetterCount = f;
	}

	// this function calculates the probability , the parameter is filename
	// which is language name
	// since this list contains data from all the languages used in the training
	// and languages can have common letters,
	// so for correctness of probability,filename is taken into account

	public void probabilityCalculator(String fname) {
		for (int i = 0; i < probabilityMatrix.size(); i++) {
			if (probabilityMatrix.get(i).getFileName().equals(fname))
				probabilityMatrix.get(i).setProbality(
						probabilityMatrix.get(i).getCounter()
								/ totalLetterCount);

		}

	}

	// Initialization of Hadoop
	public boolean hadoopWork(String filename, String outputfilename)
			throws Exception {
		BasicConfigurator.configure();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "letter count");
		job.setJarByClass(LetterCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(filename));
		FileOutputFormat.setOutputPath(job, new Path(outputfilename));
		if (job.waitForCompletion(true))
			return true;
		else
			return false;

	}

	// Simple function that calculates sum of square used for calculating
	// magnitude of a vector
	public static double sumOfSquares(String fileName) {
		double sum = 0;
		for (int i = 0; i < probabilityMatrix.size(); i++) {

			if (probabilityMatrix.get(i).getFileName().equals(fileName)) {
				sum += Math.pow(probabilityMatrix.get(i).getProbality(), 2);

			}

		}
		return sum;
	}

	// this function calculates the likelihood for every language against the
	// testing data
	public static void resultCalculator(LetterCount trainResults,
			LetterCount testResult, ArrayList<ResultMatrixNode> RS,
			ArrayList<String> fn) {
		boolean found = false;
		// for each language
		for (int i = 0; i < fn.size(); i++) {
			// adding a node
			RS.add(new ResultMatrixNode(fn.get(i), 0));
			// iterating through test data matrix
			for (int j = 0; j < testResult.probabilityMatrix.size(); j++) {
				// iterating through train data matrix
				for (int k = 0; k < trainResults.probabilityMatrix.size(); k++) {
					// if the letter matches between the train and test data
					// node , multiply the probability and divide my magnitude
					if (trainResults.probabilityMatrix.get(k).getFileName()
							.equals(fn.get(i))) {
						if (((trainResults.probabilityMatrix.get(k).getLetter()
								.equals(testResult.probabilityMatrix.get(j)
										.getLetter())))
								&& (testResult.probabilityMatrix.get(j)
										.getFileName().equals("testCase")))

						{

							RS.get(i)
									.addProbablity(
											(trainResults.probabilityMatrix
													.get(k).getProbality() * testResult.probabilityMatrix
													.get(j).getProbality())
													/ Math.sqrt((sumOfSquares(fn
															.get(i)) * sumOfSquares("testCase"))));
							;

							break;
						}

					}

				}

			}

		}

	}

	// Function used to delete output files of the previous run
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			System.out.println("yes");
			String[] children = dir.list();

			for (int i = 0; i < children.length; i++) {
				System.out.println(children[i].toString());
				deleteDir(new File(dir, children[i]));

			}

		}
		return dir.delete();

	}

	public static void main(String[] args) throws Exception {
		System.out.println("start Application");
		deleteDir(new File("englishOutput"));
		deleteDir(new File("danishOutput"));
		deleteDir(new File("finnishOutput"));
		deleteDir(new File("SwedishOutput"));
		deleteDir(new File("SpanishOutput"));
		deleteDir(new File("FrenchOutput"));
		deleteDir(new File("HungarianOutput"));
		deleteDir(new File("ChineseOutput"));
		deleteDir(new File("testOutput"));

		LetterCount train = new LetterCount();
		ArrayList<String> fileNames = new ArrayList<String>();
		ArrayList<ResultMatrixNode> ResultSet = new ArrayList<ResultMatrixNode>();

		train.hadoopWork("danish", "danishOutput");
		fileNames.add("danish");
		train.probabilityCalculator("danish");
		setTotalLetterCount(0);

		train.hadoopWork("english", "englishOutput");
		train.probabilityCalculator("english");
		fileNames.add("english");
		train.setTotalLetterCount(0);

		train.hadoopWork("finnish", "finnishOutput");
		train.probabilityCalculator("finnish");
		fileNames.add("finnish");
		train.setTotalLetterCount(0);

		train.hadoopWork("swedish", "SwedishOutput");
		train.probabilityCalculator("swedish");
		fileNames.add("swedish");
		train.setTotalLetterCount(0);

		train.hadoopWork("Spanish", "SpanishOutput");
		train.probabilityCalculator("Spanish");
		fileNames.add("Spanish");
		train.setTotalLetterCount(0);

		train.hadoopWork("French", "FrenchOutput");
		train.probabilityCalculator("French");
		fileNames.add("French");
		train.setTotalLetterCount(0);

		train.hadoopWork("Hungarian", "HungarianOutput");
		train.probabilityCalculator("Hungarian");
		fileNames.add("Hungarian");
		train.setTotalLetterCount(0);

		train.hadoopWork("Chinese", "ChineseOutput");
		train.probabilityCalculator("Chinese");
		fileNames.add("Chinese");
		train.setTotalLetterCount(0);

		LetterCount test = new LetterCount();
		test.setTotalLetterCount(0);
		test.hadoopWork("testCase", "testOutput");
		test.probabilityCalculator("testCase");
		test.setTotalLetterCount(0);

		resultCalculator(train, test, ResultSet, fileNames);
		// finding the maximum probability
		float mark = ResultSet.get(0).getProbabilty();
		int index = 0;

		System.out.println();
		System.out.println("*************  Result  *************************");
		System.out.println();

		for (int i = 0; i < ResultSet.size(); i++) {
			if (ResultSet.get(i).getProbabilty() > mark) {
				mark = ResultSet.get(i).getProbabilty();
				index = i;
			}
			System.out.println("Language: " + ResultSet.get(i).getFileName());
			System.out.println("Probability: "
					+ ResultSet.get(i).getProbabilty() * 100 + " %");
			System.out.println("**********************");
		}

		System.out.println();
		System.out.println("******   RESULT  **********");
		System.out.println();
		System.out.println("Predicted Language is:");
		System.out.println(ResultSet.get(index).getFileName()
				+ " with probability of "
				+ ResultSet.get(index).getProbabilty() * 100 + " %");

	}

}