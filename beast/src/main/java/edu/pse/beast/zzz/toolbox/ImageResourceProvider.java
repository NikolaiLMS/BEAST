package edu.pse.beast.zzz.toolbox;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class ImageResourceProvider.
 *
 * @author Holger Klein
 */
public class ImageResourceProvider {

    /** The Constant PNG_FILE_ENDING. */
    private static final String PNG_FILE_ENDING = ".png";

    /** The Constant IDS. */
    private static final String[] IDS =
        {
        "new", "load", "save", "save_as",
        "undo", "redo", "cut", "copy",
        "paste", "start", "stop",
        "showHelpToUser", "simulation"
        };

    /** The Constant RESOURCE. */
    private static final String RESOURCE = "/core/images/toolbar/";

    /** The toolbar. */
    private static ImageResourceProvider toolbar;

    /** The images. */
    private final Map<String, Image> images =
            new HashMap<String, Image>();

    /** The folder. */
    private final String folder;

    /** The ids. */
    private final String[] ids;

    /**
     * Instantiates a new image resource provider.
     *
     * @param folderString
     *            the folder string
     * @param idStrings
     *            the id strings
     */
    public ImageResourceProvider(final String folderString,
                                 final String[] idStrings) {
        this.folder = folderString;
        this.ids = idStrings;
        for (final String s : this.ids) {
            final File toRead =
                    new File(SuperFolderFinder.getSuperFolder()
                    + this.folder + s + PNG_FILE_ENDING);
            Image img = null;
            img = FileLoader.loadFileAsImage(toRead);
            images.put(s, img);
        }
    }

    /**
     * Gets the toolbar images.
     *
     * @return the toolbar images
     */
    public static ImageResourceProvider getToolbarImages() {
        if (toolbar == null) {
            toolbar = new ImageResourceProvider(RESOURCE, IDS);
        }
        return toolbar;
    }

    /**
     * Gets the image by id.
     *
     * @param id
     *            the id
     * @return the image by id
     */
    public Image getImageById(final String id) {
        return images.get(id);
    }
}