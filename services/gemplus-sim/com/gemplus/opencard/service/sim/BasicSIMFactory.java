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
import java.util.*;
import opencard.core.terminal.*;
import opencard.core.service.*;
import opencard.core.util.*;

/**
 * This class is the implementation of a CardServiceFactory that
 * provides access to a BasicSIM CardService for a smartcard
 * that is compliant with the GSM 11.11 / GSM 11.14 standards.
 *
 * Note: For proprietary implementations, i.e., classes inheriting from
 * <tt>BasicSIM</tt>, it is still possible to redefine an other factory
 * and another <tt>knows</tt> method that would be simpler (e.g., that
 * would analyze the card ATR in order to check that the card if of the
 * right type). But note that in this case, it is also necessary to
 * redefine a factory because the <tt>knows</tt> method is static and
 * so the class exact name must be known and specified by the factory.
 * 
 * @author Nguyen Thanh Lam, Tomasz Molag, Christophe.Muller@research.gemplus.com
 * @version $Id: BasicSIMFactory.java,v 1.1 2004/09/13 16:26:26 cmuller13 Exp $
 * 
 */
public class BasicSIMFactory extends CardServiceFactory { 
    
    // A tracer for debugging output
    private static Tracer ctracer = new Tracer(BasicSIM.class);

    public final static int SIM_CARDTYPE = 0;
    
    public BasicSIMFactory() {
    }
    
    protected CardType getCardType(CardID cid, CardServiceScheduler sched) {
        if (BasicSIM.knows(cid, sched)) {
            return new CardType(SIM_CARDTYPE);
        } else {
            return CardType.UNSUPPORTED;
        }
    }
    
    protected Enumeration getClasses(CardType type) {
        if (type.getType() == SIM_CARDTYPE) {
            Vector classes = new Vector ();
            classes.addElement(BasicSIM.class);
            return (classes.elements());
        } else {
            ctracer.debug("", "getClasses: no classes - strange!");
            return null;
        }
    }
}
