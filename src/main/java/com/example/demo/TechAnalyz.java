package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;

import com.opencsv.CSVReader;

public class TechAnalyz {

	public static BarSeries loadBitstampSeries(String aktiv) {

		// Reading all lines of the CSV file
		InputStream stream = TechAnalyz.class.getClassLoader().getResourceAsStream(aktiv + ".csv");
		CSVReader csvReader = null;
		List<String[]> lines = null;
		try {
			csvReader = new CSVReader(new InputStreamReader(stream, Charset.forName("UTF-8")), ',');
			lines = csvReader.readAll();
			lines.remove(0); // Removing header line
		} catch (IOException ioe) {
			Logger.getLogger(TechAnalyz.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", ioe);
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		BarSeries series = new BaseBarSeries();
		if ((lines != null) && !lines.isEmpty()) {
//         lines.forEach(t -> Arrays.asList(t).forEach(x -> System.out.println(x)));            
			// Getting the first and last trades timestamps
			ZonedDateTime beginTime = convertDateToZoneDateTime(lines.get(0)[0]);
			ZonedDateTime endTime = convertDateToZoneDateTime(lines.get(lines.size() - 1)[0]);
			if (beginTime.isAfter(endTime)) {
				Instant beginInstant = beginTime.toInstant();
				Instant endInstant = endTime.toInstant();
				beginTime = ZonedDateTime.ofInstant(endInstant, ZoneId.systemDefault());
				endTime = ZonedDateTime.ofInstant(beginInstant, ZoneId.systemDefault());
				// Since the CSV file has the most recent trades at the top of the file, we'll
				// reverse the list to feed
				// the List<Bar> correctly.
				Collections.reverse(lines);
			}
			// build the list of populated bars
			buildSeries(series, beginTime, endTime, 300, lines);
		}

		return series;
	}

	/**
	 * Builds a list of populated bars from csv data.
	 *
	 * @param beginTime the begin time of the whole period
	 * @param endTime   the end time of the whole period
	 * @param duration  the bar duration (in seconds)
	 * @param lines     the csv data returned by CSVReader.readAll()
	 */
	@SuppressWarnings("deprecation")
	private static void buildSeries(BarSeries series, ZonedDateTime beginTime, ZonedDateTime endTime, int duration,
			List<String[]> lines) {

		Duration barDuration = Duration.ofSeconds(duration);
		ZonedDateTime barEndTime = beginTime;
		// line number of trade data
		int i = 0;
		do {
			// build a bar
			barEndTime = barEndTime.plus(barDuration);
			Bar bar = new BaseBar(barDuration, barEndTime, series.function());
			do {
				// get a trade
				String[] tradeLine = lines.get(i);
				ZonedDateTime tradeTimeStamp = convertDateToZoneDateTime(tradeLine[0]);
				// if the trade happened during the bar
				if (bar.inPeriod(tradeTimeStamp)) {
					// add the trade to the bar
					double tradePrice = Double.parseDouble(tradeLine[1]);
					double tradeVolume = Double.parseDouble(tradeLine[2]);
					bar.addTrade(tradeVolume, tradePrice, series.function());
				} else {
					// the trade happened after the end of the bar
					// go to the next bar but stay with the same trade (don't increment i)
					// this break will drop us after the inner "while", skipping the increment
					break;
				}
				i++;
			} while (i < lines.size());
			// if the bar has any trades add it to the bars list
			// this is where the break drops to
			if (bar.getTrades() > 0) {
				series.addBar(bar);
			}
		} while (barEndTime.isBefore(endTime));
	}

	public static ZonedDateTime convertDateToZoneDateTime(String str) {
		ZonedDateTime zdt = null;
//	 ZonedDateTime zdt = ZonedDateTime.parse(str);
//	 return zdt;
		try {
			zdt = ZonedDateTime.parse(str);
			return zdt;
		} catch (DateTimeParseException e) {
			String[] arr = str.split(" ");
			LocalTime time = null;
			// в случае, если график дневной
			if (arr.length == 1) {
				time = LocalTime.of(0, 0);
			} else
				time = LocalTime.parse(arr[1]);
			zdt = ZonedDateTime.of(LocalDate.parse(arr[0], DateTimeFormatter.ofPattern("d-L-u")), time,
					ZoneId.of("GMT+0"));
//		 System.out.println(zdt.toString());
			return zdt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zdt;
	}
}
