package de.andreaskrienke.android.droidcavation.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the droidcavation database.
 */
public class DroidCavationContract {


    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website. A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "de.andreaskrienke.android.droidcavation";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_SUNIT = "sunit";


    /* Inner class that defines the table contents of the sunit table */
    public static final class SUnitEntry implements BaseColumns {

        public static final String TABLE_NAME = "sunit";

        // Column with the sunit number
        public static final String COLUMN_NUMBER = "number";

        // Column with foreign key to area table
        public static final String COLUMN_AREA_ID = "area_id";

        // Column with foreign key to square table
        public static final String COLUMN_SQUARE_ID = "square_id";

        /**
         * Structure from motion model
         */

        // Top Surface Date
        public static final String COLUMN_TOP_SURFACE_DATE = "top_surface_date";

        // Bottom Surface Date
        public static final String COLUMN_BOTTOM_SURFACE_DATE = "bottom_surface_date";

        /**
         * Tachy Measurements
         */

        // Tachy measurements exists
        public static final String COLUMN_TACHY_MEASUREMENTS = "tachy_measurements";

        // top surface outline
        public static final String COLUMN_TM_TOP_SURFACE_OUTLINE = "tm_top_surface_outline";

        // bottom surface outline
        public static final String COLUMN_TM_BOTTOM_SURFACE_OUTLINE = "tm_bottom_surface_outline";

        // levelments
        public static final String COLUMN_TM_LEVELMENTS = "tm_levelments";

        /**
         * DESCRIPTIONS
         */
        // shape
        public static final String COLUMN_SHAPE = "shape";

        // color
        public static final String COLUMN_COLOR = "color";

        /**
         * SEDIMENTS
         */

        // sediment type
        public static final String COLUMN_SEDIMENT_TYPE = "sediment_type";

        // sediment size
        public static final String COLUMN_SEDIMENT_SIZE = "sediment_size";

        // sediment percentage
        public static final String COLUMN_SEDIMENT_PERCENTAGE = "sediment_percentage";

        // description
        public static final String COLUMN_SHORT_DESC = "short_desc";

        /**
         * HARRIS MATRIX
         */

        // sunit_top id -> foreign key to sunit
        public static final String COLUMN_SUNIT_TOP_ID = "sunit_top_id";

        // sunit bottom id -> foreign key to sunit
        public static final String COLUMN_SUNIT_BOTTOM_ID = "sunit_bottom_id";


        /**
         * FEATURES
         */

        // associated features
        public static final String COLUMN_ASSOCIATED_FEATURE = "associated_feature";
        /**
         * FINDINGS
         */

        // findings -> charcoal
        public static final String COLUMN_FINDS_CHARCOAL = "finds_charcoal";

        // findings -> pottery
        public static final String COLUMN_FINDS_POTTERY = "finds_pottery";

        // findings -> bone
        public static final String COLUMN_FINDS_BONE = "finds_bone";

        // findings -> faience
        public static final String COLUMN_FINDS_FAIENCE = "finds_faience";

        // findings -> shell
        public static final String COLUMN_FINDS_SHELL = "finds_shell";

        // findings -> wood
        public static final String COLUMN_FINDS_WOOD = "finds_wood";

        // findings -> clay/mud
        public static final String COLUMN_FINDS_CLAY_MUD = "finds_clay_mud";

        // findings -> others
        public static final String COLUMN_FINDS_OTHERS = "finds_others";

        /**
         * Sketch/Picture
         */
        // sketch
        public static final String COLUMN_SKETCH = "sketch";

        /**
         * Dating/Interpretation
         */

        // description
        public static final String COLUMN_DATING_DESCRIPTION = "dating_description";

        /**
         * Excavation
         */

        // excavation date begin
        public static final String COLUMN_EXCAVATION_DATE_BEGIN = "excavation_date_begin";

        // excavation date end
        public static final String COLUMN_EXCAVATION_DATE_END = "excavation_date_end";

        // excavated by
        public static final String COLUMN_EXCAVATED_BY = "excavated_by";




        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUNIT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUNIT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUNIT;


        public static Uri buildSUnitUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildSUnitWithNumber(int number) {

            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_NUMBER, Integer.toString(number)).build();
        }

        public static int getSUnitIdFromUri(Uri uri) {
            return Integer.getInteger(uri.getPathSegments().get(1));
        }

    }
}
