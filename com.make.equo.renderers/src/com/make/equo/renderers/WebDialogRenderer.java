package com.make.equo.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.WBWRenderer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ui.helper.provider.dialogs.util.IDialogConstants;
import com.make.equo.ui.helper.provider.model.MButton;
import com.make.equo.ui.helper.provider.model.MWebDialog;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.swtcef.Chromium;

public class WebDialogRenderer extends WBWRenderer implements IEquoRenderer {

	static final String EQUO_RENDERERS_NAME = "toolbar";
	static final String EQUO_RENDERERS_URL = EQUO_RENDERERS_URL_PREFIX + EQUO_RENDERERS_NAME
			+ EQUO_RENDERERS_URL_SUFFIX;

	private static String ShellMinimizedTag = "shellMinimized"; //$NON-NLS-1$
	private static String ShellMaximizedTag = "shellMaximized"; //$NON-NLS-1$

	private String namespace;
	private MWebDialog dialog;

	@Inject
	private IEquoApplication equoApplication;

	@Inject
	private EquoEventHandler equoEventHandler;

	@Inject
	private IEquoServer equoProxyServer;

	private Shell realParentShell;

	@Override
	public Object createWidget(MUIElement dialog, Object parent) {
		this.namespace = "WebDialog" + Integer.toHexString(dialog.hashCode());
		this.dialog = (MWebDialog) dialog;

		Shell parentShell = getParentShell();

		prepareShell(parentShell);

		configureAndStartRenderProcess(realParentShell);

		return realParentShell;
	}

