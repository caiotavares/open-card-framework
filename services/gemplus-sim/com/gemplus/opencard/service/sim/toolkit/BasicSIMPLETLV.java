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
 * This class represents a SIMPLE TLV (Tag Length Value) structure.
 * There are methods to get specific data related to use in SIM Toolkit
 * applications (see GSM 11.14 standard).
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicSIMPLETLV.java,v 1.1 2004/09/13 16:26:27 cmuller13 Exp $
 * 
 */
public class BasicSIMPLETLV
    implements SIMPLETLV.Constants, SIMToolkitApplicationService.Constants {
    
    private CommandAPDU contents = null;

    // constructors

    /**
     * Constructor for SIMPLE TLV to SIMPLETLV.
     */
    public BasicSIMPLETLV(BasicSIMPLETLV object) {
	contents = new CommandAPDU(object.getData());
    }
    
    /**
     * Constructor for using in SIM->ME mode.
     */
    public BasicSIMPLETLV(byte[] data) {
	contents = new CommandAPDU(data);
    }
    
    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag, byte comprehension) {
	int contentsLength = 2;
	int dataLength = 0;
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	contents.append((byte) dataLength);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag, byte comprehension, byte[] data) {
	CommandAPDU temp = new CommandAPDU(data);
	int dataLength = temp.getLength();
	int contentsLength = dataLength + 2;	// tag and lengthfield
	
	if (dataLength > (int) extendedData) {
	    contentsLength++;			// extended field
	}
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	if (dataLength > (int) 0x7F) {
	    contents.append((byte)extendedData);
	}
	contents.append((byte) dataLength);
	contents.append(data);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag,
			    byte comprehension,
			    byte data1,
			    byte[] data2) {

	CommandAPDU temp = new CommandAPDU(data2);
	int dataLength = temp.getLength() + 1;	// because of data1
	int contentsLength = dataLength + 2;	// tag and lengthfield
	
	if (dataLength > (int) extendedData) {
	    contentsLength++;			// extended field
	}
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	if (dataLength > (int) 0x7F) {
	    contents.append((byte)extendedData);
	}
	contents.append((byte) dataLength);
	contents.append(data1);
	contents.append(data2);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag, byte comprehension, byte data) {
	int contentsLength = 3;
	int dataLength = 1;
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	contents.append((byte) dataLength);
	contents.append(data);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag,
			    byte comprehension,
			    byte data1,
			    byte data2) {
	int contentsLength = 4;
	int dataLength = 2;
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	contents.append((byte) dataLength);
	contents.append(data1);
	contents.append(data2);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag,
			    byte comprehension,
			    byte data1,
			    byte data2,
			    byte data3) {
	int contentsLength = 5;
	int dataLength = 3;
	contents = new CommandAPDU(contentsLength);
	contents.append((byte)(tag|comprehension));
	contents.append((byte) dataLength);
	contents.append(data1);
	contents.append(data2);
	contents.append(data3);
    }

    /**
     * Constructor for using in ME->SIM mode.
     */
    public BasicSIMPLETLV(byte tag,
			    byte comprehension,
			    byte[] data1,
			    byte[] data2,
			    byte[] data3) {
	CommandAPDU temp = new CommandAPDU(data1);
	int dataLength = temp.getLength();
	int contentsLength;
	
	temp = new CommandAPDU(data2);
	dataLength+= temp.getLength();
	temp = new CommandAPDU(data3);
	dataLength+= temp.getLength();
	
	contentsLength = dataLength + 2;	// tag and length field
	
	if (dataLength > (int) 0x7F) {
	    contentsLength++;			// extended field
	}
	contents = new CommandAPDU((byte) contentsLength);
	contents.append((byte)(tag|comprehension));
	if (dataLength > (int) extendedData) {
	    contents.append((byte)extendedData);
	}
	contents.append((byte) dataLength);
	contents.append(data1);
	contents.append(data2);
	contents.append(data3);
    }
  
    /**
     * Returns the Tag of the SIMPL TLV.
     */
    public byte getTag() {
	return (byte)(contents.getByte(0) & 0x7F);
    }
  
    /**
     * Returns the entier SIMPLE TLV object as byte array.
     */
    public byte[] getData() {
	return contents.getBytes();
    }

    /**
     * Returns the length of the contents of the SIMPLE TLV object.
     */
    public int getLength() {
	if (contents.getByte(1) == extendedData) {
	    return contents.getByte(2);
	} else {
	    return contents.getByte(1);
	}
    }
    
    /**
     * Returns the length of the entire SIMPLE TLV.
     */
    public int getLengthS() {
	return contents.getLength();
    }
  
    public boolean getComprehension() {
	return ((contents.getByte(0) & COMPREHENSION) == COMPREHENSION);
    }
    
    // methods to get data out of the SIMPLETLV_OBJECT
    
    public byte getTON_NPI() throws SIMToolkitException {
	int pointer = 1;			// pointer on length field
	
	byte exception = getTag();
	if ((exception != ADDRESS_TAG) || (exception != ADDRESS_TAG)) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	pointer++;				// pointer on data
	return (byte) contents.getByte(pointer);
    }
  
    public byte[] getDiallingNumber() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != ADDRESS_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}
    
	byte[] buffer = null;
	int pointer = 1;			// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer) - 1;
	pointer++;				// pointer on TON
	pointer++;				// pointer on number
    
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
  
    public byte[] getAlphaIdentifier() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != ALPHA_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}
    
	byte[] buffer = null;
	int pointer = 1;			// pointer on length field
	int length;
    
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	pointer++;				// pointer on alpha identifier
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }

    public byte[] getCalledPartySubaddress() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != CALLED_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;			// pointer on length field
	int length;

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	pointer++;				// pointer on address

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;

    }

    public byte[] getCapabilityParameters() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != CAPABILITY_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;			// pointer on length field
	int length;

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	pointer++;				// pointer on parameters
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }


    public byte[] getCellbroadcaastPage()	// length has to be 0x58
	throws SIMToolkitException {
	byte exception = getTag();

	if (exception != CELL_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;			// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	pointer++;				// pointer on page

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }

    public byte getCommandNumber() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != COMMAND_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;			// pointer on command number field

	return (byte) contents.getByte(pointer);
    }

    public byte getCommandType() throws SIMToolkitException {

	if (getTag() != COMMAND_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 3;		// pointer on command type field

	return (byte) contents.getByte(pointer);
    }
    
    public byte getCommandQualifier() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != COMMAND_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 4;		// pointer on command qualifier field

	return (byte) contents.getByte(pointer);
    }

    public byte getSourceIdentity() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != DEVICE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;		// pointer on source identity field

	return (byte) contents.getByte(pointer);
    }

    public byte getDestinationIdentity() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != DEVICE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 3;		// pointer on source identity field

	return (byte) contents.getByte(pointer);
    }

    public byte getTimeUnit() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != DURATION_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;		// pointer on time unit field

	return (byte) contents.getByte(pointer);
    }
    
    public byte getTimeInterval() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != DURATION_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 3;		// pointer on time interval field

	return (byte) contents.getByte(pointer);
    }

    public byte getItemIdentifier() throws SIMToolkitException {
	byte exception = getTag();

	if ((exception != ITEM_TAG) && (exception != ITEM_IDENTIFIER_TAG)) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 1;		// pointer on length field
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	pointer++;			// pointer on item identifier
	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getItemText() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != ITEM_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	length = contents.getByte(pointer) - 1;	// because of the identifier
	pointer++;
	pointer++;				// pointer on text
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte getMinLengthResponse() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != RESPONSE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;		// pointer on min field

	return (byte) contents.getByte(pointer);
    }

    public byte getMaxLengthResponse() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != RESPONSE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 3;		// pointer on max field
	
	return (byte) contents.getByte(pointer);
    }
    
    public byte getGeneralResult() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != RESULT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 1;		// pointer on length field

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	pointer++;			// pointer on result field

	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getResultInfo() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != RESULT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer) - 1;
	pointer++;
	pointer++;			// pointer on information field
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getSMS_TPDU() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != SMS_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	pointer++;			// pointer on TPDU	
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getSS_String() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != SS_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer) - 1;
	pointer++;			// pointer on TON
	pointer++;			// pointer on string
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte getDataCoding() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != TEXT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 1;		// pointer on length field
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	pointer++;			// pointer on data
	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getText() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != TEXT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	length = contents.getByte(pointer) - 1;
	pointer++;			// pointer on data coding field
	pointer++;			// pointer on text
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte getTone() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != TONE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;		// pointer on max field
	
	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getUSSD_String() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != USSD_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	length = contents.getByte(pointer);
	pointer++;			// pointer on text
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte getNubmerOfFiles() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != FILE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 1;		// pointer on length field
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	pointer++;			// pointer on number
	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getFiles() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != FILE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;

	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}

	length = contents.getByte(pointer) - 1;
	pointer++;			// pointer on data coding field
	pointer++;			// pointer on text
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getMCC_MNC() throws SIMToolkitException {
	// length has to be 0x08
	byte exception = getTag();

	if (exception != LOCATION_INFO_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 2;		// pointer on MCC 
	int length = 3;

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getLAC() throws SIMToolkitException {
	// length has to be 0x08
	byte exception = getTag();

	if (exception != LOCATION_INFO_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 5;		// pointer on LAC 
	int length = 2;

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getCellID() throws SIMToolkitException {
	// length has to be 0x08
	byte exception = getTag();

	if (exception != LOCATION_INFO_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 7;		// pointer on cell ID 
	int length = 2;

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }

    public byte[] getIMEI() throws SIMToolkitException {
	// length has to be 0x08
	byte exception = getTag();

	if (exception != IMEI_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 2;		// pointer on IMEI 
	int length = 8;

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }

    public byte[] getMeasurementsResults() throws SIMToolkitException {
	// length has to be 0x08
	byte exception = getTag();

	if (exception != MEASUREMENT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 2;		// pointer on results 
	int length = 16;

	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getActionList() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != NEXT_ACTION_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);

	pointer++;			// pointer on list
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getEventList() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != EVENT_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	
	pointer++;			// pointer on list
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getCause() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != CAUSE_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	
	pointer++;			// pointer on list
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte getLocationStatus() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != LOCATION_STATUS_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	int pointer = 2;		// pointer on slocation status field
	
	return (byte) contents.getByte(pointer);
    }
    
    public byte[] getIdentifierList() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != TRANSACTION_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	
	pointer++;			// pointer on list
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }
    
    public byte[] getChannelList() throws SIMToolkitException {
	byte exception = getTag();

	if (exception != BOCH_TAG) {
	    throw new SIMPLETLVException("Not available for this SIMPLETLV");
	}

	byte[] buffer = null;
	int pointer = 1;		// pointer on length field
	int length;
	
	if (contents.getByte(pointer) == extendedData) {
	    pointer++;
	}
	length = contents.getByte(pointer);
	
	pointer++;			// pointer on list
	
	buffer = new byte[length];
	for (int i=0; i<length; i++) {
	    buffer[i] = (byte) contents.getByte(pointer);
	    pointer++;
	}
	return buffer;
    }

}
