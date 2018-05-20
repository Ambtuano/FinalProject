package pkgApp.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import javafx.beans.value.*;

import pkgApp.RetirementApp;
import pkgCore.Retirement;

public class RetirementController implements Initializable {

	private RetirementApp mainApp = null;
	@FXML
	private TextField txtSaveEachMonth;
	@FXML
	private TextField txtYearsToWork;
	@FXML
	private TextField txtAnnualReturnWorking;
	@FXML
	private TextField txtWhatYouNeedToSave;
	@FXML
	private TextField txtYearsRetired;
	@FXML
	private TextField txtAnnualReturnRetired;
	@FXML
	private TextField txtRequiredIncome;
	@FXML
	private TextField txtMonthlySSI;

	private HashMap<TextField, String> hmTextFieldRegEx = new HashMap<TextField, String>();

	public RetirementApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(RetirementApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Adding an entry in the hashMap for each TextField control I want to validate
		// with a regular expression
		// "\\d*?" - means any decimal number
		// "\\d*(\\.\\d*)?" means any decimal, then optionally a period (.), then
		// decimal
		
		hmTextFieldRegEx.put(txtYearsToWork, "[0-9]|[0-3][0-9]|40?"); //Whole number from 0 - 40
		hmTextFieldRegEx.put(txtAnnualReturnWorking, "0*(\\.0[0-9]*)?|0.1?"); // decimal and number 0 - 0.1
		hmTextFieldRegEx.put(txtYearsRetired, "[0-9]|[0-1][0-9]?|20"); // decimal only from 0 - 20
		hmTextFieldRegEx.put(txtAnnualReturnRetired,"0*(\\.0[0-9]*)?|0.1?"); // decimal and numbers 0 - 0.1
		hmTextFieldRegEx.put(txtRequiredIncome, "2[6-9][4-9][2-9]?|[3-9][0-9][0-9][0-9]?|10000?"); // decimal only from 2642 - 10000
		hmTextFieldRegEx.put(txtMonthlySSI,"0*?|[0-9][0-9][0-9]|[0-9][0-9]|[0-9]|[0-1][0-9][0-9][0-9]?|2[0-5][0-9][0-9]|26[0-3][0-9]?|264[0-2]?" ); // decimal only from 0 - 2642
		
		// Check out these pages (how to validate controls): 
		// https://stackoverflow.com/questions/30935279/javafx-input-validation-textfield
		// https://stackoverflow.com/questions/40485521/javafx-textfield-validation-decimal-value?rq=1
		// https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0
		// There are some examples on how to validate / check format

		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();

			txtField.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
						Boolean newPropertyValue) {
					// If newPropertyValue = true, then the field HAS FOCUS
					// If newPropertyValue = false, then field HAS LOST FOCUS
					if (!newPropertyValue) {
						if (!txtField.getText().matches(strRegEx)) {
							txtField.setText("");
							txtField.requestFocus();
						}
					}
				}
			});
		}
		
//		a.	Years To Work – Whole number between 0 and 40.
//		b.	Annual Return (both fields) – Decimal number between 0 and 10%
//		c.	Years Retired – Whole number between 0 and 20.
//		d.	Required Income – Whole number between 2642 and 10,000.
//		e.	Monthly SSI – Whole number between 0 and 2642


	}

	@FXML
	public void btnClear(ActionEvent event) {
		
		System.out.println("Clear pressed");

		/*// disable read-only controls
		txtSaveEachMonth.setDisable(true);
		txtWhatYouNeedToSave.setDisable(true);

		// Clear, enable txtYearsToWork
		txtYearsToWork.clear();
		txtYearsToWork.setDisable(false);*/
		
		Iterator it = hmTextFieldRegEx.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			TextField txtField = (TextField) pair.getKey();
			String strRegEx = (String) pair.getValue();
			
			txtField.clear(); // Clears all text fields
		} 
	}

	@FXML
	public void btnCalculate(ActionEvent event) {
		
		int iYearsToWork = Integer.parseInt(txtYearsToWork.getText());
		double dAnnualReturnWorking = Double.valueOf(txtAnnualReturnWorking.getText());

		int iYearsRetired = Integer.parseInt(txtYearsRetired.getText());
		double dAnnualReturnRetired = Double.valueOf(txtAnnualReturnRetired.getText());
		double dRequiredIncome = Double.valueOf(txtRequiredIncome.getText());
		double dMonthlySSI = Double.valueOf(txtMonthlySSI.getText());
		
		Retirement retirement = new Retirement(iYearsToWork, dAnnualReturnWorking, iYearsRetired, dAnnualReturnRetired,
			dRequiredIncome, dMonthlySSI); 
		txtSaveEachMonth.clear(); 
		txtWhatYouNeedToSave.clear(); 
		
		System.out.println("calculating");

		txtSaveEachMonth.setDisable(false);
		txtWhatYouNeedToSave.setDisable(false);
		
		String whatYouNeedToSave = Double.toString(Math.abs(retirement.TotalAmountToSave()));
		String saveEachMonth = Double.toString(retirement.MonthlySavings());
		
		txtWhatYouNeedToSave.setText(whatYouNeedToSave);
		txtSaveEachMonth.setText(saveEachMonth);
		
		

	}
}
