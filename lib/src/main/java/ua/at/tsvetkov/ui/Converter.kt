/**
 * ****************************************************************************
 * Copyright (c) 2015 Alexandr Tsvetkov.
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
package ua.at.tsvetkov.ui

/**
 * Units measures converter
 *
 * @author Alexandr Tsvetkov 2015
 */
class Converter {

    companion object {

        /**
         * One inch in mm
         */
        const val INCH_IN_MM = 25.4f

        /**
         * One mm in inches
         */
        const val MM_IN_INCH = 1f / INCH_IN_MM

        /**
         * One inch in cm
         */
        const val INCH_IN_CM = 2.54f

        /**
         * One cm in inch
         */
        const val CM_IN_INCH = 1f / INCH_IN_CM

        /**
         * One inch in twip
         */
        const val INCH_IN_TWIP = 1440f

        /**
         * One twip in inch
         */
        const val TWIP_IN_INCH = 1f / INCH_IN_TWIP

        /**
         * One mm in twip
         */
        const val MM_IN_TWIP = 56.6925562674f

        /**
         * One twip in mm
         */
        const val TWIP_IN_MM = 1f / MM_IN_TWIP

        /**
         * One pt in twips
         */
        const val PT_IN_TWIP = 20f

        /**
         * One twip in pts
         */
        const val TWIP_IN_PT = 1 / PT_IN_TWIP

        /**
         * One inch in pt
         */
        const val INCH_IN_PT = 72f

        /**
         * One pt in inch
         */
        const val PT_IN_INCH = 1f / INCH_IN_PT

        /**
         * One mm in pt
         */
        const val MM_IN_PT = INCH_IN_MM / INCH_IN_PT

        /**
         * One pt in mm
         */
        const val PT_IN_MM = 1f / MM_IN_PT

        @JvmStatic
        fun inchToMm(inch: Float): Float {
            return inch * INCH_IN_MM
        }

        @JvmStatic
        fun mmToInch(mm: Float): Float {
            return mm * MM_IN_INCH
        }

        @JvmStatic
        fun inchToCm(inch: Float): Float {
            return inch * INCH_IN_CM
        }

        @JvmStatic
        fun cmToInch(cm: Float): Float {
            return cm * CM_IN_INCH
        }

        @JvmStatic
        fun inchToPt(inch: Float): Float {
            return inch * INCH_IN_PT
        }

        @JvmStatic
        fun ptToInch(pt: Float): Float {
            return pt * PT_IN_INCH
        }

        @JvmStatic
        fun mmToPt(mm: Float): Float {
            return mm * MM_IN_PT
        }

        @JvmStatic
        fun ptToMm(pt: Float): Float {
            return pt * PT_IN_MM
        }

        @JvmStatic
        fun twipToPt(twip: Float): Float {
            return twip * TWIP_IN_PT
        }

        @JvmStatic
        fun ptToTwip(pt: Float): Float {
            return pt * PT_IN_TWIP
        }

        @JvmStatic
        fun twipToInch(twip: Float): Float {
            return twip * TWIP_IN_INCH
        }

        @JvmStatic
        fun inchToTwip(inch: Float): Float {
            return inch * INCH_IN_TWIP
        }

        @JvmStatic
        fun twipToMm(twip: Float): Float {
            return twip * TWIP_IN_MM
        }

        @JvmStatic
        fun mmToTwip(mm: Float): Float {
            return mm * MM_IN_TWIP
        }

        @JvmStatic
        fun dpToPixels(dp: Float, dpi: Float): Float {
            return dp * (dpi / 160)
        }
    }

}
