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

public class CO2modAntiguo {
	
	public static void AC_CO2modAntiguo()  throws IOException {
		
		Integer diferenciaZonaHoraria = 4;
//		Variables de salida del excel
		String nombreXID = "DP_ASEG_GASES"; 
		String nombreResultado = "ResultadosCalibraciones Gases";
		String nombrePunto = "aseguramientoDeCalidadGases";
//		Rutas de acceso a excel
		String rutaArchivoEntrada = "C:/Users/lithi/Downloads/AseguramientoDeCalidad/AC-CO2-Agosto-2020.xlsx";
		String rutaArchivoSalida = "C:/Users/lithi/Downloads/AseguramientoDeCalidad/SalidaAC-CO2.xlsx";
		
		ArrayList<String> preValores = new ArrayList();
		ArrayList<String> valores = new ArrayList();
		ArrayList<String> horas = new ArrayList();
		ArrayList<Double> valorHoras2 = new ArrayList();

		String excelFilePath = rutaArchivoEntrada;

		File archivo = new File(rutaArchivoSalida);

		Workbook workbook2 = new XSSFWorkbook();

		Sheet pagina = workbook2.createSheet("Valores de punto 0");

		CellStyle style = workbook2.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		String[] titulos = { "XID de punto de datos ", "Nombre de dispositivo ", "Nombre de punto ", "Hora ", "Valor ",
				"Generada ", "Anotación ", "Modificar (agregar/eliminar) " };
		String[] datos2 = { nombreXID, nombreResultado, nombrePunto, "", "", "", "",
				"" };
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
						}
						horas.add(cell.getStringCellValue());
						break;
					case NUMERIC:
						if (Double.toString(cell.getNumericCellValue()).length() == 0
								| Double.toString(cell.getNumericCellValue()).isBlank()
								| Double.toString(cell.getNumericCellValue()).isEmpty()) {
							preValores.add(null);
						} else {
							preValores.add(Double.toString(cell.getNumericCellValue()));
						}
						horas.add(String.valueOf(Double.toString(cell.getNumericCellValue())));
						break;
					case BLANK:
						preValores.add(null);
						horas.add(String.valueOf(Double.toString(cell.getNumericCellValue())));
						break;
					case ERROR:
						preValores.add(null);
						horas.add(String.valueOf(Double.toString(cell.getNumericCellValue())));
						break;
					case _NONE:
						preValores.add(null);
						horas.add(String.valueOf(Double.toString(cell.getNumericCellValue())));
						break;

					}
				}

				/*
				 * Bloque de posible solucion
				 */
				int puntoFlotanteFecha = preValores.get(1).indexOf(".");
				int puntoFlotante2;
				
				DecimalFormat fechaFormateada = new DecimalFormat("####");
				
				String concatFechaHora = preValores.get(1).substring(0, puntoFlotanteFecha);
				
				/* Agrega 5 horas y 1 minuto mas a la celda hora */
				Double fechaDou;
				Double difMinutos = 0.00084; 	// -> 55 segundos aprox excel
				Double difHoras = 0.0415; 		// -> 1 Hora aprox excel 
				Double fechafinal;

				/* Variables JSON dentro del excel */
				String fechaRegistro = concatFechaHora;		//Fecha registro
				String horaI = concatFechaHora;				//Hora inicio
				String horaIRLA = concatFechaHora;			//Hora inicio registro lectura analizador
				String horaF = concatFechaHora;				//Hora final
				String horaFRLA = concatFechaHora;			//Hora final registro lectura analizador 2
			
				String concatFinal = concatFechaHora;

				// En caso de que venga celdas vacias no genera el error y el valor de las horas queda con el valor de la fecha en que se creo.
				// y en el caso de existir dato se transforma a un valor concatenado con la fecha, listo para ser convertido en timestamps
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
					fechafinal = fechaDou + difMinutos + (difHoras*diferenciaZonaHoraria);
					fechaRegistro = String.valueOf(fechafinal);
					
					fechaDou = Double.parseDouble(horaI);
					fechafinal = fechaDou + difMinutos + (difHoras*diferenciaZonaHoraria);
					horaI = String.valueOf(fechafinal);
					
					fechaDou = Double.parseDouble(horaIRLA);
					fechafinal = fechaDou + difMinutos + (difHoras*diferenciaZonaHoraria);
					horaIRLA = String.valueOf(fechafinal);
					
					fechaDou = Double.parseDouble(horaF);
					fechafinal = fechaDou + difMinutos + (difHoras*diferenciaZonaHoraria);
					horaF = String.valueOf(fechafinal);
					
					fechaDou = Double.parseDouble(horaFRLA);
					fechafinal = fechaDou + difMinutos + (difHoras*diferenciaZonaHoraria);
					horaFRLA = String.valueOf(fechafinal);
					
				}

				/* Concatenaciones se convierten a double */
				Double fechaRegistroConvertida = ( Double.parseDouble(fechaRegistro) - 25569.0 + (5/24) ) * 86400;
				Double horaIConvertida = ( Double.parseDouble(horaI) - 25569.0 + (5/24) ) * 86400;
				Double horaIRLAConvertida = ( Double.parseDouble(horaIRLA) - 25569.0 + (5/24) ) * 86400;
				Double horaFConvertida = ( Double.parseDouble(horaF) - 25569.0 + (5/24) ) * 86400;
				Double horaFRLAConvertida = ( Double.parseDouble(horaFRLA) - 25569.0 + (5/24) ) * 86400;
				
				/* Formateo de double para que quede en numero 1600000000 + 000 equivalente a milisegundos del timestaps*/
				String fechaRegistroString = (String.valueOf(fechaFormateada.format(fechaRegistroConvertida)))+"000";
				String horaIString = (String.valueOf(fechaFormateada.format(horaIConvertida)))+"000";
				String horaIRLAString = (String.valueOf(fechaFormateada.format(horaIRLAConvertida)))+"000";
				String horaFString = (String.valueOf(fechaFormateada.format(horaFConvertida)))+"000";
				String horaFRLAString = (String.valueOf(fechaFormateada.format(horaFRLAConvertida)))+"000";
				
				if (preValores.get(8) == null ){
					fechaRegistroString = preValores.get(19);
					horaIString = preValores.get(3);
					horaIRLAString = preValores.get(8);
					horaFString = preValores.get(14);
					horaFRLAString= preValores.get(19);
				}
				
				/*
				 * Fin del Bloque de posible solucion
				 */

				String hor = horas.get(1);
				
