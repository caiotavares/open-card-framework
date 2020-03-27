/*
 * Copyright © 2000 Gemplus
 * Av. du Pic de Bertagne - Parc d'Activités de Gémenos
 * BP 100 - 13881 Gémenos CEDEX
 * 
 * "Code derived from the original OpenCard Framework".
 * 
 * Everyone is allowed to redistribute and use this source  (source
 * code)  and binary (object code),  with or  without modification,
 * under some conditions:
 * 
 * - Everyone  must  retain  and/or  reproduce the above copyright
 * notice,  and the below  disclaimer of warranty and limitation
 * of liability  for redistribution and use of these source code
 * and object code.
 * 
 * - Everyone  must  ask a  specific prior written permission from
 * Gemplus to use the name of Gemplus.
 * 
 * DISCLAIMER OF WARRANTY
 * 
 * THIS CODE IS PROVIDED "AS IS",  WITHOUT ANY WARRANTY OF ANY KIND
 * (INCLUDING,  BUT  NOT  LIMITED  TO,  THE IMPLIED  WARRANTIES  OF
 * MERCHANTABILITY  AND FITNESS FOR  A  PARTICULAR PURPOSE)  EITHER
 * EXPRESS OR IMPLIED.  GEMPLUS DOES NOT WARRANT THAT THE FUNCTIONS
 * CONTAINED  IN THIS SOFTWARE WILL MEET THE USER'S REQUIREMENTS OR
 * THAT THE OPERATION OF IT WILL BE UNINTERRUPTED OR ERROR-FREE. NO
 * USE  OF  ANY  CODE  IS  AUTHORIZED  HEREUNDER EXCEPT UNDER  THIS
 * DISCLAIMER.
 * 
 * LIMITATION OF LIABILITY
 * 
 * GEMPLUS SHALL NOT BE LIABLE FOR INFRINGEMENTS OF  THIRD  PARTIES
 * RIGHTS. IN NO EVENTS, UNLESS REQUIRED BY APPLICABLE  LAW,  SHALL
 * GEMPLUS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES OF ANY CHARACTER  INCLUDING,
 * WITHOUT LIMITATION, DAMAGES FOR LOSS OF GOODWILL, WORK STOPPAGE,
 * COMPUTER FAILURE OR MALFUNCTION, OR ANY AND ALL OTHER DAMAGES OR
 * LOSSES, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. ALSO,
 * GEMPLUS IS  UNDER NO  OBLIGATION TO MAINTAIN,  CORRECT,  UPDATE,
 * CHANGE, MODIFY, OR OTHERWISE SUPPORT THIS SOFTWARE.
 */

//-----------------------------------------------------------------------------
// PACKAGE DEFINITION
//-----------------------------------------------------------------------------
package com.gemplus.opencard.service.sim.toolkit;

//-----------------------------------------------------------------------------
// IMPORTS
//-----------------------------------------------------------------------------

import java.util.*;

import opencard.core.terminal.*;
import opencard.core.service.InvalidCardChannelException;
import opencard.core.service.CardServiceException;
import opencard.core.service.SmartCard;
import opencard.core.service.CardRequest;
import opencard.core.OpenCardException;
import opencard.core.OpenCardRuntimeException;
import opencard.core.util.Tracer;

import com.gemplus.opencard.service.sim.*;

// application designed for this presentation

/**
 * This class is a simple test for the SIM / SIM Toolkit CardServices.
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: Test.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * @see com.gemplus.opencard.service.sim.BasicSIM
 * @see com.gemplus.opencard.service.sim.toolkit.BasicSIMToolkit
 * 
 */
