/**
 * ****************************************************************************
 * Copyright (c) 2010 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p/>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p/>
 * Project:
 * TAO Core
 * <p/>
 * License agreement:
 * <p/>
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.netchecker;

/**
 * @author A.Tsvetkov 2010, http://tsvetkov.at.ua, al@ukr.net<br>
 */
public enum NetStatus {
    NOT_DEFINED_YET("Network status is not defined yet."),
    FAULTY_URL("Wrong server url"),
    IO_ERROR("IO Error while reading the server page."),
    CONNECTION_MISSING("No default network is currently active. Please check internet connection."),
    NET_OK("Connection is good."),
    TIMEOUT("A timeout was reached while waiting for a response from the server.");

    private final String mMessage;

    NetStatus(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
