package br.cefetrj.ppcic.cpd.kmeans.vertical;

import java.util.Date;

import mpi.MPI;

public class Main {

	public static void main(String[] args) {
		
		try{
			
			MPI.Init(args);
			
			int me = MPI.COMM_WORLD.Rank();
			
			if(me == 0){
			
				int k = 3;
				
				if(args.length > 0)
					k = new Integer(args[0]);
				
				if(k > 3)
					System.out.println("N�mero m�ximo para k � 3. A m�quina possui 4 cores e processa um k por core.");
				
				if(k < 2)
					System.out.println("N�mero m�nimo para k � 2.");
				
				System.out.println("k = " + k);
				
				String file = "W:\\CEFET\\2017.3\\CPD\\trabalhos\\trab_1\\datasets\\Geo-Magnetic field and WLAN\\measure1_smartwatch_sens_min.csv";
				//String file = "W:\\CEFET\\2017.3\\CPD\\trabalhos\\trab_1\\datasets\\teste.csv";
	    	
		    	double[][] data = UtilVertical.loadFile(file, true);
		    	
		    	System.out.println("N�mero de tuplas: " + data.length);
		    	
		    	long start = new Date().getTime();
		    	
		    	new KMeansVertical(k, data).group(me);
		    	
		    	long end = new Date().getTime();
		    	
		    	System.out.println("Tempo (ms): " + (end - start));
		    	
			}else{
				
				while(true){
					
					Object[] sendObj = new Object[3];
					
					MPI.COMM_WORLD.Recv(sendObj, 0, 3, MPI.OBJECT, 0, 10);
					
					sendObj[2] = KMeansVertical.calculateDistanceToCentroid((double[]) sendObj[0], (double[][]) sendObj[1]);
					
					MPI.COMM_WORLD.Isend(sendObj,0,3,MPI.OBJECT,0,10);
					//System.out.println("Enviando distancias do core " + me + " para core 0.");
					
					//MPI.COMM_WORLD.Barrier();
					
				}
				
			}
	    	
	    	MPI.Finalize();
	    	
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
