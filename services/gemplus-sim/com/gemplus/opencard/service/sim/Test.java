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
 *  - Everyone  must  retain  and/or  reproduce the above copyright
 *    notice,  and the below  disclaimer of warranty and limitation
 *    of liability  for redistribution and use of these source code
 *    and object code.
 * 
 *  - Everyone  must  ask a  specific prior written permission from
 *    Gemplus to use the name of Gemplus.
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

//-------------------------------------------------------------------------
// IMPORTS
//-------------------------------------------------------------------------
import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

import com.gemplus.opencard.service.sim.*;

/**
 * A simpe test for the SIM CardServices
 *
 * @version $Id: Test.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $<br>
 * @author Christophe.Muller@research.gemplus.com
 */
public class Test {
    
    public static void main (String [] args) {
	int i;
        String[] result;
	
        System.out.println ("------------------------------------------------------------");
        System.out.println ("BEGIN TEST");
        System.out.println ("");
	
        try {
            // Initialize the framework
            System.out.println ("Starting OCF...");
            SmartCard.start ();
            
            System.out.println ("Waiting for a SIM smart card...");
	    CardRequest cr = new CardRequest(CardRequest.ANYCARD,
					     null,
					     BasicSIM.class);
	    
	    SmartCard   sm = SmartCard.waitForCard(cr);

            // Get a card service and perform a 'status' command
            BasicSIM cs = (BasicSIM) sm.getCardService(BasicSIM.class, true);
            
            // Try to send a STATUS to the card
            System.out.println ("");
            System.out.println ("-------------------------------------------");
            System.out.println ("STATUS");
            System.out.println ("-------------------------------------------");
	    
            ResponseAPDU status = cs.status((byte)0x16);
	    System.out.println("Response to the 'STATUS' command:");
	    System.out.println(status.toString());
	    
	    // Shutdown the framework
	    SmartCard.shutdown ();

	} catch (SIMException e) {
	    System.out.println ("SIM Exception: ");
	    e.printStackTrace();
	    if (e.getMessage () != null) {
		System.out.println ("details:");
		System.out.println (e.getMessage () );
	    }
	}
	catch (OpenCardPropertyLoadingException plfe) {
	    System.out.println ("OpenCardPropertyLoadingException: ");
	    System.out.println (plfe.getMessage () );
	}
	catch (ClassNotFoundException cnfe) {
	    System.out.println ("ClassNotFoundException: ");
	    System.out.println (cnfe.getMessage () );
	}
	catch (CardServiceException cse) {
	    System.out.println ("CardServiceException: ");
	    System.out.println (cse.getMessage () );
	}
	catch (CardTerminalException cte) {
	    System.out.println ("CardTerminalException: ");
	    System.out.println (cte.getMessage () );
	}
	
	System.out.println ("");
	System.out.println ("END TEST.");
	System.out.println ("------------------------------------------------------------");
    }
    
}

