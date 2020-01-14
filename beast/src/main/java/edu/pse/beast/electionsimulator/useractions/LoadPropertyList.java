//package edu.pse.beast.electionsimulator.useractions;
//
//import edu.pse.beast.propertylist.PropertyList;
//import edu.pse.beast.propertylist.model.PLModel;
//import edu.pse.beast.toolbox.UserAction;
//
///**
// * Loads a new property list.
// * @author Justin Hecht
// */
//public class LoadPropertyList extends UserAction {
//    private final PropertyList controller;
//
//    /**
//     * Constructor
//     * @param controller A reference to the PropertyList controller
//     */
//    public LoadPropertyList(PropertyList controller) {
//        super("load");
//        this.controller = controller;
//    }
//
//    @Override
//    public void perform() {
//        if (controller.getChangeHandler().hasChanged()) {
//            if (!controller.getFileChooser().openSaveChangesDialog(controller.getModel())) {
//                return;
//            }
//        }
//        PLModel plModel = (PLModel) controller.getFileChooser().loadObject();
//        if (plModel != null) {
//            controller.setPLModel(plModel);
//            controller.getChangeHandler().setChangedSinceSave(false);
//            controller.resetActionList();
//        }
//    }
//}
