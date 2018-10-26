/**
 * ****************************************************************************
 * Copyright (c) 2014 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 *
 * Contributors:
 * Alexandr Tsvetkov - initial API and implementation
 *
 *
 * Project:
 * TAO Core
 *
 *
 * License agreement:
 *
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 * caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 * permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 * this license agreement will remain unchanged.
 * ****************************************************************************
 */
package ua.at.tsvetkov.application

enum class Diagonal(val min: Double, val max: Double) {

    SMALL_PHONE_OR_WATCH(0.0, 2.0),
    PHONE_2(2.0, 2.5),
    PHONE_3(2.5, 3.5),
    PHONE_4(3.5, 4.5),
    PHONE_5(4.5, 5.5),
    PHONE_6(5.5, 6.5),
    TABLET_7(6.5, 7.5),
    TABLET_8(7.5, 8.5),
    TABLET_9(8.5, 9.5),
    TABLET_10(9.5, 10.5),
    TABLET_11(10.5, 11.5),
    TABLET_12(11.5, 12.5),
    TABLET_BIG(12.5, Double.MAX_VALUE);

    fun isThis(size: Double): Boolean = (size >= min) && (size < max)

}
