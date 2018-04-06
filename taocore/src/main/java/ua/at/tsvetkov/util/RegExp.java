/**
 * ****************************************************************************
 * Copyright (c) 2012 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * <p/>
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 * <p/>
 * Project:
 * TAO Library
 * <p/>
 * File name:
 * RegExp.java
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
 * 5. SPECIAL PERMISSION for this code usage in COMMERCIAL application SHOULD be obtained
 * from author.
 * ****************************************************************************
 */
package ua.at.tsvetkov.util;

/**
 * Different RegExp string constants.
 *
 * @author A.Tsvetkov 2011. http://tsvetkov.at.ua email: al@ukr.net
 */
public class RegExp {

    private RegExp() {

    }

    /**
     * Russia phone number
     */
    public static final String phoneNumberRu = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    /**
     * Ukraine phone number
     */
    public static final String phoneNumberUa = "^((8|\\+3)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
    /**
     * IP adress
     */
    public static final String ipAdress = "/^\\d+\\.\\d+\\.\\d+\\.\\d+$/";
    /**
     * IP adress
     */
    public static final String ipAdress2 = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";
    /**
     * Strong check e-mail
     */
    public static final String email = "([\\w-+]+(?:\\.[\\w-+]+)*@(?:[\\w-]+\\.)+[a-zA-Z]{2,7})";
    /**
     * E-mail
     */
    public static final String e_mail = ".+@.+\\.[a-z]+";
    /**
     * Url
     */
    public static final String url = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";

    /**
     * Hex color
     */
    public static final String hexColor = "^#?([a-f0-9]{6}|[a-f0-9]{3})$";
    /**
     * XML tag
     */
    public static final String xml = "^<([a-z]+)([^>]+)*(?:>(.*)<\\/\\1>|\\s+\\/>)$";

    /**
     * Generate password regexp
     *
     * @param min minimal length
     * @param max maximal length
     * @return regexp string
     */
    public static String getPassword(int min, int max) {
        return "^[a-z0-9_-]{" + min + ',' + max + "}$";
    }

    /**
     * Password matching expression. Password must be at least min characters, no more than max characters,
     * and must include at least one upper case letter, one lower case letter, and one numeric digit.
     *
     * @param min minimal length
     * @param max maximal length
     * @return regexp string
     */
    public static String getStrongPassword(int min, int max) {
        return "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{" + min + "," + max + "}$";
    }

    /**
     * Generate user name regexp include chars, digits and underline
     *
     * @param min minimal length
     * @param max maximal length
     * @return regexp string
     */
    public static String getUserName(int min, int max) {
        return "^[a-z0-9_-]{" + min + ',' + max + "}$";
    }

    /**
     * Generate user name regexp include only chars (Upper and lower space)
     *
     * @param min minimal length
     * @param max maximal length
     * @return regexp string
     */
    public static String getName(int min, int max) {
        return "^[a-zA-Z]{" + min + ',' + max + "}$";
    }
}
