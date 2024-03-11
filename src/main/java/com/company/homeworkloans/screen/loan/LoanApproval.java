package com.company.homeworkloans.screen.loan;

import io.jmix.ui.component.Component;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@UiController("Loan.approval")
@UiDescriptor("loan-approval.xml")
@LookupComponent("loansTable")
public class LoanApproval extends StandardLookup<Loan> {
	@Autowired
	private GroupTable<Loan> loansTable;

	@Install(to = "loansTable.[client.age]", subject = "columnGenerator")
	private Component loansTableClientAgeColumnGenerator(final Loan loan) {
		String age = String.valueOf(LocalDate.now().minusYears(loan.getClient().getBirthDate().getYear()).getYear());
		return new Table.PlainTextCell(age);
	}

}