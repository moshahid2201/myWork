#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

// function for calculating Standard Deviation
void standardDeviation(double *a, double *b, double *sdA, double *sdB,
		double meanA, double meanB, long n) {
	long i = 0;
	double test1 = meanA;
	double test2 = meanB;
	omp_set_num_threads(4);
#pragma omp parallel for reduction(+:test1,test2)
	for (i = 0; i < n; i++) {
		test1 += ((a[i] - meanA) * (a[i] - meanA)) / n;
		test2 += ((b[i] - meanB) * (b[i] - meanB)) / n;
	}
	*sdA = sqrt(test1);
	*sdB = sqrt(test2);

}
;

// function for initializing array of million values and calculating mean
void initializer(double *a, double *b, long n, double *meanA, double *meanB) {
	long i = 0;
	double test1 = *meanA;
	double test2 = *meanB;
	omp_set_num_threads(4);
#pragma omp parallel for reduction(+:test1,test2)	
	for (i = 0; i < n; i++) {
		a[i] = sin(i);
		b[i] = sin(i + 1);
		test1 += a[i];
		test2 += b[i];
	}

	*meanA = test1 / n;
	*meanB = test2 / n;
}
;
// function for calculating pearson correlation coefficient
void pearsonCoefficient(double meanA, double meanB, double *aVals,
		double *bVals, double *pearson, double sdA, double sdB, long n) {
	long i = 0;
	double test1 = 0;
	omp_set_num_threads(4);
#pragma omp parallel for reduction(+:test1)	
	for (i = 0; i < n; i++) {
		test1 += (((aVals[i] - meanA) * (bVals[i] - meanB)));
	}
	*pearson = (test1 / n) / (sdA * sdB);
}
;
int main() {
	double *arrayA = (double *) malloc(sizeof(double) * 1000000);
	double *arrayB = (double *) malloc(sizeof(double) * 1000000);
	double pearsonCCoefficient = 0.0;
	double startTime, endTime;

	long n = 1000000;
	double standardDeviationA, standardDeviationB;
	double meanA, meanB;
	meanA = meanB = 0;
	
	// call to initializer function for initializing arrays with sin(i) and sin(i+1) 
	//values, and calculating mean for both the arrays
	initializer(arrayA, arrayB, n, &meanA, &meanB);

	// call to Standard Deviation function,with arrays 
	//and mean calculated previously
	standardDeviation(arrayA, arrayB, &standardDeviationA, &standardDeviationB,
			meanA, meanB, n);

	startTime = omp_get_wtime();
	
	// call to Pearson Correlation Coefficient with mean ,array
	//	and standard Deviation calculated previously
	pearsonCoefficient(meanA, meanB, arrayA, arrayB, &pearsonCCoefficient,
			standardDeviationA, standardDeviationB, n);
	
	printf("Pearson Coefficient: %f\n", pearsonCCoefficient);
	
	endTime = omp_get_wtime();
	
	printf("Total Execution Time : \n %f Sec \n %f ms", endTime - startTime,
			(endTime - startTime) * 1000);
	printf("\n");
	free(arrayA);
	free(arrayB);
	return 0;
}

