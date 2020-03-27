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
import com.gemplus.opencard.service.sim.*;

/**
 * This class is used to save BERTLV objects incomming from the SIM,
 * after a fetch, and to extract the SIMPLE TLV oblects out of it.
 * It is used for GSM cards (SIM).
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicBERTLV.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public class BasicBERTLV implements BERTLV, BERTLV.Constants {
    
    // A tracer for debugging output
    private static Tracer ctracer = new Tracer(BasicBERTLV.class);

    private CommandAPDU content;
    private byte length = 0;
    private int nrOfObjects;	
    
    /** 
     * Methodes from the BERTLV
     */
    
    public void constructBERTLV(byte[] data) {
	content = new CommandAPDU(data);
	length = (byte)content.getLength();
	nrOfObjects = getNbOfObjects();
    }
    
    /** 
     * This method will return the number of SIMPLE TLV
     * objects withhin the BER TLV object.
     *
     * @return  number of objects in BER TLV
     */
    public int getNbOfObjects() {
	int objectLength = 0;
	int pointer = 1;	  // pointer to length
	int envelopeLength = content.getByte(pointer);
	nrOfObjects =  0;
	
	if (envelopeLength == 0) {
	    return nrOfObjects;		// the envelope has no data in it
	}
	
	if (envelopeLength == (int) 0x81) {
	    pointer++;			// pointer to length
	    envelopeLength = content.getByte(pointer);
	    ctracer.debug("getNbOfObjects", "Envelope length > 180.");
	}
	ctracer.debug("getNbOfObjects",
		      "Envelope length : "
		      + Integer.toHexString(envelopeLength)
		      + "\npointer to tag.");
	// simple TLV
	pointer++;                      // pointer on tag of object
	while (envelopeLength > 0) {
	    ctracer.debug("getNbOfObjects",
			  "Object tag (pointer = "
			  +pointer
			  +"): "
			  + Integer.toHexString(content.getByte(pointer)));
	    pointer++;			// pointer on length of object
	    envelopeLength--;
	    
	    objectLength = content.getByte(pointer);
	    ctracer.debug("getNbOfObjects",
			  "object length (pointer = "
			  +pointer
			  +"): "
			  + Integer.toHexString(objectLength));
	    
	    if (objectLength == 0x81) {
		ctracer.debug("getNbOfObjects", "Object length > 180.");
		pointer++;		// pointer on length of object
		envelopeLength--;
		objectLength = content.getByte(pointer);
	    }
	    
	    ctracer.debug("getNbOfObjects",
			  "Object "
			  + (nrOfObjects+1)
			  + " : "
			  + Integer.toHexString(objectLength)
			  + "\n.");
	    pointer++; // on data
	    envelopeLength--;
	    pointer += objectLength;
	    envelopeLength -= objectLength;
	    nrOfObjects++;
	}
	return nrOfObjects;
    }
    
    /** 
     * This method will return one SIMPLE TLV
     * objects in the BER TLV object.
     *
     * @param number the number of object within BER TLV
     * @return  requested SIMPLE TLV object
     */
    public Object readObject(int number)    // needs the number of objects
	throws BERTLVException {
	BasicSIMPLETLV simple = null;	
	byte[] buffer = null;
	int objectLength =0;
	int pointer = 1; // length
	int BERLength = content.getByte(pointer);
	int objectNr = 1;
	
	ctracer.debug("readObject", "enter readObject...");
	if (BERLength == 2) {
	    throw new BERTLVException("No objects available.");
	}
	if (number > nrOfObjects) {
	    throw new BERTLVException("Requested object does not exist.");
	}
	if (BERLength == 0x81) {	   // BER length is 2 bytes
	    pointer++;
	    BERLength = content.getByte(pointer);
	}
	pointer++;	                  // pointer auf SIMPLETLV tag
	while (objectNr != number) {
	    ctracer.debug("readObject",
			  "Passing : Object tag (pointer = "
			  +pointer
			  +"): "
			  + Integer.toHexString(content.getByte(pointer)));
	    pointer++;			  // pointer auf SIMPLETLV length
	    
	    if ((objectLength=content.getByte(pointer)) == 0x81) {
		pointer++;	         // SIMPLE length is 2 byte
		objectLength = content.getByte(pointer);
	    }
	    pointer++;			 // pointer on SIMPLE data
	    
	    pointer += objectLength;     // pointer on next SIMPLE tag
	    objectNr++;
	}
	
	// pointer on simple tag
	ctracer.debug("readObject",
		      "Object tag (pointer = "
		      +pointer
		      +"): "
		      + Integer.toHexString(content.getByte(pointer)));
	if (content.getByte(pointer+1)== 0x81) {
	    objectLength = content.getByte(pointer+2);
	    objectLength += 3;
	} else {
	    objectLength = content.getByte(pointer+1);
	    objectLength += 2;
	}
	
	buffer = new byte[objectLength];
	for (int i= 0; i < objectLength; i++) {
	    buffer[i] = (byte)content.getByte(pointer+i);
	}
	simple = new BasicSIMPLETLV(buffer);
	ctracer.debug("readObject", "exit readObject...");
	return simple;
    }
    
    /**
     * Object specific methods
     */
    
    /** This method will return the length
     * of all data withhin a BER TLV object.
     *
     * @return  length of the BER TLV
     */
    public byte getLength() {
	if (content.getByte(1) == (byte) 0x81)
	    return (byte) content.getByte(2);
	return (byte)content.getByte(1);
    }
    
    /** This method will return the tag
     * of the BER TLV object.
     *
     * @return  tag of the BER TLV
     */
    public byte getTag() {
	return (byte) content.getByte(0);
    }
    
    /** This method will return the length
     * of the BER TLV (inkl tag and length field).
     *
     * @return  length of the BER TLV
     */
    public byte getLengthB() {
	return (byte)content.getLength();
    }
    
    public byte[] getDataB() {
	return content.getBytes();
    }
    
}
