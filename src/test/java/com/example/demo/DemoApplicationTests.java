package com.example.demo;


import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
//	@Test
//	public void testCreateRule() throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		RawStrategy strategy = new RawStrategy("Moving Average", "Moving Average", "CrossedDownIndicatorRule");
//		System.out.println("rule: " + new Strategys().createRule(strategy).toString());
//		assertEquals(strategy.getRule(), "CrossedDownIndicatorRule");
//	}
	@Test
	public void testWriteToCsv() throws IOException {
		BarSeries series = TechAnalyz.loadBitstampSeries("EURUSD-copy");
		BarSeriesManager seriesManager = new BarSeriesManager(series);
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		Indicator ind1 = new EMAIndicator(closePrice, 10);
		Indicator ind2 = new EMAIndicator(closePrice, 50);
		Rule entryRule = new OverIndicatorRule(ind1, ind2);
		Rule exitRule = new UnderIndicatorRule(ind1, ind2);
		Strategy strat = new BaseStrategy(entryRule, exitRule);
		TradingRecord tradingRecord = seriesManager.run(strat);
		strats.perfomAndWriteResult(strat);
		assertTrue(tradingRecord.getPositionCount()>0);
		System.out.println("tradingrecord "+tradingRecord.getPositionCount());
		
	}
}
