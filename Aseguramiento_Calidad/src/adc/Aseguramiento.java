/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adc;

import java.io.IOException;

import adc.types.DP;
import adc.types.Flujo;
import adc.types.Gases;
import adc.types.MP;
import adc.types.gases.AntiguoNOX;
import adc.types.gases.CO2;
import adc.types.gases.NOx;
import adc.types.gases.O2;
import adc.types.gases.SO2;

public class Aseguramiento {

	public static void main(String[] args) throws IOException {
		/* Funciona correctamente Aseguramiento de Calidad en flujo, DP y MP */
//		DP.AC_DP();
//		MP.AC_MP();
//		Flujo.AC_Flujo();

		/* Realizar aqui modificacion */
//		NOx.AC_NOx();
//		O2.AC_02();
//		CO2.AC_CO2();
//		SO2.AC_SO2();
		Gases.AC_GASES();
	}

}