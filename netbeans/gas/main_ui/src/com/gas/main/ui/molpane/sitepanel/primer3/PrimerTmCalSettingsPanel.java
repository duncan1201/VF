/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.domain.core.primer3.UserInput;

/**
 *
 * @author dq
 */
public class PrimerTmCalSettingsPanel extends TmCalSettingsPanel {

    static final String PRIMER_DNA_CONC = UserInput.PRIMER_DNA_CONC;
    static final String PRIMER_SALT_MONOVALENT = UserInput.PRIMER_SALT_MONOVALENT;
    static final String PRIMER_DNTP_CONC = UserInput.PRIMER_DNTP_CONC;
    static final String PRIMER_SALT_DIVALENT = UserInput.PRIMER_SALT_DIVALENT;
    static final String PRIMER_SALT_CORRECTIONS = UserInput.PRIMER_SALT_CORRECTIONS;
    static final String PRIMER_TM_FORMULA = UserInput.PRIMER_TM_FORMULA;
    
    public static final String[] NAMES = {PRIMER_DNA_CONC, PRIMER_SALT_MONOVALENT, PRIMER_DNTP_CONC, PRIMER_SALT_DIVALENT, PRIMER_SALT_CORRECTIONS, PRIMER_TM_FORMULA};

    @Override
    public void updateUserInputFromUI(UserInput userInput) {
        String _dnaConc = getDnaConc();
        String _saltMonovalent = getSaltMonovalent();
        String _dntpConc = getDntpConc();
        String _saltDivalent = getSaltDivalent();

        userInput.set(PRIMER_DNA_CONC, _dnaConc);
        userInput.set(PRIMER_SALT_MONOVALENT, _saltMonovalent);
        userInput.set(PRIMER_DNTP_CONC, _dntpConc);
        userInput.set(PRIMER_SALT_DIVALENT, _saltDivalent);


        SaltCorrectionFormula formula = SaltCorrectionFormula.getByName((String) saltCorrections.getSelectedItem());
        userInput.set(PRIMER_SALT_CORRECTIONS, formula.getValue().toString());

        ThermoParam thermoParam = ThermoParam.getByName((String) thermoDynamics.getSelectedItem());
        userInput.set(PRIMER_TM_FORMULA, thermoParam.getValue().toString());
    }

    @Override
    public void populateUI(UserInput userInput) {
        String _dnaConc = userInput.get(PRIMER_DNA_CONC);
        if (!_dnaConc.isEmpty()) {
            dnaConc.setValue(Float.parseFloat(_dnaConc));
        }

        String _saltMonovalent = userInput.get(PRIMER_SALT_MONOVALENT);
        if (!_saltMonovalent.isEmpty()) {
            saltMonovalent.setValue(Float.parseFloat(_saltMonovalent));
        }

        String _saltDivalent = userInput.get(PRIMER_SALT_DIVALENT);
        if (!_saltMonovalent.isEmpty()) {
            saltDivalent.setValue(Float.parseFloat(_saltDivalent));
        }

        String _dntpConc = userInput.get(PRIMER_DNTP_CONC);
        if (!_saltMonovalent.isEmpty()) {
            dntpConc.setValue(Float.parseFloat(_dntpConc));
        }

        String _saltFormula = userInput.get(PRIMER_SALT_CORRECTIONS);
        if (!_saltFormula.isEmpty()) {
            SaltCorrectionFormula formula = SaltCorrectionFormula.getByValue(Integer.parseInt(_saltFormula));
            saltCorrections.setSelectedItem(formula.getName());
        }

        String _thermoFormula = userInput.get(PRIMER_TM_FORMULA);
        if (!_thermoFormula.isEmpty()) {
            ThermoParam param = ThermoParam.getByValue(Integer.parseInt(_thermoFormula));
            thermoDynamics.setSelectedItem(param.getName());
        }
    }
}
