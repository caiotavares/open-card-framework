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
package com.gemplus.opencard.service.sim;

//-----------------------------------------------------------------------------
// IMPORTS
//-----------------------------------------------------------------------------
import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

/**
 * This Exception is the base class for all types of
 * Exceptions related to SIM operations (low level
 * operations performed on a SIM card, see GSM 11.11
 * for more details).
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: SIMException.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $
 * @see com.gemplus.opencard.service.sim.SIMAccessConditionException
 * @see com.gemplus.opencard.service.sim.SIMAllMemoryException
 * @see com.gemplus.opencard.service.sim.SIMApplicationException
 * @see com.gemplus.opencard.service.sim.SIMBusyException
 * @see com.gemplus.opencard.service.sim.SIMCHVErrorException
 * @see com.gemplus.opencard.service.sim.SIMCHVStatusException
 * @see com.gemplus.opencard.service.sim.SIMCommandCodeException
 * @see com.gemplus.opencard.service.sim.SIMCommandNotAllowedException
 * @see com.gemplus.opencard.service.sim.SIMFileNotFoundException
 * @see com.gemplus.opencard.service.sim.SIMGeneralException
 * @see com.gemplus.opencard.service.sim.SIMIncreaseException
 * @see com.gemplus.opencard.service.sim.SIMInstructionClassException
 * @see com.gemplus.opencard.service.sim.SIMInvalidationException
 * @see com.gemplus.opencard.service.sim.SIMMemoryException
 * @see com.gemplus.opencard.service.sim.SIMMemoryWarningException
 * @see com.gemplus.opencard.service.sim.SIMNoCHVException
 * @see com.gemplus.opencard.service.sim.SIMNoFileSelectedException
 * @see com.gemplus.opencard.service.sim.SIMOutOfRangeException
 * @see com.gemplus.opencard.service.sim.SIMP1P2Exception
 * @see com.gemplus.opencard.service.sim.SIMP3Exception
 * @see com.gemplus.opencard.service.sim.SIMProactiveException
 * @see com.gemplus.opencard.service.sim.SIMReferenceException
 * @see com.gemplus.opencard.service.sim.SIMSecurityException
 * @see com.gemplus.opencard.service.sim.SIMTechnicalException
 * @see com.gemplus.opencard.service.sim.SIMUnknownErrorCodeException
 * @see com.gemplus.opencard.service.sim.SIMWrongFileTypeException
 */
public abstract class SIMException extends CardServiceException {

    protected byte errorType=(byte)0xFF;
    private byte errorNumber;
    private boolean validErrorNumber;
	
    public SIMException() {
	super();
	validErrorNumber = false;
	errorNumber = (byte)0xFF;
    }
	
    public SIMException(String message) {
	super(message);
	validErrorNumber = false;
	errorNumber = (byte)0xFF;
    }

    public SIMException(byte errorNumber) {
	super();
	validErrorNumber = true;
	this.errorNumber = errorNumber;
    }

    public SIMException(byte errorNumber, String message) {
	super(message);
	validErrorNumber = true;
	this.errorNumber = errorNumber;
    }	
	
    public byte getErrorNumber() {
	return errorNumber;
    }

    public byte getErrorType() {	
	return errorType;
    }
	
    public boolean hasErrorNumber() {
	return validErrorNumber;
    }

}
