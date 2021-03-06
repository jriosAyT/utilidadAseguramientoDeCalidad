package adc.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import adc.types.gases.CO;
import adc.types.gases.CO2;
import adc.types.gases.COH;
import adc.types.gases.NOx;
import adc.types.gases.O2;
import adc.types.gases.SO2;

public class Gases {

	/*
	 * ruta NOX, ruta CO2, ruta O2, ruta SO2
	 */
	public static void AC_GASES() throws IOException {

		Integer diferenciaZonaHoraria = 4;
//		Variables de salida del excel
		String nombreXID = "DP_ASEG_GASES";
		String nombreResultado = "ResultadosCalibraciones Gases";
		String nombrePunto = "aseguramientoDeCalidadGases";

//		Rutas de acceso a excel
		String rutaArchivoPrincipal = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-NOX.xlsx";
		String rutaArchivoCO2 = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-CO2.xlsx";
		String rutaArchivoO2 = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-O2.xlsx";
		String rutaArchivoSO2 = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-SO2.xlsx";
		String rutaArchivoCO = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-CO.xlsx";
		String rutaArchivoCOH = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/AC-COH.xlsx";

		String rutaArchivoSalida = "C:/Users/lithi/Downloads/AseguramientoDeCalidad2/SalidaAC-GASES.xlsx";

		ArrayList<String> preValores = new ArrayList();
		ArrayList<Double> valorHoras2 = new ArrayList();

		ArrayList<String> union = new ArrayList();
//		lista de datos JSON gases:
		ArrayList<String> datosCabecera = new ArrayList();
		ArrayList<String> datosNOX = new ArrayList();
		ArrayList<String> datosSO2 = new ArrayList();
		ArrayList<String> datosCO2 = new ArrayList();
		ArrayList<String> datosO2 = new ArrayList();
		ArrayList<String> datosCO = new ArrayList();
		ArrayList<String> datosCOH = new ArrayList();

// Datos de otros gases
		datosNOX = NOx.AC_NOx(rutaArchivoPrincipal);
		datosSO2 = SO2.AC_SO2(rutaArchivoSO2);
		datosCO2 = CO2.AC_CO2(rutaArchivoCO2);
		datosO2 = O2.AC_O2(rutaArchivoO2);
//		datosCO = CO.AC_CO(rutaArchivoCO);
//		datosCOH = COH.AC_COH(rutaArchivoCOH);

		String excelFilePath = rutaArchivoPrincipal;

		File archivo = new File(rutaArchivoSalida);

		Workbook workbook2 = new XSSFWorkbook();

		Sheet pagina = workbook2.createSheet("Valores de punto 0");

		CellStyle style = workbook2.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		String[] titulos = { "XID de punto de datos ", "Nombre de dispositivo ", "Nombre de punto ", "Hora ", "Valor ",
				"Generada ", "Anotación ", "Modificar (agregar/eliminar) " };
		String[] datos2 = { nombreXID, nombreResultado, nombrePunto, "", "", "", "", "" };
		// SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		// DataFormatter form = new DataFormatter();
		DataFormat format = workbook2.createDataFormat();
		CellStyle style2 = workbook2.createCellStyle();
		Double var;
		Double cantidadDias;
		Timestamp fechaHora;
		style2.setDataFormat(format.getFormat("dd-mm-yyyy hh:mm"));

		try (FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
				Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet firstSheet = workbook.getSheetAt(0);
			int filas = firstSheet.getLastRowNum();

			int iterador = 4;

			for (int i = 5; i < filas + 2; i++) {
				for (Cell cell : firstSheet.getRow(iterador)) {
					switch (cell.getCellType()) {
					case STRING:
						if (cell.getStringCellValue().isBlank() | cell.getStringCellValue().isEmpty()) {

							preValores.add(cell.getStringCellValue());
						} else {
							preValores.add(cell.getStringCellValue());
						}
						break;
					case NUMERIC:
						if (Double.toString(cell.getNumericCellValue()).length() == 0
								| Double.toString(cell.getNumericCellValue()).isBlank()
								| Double.toString(cell.getNumericCellValue()).isEmpty()) {
							preValores.add(null);
						} else {
							preValores.add(Double.toString(cell.getNumericCellValue()));
						}
						break;
					case BLANK:
						preValores.add(null);
						break;
					case ERROR:
						preValores.add(null);
						break;
					case _NONE:
						preValores.add(null);
						break;

					}
				}

				/*
				 * Bloque de posible solucion
				 */
				int puntoFlotanteFecha = preValores.get(1).indexOf(".");
				int puntoFlotante2;

				DecimalFormat formateoDecimal = new DecimalFormat("####");

				String concatFechaHora = preValores.get(1).substring(0, puntoFlotanteFecha);

				// Para fecha de vencimiento
				String fechaVencimiento1 = preValores.get(7);
				String fechaVencimiento2 = preValores.get(18);

				/* Agrega 5 horas y 1 minuto mas a la celda hora */
				Double fechaDou;
				Double difMinutos = 0.00084; // -> 55 segundos aprox excel
				Double difHoras = 0.0415; // -> 1 Hora aprox excel
				Double fechafinal;

				/* Variables JSON dentro del excel */
				String fechaRegistro = concatFechaHora; // Fecha registro
				String horaI = concatFechaHora; // Hora inicio
				String horaIRLA = concatFechaHora; // Hora inicio registro lectura analizador
				String horaF = concatFechaHora; // Hora final
				String horaFRLA = concatFechaHora; // Hora final registro lectura analizador 2

				String concatFinal = concatFechaHora;

				// Solucion para numero cilindro celda 3 y 14
				String numCilindro1 = null;
				if (preValores.get(2) != null) {
					String x = preValores.get(2);
					if (x.indexOf(".") != -1) {
						Double y = Double.parseDouble(x);
						numCilindro1 = formateoDecimal.format(y);
					} else {
						numCilindro1 = x;
					}
				}
				String numCilindro2 = null;
				if (preValores.get(13) != null) {
					String x = preValores.get(13);
					if (x.indexOf(".") != -1) {
						Double y = Double.parseDouble(x);
						numCilindro2 = formateoDecimal.format(y);
					} else {
						numCilindro2 = x;
					}
				}

				// En caso de que venga celdas vacias no genera el error y el valor de las horas
				// queda con el valor de la fecha en que se creo.
				// y en el caso de existir dato se transforma a un valor concatenado con la
				// fecha, listo para ser convertido en timestamps
				if (preValores.get(3) != null) {
					puntoFlotante2 = preValores.get(19).indexOf(".");
					fechaRegistro = concatFechaHora + preValores.get(19).substring(puntoFlotante2);
					puntoFlotante2 = preValores.get(3).indexOf(".");
					horaI = concatFechaHora + preValores.get(3).substring(puntoFlotante2);
					puntoFlotante2 = preValores.get(8).indexOf(".");
					horaIRLA = concatFechaHora + preValores.get(8).substring(puntoFlotante2);
					puntoFlotante2 = preValores.get(14).indexOf(".");
					horaF = concatFechaHora + preValores.get(14).substring(puntoFlotante2);
					puntoFlotante2 = preValores.get(19).indexOf(".");
					horaFRLA = concatFechaHora + preValores.get(19).substring(puntoFlotante2);

					/* Necesario para agregar las horas y minutos a la celda llamada: HORA */
					fechaDou = Double.parseDouble(horaFRLA);
					fechafinal = fechaDou + difMinutos + difHoras;
					concatFinal = String.valueOf(fechafinal);

					/* Suma de hora deacuerdo a zona horaria */
					fechaDou = Double.parseDouble(fechaRegistro);
					fechafinal = fechaDou + difMinutos + (difHoras * diferenciaZonaHoraria);
					fechaRegistro = String.valueOf(fechafinal);

					fechaDou = Double.parseDouble(horaI);
					fechafinal = fechaDou + difMinutos + (difHoras * diferenciaZonaHoraria);
					horaI = String.valueOf(fechafinal);

					fechaDou = Double.parseDouble(horaIRLA);
					fechafinal = fechaDou + difMinutos + (difHoras * diferenciaZonaHoraria);
					horaIRLA = String.valueOf(fechafinal);

					fechaDou = Double.parseDouble(horaF);
					fechafinal = fechaDou + difMinutos + (difHoras * diferenciaZonaHoraria);
					horaF = String.valueOf(fechafinal);

					fechaDou = Double.parseDouble(horaFRLA);
					fechafinal = fechaDou + difMinutos + (difHoras * diferenciaZonaHoraria);
					horaFRLA = String.valueOf(fechafinal);

				}

				/* Concatenaciones se convierten a double */
				Double fechaRegistroConvertida = (Double.parseDouble(fechaRegistro) - 25569.0 + (5 / 24)) * 86400;
				Double horaIConvertida = (Double.parseDouble(horaI) - 25569.0 + (5 / 24)) * 86400;
				Double horaIRLAConvertida = (Double.parseDouble(horaIRLA) - 25569.0 + (5 / 24)) * 86400;
				Double horaFConvertida = (Double.parseDouble(horaF) - 25569.0 + (5 / 24)) * 86400;
				Double horaFRLAConvertida = (Double.parseDouble(horaFRLA) - 25569.0 + (5 / 24)) * 86400;

				Double fechaVencimientoConvertida1 = (Double.parseDouble(fechaVencimiento1) - 25569.0 + (5 / 24))
						* 86400;
				Double fechaVencimientoConvertida2 = (Double.parseDouble(fechaVencimiento2) - 25569.0 + (5 / 24))
						* 86400;

				/*
				 * Formateo de double para que quede en numero 1600000000 + 000 equivalente a
				 * milisegundos del timestaps
				 */
				String fechaRegistroString = (String.valueOf(formateoDecimal.format(fechaRegistroConvertida))) + "000";
				String horaIString = (String.valueOf(formateoDecimal.format(horaIConvertida))) + "000";
				String horaIRLAString = (String.valueOf(formateoDecimal.format(horaIRLAConvertida))) + "000";
				String horaFString = (String.valueOf(formateoDecimal.format(horaFConvertida))) + "000";
				String horaFRLAString = (String.valueOf(formateoDecimal.format(horaFRLAConvertida))) + "000";

				String fechaVencimientoString1 = (String.valueOf(formateoDecimal.format(fechaVencimientoConvertida1)))
						+ "000";
				String fechaVencimientoString2 = (String.valueOf(formateoDecimal.format(fechaVencimientoConvertida2)))
						+ "000";

				if (preValores.get(3) == null) {
					fechaRegistroString = preValores.get(19);
					horaIString = preValores.get(3);
					horaIRLAString = preValores.get(8);
					horaFString = preValores.get(14);
					horaFRLAString = preValores.get(19);
					fechaVencimientoString1 = preValores.get(7);
					fechaVencimientoString2 = preValores.get(18);
				}

				/*
				 * Fin del Bloque de posible solucion
				 */

				String dato = "{\"fechaRegistros\":" + fechaRegistroString;

				datosCabecera.add(dato);

				/* var toma un valor listo para ser mostrado en EXCEL como fecha */
				var = Double.parseDouble(concatFinal);
				valorHoras2.add(var);
				preValores.clear();
				iterador++;
			}

//********************************************************************************************************
//			      					ITERACION IMPORTANTE: ATENTO AQUI 
//			Iteracion necesaria para agregar los datos de cada uno de los archivos excel subidos.	
//********************************************************************************************************	

			for (int x = 0; x < datosCabecera.size(); x++) {
				union.add(datosCabecera.get(x) 
						+ datosNOX.get(x) 
						+ datosSO2.get(x) 
//						+ datosCO.get(x)
						+ datosCO2.get(x) 
						+ datosO2.get(x)
//						+ datosCOH.get(x)
						+ "}"); //
			}
			
//********************************************************************************************************
//									# ITERACION IMPORTANTE: ATENTO AQUI 
//********************************************************************************************************	
			
//			Escritura EXCEL
			Row fila = pagina.createRow(0);

			for (int i = 0; i < titulos.length; i++) {
				Cell celda = fila.createCell(i);
				celda.setCellStyle(style);
				celda.setCellValue(titulos[i]);
				Font font = workbook2.createFont();
				font.setBold(true);
				style.setFont(font);
			}

			for (int i = 0; i < union.size(); i++) {
				fila = pagina.createRow(i + 1);
				for (int j = 0; j < datos2.length; j++) {
					String[] datos3 = { nombreXID, nombreResultado, nombrePunto, "", union.get(i), union.get(i), "",
							"add" };
					Cell celda = fila.createCell(j);
					Cell co = fila.getCell(3);
					if (fila.getCell(3) != null) {
						co.setCellValue((valorHoras2.get(i)));
						co.setCellStyle(style2);
					}
					celda.setCellValue(datos3[j]);
				}
			}
			pagina.autoSizeColumn(0);
			pagina.autoSizeColumn(1);
			pagina.autoSizeColumn(2);
			pagina.autoSizeColumn(3);

			FileOutputStream salida = new FileOutputStream(archivo);
			workbook2.write(salida);
			workbook2.close();
		}
	}
}
