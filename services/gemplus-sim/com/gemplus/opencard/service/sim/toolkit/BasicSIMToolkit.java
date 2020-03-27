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

import opencard.core.terminal.ResponseAPDU;
import opencard.core.terminal.CardTerminalException;
import opencard.core.service.CardServiceException;
import opencard.core.util.Tracer;

import com.gemplus.opencard.service.sim.*;

/**
 * This class is designed as a basic default class which allows
 * programmers to access GSM 11.14-compliant smartcards.
 * <p>It provides all methods necessary to access SIM Toolkit smartcards,
 * as describes in the GSM 11.14 norm.
 * <p>It is intended that the CardService developers for specific
 * card use this class as a base class and by inheriting its
 * functionalities.
 *
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicSIMToolkit.java,v 1.2 2004/09/17 07:34:54 antoinethomas Exp $
 * @see com.gemplus.opencard.service.sim.toolkit.SIMToolkitApplicationService
 * @see com.gemplus.opencard.service.sim.BasicSIM
 * 
 */
public class BasicSIMToolkit
    extends Object
    implements SIMToolkitApplicationService, SIMCardService.Constants {
    
    /**
     * SIM CardService helper object for handling low-level APDU
     * protocol with the SIM card.
     */
    private static SIMCardService sim_ = null;

    protected ResponseAPDU response = new ResponseAPDU(258);

    // A tracer for debugging output
    private static Tracer ctracer = new Tracer(BasicSIMToolkit.class);

    /**
     * Constructor
     */
    public BasicSIMToolkit(SIMCardService sim) {
	this.sim_ = sim;
    }
    
    /**
     * Utility method for translating ResponseAPDU codes to exceptions
     */
    protected void throwException(ResponseAPDU apdu) 
	throws SIMException {
	byte sw1, sw2;
    
	sw1 = apdu.sw1();
	sw2 = apdu.sw2();
	ctracer.debug("throwException",
		      "SW1 = "
		      + Integer.toHexString(sw1)
		      + " | SW2 = "
		      + Integer.toHexString(sw2));
	switch(sw1) {
	case SW1_PROACTIVE:
	    switch(sw2) {
	    case SW2_BUSY :
		throw new SIMBusyException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_MEMORY:
	    switch(sw2) {
	    case SW2_MEMORY :
		throw new SIMMemoryException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_FILE:
	    switch(sw2) {
	    case SW2_NO_FILE_SELECTED :
		throw new SIMNoFileSelectedException();
	    case SW2_OUT_OF_RANGE:
		throw new SIMOutOfRangeException();
	    case SW2_FILE_NOT_FOUND:
		throw new SIMFileNotFoundException();
	    case SW2_WRONG_FILE_TYPE:
		throw new SIMWrongFileTypeException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_SECURITY:
	    switch(sw2) {
	    case SW2_NO_CHV:
		throw new SIMNoCHVException();
	    case SW2_ACCESS_CONDITION:
		throw new SIMAccessConditionException();
	    case SW2_CHV_STATUS:
		throw new SIMCHVStatusException();
	    case SW2_INVALIDATION:
		throw new SIMInvalidationException();
	    case SW2_CHV_ERROR:
		throw new SIMCHVErrorException();
	    case SW2_INCREASE:
		throw new SIMIncreaseException();
	    default:
		throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	    }
	case SW1_P3:
	    throw new SIMP3Exception(sw2);
	case SW1_P1P2:
	    throw new SIMP1P2Exception(sw2);
	case SW1_COMMAND_CODE:
	    throw new SIMCommandCodeException(sw2);
	case SW1_INSTRUCTION_CLASS:
	    throw new SIMInstructionClassException(sw2);
	case SW1_TECHNICAL:
	    throw new SIMTechnicalException(sw2);
	case (byte)0x90:
	case (byte)0x91:
	case (byte)0x9E:
	case (byte)0x9F:
	    break;
	default:
	    throw new SIMUnknownErrorCodeException(sw1, sw2, "Unknown error code.");
	}
    }
    
    /**
     * Methods from the SIMToolkitApplicationService interface.
     */

    /**
     * This method allows to send a fetch command.
     * 
     * @return  an enumeration from the BER TLV objects, if any available
     */
    public Enumeration fetch(int length, BERTLV ber)
	throws CardServiceException, CardTerminalException {
	Vector list = new Vector();
	
	response = sim_.fetch((byte)length);
	throwException(response);
	
	ctracer.debug("fetch", "CONSTRUCT BER...");
	ber.constructBERTLV(response.data());
	ctracer.debug("fetch", "CONSTRUCT BER ok.\ngetNbOfObjects...");
	int number = ber.getNbOfObjects();
	ctracer.debug("fetch", number + " objects.");
	for (int i=1; i <= number; i++) {
	    ctracer.debug("fetch", "Add object "+i+" to list.");
	    list.addElement(ber.readObject(i));
	}
	ctracer.debug("fetch", "list done.");	
	return list.elements();
    }
    
    /**
     * This method allows to send a terminal profile command.
     * 
     * @return  void
     */
    public void profileDownload(Profile profile) 
	throws CardServiceException, CardTerminalException {
	response = sim_.terminalProfile((byte)profile.getLength(), profile.getProfile());
	throwException(response);
    }
    
    /**
     * This method allows to send an envelope.
     * 
     * @return  byte[]
     */
    public byte[] envelope(Envelope envelope) 
	throws CardServiceException, CardTerminalException {
	response = sim_.envelope((byte)envelope.getLength(),
				      envelope.getData());
	if (response.sw1() == (byte) 0x9F){
	    response = sim_.getResponse(response.sw2());
	}
	throwException(response);
	return response.data();	
    }
    
    /**
     * This method allows to send a terminal response
     * 
     * @return  void
     */
    public void terminalResponse(TerminalResponse terminalresponse) 
	throws CardServiceException, CardTerminalException {
	ctracer.debug("terminalResponse",
		      "terminal response length : "
		      + Integer.toHexString(terminalresponse.getLength()));
	ctracer.debug("terminalResponse",
		      "terminal response length : "
		      + Integer.toHexString(terminalresponse.getData().length));
	response = sim_.terminalResponse((byte)terminalresponse.getLength(),
					      terminalresponse.getData());
	ctracer.debug("terminalResponse",
		      "SW1 : "
		      + Integer.toHexString(response.sw1())
		      + " & SW2 : "
		      + Integer.toHexString(response.sw2()));
	throwException(response);
    }

}
