#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <omp.h>

// function for calculating Standard Deviation
void standardDeviation(double *a, double *b, double *sdA, double *sdB,
		double meanA, double meanB, long n) {
	long i = 0;
	for (i = 0; i < n; i++) {
		*sdA += ((a[i] - meanA) * (a[i] - meanA)) / n;
		*sdB += ((b[i] - meanB) * (b[i] - meanB)) / n;
	}
	*sdA = sqrt(*sdA);
	*sdB = sqrt(*sdB);

}
;
// function for initializing array of million values and calculating mean
void initializer(double *a, double *b, long n, double *meanA, double *meanB) {
	long i = 0;
	for (i = 0; i < n; i++) {
		a[i] = sin(i);
		b[i] = sin(i + 1);
		*meanA += a[i];
		*meanB += b[i];
	}
	*meanA = *meanA / n;
	*meanB = *meanB / n;
}
;
// function for calculating pearson correlation coefficient
void pearsonCoefficient(double meanA, double meanB, double *aVals,
		double *bVals, double *pearson, double sdA, double sdB, long n) {
	long i = 0;
	for (i = 0; i < n; i++) {
		*pearson += (((aVals[i] - meanA) * (bVals[i] - meanB)) / n)
				/ (sdA * sdB);
	}
}
;
int main() {
	int size, rank;
	double startTime, endTime;
	double *arrayA = (double *) malloc(sizeof(double) * 1000000);
	double *arrayB = (double *) malloc(sizeof(double) * 1000000);
	double pearsonCCoefficient = 0.0;
	long n = 1000000;

	double standardDeviationA, standardDeviationB;
	double meanA, meanB;
	// call to initializer function for initializing arrays with sin(i) and sin(i+1) values,
	//	and calculating mean for both the arrays
	initializer(arrayA, arrayB, n, &meanA, &meanB);
	// call to Standard Deviation function,with arrays and mean calculated previously
	standardDeviation(arrayA, arrayB, &standardDeviationA, &standardDeviationB,
			meanA, meanB, n);
	startTime = omp_get_wtime();
	// call to Pearson Correlation Coefficient with mean ,array and 
	//standard Deviation calculated previously
	pearsonCoefficient(meanA, meanB, arrayA, arrayB, &pearsonCCoefficient,
			standardDeviationA, standardDeviationB, n);
	;
	printf("Pearson Coefficient:%f\n", pearsonCCoefficient);
	endTime = omp_get_wtime();
	printf("Total Execution Time : \n %f Sec \n %f ms", endTime - startTime,
			(endTime - startTime) * 1000);
	printf("\n");
	return 0;
}

