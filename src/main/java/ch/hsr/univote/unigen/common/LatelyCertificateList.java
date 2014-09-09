/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.common;

import ch.bfh.univote.common.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class LatelyCertificateList {

    protected List<Certificate> latelyCertificateList;

    public List<Certificate> getLatelyCertificateList() {
        if (latelyCertificateList == null) {
            latelyCertificateList = new ArrayList<Certificate>();
        }
        return this.latelyCertificateList;
    }

    public void setLatelyCertificateList(List<Certificate> latelyCertificateList) {
        this.latelyCertificateList = latelyCertificateList;
    }
}
