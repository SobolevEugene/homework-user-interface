package com.company.homeworkloans.app;

import com.company.homeworkloans.entity.Client;
import com.company.homeworkloans.entity.Loan;
import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.core.DataManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class LoanService {
	private final DataManager dataManager;

	public LoanService(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void loanSave(Client client, BigDecimal amount) {
		var newLoan = dataManager.create(Loan.class);
		newLoan.setAmount(amount);
		newLoan.setClient(client);
		newLoan.setRequestDate(LocalDate.now());
		newLoan.setStatus(LoanStatus.REQUESTED);
		dataManager.save(newLoan);
	}
}