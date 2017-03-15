import org.junit.Test;
import static org.junit.Assert.*;

public class testProb5 {
	
	/* writeCSVFile: ensure that CSV file is still produced regardless of values */
	@Test
	public void writeCSVFile(){
		String[] stubStringArray= new String[10];
		Double[][] stubDoubleArray= new Double[10][10];
		assertTrue(Prob5.printCSV("Prob5.csv", stubStringArray, stubDoubleArray));
	}
	
	/* outputUnchanged: nothing changed after refactoring */
	@Test
	public void outputUnchanged(){
		double tolerance = 0.0000001;
		Prob5 problem5 = new Prob5();
		problem5.setProblemParameters();
		Double[][] desiredOutput =
			{{0.0 ,0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1},
			 {473.0, 464.5, 458.8458333333, 455.9438541667, 455.746088831, 458.2494427638, 463.4956522642, 471.5719803133, 482.6126680585, 496.801166106, 514.3731827088},
			 {473.0, 439.5, 408.4291666667, 379.2719791667, 351.5446420718, 324.7870579017, 298.5551924299, 272.413706064, 245.9287290219, 218.6606604006, 190.1568716242},
			 {473.0, 447.4301947767, 424.4217261329, 403.5928952723, 384.5981528242, 367.1223661942, 350.8755913514, 335.5882623112, 321.0067185045, 306.8889958214, 293.000811491}
			};

		Double [][] actualOutput =problem5.calculate(-1000.0, -3500.0);
	
		int error =0;
		for (int i=0; i<4 ; i++){
			for (int j=0; j<11; j++){
				if(Math.abs(actualOutput[i][j] - desiredOutput[i][j]) > tolerance){
					error=1;
					break;
				}
			}
		}
		
		assertTrue(error==0);
	}
	
	/* correctRK2Formula: using another set of values to ensure that the RK2
	 * code is correct after refactoring */
	@Test
	public void correctRK2Formula(){
		Sys2ODEsRK2 sys2 = new Sys2ODEsRK2();
		String fun1 = "(0 - y + z)*(2.71828)^(1 - x) + (0.5)*y";
	    String fun2 = "y - z^2";
	    double tolerance = 0.0000001;
  		Double[][] desiredResult = {
  				{0.0, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0},
  				
  				{3.0,2.368444547983124, 2.1655860390982786, 2.148076631124787,
  				2.2219486713754844, 2.3517228198621787, 2.5249136842074713,
  				2.73843605996376, 2.9933513268707785, 3.2928151621459048,
  				3.6412842832407537, 4.044234351518173, 4.508094745478082},
  				
  				{0.2,0.6961875919967491, 1.0082882425236197, 1.2002062726202378,
  					1.3240551705189614,1.413613063485959, 1.488732089630473, 1.560578922054308,
  					1.6353096919212202, 1.7162890954745766, 1.8053566948958946,
  					1.9035352002124761, 2.0114250082064338}
  		};
  		
  		/*
  		 * lowerbound = 0
  		 * upperbound = 3
  		 * y0 = 3
  		 * z0 = 0.2
  		 * h = 0.25
  		 * */
		Double[][] actualResult = sys2.calculate(fun1,fun2,3.0,0.2,0.0,3.0,0.25);

		int error = 0;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 13; j++) {
				if (Math.abs(desiredResult[i][j] - actualResult[i][j]) > tolerance) {
					error = 1;
					break;
				}
			}
		}

		assertTrue(error == 0);
	}

}

