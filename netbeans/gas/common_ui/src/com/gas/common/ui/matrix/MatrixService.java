/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.matrix;

import com.gas.common.ui.matrix.api.MatrixList;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.matrix.api.IMatrixService;
import static com.gas.common.ui.matrix.api.IMatrixService.GONNET_PAM_250;
import static com.gas.common.ui.matrix.api.IMatrixService.IUB;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMatrixService.class)
public class MatrixService implements IMatrixService {

    MatrixList all = new MatrixList();

    @Override
    public MatrixList getAllMatrices() {
        if (all.isEmpty()) {
            all = new MatrixList();
            all.add(new Matrix(IUB, true, Data.iubDNA));
            all.add(new Matrix(CLUSTALW, true, Data.clustalwDNA));
            all.add(new Matrix(IDENTITY, false, Data.identityProtein));
            all.add(new Matrix(GONNET_PAM_80, false, Data.gon80mt));
            all.add(new Matrix(GONNET_PAM_120, false, Data.gon120mt));
            all.add(new Matrix(GONNET_PAM_250, false, Data.gon250mt));
            all.add(new Matrix(GONNET_PAM_350, false, Data.gon350mt));
        }
        return all;
    }

    @Override
    public Matrix getDefaultProteinMatrix() {
        return getAllMatrices().getMatrix(GONNET_PAM_250);
    }

    @Override
    public Matrix getDefaultDnaMatrix() {
        return getAllMatrices().getMatrix(IUB);
    }
}
