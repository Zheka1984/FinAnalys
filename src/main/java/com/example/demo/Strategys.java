package com.example.demo;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.sjwimmer.ta4jchart.chartbuilder.IndicatorConfiguration.Builder.of;
import org.sjwimmer.ta4jchart.chartbuilder.TacChartBuilder;

import java.util.ArrayList;
import java.util.List;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Position;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.Returns;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.*;


import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.time.ZonedDateTime;

public class Strategys {
	String aktiv = "RTSI";
	BarSeries series = TechAnalyz.loadBitstampSeries(aktiv);
    Strategy strategy = null;
    TradingRecord tradingRecord = null;
    BarSeriesManager seriesManager = new BarSeriesManager(series);
    ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
//    static int shortEmaValue = 10;
//    static int longEmaValue = 50;
//	static EMAIndicator shortEma = new EMAIndicator(closeprice, shortEmaValue);
//      static EMAIndicator longEma = new EMAIndicator(closeprice, longEmaValue);

//       static StochasticOscillatorKIndicator stochasticOscillK = new StochasticOscillatorKIndicator(series, 14);

//      static MACDIndicator macd = new MACDIndicator(closeprice, 15, 50);
//      static EMAIndicator emaMacd = new EMAIndicator(macd, 18);
 
public String getAktiv() {
		return aktiv;
	}
	public void setAktiv(String aktiv) {
		this.aktiv = aktiv;
	}
	public BarSeries getSeries() {
		return series;
	}
	public void setSeries(BarSeries series) {
		this.series = series;
	}
	public Strategy getStrategy() {
		return strategy;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	public TradingRecord getTradingRecord() {
		return tradingRecord;
	}
	public void setTradingRecord(TradingRecord tradingRecord) {
		this.tradingRecord = tradingRecord;
	}
	public BarSeriesManager getSeriesManager() {
		return seriesManager;
	}
	public void setSeriesManager(BarSeriesManager seriesManager) {
		this.seriesManager = seriesManager;
	}
	public ClosePriceIndicator getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(ClosePriceIndicator closePrice) {
		this.closePrice = closePrice;
	}
	//	public static Strategy simpleStrategy(BarSeries series) {
//	        // Entry rule
//	        Rule entryRule = new OverIndicatorRule(shortEma, longEma) // Trend
//	                .and(new CrossedDownIndicatorRule(stochasticOscillK, 10)) // Signal 1
//	                .and(new OverIndicatorRule(macd, emaMacd)); // Signal 2
//	        
//	        // Exit rule
//	        Rule exitRule = new UnderIndicatorRule(shortEma, longEma) // Trend
//	                .and(new CrossedUpIndicatorRule(stochasticOscillK, 80)) // Signal 1
//	                .and(new UnderIndicatorRule(macd, emaMacd)); // Signal 2
//	        
//	        return new BaseStrategy(entryRule, exitRule);
//	}
	public Rule createRule(RawStrategy strategy) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Rule rule = null;
		Indicator indicator1 = null;
		Indicator indicator2 = null;
		switch(strategy.getIndicator1()) {
		case("Exponential Moving Average"):
			indicator1 = new EMAIndicator(closePrice, strategy.getPeriod1());
		    break;
		case("MACD"):
			indicator1 = new MACDIndicator(closePrice, strategy.getPeriod1(), strategy.getPeriod2());
		    break;
		}
		switch(strategy.getIndicator2()) {
		case("Exponential Moving Average"):
			indicator2 = new EMAIndicator(closePrice, strategy.getPeriod1());
		    break;
		case("MACD"):
			indicator2 = new MACDIndicator(closePrice, strategy.getPeriod1(), strategy.getPeriod2());
		    break;
		}
		
		switch(strategy.getRule()) {
		case("CrossedDownIndicatorRule"):
			rule = new CrossedDownIndicatorRule(indicator1, indicator2);
		    break;
		case("CrossedUpIndicatorRule"):
			rule = new CrossedUpIndicatorRule(indicator1, indicator2);
	        break;
		case("IsEqualRule"):
			rule = new IsEqualRule(indicator1, indicator2);
		    break;
		case("OverIndicatorRule"):
			rule = new OverIndicatorRule(indicator1, indicator2);
		    break;    
		case("UnderIndicatorRule"):
			rule = new UnderIndicatorRule(indicator1, indicator2);
		    break;        
		}
		return rule;
	}
	public Strategy workingStrategy(ListWrapper rules) throws NoSuchMethodException, SecurityException, ClassNotFoundException, 
	InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Rule entryRule = createRule(rules.getList1().get(0));
		Rule exitRule = createRule(rules.getList2().get(0));
		for(int i = 1; i < rules.getList1().size(); i++) {
			entryRule = entryRule.and(createRule(rules.getList1().get(i)));
		}
		for(int i = 1; i < rules.getList2().size(); i++) {
			exitRule = exitRule.and(createRule(rules.getList2().get(i)));
		}
		return new BaseStrategy(entryRule, exitRule);
	}
	public void perfomAndWriteResult(Strategy bstr) throws IOException {
		List<Position> listPoz = null;
		tradingRecord = seriesManager.run(bstr);
		listPoz = tradingRecord.getPositions();
		writeResultToCSV(listPoz);
	}
