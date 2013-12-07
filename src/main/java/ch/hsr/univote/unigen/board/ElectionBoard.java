/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.board;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * OLD CAN BEE DELETED
 * @author Gian
 */
@WebService
public interface ElectionBoard {
    @WebMethod
    String reverse(String value);
}
