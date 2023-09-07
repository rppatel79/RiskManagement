package com.rp.risk_management.analytics.security.options;

import com.rp.risk_management.model.Option;
import dev.peterrhodes.optionpricing.OptionBuilder;
import dev.peterrhodes.optionpricing.PricingModel;
import dev.peterrhodes.optionpricing.PricingModelSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoxRossRubinsteinPricer implements OptionPricer {
    private static final Logger logger_ = LogManager.getLogger(CoxRossRubinsteinPricer.class);
    private final double optionPrice_;

    /**
     * Constructor to initialise a binomial tree using an option.
     * Infers parameters from the option.
     *
     * @param option the option to price
     */
    public CoxRossRubinsteinPricer(int timeSteps, Option option, double rate, double volatility, double dividendYield) {
        PricingModel pricingModel = PricingModelSelector.coxRossRubinstein(timeSteps);
        dev.peterrhodes.optionpricing.Option option1 = buildOption(option, rate, volatility, dividendYield);
        optionPrice_ = pricingModel.price(option1);
    }

    private static dev.peterrhodes.optionpricing.Option buildOption(Option option, double rate, double volatility, double dividendYield) {
        OptionBuilder ob = new OptionBuilder(option.getInitialStockPrice(),
                option.getStrike(),
                option.getTimeToMaturity(),
                volatility,
                rate,
                dividendYield);
        if (option.getOptionStyle() == Option.OptionStyle.American)
            ob.styleAmerican();
        else if (option.getOptionStyle() == Option.OptionStyle.European)
            ob.styleEuropean();
        else
            throw new UnsupportedOperationException("The model does not support [" + option.getOptionStyle() + "]");
        if (option.getOptionType() == Option.OptionType.Put)
            ob.typePut();
        else if (option.getOptionType() == Option.OptionType.Call)
            ob.typeCall();
        return ob.build();
    }

    @Override
    public double getOptionPrice() {
        return optionPrice_;
    }
}