//	public void paintStrategy() {
//		final Strategy strategy = simpleStrategy(series);
//		final TradingRecord tradingRecord = new BarSeriesManager(series).run(strategy);
//		final Returns returns = new Returns(series, tradingRecord, Returns.ReturnType.ARITHMETIC);
//		TacChartBuilder.of(series)
//	    .withIndicator(
//	        of(shortEma)
//	            .name("Short Ema")
//	            .color(Color.BLUE))
//	    .withIndicator(
//	            of(longEma)
//	                .name("Long Ema")
//	                .color(Color.GRAY))
//	    .withTradingRecord(tradingRecord)
//	    .buildAndShow();
//	}
	//приведение даты к формату 01, 02 и т.д. при необходимости
	public static String changeDate(int date) {
	 String i = date<10 ? "0"+String.valueOf(date) : String.valueOf(date);
		return i;
	}
	//приведение года к сокращенному виду без столетия
	public static String changeYear(int year) {
	 if(year >= 2000) return String.valueOf(year-2000);
	 if(year >= 1900) return String.valueOf(year - 1900);
	 return String.valueOf(year);
	}
	//формирование строки с временем (в формате %d-%m-%y), типом сделки, ценой и количеством 
//	для записи в массив
	public static String[] lineForArray(ZonedDateTime zdtime, Trade trade) {
		String time = changeDate(zdtime.getDayOfMonth())+"-"+changeDate(zdtime.getMonthValue())
		+"-"+changeYear(zdtime.getYear());
		String type = trade.getType().toString();
		String price = String.valueOf(trade.getPricePerAsset().doubleValue());
		String quantity = String.valueOf(trade.getAmount().intValue());
		return new String[] {time, type, price, quantity};
	}
	
	public void writeResultToCSV(List<Position> listPoz) throws IOException {
		Path path = Paths.get("C:\\Users\\Админ\\eclipse-workspace\\demo\\src\\main\\resources\\listPoz.csv");
		List<String[]> ar = new ArrayList<>();
		ar.add(new String[] {"time", "type", "price", "quantity"});
		CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
		listPoz.forEach(t -> {
			ZonedDateTime zdtime = series.getBar(t.getEntry().getIndex()).getEndTime();
			ar.add(lineForArray(zdtime, t.getEntry()));
			ZonedDateTime zdtime1 = series.getBar(t.getExit().getIndex()).getEndTime();
			ar.add(lineForArray(zdtime1, t.getExit()));		
		});
//		System.out.println(path.toString());
		writer.writeAll(ar);
	     writer.close();
	}

}
