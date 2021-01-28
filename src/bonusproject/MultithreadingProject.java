//****Bonus Project / Multi-threading****
//
//Author: Scott Tabaka
//Instructor: Steve Riegerix
//Class: CMPSCI 2261(Fall 2018)
//Due Date: December 9, 2018

package bonusproject;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MultithreadingProject
{
	final int matsize = 1000;
	int threadcount = 10;
    int A[][];
    int B[][];
    int C[][];

    MultithreadingProject()
    {
        A= new int [matsize][matsize];
        B= new int [matsize][matsize];        
        C= new int [matsize][matsize];
    }
    
	public static int[][] createMatrix(int matsize)
	{
		int[][] M = new int[matsize][matsize];
		Random rand = new Random();
		
		for(int i=0;i<matsize;i++)
		{
			for(int j=0;j<matsize;j++)
			{
				M[i][j] = rand.nextInt(9 - 0 + 1) + 0;
			}
		}
		return M;
	}
    
    public void multiThreadProgram()
    {
    	long startTime = System.currentTimeMillis();
        System.out.println("\nMultiplying Matrices A & B without multithreading...");
        basicMatrixMult(); 
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Total execution time of matrixMultiply function is : "+ elapsedTime +" ms\n");

        while(threadcount<=matsize)
        {
	        try
	        {
	        	ExecutorService executor = Executors.newFixedThreadPool(threadcount);
	        	System.out.println("Multiplying Matrices A & B with " + threadcount + " threads...");
		        startTime = System.currentTimeMillis();
		        for(int i=0;i<matsize;i++)
		        {
		            for(int j=0;j<matsize;j++)
		            {
		                RunnableClass ob = new RunnableClass(i,j,this);
		                executor.execute(ob);
		            }
		        }
	
		        executor.shutdown();
		        while (!executor.isTerminated()){}
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;
				System.out.println("Total execution time of matrix Multiply function using multiple threads(" + threadcount+ ") : "+ elapsedTime +" ms\n");
				threadcount = threadcount * 10;
	        }catch(Exception e){}	        
        }
	}

    void basicMatrixMult()
    {
        for(int i=0;i<matsize;i++)
        {
            for(int j=0;j<matsize;j++)
            {
                for(int k=0;k<matsize;k++)
                {
                    C[i][j]+=A[i][k]*B[k][j];
                }
            }
        }
    }
  
    public static void main(String args[])
    {
    	MultithreadingProject matrixobject = new MultithreadingProject();
    	
    	System.out.println("Building Matrix A("+matrixobject.matsize+"x"+matrixobject.matsize+")...");
    	matrixobject.A  = createMatrix(matrixobject.matsize);
    	System.out.println("Building Matrix B("+matrixobject.matsize+"x"+matrixobject.matsize+")...");
    	matrixobject.B  = createMatrix(matrixobject.matsize);

        matrixobject.multiThreadProgram();
    }
}

class RunnableClass implements Runnable
{
    int i,j;
    MultithreadingProject matrixobject;

    RunnableClass(int i1,int j1,MultithreadingProject ob)
    {
        i=i1;
        j=j1;
        matrixobject=ob;
    }

     public void run()
    {
        int tempsum=0;

        for(int k=0;k<matrixobject.matsize;k++)
        {

        	tempsum+=matrixobject.A[i][k]*matrixobject.B[k][j];
        }

        matrixobject.C[i][j]=tempsum;
    }
}
