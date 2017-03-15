/*
* Copyright (c) 2016 UPD CS 131 Arel Latoga, Faneallrich Yao. All rights reserved.
*/

import org.mariuszgromada.math.mxparser.*;
import java.io.PrintWriter;
import java.io.IOException;

public class Prob5
{
    public Double length;
    public Double T0;
    public Double TL;
    public Double surroundTemp;

    public Double heatCoeff;
    public Double perimeter;
    public Double epsilon;
    public Double stefanBoltzmann;
    public Double k;
    public Double crossArea;

    public String dT;
    public String dw;
    
    /* Declaration of hard-coded values. The expression string is also parsed here. */
    public void setProblemParameters()
    {
        length = 1.0;
        T0 = 473.0;
        TL = 293.0;
        surroundTemp = 293.0;

        heatCoeff = 40.0;
        perimeter = 0.016;
        epsilon = 0.4;
        stefanBoltzmann = 5.67 * Math.pow(10, -8);
        k = 240.0;
        crossArea = 1.6 * Math.pow(10, -5);

        dT = "z";
        String convection_term = String.format("(%f * %f)/(%f * %f) * (y - %f)",heatCoeff, perimeter, k, crossArea, surroundTemp);
        String radiation_term = String.format("(%f * %f * %f)/(%f * %f) * ((y ^ 4) - (%f ^ 4))",epsilon, stefanBoltzmann, perimeter, k, crossArea, surroundTemp);
        dw = convection_term + "+" + radiation_term;
    }
    
    /* Creates a CSV file that contains all the values computed in the calculate function. Returns true without errors, false otherwise. */
    public static boolean printCSV(String filename, String[] titles, Double[][] values)
    {
      try
      {
        PrintWriter outputfile = new PrintWriter(filename, "UTF-8");
        for (int i = 0; i < titles.length; i++)
        {
          outputfile.print(titles[i]);
          outputfile.print(",");
        }
        outputfile.print("\n");
        for (int j = 0; j < values[0].length; j++)
        {
          for (int i = 0; i < values.length; i++)
          {
            outputfile.print(values[i][j]);
            outputfile.print(",");
          }
          outputfile.print("\n");
        }
        outputfile.close();
        return true;
      }
      catch (IOException e)
      {
        System.out.println("Error writing to file " + filename + "!");
        return false;
      }
    }
    
    /* Calls Sys2ODEsRK2 to calculate the necessary values. */
    public Double[][] calculate(Double guess1, Double guess2)
    {
      Double lowerBound = guess1;
      Double upperBound = guess2;

      Double[][] lowerResult = Sys2ODEsRK2.calculate(dT, dw, T0, lowerBound, 0.0, 0.1, 0.01);
      Double[] lowerT = lowerResult[1];
      Double lowerTL = lowerT[lowerT.length - 1];

      Double[][] upperResult = Sys2ODEsRK2.calculate(dT, dw, T0, upperBound, 0.0, 0.1, 0.01);
      Double[] upperT = upperResult[1];
      Double upperTL = upperT[upperT.length - 1];

      Double slope = (upperTL - lowerTL)/(upperBound - lowerBound);

      System.out.println(lowerTL);
      System.out.println(upperTL);

      Sec ng = new Sec(String.format("f(x) = %f * (x - %f) + %f - %f", slope, lowerBound, lowerTL, TL),String.format("Xa = %f", lowerBound),String.format("Xb = %f", upperBound),String.format("err = %f", 0.000001),String.format("imax = %d", 100000));
      Double newGuess = ng.Answer();
      Double[][] newResult = Sys2ODEsRK2.calculate(dT, dw, T0, newGuess, 0.0, 0.1, 0.01);
      Double[] newT = newResult[1];
      Double[] xValues = newResult[0];

      String[] titles = {"x", "w(0) = -1000", "w(0) = -3500", String.format("w(0) = %f\n", newGuess)};
      Double[][] values = {xValues, lowerT, upperT, newT};
      printCSV("Prob5.csv", titles, values);
      return values;
    }

    public static void main(String[] args)
    {
      Prob5 prob5 = new Prob5();
      prob5.setProblemParameters();
      prob5.calculate(-1000.0, -3500.0);
    }
}
