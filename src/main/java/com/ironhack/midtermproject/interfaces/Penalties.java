package com.ironhack.midtermproject.interfaces;

import com.ironhack.midtermproject.utils.Money;

public interface Penalties {

    public void applyPenalty(Long accountId, Money startBalance);

}
