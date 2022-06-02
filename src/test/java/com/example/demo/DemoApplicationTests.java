package com.example.demo;


import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesManager;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

@SpringBootTest
public class DemoApplicationTests {
	Strategys strats = new Strategys();
	TechAnalyz ta = new TechAnalyz();
	Indicator ind1; 
	Indicator ind2;
	Rule entryRule;
	Rule exitRule;
	Strategy strat;
	BarSeries series;
	BarSeriesManager seriesManager;
	@Before
	public void beforeTest() {
		series = TechAnalyz.loadBitstampSeries("EURUSD-copy");
		seriesManager = new BarSeriesManager(series);
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		ind1 = new EMAIndicator(closePrice, 10);
		ind2 = new EMAIndicator(closePrice, 50);
		entryRule = new OverIndicatorRule(ind1, ind2);
		exitRule = new UnderIndicatorRule(ind1, ind2);
		strat = new BaseStrategy(entryRule, exitRule);
	}
//	@Test
//	public void testCreateRule() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		RawStrategy strategy = new RawStrategy("Moving Average", "Moving Average", "CrossedDownIndicatorRule");
//		System.out.println("rule: " + new Strategys().createRule(strategy).toString());
//		assertEquals(strategy.getRule(), "CrossedDownIndicatorRule");
//	}
	/*
	 * @Test public void testWriteToCsv() throws IOException {
	 * 
	 * TradingRecord tradingRecord = seriesManager.run(strat);
	 * strats.perfomAndWriteResult(strat);
	 * assertTrue(tradingRecord.getPositionCount()>0);
	 * System.out.println("tradingrecord "+tradingRecord.getPositionCount());
	 * 
	 * }
	 */
	@Test
	public void testWorkingStrategy() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		ListWrapper listWr = new ListWrapper();
	    RawStrategy rstrat1 = new RawStrategy();
		RawStrategy rstrat2 = new RawStrategy();
		rstrat1.setIndicator1("Exponential Moving Average");
		rstrat1.setIndicator2("Exponential Moving Average");
		rstrat1.setPeriod1(10);
		rstrat1.setPeriod2(50);
		rstrat1.setRule("OverIndicatorRule");
		rstrat2.setIndicator1("Exponential Moving Average");
		rstrat2.setIndicator2("Exponential Moving Average");
		rstrat2.setPeriod1(10);
		rstrat2.setPeriod2(50);
		rstrat2.setRule("UnderIndicatorRule");
		List<RawStrategy> list1 = new ArrayList<>(); list1.add(rstrat1);
		List<RawStrategy> list2 = new ArrayList<>(); list2.add(rstrat2);
		listWr.setList1(list1);
		listWr.setList2(list2);
		Rule entryRule = strats.createRule(rstrat1);
		Rule exitRule = strats.createRule(rstrat2);
		System.out.println("данные стратегии "+listWr.getList2().get(0).getIndicator1()+" "+listWr.getList2().get(0).getIndicator2()
		+" "+listWr.getList2().get(0).getPeriod1()+" "+listWr.getList2().get(0).getPeriod2()+" "+listWr.getList2().get(0).getRule());
		strats.perfomAndWriteResult(new BaseStrategy(entryRule, exitRule));
		
		
	}
}
