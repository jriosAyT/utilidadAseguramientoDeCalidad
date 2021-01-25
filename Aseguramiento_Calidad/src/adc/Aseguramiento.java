/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adc;

import java.io.IOException;

import adc.types.CO2;
import adc.types.DP;
import adc.types.Flujo;
import adc.types.MP;
import adc.types.NOx;
import adc.types.O2;
import adc.types.SO2;

public class Aseguramiento {

	public static void main(String[] args) throws IOException {		
		DP.AC_DP();
		MP.AC_MP();
		Flujo.AC_Flujo();
		NOx.AC_NOx();	
		O2.AC_02();
		CO2.AC_CO2();
		SO2.AC_SO2();
	}

}