/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.board;

import javax.xml.ws.Endpoint;

/**
 *
 * @author Gian
 */
public class Publisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ElectionBoardService/ElectionBoardServiceImpl",new ElectionBoardWebService());
    }
}
