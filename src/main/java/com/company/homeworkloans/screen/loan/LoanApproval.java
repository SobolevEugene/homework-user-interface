package com.company.homeworkloans.screen.loan;

import com.company.homeworkloans.app.LoanService;
import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@UiController("Loan.approval")
@UiDescriptor("loan-approval.xml")
@LookupComponent("loansTable")
public class LoanApproval extends StandardLookup<Loan> {

	private Loan currentLoan;
	@Autowired
	private CollectionLoader<Loan> clientOtherLoansDl;
	@Autowired
	private CollectionLoader<Loan> loansDl;
	@Autowired
	private CollectionContainer<Loan> clientOtherLoansDc;
	@Autowired
	private LoanService loanService;
	@Autowired
	private Notifications notifications;

	@Install(to = "loansTable.[client.age]", subject = "columnGenerator")
	private Component loansTableClientAgeColumnGenerator(final Loan loan) {
		String age = String.valueOf(LocalDate.now().minusYears(loan.getClient().getBirthDate().getYear()).getYear());
		return new Table.PlainTextCell(age);
	}

	@Subscribe
	public void onAfterShow(final AfterShowEvent event) {
		clearClientOtherLoansTable();
	}


	@Subscribe(id = "loansDc", target = Target.DATA_CONTAINER)
	public void onLoansDcItemChange(final InstanceContainer.ItemChangeEvent<Loan> event) {
		currentLoan = event.getItem();
		if (currentLoan == null) {
			clearClientOtherLoansTable();
			return;
		} else {
			clientOtherLoansDl.setParameter("clientId", currentLoan.getClient().getId());
			clientOtherLoansDl.setParameter("loanId", currentLoan.getId());
		}

		clientOtherLoansDl.load();
	}


	private void clearClientOtherLoansTable() {
		clientOtherLoansDc.getMutableItems().clear();
	}

	@Subscribe("loansTable.approveAction")
	public void onLoansTableApproveAction(final Action.ActionPerformedEvent event) {
		if (currentLoan!=null) {
			loanService.updateLoanStatus(currentLoan, LoanStatus.APPROVED);
			loansDl.load();
			notifications.create().withCaption("Approved").show();
		}

	}

	@Subscribe("loansTable.rejectAction")
	public void onLoansTableRejectAction(final Action.ActionPerformedEvent event) {
		if (currentLoan!=null) {
			loanService.updateLoanStatus(currentLoan, LoanStatus.REJECTED);
			loansDl.load();
			notifications.create().withCaption("Rejected").show();
		}

	}


}