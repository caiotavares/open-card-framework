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
import opencard.core.terminal.CommandAPDU;
import opencard.core.util.Tracer;

/**
 * This class represents a SIM Toolkit Terminal Response.<br>
 * Note: Terminal response does not have a tag!<br>
 * It is used for GSM cards (SIM). (see GSM 11.14 standard).
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicTerminalResponse.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public class BasicTerminalResponse
    implements SIMPLETLV.Constants, TerminalResponse {
  
    // A tracer for debugging output
    private static Tracer ctracer = new Tracer(BasicTerminalResponse.class);

    private CommandAPDU contents = new CommandAPDU(260);
    
    /** 
     * Constructor
     */
    public BasicTerminalResponse(BasicSIMPLETLV object1,
				  BasicSIMPLETLV object2,
				  BasicSIMPLETLV object3)
	throws TerminalResponseException {
	byte tag1 = object1.getTag();
	byte tag2 = object2.getTag();
	byte tag3 = object3.getTag();
	int length;
	
	ctracer.debug("Constructor", "Object 1 tag : "+ tag1);
	ctracer.debug("Constructor", "Object 2 tag : "+ tag2);
	ctracer.debug("Constructor", "Object 3 tag : "+ tag3);
	
	length = object1.getLengthS()
	    + object2.getLengthS()
	    + object3.getLengthS();

	ctracer.debug("Constructor", "length : "+ length);

	contents.setLength(0);
	if (tag1 != COMMAND_TAG) {
	    throw new TerminalResponseException("Missing command details");
	}
	if (tag2 != DEVICE_TAG) {
	    throw new TerminalResponseException("Missing device identities");
	}
	if (tag3 != RESULT_TAG) {
	    throw new TerminalResponseException("Missing result");
	}
	contents.append(object1.getData());
	contents.append(object2.getData());
	contents.append(object3.getData());
    }
  
    public BasicTerminalResponse(BasicSIMPLETLV object1,
				  BasicSIMPLETLV object2,
				  BasicSIMPLETLV object3,
				  BasicSIMPLETLV object4)
	throws TerminalResponseException {

	byte tag1 = object1.getTag();
	byte tag2 = object2.getTag();
	byte tag3 = object3.getTag();
	byte tag4 = object4.getTag();
	int length;
	
	length = object1.getLengthS()
	    + object2.getLengthS()
	    + object3.getLengthS()
	    + object4.getLengthS();
	
	if (tag1 != COMMAND_TAG) {
	    throw new TerminalResponseException("Missing command details");
	}
	if (tag2 != DEVICE_TAG) {
	    throw new TerminalResponseException("Missing device identities");
	}
	if (tag3 != RESULT_TAG) {
	    throw new TerminalResponseException("Missing result");
	}
	contents.setLength(0);
	contents.append(object1.getData());
	contents.append(object2.getData());
	contents.append(object3.getData());
	contents.append(object4.getData());
    }
    
    /**
     * methods from TerminalResponse
     */
    
    /**
     * This method will return the length of the terminal response.
     *
     * @return  length of the BER TLV
     */
    public int getLength() {
	return contents.getBytes().length;
    }
    
    /**
     * This method will return the terminal response as a byte array.
     */
    public byte[] getData() {
	return contents.getBytes();
    }
    
}