public class Test
    implements SIMPLETLV.Constants, SIMToolkitApplicationService.Constants {

    public static void main(String[] args) {

	SIMCardService  sim = null;
	SIMToolkitApplicationService as = null;
	SmartCard card = null;
	ResponseAPDU myresponse; 
	BasicProfile profile = new BasicProfile();
	Vector choice = new Vector();
	BasicSIMPLETLV command = null;
	BasicBERTLV ber = new BasicBERTLV();
	byte chvType = 1;
	int i;
	
        System.out.println ("------------------------------------------------------------");
        System.out.println ("BEGIN TEST");
        System.out.println ("");

	try {
            // Initialize the framework
            System.out.println ("Starting OCF...");
	    SmartCard.start();   

            System.out.println ("Waiting for a SIM smart card...");
            card = SmartCard.waitForCard(new CardRequest(CardRequest.ANYCARD,
							 null,
							 SIMCardService.class));
	    
            // Get a SIM Card Service
	    sim = (SIMCardService) card.getCardService(SIMCardService.class, true);
	    System.out.println ("CardService: "
				+ sim.toString()
				+ "|"
				+ card.isStarted());
	    
            // Create a SIM Toolkit Service with 'sim' as a helper
	    as = new BasicSIMToolkit(sim);

            // First Menu
            System.out.println ("");
            System.out.println ("-------------------------------------------");
            System.out.println ("1) Unblock CHV 1");
            System.out.println ("2) Default Processing (infinite loop)");
            System.out.println ("-------------------------------------------");
	    i = System.in.read() - 48;

	    switch(i) {

	    case 1:
		String unblockchv = "11111111";
		String chv1="1234";
		byte[] unbpin=unblockchv.getBytes();
		byte[] ancpin=chv1.getBytes();
		int number = 0;
		
		System.out.println ("Send unblockCHV...");
		
		myresponse = sim.unblockCHV((byte) 0x00, unbpin , ancpin);
		System.out.println ("SW : " + Integer.toHexString(myresponse.sw()));
		System.out.println ("unblockCHV Ok.");
		break;      

	    default:
		System.out.println ("Default Choice.");
		System.out.println ("Send verifyCHV...");
		myresponse = new ResponseAPDU((sim.verifyCHV(chvType)).getBuffer());
		System.out.println ("SW : " + Integer.toHexString(myresponse.sw()));
		if (myresponse.sw1() != (byte)0x90 && myresponse.sw1() != (byte)0x91) {
		    System.out.println ("Wrong PIN!");
		    card.close();
		    // Shutdown the framework
		    SmartCard.shutdown ();
		    System.out.println ("");
		    System.out.println ("END TEST.");
		    System.out.println ("------------------------------------------------------------");
		    System.exit(1);
		}
		System.out.println ("verifyCHV Ok\nsetFeature...");
		profile.setFeature(DISPLAYTEXT);
		profile.setFeature(SETUPMENU);
		System.out.println ("setFeature Ok\nprofileDownload...");
		as.profileDownload(profile);
		System.out.println ("profileDownload Ok\nproactiveCommandInWaiting ?");
		System.out.println("");
		while (true) {
		    myresponse = sim.status((byte)16); // polling
		    System.out.println ("| SW1 = "
					+ Integer.toHexString(myresponse.sw1())
					+ " SW2 = "
					+ Integer.toHexString(myresponse.sw2())
					+ " |");
		    if (sim.proactiveCommandWaiting()) {
			Enumeration e;
			System.out.println ("Proactive Command is Waiting.\nFetch...");
			try {
			    int length = sim.getProactiveCommandLength();
			    System.out.println();
			    System.out.println ("Fetch length = "
						+ Integer.toHexString(length));
			    e = as.fetch((byte)length, ber);
			    System.out.println ("Fetch Ok.");
			    BasicSIMPLETLV item = (BasicSIMPLETLV) e.nextElement();
			    System.out.println ("Command Details Tag:"
						+ item.getTag());
			    if (item.getTag() == COMMAND_TAG) {
				System.out.println ("Command Details...");
				BasicTerminalResponse terminalResponse = null;
				BasicSIMPLETLV device = null;
				BasicSIMPLETLV result = null;
				command = new BasicSIMPLETLV(item);
				
				System.out.println ("Command Type = " 
						    + Integer.toHexString(item.getCommandType()));

				switch(item.getCommandType()) {

				case (byte) 0x25:
				    int choix;
				    // Skip Device Identities
				    item = (BasicSIMPLETLV) e.nextElement(); 
				    System.out.println ("Skipped Simple TLV Tag:" + item.getTag());
				    // Skip Alpha Identifier
				    item = (BasicSIMPLETLV) e.nextElement();
				    System.out.println ("Skipped Simple TLV Tag:" + item.getTag());
				    // Proactive Command Menu
				    System.out.println ("");
				    System.out.println ("---------------------");
				    System.out.println ("-- Choose between: --");
				    choice.removeAllElements();
				    i = 0;
				    while(e.hasMoreElements()) {
					// Next ITEM
					item = (BasicSIMPLETLV) e.nextElement();
					//  System.out.println ("Simple TLV Tag: " + Integer.toHexString(item.getTag()));
					if (item.getTag() == ITEM_TAG) {
					    choice.addElement(new Byte(item.getItemIdentifier()));
					    System.out.print("   ");
					    System.out.print(i);
					    System.out.print(") ");
					    System.out.println(new String(item.getItemText()));
					    i++;
					}
				    }
				    System.out.println ("---------------------");
				    choix = 0x7fff;
				    System.out.println ("");
				    while(choix >= i || choix < 0) {
					choix = System.in.read() - 48; 
				    }
				    System.out.println ("---------------------");
				    System.out.println ("-- Choice: " + new Integer(choix).toString() + "       --");
				    System.out.println ("---------------------");
				    System.out.println ("");
				    result = new BasicSIMPLETLV(RESULT_TAG,
								NO_COMPREHENSION,
								(byte) 0x00, 
								((Byte)choice.elementAt(choix)).byteValue());
				    System.out.println ("Simple TLV Tag: " + Integer.toHexString(result.getTag()));
				    break;

				case (byte) 0x21:
				    // Skip Device Identities
				    item = (BasicSIMPLETLV) e.nextElement();
				    // Next Text
				    item = (BasicSIMPLETLV) e.nextElement();
				    System.out.println ("Receiving following message from SIM: ");
				    System.out.println(item.getText());
				    result = new BasicSIMPLETLV(RESULT_TAG,
								NO_COMPREHENSION,
								(byte) 0x00);
				    break;

				default:
				    // error !!!!!!!
				    result = new BasicSIMPLETLV(RESULT_TAG,
								NO_COMPREHENSION, 
								(byte) 0x31);
				}
				device = new BasicSIMPLETLV(DEVICE_TAG,
							    NO_COMPREHENSION,
							    (byte)ME,
							    (byte)SIM);
				System.out.println ("Simple TLV Tag: " + Integer.toHexString(device.getTag()));
				System.out.println ("Constructing Terminal Response...");
				terminalResponse = new BasicTerminalResponse(command, device, result);
				System.out.println ("Constructing Terminal Response Ok.");
				try {
				    System.out.println ("Send Terminal Response...");
				    as.terminalResponse(terminalResponse);
				    System.out.println ("Send Terminal Response Ok.");
				} catch (Exception ex){
				    System.err.println(ex.getMessage());
				}
			    }
			    System.out.println("");
			} catch (SIMException ex) {
			    System.out.println(ex.getMessage());
			}
		    } // if
		}
	    }
	} catch(ClassNotFoundException e) {
	    System.out.println ("Error 1 : " + e.getMessage());
	} catch(CardServiceException e) {
	    System.out.println ("Error 3 : " + e.getMessage());
	} catch (InvalidCardChannelException e) {
	    System.out.println ("Error 3.5 : " + e.getMessage());
	} catch(CardTerminalException e) {
	    System.out.println ("Error 4 : "
				+ e.getMessage()
				+ e.getCardTerminal()
				+ e.getSlot());
	} catch(OpenCardRuntimeException e) {
	    System.out.println ("OpenCardRuntimeException error : "
				+ e.getMessage());
	} catch(OpenCardException e) {
	    System.out.println ("Opencard Error : " + e.getMessage());
	} catch(opencard.opt.iso.fs.CardIOException e) {
	    System.out.println ("opencard.opt.iso.fs.CardIOException : "
				+ e.getMessage());
	} catch(opencard.opt.terminal.protocol.T1BlockNotImplementedFeatureException e) {
	    System.out.println ("opencard.opt.terminal.protocol.T1BlockNotImplementedFeatureException : " + e.getMessage());
	} catch(opencard.opt.terminal.protocol.T1DataPacketTooLongException e) {
	    System.out.println ("opencard.opt.terminal.protocol.T1DataPacketTooLongException : " + e.getMessage());
	} catch(java.io.IOException e) {
	    System.out.println ("java.io.IOException : " + e.getMessage());
	} catch(Exception e) {
	    System.out.println ("Error 5 : " + e.getMessage());
	} finally {
	    try {
		card.close();
		// Shutdown the framework
		SmartCard.shutdown ();
		System.out.println ("");
		System.out.println ("END TEST.");
		System.out.println ("------------------------------------------------------------");
	    } catch (Exception ex) {
		System.out.println ("Error in Shutdown.");
	    }
	    System.exit(1);
	}
    }
}
