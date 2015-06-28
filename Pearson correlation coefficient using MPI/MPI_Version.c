#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>

void initializer(double *a, double *b, long n, double *meanA, double *meanB,
		int rank, int numProcs) {
	long i = 0;
	*meanA = 0;
	*meanB = 0;
	for (i = 0; i < n; i++) {
		
		a[i] = sin((n * rank) + i);
		b[i] = sin((n * rank) + (i + 1));
		       
		*meanA += a[i];
		*meanB += b[i];
	}
	*meanA = *meanA / (n * numProcs);
	*meanB = *meanB / (n * numProcs);
}
;

void standardDeviation(double *a, double *b, double *sdA, double *sdB,
		double meanA, double meanB, long n, int numProcs) {
	long i = 0;
	*sdA = 0;
	*sdB = 0;
	for (i = 0; i < n; i++) {
		*sdA += ((pow(a[i] - meanA, 2.0)) / (n * numProcs));
		*sdB += ((pow(b[i] - meanB, 2.0)) / (n * numProcs));
	}
//	*sdA = sqrt(*sdA);
//	*sdB = sqrt(*sdB);

}
;
void pearsonCoefficient(double meanA, double meanB, double *aVals,
		double *bVals, double *pearson, double sdA, double sdB, long n,
		int size) {
	long i = 0;
	for (i = 0; i < n; i++) {
		*pearson += (((aVals[i] - meanA) * (bVals[i] - meanB)) / (n * size))
				/ (sdA * sdB);
	}
}

int main(int argc, char **argv) {
	int size, rank;
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	long localArraySize = 1000000 / size;
	double *arrayA = (double *) malloc(sizeof(double) * localArraySize);
	double *arrayB = (double *) malloc(sizeof(double) * localArraySize);
	double *combinedA;
	double *combinedB;
	double pearsonCCoefficient = 0.0;
	double pearsonCC = 0.0;
	double sdA, sdB;
	double standardDeviationA, standardDeviationB;
	double localMeanA, localMeanB;
	double MeanA, MeanB;
	double localStart, localEnd, localElapsed, Elapsed;
	localStart = MPI_Wtime();
	int test = 0;
	int status = -1;
	localMeanA = 0;
	localMeanB = 0;
	initializer(arrayA, arrayB, localArraySize, &localMeanA, &localMeanB, rank,
			size); // For initializing A values and Y values with random values and calculation mean

	if (rank == 0) {
		combinedA = malloc(localArraySize * size * (sizeof(double)));
		combinedB = malloc(localArraySize * size * (sizeof(double)));
	}
	//MPI_Barrier(MPI_COMM_WORLD);    

	// Combining the distributed arrays to single array.	

	// MPI GATHER statement 
	MPI_Gather(arrayA, localArraySize, MPI_DOUBLE, combinedA, localArraySize,
			MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Gather(arrayB, localArraySize, MPI_DOUBLE, combinedB, localArraySize,
			MPI_DOUBLE, 0, MPI_COMM_WORLD);

	// Calculation of overall mean 
	MPI_Reduce(&localMeanA, &MeanA, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);
	MPI_Reduce(&localMeanB, &MeanB, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);


	// Assigning global mean to process local mean for standard deviation
	MPI_Bcast(&MeanA, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Bcast(&MeanB, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);


	// Calculation of Standard deviation on every chunk
	standardDeviation(arrayA, arrayB, &sdA, &sdB, MeanA, MeanB, localArraySize,
			size);
	// Combining local standard deviation to overall standard deviation
	MPI_Reduce(&sdA, &standardDeviationA, 1, MPI_DOUBLE, MPI_SUM, 0,
			MPI_COMM_WORLD);
	MPI_Reduce(&sdB, &standardDeviationB, 1, MPI_DOUBLE, MPI_SUM, 0,
			MPI_COMM_WORLD);


	if (rank == 0) {
		standardDeviationA = sqrt(standardDeviationA);
		standardDeviationB = sqrt(standardDeviationB);
	}

	// Assigning overall standard deviation to process local standard deviation	
	MPI_Bcast(&standardDeviationA, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);
	MPI_Bcast(&standardDeviationB, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);


	// Calculation of Pearson Correlation Coefficient  
	pearsonCoefficient(MeanA, MeanB, arrayA, arrayB, &pearsonCC,
			standardDeviationA, standardDeviationB, localArraySize, size);


	// Calculation of total Pearson Correlation Coefficient
	MPI_Reduce(&pearsonCC, &pearsonCCoefficient, 1, MPI_DOUBLE, MPI_SUM, 0,
			MPI_COMM_WORLD);

	localEnd = MPI_Wtime();
	localElapsed = localEnd - localStart;
	printf("\n Elapsed time for process %d is %f", rank, localElapsed);
	MPI_Reduce(&localElapsed, &Elapsed, 1, MPI_DOUBLE, MPI_MAX, 0,
			MPI_COMM_WORLD);
	if (rank == 0) {

		int j = 0;
		
		printf("\n pearson coefficient total %f", pearsonCCoefficient);
		printf("\n Total Elapsed Time %f", Elapsed);
		printf("\n");
	}
	MPI_Finalize();
	return 0;
}
