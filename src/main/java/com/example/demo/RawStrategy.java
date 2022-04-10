package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Indicator;
import org.ta4j.core.Rule;

public class RawStrategy {

	public String indicator1;

	public String indicator2;

	public String rule;

	public int getPeriod1() {
		return period1;
	}

	public void setPeriod1(int period1) {
		this.period1 = period1;
	}

	public int getPeriod2() {
		return period2;
	}

	public void setPeriod2(int period2) {
		this.period2 = period2;
	}

	public int period1;

	public int period2;

	public RawStrategy(String indicator1, String indicator2, String rule) {
		super();
		this.indicator1 = indicator1;
		this.indicator2 = indicator2;
		this.rule = rule;
	}

	public RawStrategy() {
		super();
	}

	public String getIndicator1() {
		return indicator1;
	}

	public void setIndicator1(String indicator1) {
		this.indicator1 = indicator1;
	}

	public String getIndicator2() {
		return indicator2;
	}

	public void setIndicator2(String indicator2) {
		this.indicator2 = indicator2;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}