	@Override
	public void onActionPerformedOnElement() {
		equoEventHandler.on(namespace + "_itemClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("command");
			if (value != null) {
				realParentShell.getDisplay().syncExec(() -> {
					dialog.setResponse(value.getAsInt());
					realParentShell.dispose();
				});
			}
		});
	}

	@Override
	public EquoEventHandler getEquoEventHandler() {
		return equoEventHandler;
	}

	@Override
	public List<Map<String, String>> getEclipse4Model(String namespace) {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();

		HashMap<String, String> model = new HashMap<String, String>();
		model.put("title", this.dialog.getTitle());
		model.put("message", this.dialog.getMessage());
		model.put("type", String.valueOf(this.dialog.getType()));
		e4Model.add(model);

		EList<MButton> buttons = this.dialog.getButtons();
		for (MButton e : buttons) {
			HashMap<String, String> elementModel = new HashMap<String, String>();
			elementModel.put("bLabel", e.getLabel());
			elementModel.put("action", String.valueOf(e.getAction()));
			elementModel.put("id", e.getElementId());
			e4Model.add(elementModel);
		}
		return e4Model;
	}

	@Override
	public List<String> getJsFileNamesForRendering() {
		return Lists.newArrayList("renderers/webDialogRenderer.js");
	}

	@Override
	public String getNamespace() {
		return this.namespace;
	}

	@Override
	public List<String> getFrameworkContributionJSONFileNames() {
		return Lists.newArrayList();
	}

	@Override
	public String getModelContributionPath() {
		return "contributions/dialogs/";
	}

	@Override
	public IEquoServer getEquoProxyServer() {
		return equoProxyServer;
	}

	@Override
	public IEquoApplication getEquoApplication() {
		return equoApplication;
	}

	@Override
	public Chromium createBrowserComponent(Composite parent) {
		Chromium browser = new Chromium(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return browser;
	}

	@Override
	public void postProcess(MUIElement shellME) {
		super.postProcess(shellME);

		Shell shell = (Shell) shellME.getWidget();

		// Capture the max/min state
		final MUIElement disposeME = shellME;
		shell.addDisposeListener(e -> {
			Shell shell1 = (Shell) e.widget;
			if (disposeME != null) {
				disposeME.getTags().remove(ShellMinimizedTag);
				disposeME.getTags().remove(ShellMaximizedTag);
				if (shell1.getMinimized()) {
					disposeME.getTags().add(ShellMinimizedTag);
				}
				if (shell1.getMaximized()) {
					disposeME.getTags().add(ShellMaximizedTag);
				}
			}
		});

		// Apply the correct shell state
		if (shellME.getTags().contains(ShellMaximizedTag)) {
			shell.setMaximized(true);
		} else if (shellME.getTags().contains(ShellMinimizedTag)) {
			shell.setMinimized(true);
		}

		shell.layout(true);
		forceLayout(shell);
		if (shellME.isVisible()) {
			shell.open();

			if (dialog.isBlocker()) {
				runEventLoop(shell);
			} else {
				dialog.setResponse(IDialogConstants.OK_ID);
			}

		} else {
			shell.setVisible(false);
		}
	}

	private void forceLayout(Shell shell) {
		int i = 0;
		while (shell.isLayoutDeferred()) {
			shell.setLayoutDeferred(false);
			i++;
		}
		while (i > 0) {
			shell.setLayoutDeferred(true);
			i--;
		}
	}

	private void runEventLoop(Shell loopShell) {
		Display display;
		if (loopShell == null) {
			display = Display.getCurrent();
		} else {
			display = loopShell.getDisplay();
		}

		while (loopShell != null && !loopShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (!display.isDisposed()) {
			display.update();
		}
	}

	private void prepareShell(Shell parentShell) {
		realParentShell = new Shell(parentShell, SWT.NONE);

		realParentShell.addShellListener(new ShellAdapter() {

			@Override
			public void shellClosed(ShellEvent event) {
				event.doit = false; // cancel close
				realParentShell.getDisplay().syncExec(() -> {
					dialog.getParent().getChildren().remove(dialog);
					realParentShell.dispose();
				});
			}
		});

		realParentShell.setSize(500, 230);

		// Center the dialog relative to the parent shell
		Rectangle parentBounds = parentShell.getBounds();
		Rectangle thisBounds = realParentShell.getBounds();
		int locationX = (parentBounds.width - thisBounds.width) / 2 + parentBounds.x;
		int locationY = (parentBounds.height - thisBounds.height) / 2 + parentBounds.y;
		realParentShell.setLocation(new Point(locationX, locationY));

		realParentShell.setLayout(new GridLayout(1, false));

		realParentShell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	/**
	 * @return this dialog's parent shell
	 */

	private Shell getParentShell() {
		Shell parent = (Shell) this.dialog.getParentShell();
		if (parent == null) {
			Display d = Display.getCurrent();

			if (d == null) {
				return null;
			}

			parent = d.getActiveShell();

			// Make sure we don't pick a parent that has a modal child (this can lock the
			// app)
			if (parent == null) {
				// If this is a top-level window, then there must not be any open modal windows.
				parent = getModalChild(Display.getCurrent().getShells());
			} else {
				// If we picked a parent with a modal child, use the modal child instead
				Shell modalChild = getModalChild(parent.getShells());
				if (modalChild != null) {
					parent = modalChild;
				}
			}
		}
		return parent;

	}

	/**
	 * Returns the most specific modal child from the given list of Shells.
	 *
	 * @param toSearch shells to search for modal children
	 * @return the most specific modal child, or null if none
	 */
	private static Shell getModalChild(Shell[] toSearch) {
		int modal = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL;

		for (int i = toSearch.length - 1; i >= 0; i--) {
			Shell shell = toSearch[i];

			// Check if this shell has a modal child
			Shell[] children = shell.getShells();
			Shell modalChild = getModalChild(children);
			if (modalChild != null) {
				return modalChild;
			}

			// If not, check if this shell is modal itself
			if (shell.isVisible() && (shell.getStyle() & modal) != 0) {
				return shell;
			}
		}

		return null;
	}

	@Override
	public String getEquoRendererURL() {
		return EQUO_RENDERERS_URL;
	}

}