//				***********************************				
//				********** ATENTO AQUI ************
//				***********************************
				String dato = 
						"{\"fechaRegistros\":" + fechaRegistroString
						+ ",\"CO2\":{" + "\"nivelCero\":{"
						+ "\"numCilindro\":" + preValores.get(2) + "," 
						+ "\"horaInicio\":" + horaIString + ","
						+ "\"concentracionNivelPatron\":" + preValores.get(4) + "," 
						+ "\"porcentajeIncertidumbre\":"+ preValores.get(6) + "," 
						+ "\"vencimiento\":" + preValores.get(7) + "," 
						+ "\"horaRegistroLectura\":" + horaIRLAString + "," 
						+ "\"escalaAnalizador\":" + preValores.get(9) + "," 
						+ "\"valorLectura\":" + preValores.get(10) + "," 
						+ "\"porcentajeNivel\":" + preValores.get(5) + "," 
						+ "\"diferencia\":" + preValores.get(11) + "," 
						+ "\"error\":" + preValores.get(12) + "}," 
						+ "\"nivelSpan\":{" + "\"numCilindro\":" + preValores.get(13) + "," 
						+ "\"horaInicio\":" + horaFString + ","
						+ "\"concentracionNivelPatron\":" + preValores.get(15) + "," 
						+ "\"porcentajeIncertidumbre\":" + preValores.get(17) + "," 
						+ "\"vencimiento\":" + preValores.get(18) + "," 
						+ "\"horaRegistroLectura\":" + horaFRLAString + "," 
						+ "\"escalaAnalizador\":" + preValores.get(20) + "," 
						+ "\"valorLectura\":" + preValores.get(21) + "," 
						+ "\"porcentajeNivel\":" + preValores.get(16) + ","
						+ "\"diferencia\":" + preValores.get(22) + "," 
						+ "\"error\":" + preValores.get(23) 
						+ "}}}";

				valores.add(dato);

				/* var toma un valor listo para ser mostrado en EXCEL como fecha */
				var = Double.parseDouble(concatFinal);
				valorHoras2.add(var);
				horas.clear();
				preValores.clear();
				iterador++;
			}

			Row fila = pagina.createRow(0);

			for (int i = 0; i < titulos.length; i++) {
				Cell celda = fila.createCell(i);
				celda.setCellStyle(style);
				celda.setCellValue(titulos[i]);
				Font font = workbook2.createFont();
				font.setBold(true);
				style.setFont(font);
			}

			for (int i = 0; i < valores.size(); i++) {
				fila = pagina.createRow(i + 1);
				for (int j = 0; j < datos2.length; j++) {
					String[] datos3 = { nombreXID, nombreResultado, nombrePunto, "",
							valores.get(i), valores.get(i), "", "add" };
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
