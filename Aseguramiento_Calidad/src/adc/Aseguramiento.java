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

//		DP.AC_DP();
//		MP.AC_MP();
//		Flujo.AC_Flujo();

/*
 *  Para el aseguramiento de calidad de gases es necesario tener presente lo siguiente:
 *  
 *  - Si se desea cambiar el valor de 1 gas, es necesario tener la informacion del resto de los gases. Ya que en caso contrario los otros gases quedaran nulos.
 *  - Los archivos excel subidosno deben poseer formulas. 
 *  - Se debe asegurar de que la cantidad de datos a remplazar en los 4 archivos sean iguales entre si, en caso contrario solo se tomaran la cantidad de datos
 *  que posee el primer elemento ingresado (en este caso NOX). PD: agregar valores de dias diferentes no ha sido probado en un ambilogger para ver lo que ocurre. 
 *  
 */

		Gases.AC_GASES();
	}

}