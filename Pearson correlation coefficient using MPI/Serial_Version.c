#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <mpi.h>

void standardDeviation(double *a, double *b, double *sdA, double *sdB,
		double meanA, double meanB, long n) {
	long i = 0;
	for (i = 0; i < n; i++) {
		*sdA += ((pow(a[i] - meanA, 2.0)) / n);
		*sdB += ((pow(b[i] - meanB, 2.0)) / n);
	}
	*sdA = sqrt(*sdA);
	*sdB = sqrt(*sdB);

}
;

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
	MPI_Init(NULL, NULL);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	double startTime, endTime;
	startTime = MPI_Wtime();
	double *arrayA = (double *) malloc(sizeof(double) * 1000000);
	double *arrayB = (double *) malloc(sizeof(double) * 1000000);
	double pearsonCCoefficient = 0.0;
	long n = 1000000;
	double standardDeviationA, standardDeviationB;
	double meanA, meanB;
	initializer(arrayA, arrayB, n, &meanA, &meanB);
	standardDeviation(arrayA, arrayB, &standardDeviationA, &standardDeviationB,
			meanA, meanB, n);
	pearsonCoefficient(meanA, meanB, arrayA, arrayB, &pearsonCCoefficient,
			standardDeviationA, standardDeviationB, n);

	printf("Pearson Coefficient:%f\n", pearsonCCoefficient);
	endTime = MPI_Wtime();
	printf("Total Elapsed Time for Serial Version : %f \n",
			endTime - startTime);
//	system("pause");
}
