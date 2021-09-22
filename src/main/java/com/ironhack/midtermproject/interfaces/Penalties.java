package com.ironhack.midtermproject.interfaces;

import com.ironhack.midtermproject.dao.Money;

public interface Penalties {

    public void applyPenalty(Long accountId, Money startBalance);

}
