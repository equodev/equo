package com.make.equo.monaco;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class SaveFileEditorHandler extends ParameterizedHandler {
	private static EquoMonacoEditor editor;

	public SaveFileEditorHandler setEditor(EquoMonacoEditor editor) {
		SaveFileEditorHandler.editor = editor;
		return this;
	}

	@Execute
	public void execute() {
		String path = editor.getFilePath();
		if (path != "") {
			editor.getContentsAsync(content -> {
				saveFile(path, content);
			});
		}
	}

	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			IEquoEventHandler eventHandler) {
		String path = editor.getFilePath();
		if (path != "") {
			editor.getContentsAsync(content -> {
				Map<String, Object> response = saveFile(path, content);
				eventHandler.send(idResponse, response);
			});
		}
	}

	protected Map<String, Object> saveFile(String path, String content) {
		Map<String, Object> response = new HashMap<>();
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(path));
			writer.print(content);
			writer.close();
			response.put("ok", "ok");
		} catch (FileNotFoundException e) {
			response.put("err", 1);
		}
		return response;
	}

	@Override
	public String getCommandId() {
		return IWorkbenchCommandConstants.FILE_SAVE;
	}

	@Override
	public IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id") };
		return parameters;
	}

	@Override
	public String getShortcut() {
		return "M1+S";
	}

}