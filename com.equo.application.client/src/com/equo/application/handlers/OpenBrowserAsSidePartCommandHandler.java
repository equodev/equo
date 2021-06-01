package com.equo.application.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainerElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;

import com.equo.application.model.browser.IPositionConstants;
import com.equo.application.util.IConstants;

public class OpenBrowserAsSidePartCommandHandler {

    @Inject
    private IEventBroker broker;
    private static final String removeEvent = "org/eclipse/e4/ui/model/ui/ElementContainer/children/REMOVE";

    @Execute
    public void execute(@Named(IConstants.EQUO_OPEN_BROWSER_AS_SIDE_PART) String url,
	    @Named(IConstants.EQUO_BROWSER_PART_NAME) String newPartName,
	    @Named(IConstants.EQUO_BROWSER_PART_POSITION) String position, MTrimmedWindow mTrimmedWindow,
	    MPart mainPart, UISynchronize sync) {
	sync.syncExec(() -> {
	    mTrimmedWindow.getChildren().remove(mainPart);
	    createNewPartstack(url, newPartName, position, mTrimmedWindow, mainPart);
	});
    }

    private void createNewPartstack(String url, String partName, String position, MTrimmedWindow mTrimmedWindow,
	    MPart mainPart) {
	MPartSashContainer parentSashContainer;
	if (mTrimmedWindow.getChildren() != null && !mTrimmedWindow.getChildren().isEmpty()
		&& mTrimmedWindow.getChildren().get(0) instanceof MPartSashContainer) {
	    parentSashContainer = (MPartSashContainer) mTrimmedWindow.getChildren().get(0);
	} else {
	    parentSashContainer = MBasicFactory.INSTANCE.createPartSashContainer();
	    mTrimmedWindow.getChildren().add((MWindowElement) parentSashContainer);
	}

	MPartStack newPartStack = MBasicFactory.INSTANCE.createPartStack();
	if (partName == null) {
	    newPartStack.getTags().add("NoTitle");
	}
	String newPartStackSuffix = partName != null ? partName.toLowerCase() : "side.partstack." + position;
	String newPartStackId = IConstants.EQUO_PART_IN_PARTSTACK_ID + "." + newPartStackSuffix;
	newPartStack.setElementId(newPartStackId);

	MPart newPart = MBasicFactory.INSTANCE.createPart();
	newPartStack.getChildren().add(newPart);
	// TODO generate a unique id...
	String partSuffix = partName != null ? partName.toLowerCase() : "side." + position;
	String partId = IConstants.EQUO_BROWSER_IN_PARTSTACK_ID + "." + partSuffix;
	newPart.setElementId(partId);
	newPart.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);
	newPart.getProperties().put(IConstants.MAIN_URL_KEY, url);
	newPart.setCloseable(true);
	newPart.getTags().add("removeOnHide");
	if (partName != null) {
	    newPart.setLabel(partName);
	}
	if (position != null) {
	    switch (position) {
	    case IPositionConstants.RIGHT:
		addChildrenToSashContainer(mainPart, newPartStack, parentSashContainer, true);
		break;
	    case IPositionConstants.LEFT:
		addChildrenToSashContainer(newPartStack, mainPart, parentSashContainer, true);
		break;
	    case IPositionConstants.BOTTOM:
		addChildrenToSashContainer(mainPart, newPartStack, parentSashContainer, false);
		break;
	    case IPositionConstants.TOP:
		addChildrenToSashContainer(newPartStack, mainPart, parentSashContainer, false);
		parentSashContainer.setHorizontal(false);
		break;
	    default:
		addChildrenToSashContainer(mainPart, newPartStack, parentSashContainer, false);
		break;
	    }
	} else {
	    addChildrenToSashContainer(mainPart, newPartStack, parentSashContainer, false);
	}

	parentSashContainer.setSelectedElement(newPartStack);
	parentSashContainer.setVisible(true);
	parentSashContainer.setToBeRendered(true);
	broker.subscribe(removeEvent, new CloseContainerEventHandler(newPartStack, broker));
    }

    private void addChildrenToSashContainer(MPartSashContainerElement firstElement,
	    MPartSashContainerElement secondElement, MPartSashContainer partSashContainer, boolean horizontal) {
	partSashContainer.getChildren().add(firstElement);
	partSashContainer.getChildren().add(secondElement);
	partSashContainer.setHorizontal(horizontal);
    }
}
