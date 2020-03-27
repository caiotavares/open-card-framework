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
import com.gemplus.opencard.service.sim.*;
import com.gemplus.opencard.service.sim.toolkit.*;

/**
 * This class is a SIM Toolkit Envelope Object. <br>
 * This is a BER TLV from the ME to the SIM.<br>
 * It is used for GSM cards (SIM). (see GSM 11.14 standard).
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicEnvelope.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public class BasicEnvelope implements BERTLV.Constants, Envelope {
    
    private CommandAPDU content = null;
    private byte nrOfObjects;

    /** 
     * Constructor
     * (Note: the right order of the objects is not respected!)
     *
     * @tag  tag of the envelope (BER TLV tag)
     * @object  SIMPLE TLV (like address)
     */
    
    public BasicEnvelope(byte tag,
			  BasicSIMPLETLV object1,
			  BasicSIMPLETLV object2,
			  BasicSIMPLETLV object3,
			  BasicSIMPLETLV object4,
			  BasicSIMPLETLV object5) {
	int tempLength
	    = object1.getLength()
	    + object2.getLength()
	    +  object3.getLength()
	    + object4.getLength()
	    + object5.getLength();
	
	if (tempLength > (byte) 0x7F) {
	    content = new CommandAPDU(tempLength +3); //length fiel is 2 bytes
	}
	content = new CommandAPDU(tempLength +2);
	content.append(tag);
	if (tempLength > (byte) 0x7F) {
	    content.append((byte) 0x81);
	}
	content.append((byte)tempLength);       
	content.append(object1.getData());
	content.append(object2.getData());
	content.append(object3.getData());
	content.append(object4.getData());
	content.append(object5.getData());
	nrOfObjects = (byte) 5;
    }
    
    public BasicEnvelope(byte tag,
			  BasicSIMPLETLV object1,
			  BasicSIMPLETLV object2,
			  BasicSIMPLETLV object3,
			  BasicSIMPLETLV object4) {
	int tempLength
	    = object1.getLength()
	    + object2.getLength()
	    + object3.getLength()
	    + object4.getLength();
    
	if (tempLength > (byte) 0x7F) {
	    content = new CommandAPDU(tempLength +3); //length fiel is 2 bytes
	}
	content = new CommandAPDU(tempLength +2);
	content.append(tag);
	if (tempLength > (byte) 0x7F) {
	    content.append((byte) 0x81);
	}
	content.append((byte)tempLength);
	content.append(object1.getData());
	content.append(object2.getData());
	content.append(object3.getData());
	content.append(object4.getData());
	nrOfObjects = (byte) 4;
    }
    
    public BasicEnvelope(byte tag,
			  BasicSIMPLETLV object1,
			  BasicSIMPLETLV object2,
			  BasicSIMPLETLV object3) {
	int tempLength
	    = object1.getLength()
	    + object2.getLength()
	    + object3.getLength();
	
	if (tempLength > (byte) 0x7F) {
	    content = new CommandAPDU(tempLength +3);  //length fiel is 2 bytes
	}
	content = new CommandAPDU(tempLength +2);
	content.append(tag);
	if (tempLength > (byte) 0x7F) {
	    content.append((byte) 0x81);
	}
	content.append((byte)tempLength);
	content.append(object1.getData());
	content.append(object2.getData());
	content.append(object3.getData());
	nrOfObjects = (byte) 3;
    }
    
    public BasicEnvelope(byte tag,
			  BasicSIMPLETLV object1,
			  BasicSIMPLETLV object2) {
	int tempLength = object1.getLength() + object2.getLength();
	
	if (tempLength > (byte) 0x7F) {
	    content = new CommandAPDU(tempLength +3); //length fiel is 2 bytes
	}
	content = new CommandAPDU(tempLength +2);
	content.append(tag);
	if (tempLength > (byte) 0x7F) {
	    content.append((byte) 0x81);
	}
	content.append((byte)tempLength);
	content.append(object1.getData());
	content.append(object2.getData());
	nrOfObjects = (byte) 2;
    }
    
    public BasicEnvelope(byte tag, BasicSIMPLETLV object1) {
	int tempLength = object1.getLength();
	
	if (tempLength > (byte) 0x7F) {
	    content = new CommandAPDU(tempLength +3); //length fiel is 2 bytes
	}
	content = new CommandAPDU(tempLength +2);
	content.append(tag);
	if (tempLength > (byte) 0x7F) {
	    content.append((byte) 0x81);
	}
	content.append((byte)tempLength);
	content.append(object1.getData());
	nrOfObjects = (byte) 1;
    }
    
    public BasicEnvelope(byte tag) {
	content = new CommandAPDU(2);
	content.append(tag);
	content.append((byte) 0x00);
	nrOfObjects = (byte) 0;
    }
    
    public BasicEnvelope() {
	content = new CommandAPDU(0);
	nrOfObjects = (byte) 0;
    }
    
    /**
     * methodes
     */
    
    /**
     * This method will return the length of the envelope data.
     * @return  length
     */
    public byte getDataLength() {
	if (content.getByte(1) == (byte) 0x81) {
	    return (byte) content.getByte(2);
	}
	return (byte)content.getByte(1);
    }
    
    /**
     * method from Envelope interface
     */
    
    /**
     * This method will return the length of the entire envelope.
     * @return  length
     */
    public int getLength() {
	return (byte)content.getLength();
    }
    
    /**
     * This method will return the byte array of the envelope.
     * (+tag+length field(s))
     * @return  byte[]
     */
    public byte[] getData() {
	return content.getBuffer();
    }
    
}
