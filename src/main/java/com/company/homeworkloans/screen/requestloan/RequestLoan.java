package com.company.homeworkloans.screen.requestloan;

import com.company.homeworkloans.app.LoanService;
import com.company.homeworkloans.entity.Client;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@UiController("RequestLoan")
@UiDescriptor("request-loan.xml")
public class RequestLoan extends Screen {

	private Client client;
	@Autowired
	private LoanService loanService;
	@Autowired
	private EntityComboBox<Client> clientComboBox;
	@Autowired
	private TextField<Object> amountField;

	@Autowired
	private Notifications notifications;

	@Subscribe
	public void onBeforeShow(final BeforeShowEvent event) {
		if (client != null) {
			clientComboBox.setValue(client);
		}
	}


	@Subscribe("requestAction")
	public void onRequestAction(final Action.ActionPerformedEvent event) {
		var selectedClient = clientComboBox.getValue();
		var amount = (BigDecimal) amountField.getValue();

		if (selectedClient == null || amount == null) {
			notifications.create().withCaption("Values can't be null").show();
			return;
		}

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			notifications.create().withCaption("The amount can't be less or equal zero").show();
			return;
		}

		loanService.loanSave(selectedClient, amount);
		close(StandardOutcome.CLOSE);
	}

	@Subscribe("windowClose")
	public void onWindowClose(final Action.ActionPerformedEvent event) {
		close(StandardOutcome.DISCARD);
	}

	public RequestLoan withClient(Client client) {
		this.client = client;
		return this;
	}

}